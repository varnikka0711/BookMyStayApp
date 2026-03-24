import java.util.*;

/**
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class demonstrates how
 * confirmed bookings are stored
 * and reported.
 *
 * The system maintains an ordered
 * audit trail of reservations.
 *
 * @version 8.0
 */
public class BookMyStayApp {

    /**
     * CLASS - Reservation
     * Represents a confirmed booking.
     */
    static class Reservation {
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

    /**
     * CLASS - BookingHistory
     * Maintains ordered list of reservations.
     */
    static class BookingHistory {

        private List<Reservation> reservations;

        public BookingHistory() {
            reservations = new ArrayList<>();
        }

        public void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }

        public List<Reservation> getAllReservations() {
            return reservations;
        }
    }

    /**
     * CLASS - BookingReportService
     * Handles reporting logic.
     */
    static class BookingReportService {

        public void printBookingReport(List<Reservation> reservations) {
            System.out.println("\nBooking History Report");
            for (Reservation r : reservations) {
                System.out.println("Guest: " + r.getGuestName() +
                        ", Room Type: " + r.getRoomType());
            }
        }
    }

    /**
     * Application entry point.
     *
     * @param args Command-Line arguments
     */
    public static void main(String[] args) {

        System.out.println("Booking History and Reporting");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Add confirmed bookings
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.printBookingReport(history.getAllReservations());
    }
}