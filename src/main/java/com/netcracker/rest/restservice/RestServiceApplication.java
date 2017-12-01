package com.netcracker.rest.restservice;

import com.netcracker.rest.restservice.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestServiceApplication {

	@Autowired
	private GoogleMapsService googleMapsService;

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
//			System.out.println("Start using Google Maps service");
//			PlacesSearchResponse places = googleMapsService.findPlaces("-33.8670522", "151.1957362");
//			Gson gson = new GsonBuilder().create();
//			for (PlacesSearchResult result : places.results) {
//				System.out.println(result.name);
//				System.out.println(gson.toJson(result.name));
//			}
//			PlacesSearchResponse placesNextPage = googleMapsService.findPlacesNextPage(places.nextPageToken);
//			for (PlacesSearchResult result : placesNextPage.results) {
//				System.out.println(result.name);
//			}
		};
	}
}
