import java.util.*;

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class demonstrates how confirmed
 * bookings can be cancelled safely.
 *
 * Inventory is restored and rollback
 * history is maintained.
 *
 * @version 10.0
 */
public class BookMyStayApp {

    /**
     * CLASS - RoomInventory
     * Manages room availability.
     */
    static class RoomInventory {

        private Map<String, Integer> rooms;

        public RoomInventory() {
            rooms = new HashMap<>();
            rooms.put("Single", 5); // initial count
        }

        public void incrementRoom(String roomType) {
            rooms.put(roomType, rooms.getOrDefault(roomType, 0) + 1);
        }

        public int getAvailableRooms(String roomType) {
            return rooms.getOrDefault(roomType, 0);
        }
    }

    /**
     * CLASS - BookingHistory
     * Stores active reservations.
     */
    static class BookingHistory {

        private Map<String, String> reservations = new HashMap<>();

        public void addReservation(String reservationId, String roomType) {
            reservations.put(reservationId, roomType);
        }

        public boolean exists(String reservationId) {
            return reservations.containsKey(reservationId);
        }

        public String removeReservation(String reservationId) {
            return reservations.remove(reservationId);
        }
    }

    /**
     * CLASS - CancellationService
     * Handles cancellation and rollback.
     */
    static class CancellationService {

        private Stack<String> rollbackStack = new Stack<>();

        public void cancelBooking(String reservationId,
                                  BookingHistory history,
                                  RoomInventory inventory) {

            // Validate existence
            if (!history.exists(reservationId)) {
                System.out.println("Cancellation failed: Reservation not found.");
                return;
            }

            // Remove booking
            String roomType = history.removeReservation(reservationId);

            // Restore inventory
            inventory.incrementRoom(roomType);

            // Push to rollback stack
            rollbackStack.push(reservationId);

            // Success message
            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        }

        public void printRollbackHistory() {
            System.out.println("\nRollback History (Most Recent First):");
            for (int i = rollbackStack.size() - 1; i >= 0; i--) {
                System.out.println("Released Reservation ID: " + rollbackStack.get(i));
            }
        }
    }

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        // Simulate a confirmed booking
        String reservationId = "Single-1";
        history.addReservation(reservationId, "Single");

        // Perform cancellation
        cancellationService.cancelBooking(reservationId, history, inventory);

        // Print rollback history
        cancellationService.printRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getAvailableRooms("Single"));
    }
}