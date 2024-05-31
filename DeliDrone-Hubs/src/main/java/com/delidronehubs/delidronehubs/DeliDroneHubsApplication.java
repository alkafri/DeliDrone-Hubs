package com.delidronehubs.delidronehubs;

import com.delidronehubs.delidronehubs.classes.Client;
import com.delidronehubs.delidronehubs.classes.Drone;
import com.delidronehubs.delidronehubs.classes.Hub;
import com.delidronehubs.delidronehubs.classes.Merchandiser;
import com.delidronehubs.delidronehubs.services.ClientService;
import com.delidronehubs.delidronehubs.services.DroneService;
import com.delidronehubs.delidronehubs.services.HubService;
import com.delidronehubs.delidronehubs.services.MerchandiserService;
import com.delidronehubs.delidronehubs.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class DeliDroneHubsApplication implements CommandLineRunner {
    @Autowired
    private HubService hubService;
    @Autowired
    private DroneService droneService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private MerchandiserService merchandiserService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliverySimulation deliverySimulation;
    @Autowired
    private DeliverySimulationManual deliverySimulationManual;

    public static void main(String[] args) {
        SpringApplication.run(DeliDroneHubsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("========== DeliDrone-Hubs Menu ==========");
            System.out.println("LIST OPERATIONS:");
            System.out.println("1- List all hubs");
            System.out.println("2- List all drones");
            System.out.println("3- List all clients");
            System.out.println("4- List all merchandisers");
            System.out.println("5- SIMULATE ORDER DELIVERY [AUTOMATIC-RANDOM]");
            System.out.println("6- SIMULATE ORDER DELIVERY [MANUAL]");
            System.out.println("7- Exit DeliDrone-Hubs");
            System.out.println("====================================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listAllHubs();
                    break;
                case 2:
                    listAllDrones();
                    break;
                case 3:
                    listAllClients();
                    break;
                case 4:
                    listAllMerchandisers();
                    break;
                case 5:
                    deliverySimulation.simulateDelivery();
                    break;
                case 6:
                    deliverySimulationManual.simulateManualDelivery();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting DeliDrone-Hubs. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
        scanner.close();
    }

    private void listAllHubs() {
        List<Hub> hubs = hubService.getAllHubs();
        if (hubs.isEmpty()) {
            System.out.println("No hubs found.");
        } else {
            hubs.forEach(hub -> System.out.println(hub.toString()));
        }
    }

    private void listAllDrones() {
        List<Drone> drones = droneService.getAllDrones();
        if (drones.isEmpty()) {
            System.out.println("No drones found.");
        } else {
            drones.forEach(drone -> System.out.println(drone.toString()));
        }
    }

    private void listAllClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            clients.forEach(client -> System.out.println(client.toString()));
        }
    }

    private void listAllMerchandisers() {
        List<Merchandiser> merchandisers = merchandiserService.getAllMerchandisers();
        if (merchandisers.isEmpty()) {
            System.out.println("No merchandisers found.");
        } else {
            merchandisers.forEach(merchandiser -> System.out.println(merchandiser.toString()));
        }
    }

    private Merchandiser findOrCreateMerchandiser(String name) {
        // Implement logic to find or create a Merchandiser by name
        // For simplicity, assume we are just creating a new Merchandiser
        Merchandiser merchandiser = new Merchandiser();
        merchandiser.setName(name);
        merchandiser.setEmail(name + "@example.com");
        merchandiser.setPhone("1234567890");
        merchandiser.setAddress("Unknown");
        return merchandiserService.createMerchandiser(merchandiser);
    }

    private Client findOrCreateClient(String name) {
        // Implement logic to find or create a Client by name
        // For simplicity, assume we are just creating a new Client
        Client client = new Client();
        client.setName(name);
        client.setEmail(name + "@example.com");
        client.setPhone("1234567890");
        client.setAddress("Unknown");
        return clientService.createClient(client);
    }
}
