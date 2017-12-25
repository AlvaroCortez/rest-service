package com.netcracker.rest.restservice.model;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.net.URL;

@AllArgsConstructor
public class Place {
    public double lat;
    public double lng;
    public String name;
    public String address;
    public URL icon;
    public long durationInSeconds;
    public long distanceInMeters;
    public BigDecimal price;
}
