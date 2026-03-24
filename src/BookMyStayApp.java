import java.util.*;

/**
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * Demonstrates validation and error handling
 * using custom exceptions and fail-fast design.
 *
 * @version 9.0
 */
public class BookMyStayApp {

    /**
     * CUSTOM EXCEPTION
     * Represents invalid booking scenarios.
     */
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    /**
     * CLASS - RoomInventory
     * Maintains available room counts.
     */
    static class RoomInventory {

        private Map<String, Integer> rooms;

        public RoomInventory() {
            rooms = new HashMap<>();
            rooms.put("Single", 2);
            rooms.put("Double", 2);
            rooms.put("Suite", 1);
        }

        public boolean isAvailable(String roomType) {
            return rooms.containsKey(roomType) && rooms.get(roomType) > 0;
        }

        public void allocateRoom(String roomType) throws InvalidBookingException {
            if (!rooms.containsKey(roomType)) {
                throw new InvalidBookingException("Invalid room type selected.");
            }

            int count = rooms.get(roomType);

            if (count <= 0) {
                throw new InvalidBookingException("No rooms available for selected type.");
            }

            rooms.put(roomType, count - 1);
        }
    }

    /**
     * CLASS - ReservationValidator
     * Validates booking input.
     */
    static class ReservationValidator {

        public void validate(String guestName, String roomType) throws InvalidBookingException {

            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            // Case-sensitive validation as required
            if (!roomType.equals("Single") &&
                    !roomType.equals("Double") &&
                    !roomType.equals("Suite")) {

                throw new InvalidBookingException("Invalid room type selected.");
            }
        }
    }

    /**
     * CLASS - BookingRequestQueue
     * Simulates booking queue (basic placeholder).
     */
    static class BookingRequestQueue {

        private Queue<String> queue = new LinkedList<>();

        public void addRequest(String guestName) {
            queue.offer(guestName);
        }
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validation (fail-fast)
            validator.validate(guestName, roomType);

            // Add to queue
            bookingQueue.addRequest(guestName);

            // Allocate room
            inventory.allocateRoom(roomType);

            // Success message
            System.out.println("Booking successful!");

        } catch (InvalidBookingException e) {

            // Handle domain-specific validation errors
            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}