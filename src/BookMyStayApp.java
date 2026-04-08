import java.util.*;

class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("single", 4);
        rooms.put("double", 3);
        rooms.put("suite", 2);
    }

    public void increaseRoom(String type) {
        rooms.put(type, rooms.getOrDefault(type, 0) + 1);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }
}

class CancellationService {
    private Stack<String> releaseStack;
    private Map<String, String> reservationRecord;

    public CancellationService() {
        releaseStack = new Stack<>();
        reservationRecord = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRecord.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (reservationRecord.containsKey(reservationId)) {
            String roomType = reservationRecord.get(reservationId);
            inventory.increaseRoom(roomType);
            releaseStack.push("Released Reservation ID: " + reservationId + ", " + roomType);
            reservationRecord.remove(reservationId);
            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        }
    }

    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");
        while (!releaseStack.isEmpty()) {
            System.out.println(releaseStack.pop());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        service.registerBooking("101", "single");

        service.cancelBooking("101", inventory);

        service.showRollbackHistory();

        System.out.println("\nUpdated Single Room Availability: " + inventory.getAvailability("single"));
    }
}