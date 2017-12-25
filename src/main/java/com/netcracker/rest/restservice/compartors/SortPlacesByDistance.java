package com.netcracker.rest.restservice.compartors;

import com.netcracker.rest.restservice.model.Place;

import java.util.Comparator;

public class SortPlacesByDistance implements Comparator<Place> {

    @Override
    public int compare(Place place1, Place place2) {
        return Long.compare(place1.distanceInMeters, place2.distanceInMeters);
    }
}
