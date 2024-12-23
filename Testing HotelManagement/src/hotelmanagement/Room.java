package hotelmanagement;


import java.util.Arrays;
import java.util.List;

public class Room {
    private String roomNumber;
    private String roomType;
    private boolean hasFridge;
    private boolean hasHairDryer;
    private boolean hasAirConditioner;
    private boolean isClean;
    private boolean hasProblems;
    private boolean isAvailable;
    private List<String> amenities;
    private double price;
    private String roomStatus;  // Προσθέτουμε το πεδίο roomStatus

    // Ενημερωμένος κατασκευαστής για amenities και τιμή
    public Room(String roomNumber, String roomType, boolean hasFridge, boolean hasHairDryer, boolean hasAirConditioner, List<String> amenities, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.hasFridge = hasFridge;
        this.hasHairDryer = hasHairDryer;
        this.hasAirConditioner = hasAirConditioner;
        this.isClean = true;
        this.hasProblems = false;
        this.isAvailable = true;
        this.amenities = amenities;
        this.price = price;
    }

    // Νέος κατασκευαστής για τη δημιουργία δωματίου με μόνο αριθμό και τύπο
    public Room(String roomNumber, String roomType, String amenities, String roomStatus, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.amenities = Arrays.asList(amenities.split(";")); // Μετατροπή σε List<String>
        this.roomStatus = roomStatus;  // Ορισμός του roomStatus
        this.price = price;
        this.hasFridge = amenities.contains("Fridge");
        this.hasHairDryer = amenities.contains("Hairdryer");
        this.hasAirConditioner = amenities.contains("Air Conditioning");
        this.isClean = true; // Προεπιλεγμένη τιμή
        this.hasProblems = false; // Προεπιλεγμένη τιμή
    }

    // Getters και Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }

    public boolean hasProblems() {
        return hasProblems;
    }

    public void setProblems(boolean problems) {
        hasProblems = problems;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRoomStatus() {
        if (!isAvailable) {
            return "Occupied";
        }
        return "Available";
    }

    public void setStatus(String status) {
        if (status.equalsIgnoreCase("Available")) {
            this.isAvailable = true;
            this.isClean = true;
            this.hasProblems = false;
        } else if (status.equalsIgnoreCase("Occupied")) {
            this.isAvailable = false;
        } else if (status.equalsIgnoreCase("Needs Cleaning")) {
            this.isAvailable = false;
            this.isClean = false;
        } else if (status.equalsIgnoreCase("Needs Repairs")) {
            this.isAvailable = false;
            this.hasProblems = true;
        }
    }

    public String getRoomDetails() {
        return "Room " + roomNumber + " (" + roomType + "): " +
                "Fridge: " + (hasFridge ? "Yes" : "No") + ", " +
                "Hair Dryer: " + (hasHairDryer ? "Yes" : "No") + ", " +
                "Air Conditioner: " + (hasAirConditioner ? "Yes" : "No") + ", " +
                "Price: " + price;
    }

    public int getCapacity() {
        switch (roomType.toLowerCase()) {
            case "single":
                return 1;
            case "double":
                return 2;
            case "triple":
                return 3;
            case "queen":
                return 2;
            case "suite":
                return 2;
            default:
                return 0; // Άγνωστος τύπος δωματίου
        }
    }
}

