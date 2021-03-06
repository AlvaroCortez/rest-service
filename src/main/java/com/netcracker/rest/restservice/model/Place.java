package com.netcracker.rest.restservice.model;

import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
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
    public Duration duration;
    public Distance distance;
    public BigDecimal price;
}
