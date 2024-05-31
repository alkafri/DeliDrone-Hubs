package com.delidronehubs.delidronehubs;

import com.delidronehubs.delidronehubs.classes.Client;
import com.delidronehubs.delidronehubs.classes.Hub;
import com.delidronehubs.delidronehubs.classes.Merchandiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.util.*;

@Component
@RestController
@RequestMapping("/api/manual-delivery")
public class DeliverySimulationManual {
    private static final Random random = new Random();
    private static final int MINUTES_PER_SECOND = 3;

    private final RestTemplate restTemplate;

    @Autowired
    public DeliverySimulationManual(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to simulate the delivery process manually
    public void simulateManualDelivery() {
        Scanner scanner = new Scanner(System.in);

        // Retrieve and list all clients
        List<Client> clients = getAllClients();
        System.out.println("===============================================================");
        System.out.println("List of available Clients:");
        System.out.println("===============================================================");
        for (int i = 0; i < clients.size(); i++) {
            System.out.println((i + 1) + ". " + clients.get(i).getName() + " - " + clients.get(i).getAddress());
        }
        System.out.println("---------------------------------------------------------------");
        System.out.print("Select a client by entering the corresponding number:");
        int clientChoice = scanner.nextInt() - 1;
        System.out.println("---------------------------------------------------------------");
        Client client = clients.get(clientChoice);
        String clientName = client.getName();

        // Retrieve and list all merchandisers
        List<Merchandiser> merchandisers = getAllMerchandisers();
        System.out.println("===============================================================");
        System.out.println("List of available merchandiser:");
        System.out.println("===============================================================");
        for (int i = 0; i < merchandisers.size(); i++) {
            System.out.println((i + 1) + ". " + merchandisers.get(i).getName() + " - " + merchandisers.get(i).getAddress());
        }
        System.out.println("---------------------------------------------------------------");
        System.out.print("Select a merchandiser by entering the corresponding number:");
        int merchandiserChoice = scanner.nextInt() - 1;
        System.out.println("---------------------------------------------------------------");
        Merchandiser merchandiser = merchandisers.get(merchandiserChoice);
        String merchandiserName = merchandiser.getName();

        // Convert addresses to coordinates
        double[] clientCoordinates = convertAddressToCoordinates(client.getAddress());
        double deliveryLatitude = clientCoordinates[0];
        double deliveryLongitude = clientCoordinates[1];

        double[] merchandiserCoordinates = convertAddressToCoordinates(merchandiser.getAddress());
        double pickupLatitude = merchandiserCoordinates[0];
        double pickupLongitude = merchandiserCoordinates[1];

        // Choose the nearest hub for both client and merchandiser
        Hub nearestHubForClient = getNearestHub(deliveryLatitude, deliveryLongitude);
        Hub nearestHubForMerchandiser = getNearestHub(pickupLatitude, pickupLongitude);

        // Display and simulate delivery
        displayAndSimulate(nearestHubForMerchandiser, merchandiserName, pickupLatitude, pickupLongitude, clientName, deliveryLatitude, deliveryLongitude);
    }

    @PostMapping("/simulate")
    public void simulateDeliveryByUUID(@RequestParam UUID clientUuid, @RequestParam UUID merchandiserUuid) {
        Client client = getClientByUUID(clientUuid);
        Merchandiser merchandiser = getMerchandiserByUUID(merchandiserUuid);

        double[] clientCoordinates = convertAddressToCoordinates(client.getAddress());
        double deliveryLatitude = clientCoordinates[0];
        double deliveryLongitude = clientCoordinates[1];

        double[] merchandiserCoordinates = convertAddressToCoordinates(merchandiser.getAddress());
        double pickupLatitude = merchandiserCoordinates[0];
        double pickupLongitude = merchandiserCoordinates[1];

        Hub nearestHubForClient = getNearestHub(deliveryLatitude, deliveryLongitude);
        Hub nearestHubForMerchandiser = getNearestHub(pickupLatitude, pickupLongitude);

        displayAndSimulate(nearestHubForMerchandiser, merchandiser.getName(), pickupLatitude, pickupLongitude, client.getName(), deliveryLatitude, deliveryLongitude);
    }

    // Method to display the hub name and simulate the delivery process
    private void displayAndSimulate(Hub hub, String merchandiserName, double pickupLatitude, double pickupLongitude, String clientName, double deliveryLatitude, double deliveryLongitude) {
        DecimalFormat df = new DecimalFormat("#.##");
        double hubLatitude = hub.getLatitude();
        double hubLongitude = hub.getLongitude();

        double distanceToMerchandiser = calculateDistance(hubLatitude, hubLongitude, pickupLatitude, pickupLongitude);
        String formattedDistanceToMerchandiser = df.format(distanceToMerchandiser);
        System.out.println("*************************************************************************************************************");
        System.out.println("Drone dispatched from hub (" + hub.getLocation() + ") to merchandiser: " + merchandiserName + ". Distance: " + formattedDistanceToMerchandiser + " km.");
        System.out.println("*************************************************************************************************************");
        simulateDispatchToMerchandiser(merchandiserName, hub.getLocation(), hubLatitude, hubLongitude, pickupLatitude, pickupLongitude);

        double deliveryDistance = calculateDistance(pickupLatitude, pickupLongitude, deliveryLatitude, deliveryLongitude);
        String formattedDeliveryDistance = df.format(deliveryDistance);
        System.out.println("*************************************************************************************************************");
        System.out.println("Drone dispatched from merchandiser (" + merchandiserName + ") to client: " + clientName + ". Distance: " + formattedDeliveryDistance + " km.");
        System.out.println("*************************************************************************************************************");
        simulateDispatchToClient(clientName, pickupLatitude, pickupLongitude, deliveryLatitude, deliveryLongitude);

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
        double distance = calculateDistance(hubLatitude, hubLongitude, pickupLatitude, pickupLongitude);
        int tripTime = (int) (distance / 50 * 60); // Assuming an average drone speed of 50 km/h
        System.out.println("Total trip time: " + tripTime + " minutes");

        for (int i = tripTime * MINUTES_PER_SECOND; i >= 0; i -= 1 * MINUTES_PER_SECOND) {
            System.out.println("Countdown: " + i / MINUTES_PER_SECOND + " minutes to reach " + merchandiserName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to simulate dispatching drone from merchandiser to client
    private void simulateDispatchToClient(String clientName, double pickupLatitude, double pickupLongitude, double deliveryLatitude, double deliveryLongitude) {
        double distance = calculateDistance(pickupLatitude, pickupLongitude, deliveryLatitude, deliveryLongitude);
        int tripTime = (int) (distance / 50 * 60); // Assuming an average drone speed of 50 km/h
        System.out.println("Total trip time: " + tripTime + " minutes");

        for (int i = tripTime * MINUTES_PER_SECOND; i >= 0; i -= 1 * MINUTES_PER_SECOND) {
            System.out.println("Countdown: " + i / MINUTES_PER_SECOND + " minutes to reach " + clientName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to calculate distance between two coordinates (latitude and longitude)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    // Method to get the nearest hub based on coordinates
    private Hub getNearestHub(double latitude, double longitude) {
        List<Hub> hubs = getAllHubs();
        Hub nearestHub = null;
        double minDistance = Double.MAX_VALUE;

        for (Hub hub : hubs) {
            double distance = calculateDistance(latitude, longitude, hub.getLatitude(), hub.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                nearestHub = hub;
            }
        }
        return nearestHub;
    }

    // Method to fetch a client by UUID
    private Client getClientByUUID(UUID clientUuid) {
        return restTemplate.getForObject("http://localhost:9876/api/clients/" + clientUuid, Client.class);
    }

    // Method to fetch a merchandiser by UUID
    private Merchandiser getMerchandiserByUUID(UUID merchandiserUuid) {
        return restTemplate.getForObject("http://localhost:9876/api/merchandisers/" + merchandiserUuid, Merchandiser.class);
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
