import java.util.*;

/**
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Demonstrates thread-safe booking using synchronized access.
 *
 * @version 11.0
 */
public class BookMyStayApp {

    /**
     * CLASS - BookingRequest
     */
    static class BookingRequest {
        String guestName;
        String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    /**
     * CLASS - BookingRequestQueue
     * Shared queue for requests
     */
    static class BookingRequestQueue {
        private Queue<BookingRequest> queue = new LinkedList<>();

        public synchronized void addRequest(BookingRequest request) {
            queue.offer(request);
        }

        public synchronized BookingRequest getRequest() {
            return queue.poll();
        }
    }

    /**
     * CLASS - RoomInventory
     * Shared inventory (thread-safe)
     */
    static class RoomInventory {

        private Map<String, Integer> rooms = new HashMap<>();
        private Map<String, Integer> counters = new HashMap<>();

        public RoomInventory() {
            rooms.put("Single", 5);
            rooms.put("Double", 3);
            rooms.put("Suite", 2);

            counters.put("Single", 0);
            counters.put("Double", 0);
            counters.put("Suite", 0);
        }

        public synchronized String allocateRoom(String roomType) {

            if (!rooms.containsKey(roomType) || rooms.get(roomType) <= 0) {
                return null;
            }

            // decrement inventory
            rooms.put(roomType, rooms.get(roomType) - 1);

            // increment counter for unique room ID
            int count = counters.get(roomType) + 1;
            counters.put(roomType, count);

            return roomType + "-" + count;
        }

        public void printInventory() {
            System.out.println("\nRemaining Inventory:");
            for (String type : rooms.keySet()) {
                System.out.println(type + ": " + rooms.get(type));
            }
        }
    }

    /**
     * CLASS - AllocationService
     */
    static class AllocationService {

        public void allocate(BookingRequest request, RoomInventory inventory) {

            String roomId = inventory.allocateRoom(request.roomType);

            if (roomId != null) {
                System.out.println("Booking confirmed for Guest: "
                        + request.guestName + ", Room ID: " + roomId);
            }
        }
    }

    /**
     * CLASS - ConcurrentBookingProcessor
     * Runnable thread
     */
    static class ConcurrentBookingProcessor implements Runnable {

        private BookingRequestQueue queue;
        private RoomInventory inventory;
        private AllocationService service;

        public ConcurrentBookingProcessor(BookingRequestQueue queue,
                                          RoomInventory inventory,
                                          AllocationService service) {
            this.queue = queue;
            this.inventory = inventory;
            this.service = service;
        }

        @Override
        public void run() {
            while (true) {
                BookingRequest request;

                // synchronized retrieval
                synchronized (queue) {
                    request = queue.getRequest();
                }

                if (request == null) break;

                service.allocate(request, inventory);
            }
        }
    }

    /**
     * MAIN METHOD
     */
    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        // Shared components
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        AllocationService allocationService = new AllocationService();

        // Add booking requests
        bookingQueue.addRequest(new BookingRequest("Abhi", "Single"));
        bookingQueue.addRequest(new BookingRequest("Vanmathi", "Double"));
        bookingQueue.addRequest(new BookingRequest("Kural", "Suite"));
        bookingQueue.addRequest(new BookingRequest("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        // Start concurrent processing
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Print final inventory
        inventory.printInventory();
    }
}