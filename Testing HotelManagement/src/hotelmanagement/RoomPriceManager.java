package hotelmanagement;

import java.io.*;
import java.util.*;

public class RoomPriceManager {
    private Map<String, Double> roomPrices;
    private static final String PRICE_FILE_NAME = "room_prices.txt";
    private static final String ROOMS_FILE_NAME = "rooms.txt";

    public RoomPriceManager() {
        roomPrices = new HashMap<>();
        loadPricesFromFile(); // Φορτώνει τις τιμές κατά την εκκίνηση
    }

    public double getPriceForRoom(String roomType) {
        return roomPrices.getOrDefault(roomType, 0.0);
    }

    public void updatePriceForRoom(String roomType, double newPrice) {
        roomPrices.put(roomType, newPrice);
        savePricesToFile(); // Αποθηκεύει τις τιμές στο αρχείο `room_prices.txt`
        updateRoomsFile(roomType, newPrice); // Ενημερώνει τις τιμές στο `rooms.txt`
    }

    public void savePricesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRICE_FILE_NAME))) {
            for (Map.Entry<String, Double> entry : roomPrices.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving room prices: " + e.getMessage());
        }
    }

    private void loadPricesFromFile() {
        File file = new File(PRICE_FILE_NAME);
        if (!file.exists()) {
            setDefaultPrices(); // Αν το αρχείο δεν υπάρχει, θέτουμε τις default τιμές
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    roomPrices.put(roomType, price);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading room prices: " + e.getMessage());
            setDefaultPrices(); // Αν υπάρξει σφάλμα, επιστρέφουμε στις default τιμές
        }
    }

    private void setDefaultPrices() {
        roomPrices.put("Single", 50.0);
        roomPrices.put("Double", 100.0);
        roomPrices.put("Triple", 150.0);
        roomPrices.put("Suite", 200.0);
        roomPrices.put("Queen", 250.0);
    }

    private void updateRoomsFile(String roomType, double newPrice) {
        File roomsFile = new File(ROOMS_FILE_NAME);
        if (!roomsFile.exists()) {
            System.err.println("Error: rooms.txt does not exist.");
            return;
        }

        try {
            // Διαβάζουμε τις γραμμές και τις επεξεργαζόμαστε
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(roomsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[1].equalsIgnoreCase(roomType)) {
                        parts[4] = String.format("%.2f", newPrice); // Ενημερώνουμε την τιμή
                    }
                    lines.add(String.join(",", parts));
                }
            }

            // Επανεγγράφουμε το αρχείο `rooms.txt`
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(roomsFile))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating rooms file: " + e.getMessage());
        }
    }
}





//package hotelmanagement;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RoomPriceManager {
//    private Map<String, Double> roomPrices;
//
//    public RoomPriceManager() {
//        roomPrices = new HashMap<>();
//        roomPrices.put("Single", 50.0);  // Default τιμές
//        roomPrices.put("Double", 100.0);
//        roomPrices.put("Triple", 150.0);
//        roomPrices.put("Suite", 200.0);
//        roomPrices.put("Queen", 250.0);
//    }
//
//    public double getPriceForRoom(String roomType) {
//        return roomPrices.getOrDefault(roomType, 0.0);  // Επιστρέφει την τιμή του δωματίου
//    }
//
//    public void updatePriceForRoom(String roomType, double newPrice) {
//        roomPrices.put(roomType, newPrice);  // Ενημέρωση τιμής για το δωμάτιο
//    }
//}
