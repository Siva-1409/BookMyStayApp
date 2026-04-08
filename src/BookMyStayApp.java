import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNext() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("single", 2);
        rooms.put("double", 2);
        rooms.put("suite", 1);
    }

    public boolean allocateRoom(String type) {
        int count = rooms.getOrDefault(type, 0);
        if (count > 0) {
            rooms.put(type, count - 1);
            return true;
        }
        return false;
    }

    public void printInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String key : rooms.keySet()) {
            System.out.println(key + ": " + rooms.get(key));
        }
    }
}

class RoomAllocationService {
    public void allocate(Reservation r, RoomInventory inventory) {
        if (inventory.allocateRoom(r.getRoomType())) {
            System.out.println("Booking confirmed for Guest: " + r.getGuestName() +
                    ", Room ID: " + r.getRoomType() + "-" + r.getReservationId());
        } else {
            System.out.println("Booking failed for Guest: " + r.getGuestName());
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue,
                                      RoomInventory inventory,
                                      RoomAllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                r = bookingQueue.getNext();
            }

            synchronized (inventory) {
                allocationService.allocate(r, inventory);
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        queue.addRequest(new Reservation("Priya", "single", "1"));
        queue.addRequest(new Reservation("Rahul", "double", "1"));
        queue.addRequest(new Reservation("Kiran", "suite", "1"));
        queue.addRequest(new Reservation("Sneha", "single", "2"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        inventory.printInventory();
    }
}