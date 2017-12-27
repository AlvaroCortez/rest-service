package com.netcracker.rest.restservice.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.netcracker.rest.restservice.model.Place;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class GoogleMapsService {

    private final GeoApiContext context;
    private static final long MINUTES_PER_HOUR = 60;
    private static final long SECONDS_PER_MINUTE = 60;
    private static final long AVERAGE_PRICE_OF_GASOLINE = 35;
    private static final double LITERS_PER_KILOMETER = 0.1;
    private static final long METERS_PER_KILOMETER = 1000;

    @Autowired
    public GoogleMapsService(GeoApiContext context) {
        this.context = context;
    }

    public List<Place> findPlaces(String lat,
                                  String lng,
                                  int radius,
                                  String type,
                                  int hours,
                                  Double carGasolinePrice,
                                  Double carGasolineConsumption
    ) throws InterruptedException, ApiException, IOException {
        List<Place> places = new ArrayList<>();
        long hoursInSeconds = hours * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
        LatLng currentPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(context, currentPosition).
                radius(radius).
                type(PlaceType.valueOf(type.toUpperCase())).
                await();
        places.addAll(filterDuration(placesSearchResponse, currentPosition, hoursInSeconds, carGasolinePrice, carGasolineConsumption));
        while (placesSearchResponse != null && placesSearchResponse.nextPageToken != null) {
            placesSearchResponse = PlacesApi.nearbySearchNextPage(context, placesSearchResponse.nextPageToken)
                    .awaitIgnoreError();
            if(placesSearchResponse != null) {
                places.addAll(filterDuration(placesSearchResponse, currentPosition, hoursInSeconds, carGasolinePrice, carGasolineConsumption));
            }
        }
//      DistanceMatrix matrix = DistanceMatrixApi.newRequest(context).origins(currentPosition).destinations().await();
        return places;
    }

    private List<Place> filterDuration(PlacesSearchResponse placesSearchResponse,
                                       LatLng currentPosition,
                                       long hoursInSeconds,
                                       Double carGasolinePrice,
                                       Double carGasolineConsumption
    ) throws InterruptedException, ApiException, IOException {
        List<Place> places = new ArrayList<>();
        int countPlaces = placesSearchResponse.results.length;
        if(countPlaces == 0) {
            return Collections.emptyList();
        }
        LatLng[] locations = new LatLng[countPlaces];
        for (int i = 0; i < countPlaces; i++) {
            locations[i] = placesSearchResponse.results[i].geometry.location;
        }
        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(currentPosition)
                .destinations(locations)
                .await();
        int i = 0;
        for (PlacesSearchResult result : placesSearchResponse.results) {
            DistanceMatrixElement destinationPlace = matrix.rows[0].elements[i++];
            if (destinationPlace.duration.inSeconds < hoursInSeconds) {
                long longPrice = (long) (destinationPlace.distance.inMeters * carGasolinePrice / METERS_PER_KILOMETER * carGasolineConsumption);
                BigDecimal price = BigDecimal.valueOf(longPrice);
                Place place = new Place(result.geometry.location.lat,
                                        result.geometry.location.lng,
                                        result.name,
                                        result.vicinity,
                                        result.icon,
                                        destinationPlace.duration,
                                        destinationPlace.distance,
                                        price
                );
                places.add(place);
            }
        }
        return places;
    }

    public Place findLuckyPlace(String lat, String lng) throws InterruptedException, ApiException, IOException {
//        double lat = getRandomNumberInRange(-90, 90);
//        double lng = getRandomNumberInRange(-180, 180);
        LatLng currentPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(context, currentPosition).await();
        int placesNumber = getIntRandomNumberInRange(1, 20);
        PlacesSearchResult result = placesSearchResponse.results[placesNumber];
        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(currentPosition)
                .destinations(result.geometry.location)
                .await();
        long longPrice = (long) (matrix.rows[0].elements[0].distance.inMeters * AVERAGE_PRICE_OF_GASOLINE / METERS_PER_KILOMETER * LITERS_PER_KILOMETER);
        BigDecimal price = BigDecimal.valueOf(longPrice);
        Place place = new Place(result.geometry.location.lat,
                                result.geometry.location.lng,
                                result.name,
                                result.vicinity,
                                result.icon,
                                matrix.rows[0].elements[0].duration,
                                matrix.rows[0].elements[0].distance,
                                price
        );
        return place;
    }

    private static int getIntRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt()*((max - min) + 1) + min;
    }

    private static double getDoubleRandomNumberInRange(double min, double max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextDouble()*((max - min) + 1) + min;
    }
}
