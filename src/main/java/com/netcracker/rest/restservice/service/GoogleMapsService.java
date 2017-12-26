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
import java.util.List;

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
                                  int hours
    ) throws InterruptedException, ApiException, IOException {
        List<Place> places = new ArrayList<>();
        long hoursInSeconds = hours * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
        LatLng currentPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(context, currentPosition).
                radius(radius).
                type(PlaceType.valueOf(type.toUpperCase())).
                await();
        places.addAll(filterDuration(placesSearchResponse, currentPosition, hoursInSeconds));
        while (placesSearchResponse.nextPageToken != null) {
            placesSearchResponse = PlacesApi.nearbySearchNextPage(context, placesSearchResponse.nextPageToken)
                    .awaitIgnoreError();
            places.addAll(filterDuration(placesSearchResponse, currentPosition, hoursInSeconds));
        }
//      DistanceMatrix matrix = DistanceMatrixApi.newRequest(context).origins(currentPosition).destinations().await();
        return places;
    }

    private List<Place> filterDuration(PlacesSearchResponse placesSearchResponse,
                                       LatLng currentPosition,
                                       long hoursInSeconds
    ) throws InterruptedException, ApiException, IOException {
        List<Place> places = new ArrayList<>();
        for (PlacesSearchResult result : placesSearchResponse.results) {
            DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                    .origins(currentPosition)
                    .destinations(result.geometry.location)
                    .await();
            if (matrix.rows[0].elements[0].duration.inSeconds < hoursInSeconds) {
                long longPrice = (long) (matrix.rows[0].elements[0].distance.inMeters * AVERAGE_PRICE_OF_GASOLINE / METERS_PER_KILOMETER * LITERS_PER_KILOMETER);
                BigDecimal price = BigDecimal.valueOf(longPrice);
                Place place = new Place(result.geometry.location.lat,
                                        result.geometry.location.lng,
                                        result.name,
                                        result.vicinity,
                                        result.icon,
                                        matrix.rows[0].elements[0].duration.inSeconds,
                                        matrix.rows[0].elements[0].distance.inMeters,
                                        price
                );
                places.add(place);
            }
        }
        return places;
    }
}
