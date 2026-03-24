import java.io.*;
import java.util.*;

/**
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates how system state
 * can be restored after an application restart.
 *
 * Inventory data is loaded from a file
 * before any booking operations occur.
 *
 * @version 12.0
 */
public class BookMyStayApp {

    /**
     * CLASS - RoomInventory
     * Serializable inventory state.
     */
    static class RoomInventory implements Serializable {

        private static final long serialVersionUID = 1L;

        private Map<String, Integer> rooms;

        public RoomInventory() {
            rooms = new HashMap<>();
            rooms.put("Single", 5);
            rooms.put("Double", 3);
            rooms.put("Suite", 2);
        }

        public Map<String, Integer> getRooms() {
            return rooms;
        }

        public void printInventory() {
            System.out.println("\nCurrent Inventory:");
            for (String type : rooms.keySet()) {
                System.out.println(type + ": " + rooms.get(type));
            }
        }
    }

    /**
     * CLASS - PersistenceService
     * Handles save and load operations.
     */
    static class PersistenceService {

        private static final String FILE_NAME = "inventory.dat";

        /**
         * Save inventory to file
         */
        public void saveInventory(RoomInventory inventory) {
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(inventory);
                System.out.println("Inventory saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving inventory.");
            }
        }

        /**
         * Load inventory from file
         */
        public RoomInventory loadInventory() {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                return (RoomInventory) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                return null;
            }
        }
    }

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("System Recovery");

        PersistenceService persistenceService = new PersistenceService();

        // Attempt to load existing inventory
        RoomInventory inventory = persistenceService.loadInventory();

        if (inventory == null) {
            System.out.println("No valid inventory data found. Starting fresh.");
            inventory = new RoomInventory();
        }

        // Display inventory
        inventory.printInventory();

        // Save inventory before shutdown
        persistenceService.saveInventory(inventory);
    }
}