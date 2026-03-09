import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocator = new RoomAllocationService();

        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Double"));
        bookingQueue.addRequest(new Reservation("Vamathi", "Suite"));

        while (bookingQueue.hasPendingRequests()) {
            Reservation r = bookingQueue.getNextRequest();
            allocator.allocateRoom(r, inventory);
        }
    }
}

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public int getAvailableRooms(String type) {
        return availability.get(type);
    }

    public void decreaseRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

class RoomAllocationService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Integer> roomCounter = new HashMap<>();

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();

        if (inventory.getAvailableRooms(type) > 0) {

            String roomId = generateRoomId(type);

            allocatedRoomIds.add(roomId);

            inventory.decreaseRoom(type);

            System.out.println("Booking confirmed for Guest: "
                    + reservation.getGuestName()
                    + ", Room ID: " + roomId);
        } else {
            System.out.println("No rooms available for " + type);
        }
    }

    private String generateRoomId(String type) {

        int count = roomCounter.getOrDefault(type, 0) + 1;
        roomCounter.put(type, count);

        return type + "-" + count;
    }
}