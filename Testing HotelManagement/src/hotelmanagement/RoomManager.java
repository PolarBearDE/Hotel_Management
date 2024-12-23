package hotelmanagement;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class RoomManager {

    // Δηλώνουμε τη λίστα των δωματίων
    private static List<Room> rooms = new ArrayList<>();

    // Φόρτωση δωματίων με δυνατότητα εισαγωγής από τον υπάλληλο
    public static void loadRooms() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many rooms would you like to add?");
        int roomCount = scanner.nextInt();
        scanner.nextLine(); // Καθαρισμός του buffer του scanner

        for (int i = 0; i < roomCount; i++) {
            System.out.println("Enter room number (e.g., 101):");
            String roomNumber = scanner.nextLine();

            System.out.println("Enter room type (e.g., Single, Double, Suite, Triple):");
            String roomType = scanner.nextLine();

            System.out.println("Does the room have a fridge (true/false)?");
            boolean isFridge = scanner.nextBoolean();

            System.out.println("Does the room have a hair dryer (true/false)?");
            boolean isHairDryer = scanner.nextBoolean();

            System.out.println("Does the room have an air conditioner (true/false)?");
            boolean isAirConditioner = scanner.nextBoolean();
            scanner.nextLine(); // Καθαρισμός του buffer του scanner

            System.out.println("Enter the amenities available (separate with commas, e.g., Air Conditioner, Mini Fridge, TV):");
            String amenitiesInput = scanner.nextLine();
            List<String> amenities = List.of(amenitiesInput.split(","));

            // Ζήτηση τιμής δωματίου
            System.out.println("Enter the room price (e.g., 100.0):");
            double price = scanner.nextDouble();
            scanner.nextLine(); // Καθαρισμός του buffer του scanner

            // Δημιουργία του δωματίου με την τιμή
            rooms.add(new Room(roomNumber, roomType, isFridge, isHairDryer, isAirConditioner, amenities, price));
            System.out.println("Room added successfully: " + roomNumber);
        }

        System.out.println("Rooms loaded successfully.");
    }


    // Μέθοδος για εμφάνιση όλων των δωματίων
    public static void showRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (Room room : rooms) {
                System.out.println(room.getRoomDetails());
                System.out.println(room.getRoomStatus());
            }
        }
    }
}
