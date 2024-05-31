package com.delidronehubs.delidronehubs;

import com.delidronehubs.delidronehubs.classes.Client;
import com.delidronehubs.delidronehubs.classes.Hub;
import com.delidronehubs.delidronehubs.classes.Merchandiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DeliverySimulation {
    private static final Random random = new Random();
    private static final int MINUTES_PER_SECOND = 3;

    private final RestTemplate restTemplate;

    @Autowired
    public DeliverySimulation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to simulate the delivery process
    public void simulateDelivery() {
        // Retrieve all hubs from the database
        List<Hub> hubs = getAllHubs();
        // Choose a hub randomly from the database
        Hub hub = hubs.get(random.nextInt(hubs.size()));
        String hubName = hub.getLocation();
        double hubLatitude = hub.getLatitude();
        double hubLongitude = hub.getLongitude();

        // Display the hub name at the start of the simulation
        //System.out.println("*************************************************************************************************************");
        //System.out.println("Drone dispatched from hub: " + hubName);

        // Choose a client randomly from the database
        List<Client> clients = getAllClients();
        Client client = clients.get(random.nextInt(clients.size()));
        String clientName = client.getName();

        // Choose a merchandiser randomly from the database
        List<Merchandiser> merchandisers = getAllMerchandisers();
        Merchandiser merchandiser = merchandisers.get(random.nextInt(merchandisers.size()));
        String merchandiserName = merchandiser.getName();

        // Convert merchandiser address to latitude and longitude
        double[] merchandiserCoordinates = convertAddressToCoordinates(merchandiser.getAddress());
        double pickupLatitude = merchandiserCoordinates[0];
        double pickupLongitude = merchandiserCoordinates[1];

        // Calculate the distance between hub and merchandiser
        double distance = calculateDistance(hubLatitude, hubLongitude, pickupLatitude, pickupLongitude);

        // Display the distance in the log message
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedDistance = df.format(distance);
        System.out.println("*************************************************************************************************************");
        System.out.println("Drone dispatched from hub (" + hubName + ") to merchandiser: " + merchandiserName + ". Distance: " + formattedDistance + " km.");
        System.out.println("*************************************************************************************************************");

        // Simulate dispatching drone from hub to merchandiser
        simulateDispatchToMerchandiser(merchandiserName, hubName, hubLatitude, hubLongitude, pickupLatitude, pickupLongitude);

        // Simulate loading package by merchandiser
        //simulatePackageLoading(merchandiserName);

        // Convert client address to latitude and longitude
        double[] clientCoordinates = convertAddressToCoordinates(client.getAddress());
        double deliveryLatitude = clientCoordinates[0];
        double deliveryLongitude = clientCoordinates[1];

        // Calculate the distance between merchandiser and client
        double deliveryDistance = calculateDistance(pickupLatitude, pickupLongitude, deliveryLatitude, deliveryLongitude);

        // Display the distance in the log message
        String formattedDeliveryDistance = df.format(deliveryDistance);
        System.out.println("*************************************************************************************************************");
        System.out.println("Drone dispatched from merchandiser (" + merchandiserName + ") to client: " + clientName + ". Distance: " + formattedDeliveryDistance + " km.");
        System.out.println("*************************************************************************************************************");

        // Simulate dispatching drone from merchandiser to client
        simulateDispatchToClient(clientName, pickupLatitude, pickupLongitude, deliveryLatitude, deliveryLongitude);

        // Show delivery complete and dispatch drone to hub
        System.out.println("*************************************************************************************************************");
        System.out.println("Delivery complete! Drone is dispatched back to its hub.");
        System.out.println("*************************************************************************************************************");
        System.out.println("*************************************************************************************************************");
    }

    // Method to fetch all clients from the database via REST API
    public List<Client> getAllClients() {
        Client[] clientsArray = restTemplate.getForObject("http://localhost:9876/api/clients", Client[].class);
        return Arrays.asList(clientsArray);
    }

    // Method to fetch all merchandisers from the database via REST API
    private List<Merchandiser> getAllMerchandisers() {
        ResponseEntity<List<Merchandiser>> responseEntity = restTemplate.exchange(
                "http://localhost:9876/api/merchandisers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Merchandiser>>() {}
        );
        return responseEntity.getBody();
    }

    // Method to fetch all hubs from the database via REST API
    private List<Hub> getAllHubs() {
        ResponseEntity<List<Hub>> responseEntity = restTemplate.exchange(
                "http://localhost:9876/api/hubs",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Hub>>() {}
        );
        return responseEntity.getBody();
    }

    // Method to convert address to coordinates (latitude and longitude)
    private double[] convertAddressToCoordinates(String address) {
        // Reformat the address: replace spaces with '+'
        String formattedAddress = address.replace(" ", "+");

        String apiKey = "AIzaSyCgKnOrEIUhZulP_FDFZE2ncE-KkK6b0sk";
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", formattedAddress)
                .queryParam("key", apiKey)
                .toUriString();

        try {
            ResponseEntity<GeocodingResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    GeocodingResponse.class
            );

            GeocodingResponse response = responseEntity.getBody();
            if (response != null && !response.getResults().isEmpty()) {
                double latitude = response.getResults().get(0).getGeometry().getLocation().getLat();
                double longitude = response.getResults().get(0).getGeometry().getLocation().getLng();
                return new double[]{latitude, longitude};
            } else {
                System.err.println("No results found for address: " + address);
                throw new RuntimeException("Failed to get coordinates for address: " + address);
            }
        } catch (Exception e) {
            System.err.println("Error occurred while getting coordinates for address: " + address);
            e.printStackTrace();
            throw new RuntimeException("Failed to get coordinates for address: " + address, e);
        }
    }

    // Method to simulate dispatching drone from hub to merchandiser
    private void simulateDispatchToMerchandiser(String merchandiserName, String hubName, double hubLatitude, double hubLongitude, double pickupLatitude, double pickupLongitude) {
        //System.out.println("Drone dispatched from hub (" + hubName + ") to merchandiser: " + merchandiserName);
        // Calculate the distance between hub and merchandiser
        double distance = calculateDistance(hubLatitude, hubLongitude, pickupLatitude, pickupLongitude);
        // Simulate time to reach merchandiser
        int tripTime = (int) (distance / 50 * 60); // Assuming an average drone speed of 50 km/h
        System.out.println("Total trip time: " + tripTime + " minutes");

        // Simulate countdown until drone reaches merchandiser
        for (int i = tripTime * MINUTES_PER_SECOND; i >= 0; i -= 1 * MINUTES_PER_SECOND) {
            System.out.println("Countdown: " + i / MINUTES_PER_SECOND + " minutes to reach " + merchandiserName);
            // Delay for demonstration purposes
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to simulate dispatching drone from merchandiser to client
    private void simulateDispatchToClient(String clientName, double pickupLatitude, double pickupLongitude, double deliveryLatitude, double deliveryLongitude) {
        //System.out.println("Drone dispatched from merchandiser to client: " + clientName);
        // Calculate the distance between merchandiser and client
        double distance = calculateDistance(pickupLatitude, pickupLongitude, deliveryLatitude, deliveryLongitude);
        // Simulate time to reach client
        int tripTime = (int) (distance / 50 * 60); // Assuming an average drone speed of 50 km/h
        System.out.println("Total trip time: " + tripTime + " minutes");

        // Simulate countdown until drone reaches client
        for (int i = tripTime * MINUTES_PER_SECOND; i >= 0; i -= 1 * MINUTES_PER_SECOND) {
            System.out.println("Countdown: " + i / MINUTES_PER_SECOND + " minutes to reach " + clientName);
            // Delay for demonstration purposes
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to calculate distance between two coordinates (latitude and longitude)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate the distance between two points on the Earth's surface
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    // Class to map the geocoding response from Google Maps API
    private static class GeocodingResponse {
        private List<Result> results;

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        private static class Result {
            private Geometry geometry;

            public Geometry getGeometry() {
                return geometry;
            }

            public void setGeometry(Geometry geometry) {
                this.geometry = geometry;
            }

            private static class Geometry {
                private Location location;

                public Location getLocation() {
                    return location;
                }

                public void setLocation(Location location) {
                    this.location = location;
                }

                private static class Location {
                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }
    }
}
