import java.io.*;
import java.util.*;

class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void display() {
        System.out.println("Current Inventory:");
        for (String key : rooms.keySet()) {
            System.out.println(key + ": " + rooms.get(key));
        }
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            System.out.println("\nInventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                inventory.addRoom(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory.");
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService service = new FilePersistenceService();

        System.out.println("System Recovery");

        service.loadInventory(inventory, filePath);

        if (inventory.getRooms().isEmpty()) {
            inventory.addRoom("single", 5);
            inventory.addRoom("double", 3);
            inventory.addRoom("suite", 2);
        }

        inventory.display();

        service.saveInventory(inventory, filePath);
    }
}