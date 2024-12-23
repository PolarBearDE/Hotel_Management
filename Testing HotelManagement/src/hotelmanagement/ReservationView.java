package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationView extends JFrame {
    private JList<String> roomSelectionList;
    private JTextField customerNameField, customerSurnameField, customerIDField, customerCountryField, fromDateField, toDateField;
    private JLabel totalPriceLabel;
    private JButton reserveButton, groupReserveButton;

    private List<Room> rooms;

    public ReservationView() {
        this(new ArrayList<>()); // Default constructor καλεί τον constructor με λίστα
    }

    public ReservationView(List<Room> rooms) {
        this.rooms = rooms;

        setTitle("Room Reservation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        formPanel.add(new JLabel("Available Rooms:"));
        roomSelectionList = new JList<>(
                rooms.stream()
                        .filter(room -> "Available".equals(room.getRoomStatus()))
                        .map(Room::getRoomNumber)
                        .toArray(String[]::new)
        );
        formPanel.add(new JScrollPane(roomSelectionList));

        formPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        formPanel.add(customerNameField);

        formPanel.add(new JLabel("Customer Surname:"));
        customerSurnameField = new JTextField();
        formPanel.add(customerSurnameField);

        formPanel.add(new JLabel("Customer ID:"));
        customerIDField = new JTextField();
        formPanel.add(customerIDField);

        formPanel.add(new JLabel("Customer Country:"));
        customerCountryField = new JTextField();
        formPanel.add(customerCountryField);

        formPanel.add(new JLabel("From Date (YYYY-MM-DD):"));
        fromDateField = new JTextField();
        formPanel.add(fromDateField);

        formPanel.add(new JLabel("To Date (YYYY-MM-DD):"));
        toDateField = new JTextField();
        formPanel.add(toDateField);

        formPanel.add(new JLabel("Total Price:"));
        totalPriceLabel = new JLabel("0.0");
        formPanel.add(totalPriceLabel);

        reserveButton = new JButton("Reserve Room");
        reserveButton.addActionListener(e -> reserveRoom());
        formPanel.add(reserveButton);

        groupReserveButton = new JButton("Reserve Group");
        groupReserveButton.addActionListener(e -> reserveGroup());
        formPanel.add(groupReserveButton);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void reserveRoom() {
        String roomNumber = roomSelectionList.getSelectedValue();
        if (roomNumber == null) {
            JOptionPane.showMessageDialog(this, "Please select a room!");
            return;
        }

        reserve(roomNumber, false);
    }

    private void reserveGroup() {
        List<String> selectedRooms = roomSelectionList.getSelectedValuesList();
        if (selectedRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one room!");
            return;
        }

        reserve(String.join(",", selectedRooms), true);
    }

    private void reserve(String roomNumberOrGroup, boolean isGroup) {
        String customerName = customerNameField.getText();
        String customerSurname = customerSurnameField.getText();
        String customerID = customerIDField.getText();
        String customerCountry = customerCountryField.getText();
        String fromDate = fromDateField.getText();
        String toDate = toDateField.getText();

        if (customerName.isEmpty() || customerSurname.isEmpty() || customerID.isEmpty() || customerCountry.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        double totalPrice = calculateTotalPrice(roomNumberOrGroup, fromDate, toDate, isGroup);
        saveReservationToFile(customerName, customerSurname, customerID, customerCountry, roomNumberOrGroup, fromDate, toDate, totalPrice);

        if (!isGroup) {
            updateRoomStatus(roomNumberOrGroup, "Occupied");  // Καλούμε τη μέθοδο με το νέο status "Occupied"
        } else {
            for (String room : roomNumberOrGroup.split(",")) {
                updateRoomStatus(room, "Occupied");  // Καλούμε τη μέθοδο για κάθε δωμάτιο με το νέο status "Occupied"
            }
        }

        JOptionPane.showMessageDialog(this, "Reservation completed successfully!");
    }


    private void saveReservationToFile(String customerName, String customerSurname, String customerID,
                                       String customerCountry, String roomNumberOrGroup, String fromDate,
                                       String toDate, double totalPrice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reservations.txt", true))) {
            writer.write(customerName + "," + customerSurname + "," + customerID + "," + customerCountry + "," +
                    roomNumberOrGroup + "," + fromDate + "," + toDate + "," + totalPrice + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving reservation: " + e.getMessage());
        }
    }

    private double calculateTotalPrice(String roomNumberOrGroup, String fromDate, String toDate, boolean isGroup) {
        List<String> roomNumbers = List.of(roomNumberOrGroup.split(","));
        double totalBasePrice = roomNumbers.stream()
                .mapToDouble(this::getBasePriceForRoom)
                .sum();
        int numberOfNights = calculateNumberOfNights(fromDate, toDate);
        double totalPrice = totalBasePrice * numberOfNights;

        if (isGroup && calculateTotalGuests(roomNumbers) >= 20) {
            totalPrice *= 0.75;
        }

        return totalPrice;
    }

    private int calculateTotalGuests(List<String> roomNumbers) {
        return roomNumbers.stream()
                .mapToInt(this::getRoomCapacity)
                .sum();
    }

    private int calculateNumberOfNights(String fromDate, String toDate) {
        String[] fromParts = fromDate.split("-");
        String[] toParts = toDate.split("-");
        int fromDay = Integer.parseInt(fromParts[2]);
        int toDay = Integer.parseInt(toParts[2]);
        return toDay - fromDay;
    }

    private double getBasePriceForRoom(String roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber().equals(roomNumber))
                .findFirst()
                .map(Room::getPrice)
                .orElse(0.0);
    }

    private int getRoomCapacity(String roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber().equals(roomNumber))
                .findFirst()
                .map(Room::getCapacity)
                .orElse(1);
    }

    private void updateRoomStatus(String roomNumberOrGroup, String newStatus) {
        List<String> selectedRoomNumbers = List.of(roomNumberOrGroup.split(","));
        List<String> updatedRooms = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] roomData = line.split(",");
                if (roomData.length < 5) {
                    System.out.println("Skipping invalid line: " + line);  // Εκτύπωση για γραμμές με λιγότερα δεδομένα
                    continue;  // Προχωράμε στην επόμενη γραμμή αν τα δεδομένα είναι ελλιπή
                }

                String roomNumber = roomData[0];
                String roomType = roomData[1];
                String amenities = roomData[2];
                String status = roomData[3];
                double price = 0.0;

                // Προσπάθεια να μετατρέψουμε την τιμή σε double
                try {
                    price = Double.parseDouble(roomData[4]);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping room with invalid price: " + line);
                    continue;  // Αν η τιμή δεν είναι έγκυρη, παραλείπουμε αυτή τη γραμμή
                }

                // Αν το δωμάτιο είναι επιλεγμένο και το status του είναι "Available", το αλλάζουμε σε "Occupied"
                if (selectedRoomNumbers.contains(roomNumber) && "Available".equals(status)) {
                    status = newStatus;  // Αλλάζουμε το status σε αυτό που έχει περάσει
                }

                // Προσθέτουμε το ενημερωμένο δωμάτιο στη λίστα
                updatedRooms.add(roomNumber + "," + roomType + "," + amenities + "," + status + "," + price);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading rooms file: " + e.getMessage());
            return;
        }

        // Επαναγράφουμε το αρχείο rooms.txt με τις ενημερωμένες καταστάσεις
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (String updatedRoom : updatedRooms) {
                writer.write(updatedRoom + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating room status: " + e.getMessage());
        }
    }




}






//main CODE!!!!
//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.util.List;
//
//public class ReservationView extends JFrame {
//    private JComboBox<String> roomSelection;
//    private JTextField customerNameField, customerSurnameField, customerIDField, customerCountryField, fromDateField, toDateField;
//    private JLabel totalPriceLabel;
//    private JButton reserveButton;
//
//    private List<Room> rooms;
//
//    public ReservationView(List<Room> rooms) {
//        this.rooms = rooms;
//
//        setTitle("Room Reservation");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(800, 400);
//
//        // Δημιουργία πίνακα φόρμας για την είσοδο των δεδομένων
//        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
//
//        formPanel.add(new JLabel("Available Rooms:"));
//        roomSelection = new JComboBox<>(rooms.stream()
//                .filter(room -> "Available".equals(room.getRoomStatus()))
//                .map(Room::getRoomNumber)
//                .toArray(String[]::new));
//        formPanel.add(roomSelection);
//
//        formPanel.add(new JLabel("Customer Name:"));
//        customerNameField = new JTextField();
//        formPanel.add(customerNameField);
//
//        formPanel.add(new JLabel("Customer Surname:"));
//        customerSurnameField = new JTextField();
//        formPanel.add(customerSurnameField);
//
//        formPanel.add(new JLabel("Customer ID:"));
//        customerIDField = new JTextField();
//        formPanel.add(customerIDField);
//
//        formPanel.add(new JLabel("Customer Country:"));
//        customerCountryField = new JTextField();
//        formPanel.add(customerCountryField);
//
//        formPanel.add(new JLabel("From Date (YYYY-MM-DD):"));
//        fromDateField = new JTextField();
//        formPanel.add(fromDateField);
//
//        formPanel.add(new JLabel("To Date (YYYY-MM-DD):"));
//        toDateField = new JTextField();
//        formPanel.add(toDateField);
//
//        formPanel.add(new JLabel("Total Price:"));
//        totalPriceLabel = new JLabel("0.0");
//        formPanel.add(totalPriceLabel);
//
//        // Δημιουργία κουμπιού για την ολοκλήρωση της κράτησης
//        reserveButton = new JButton("Reserve Room");
//        reserveButton.addActionListener(e -> reserveRoom());
//        formPanel.add(reserveButton);
//
//        add(formPanel, BorderLayout.CENTER);
//        setVisible(true);
//    }
//
//    private void reserveRoom() {
//        String roomNumber = (String) roomSelection.getSelectedItem();
//        String customerName = customerNameField.getText();
//        String customerSurname = customerSurnameField.getText();
//        String customerID = customerIDField.getText();
//        String customerCountry = customerCountryField.getText();
//        String fromDate = fromDateField.getText();
//        String toDate = toDateField.getText();
//
//        if (roomNumber == null || customerName.isEmpty() || customerSurname.isEmpty() || customerID.isEmpty() || customerCountry.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "All fields are required!");
//            return;
//        }
//
//        // Εύρεση του επιλεγμένου δωματίου
//        Room selectedRoom = rooms.stream()
//                .filter(room -> room.getRoomNumber().equals(roomNumber))
//                .findFirst().orElse(null);
//
//        if (selectedRoom != null) {
//            selectedRoom.setStatus("Occupied");
//            saveRoomStatusToFile(roomNumber, "Occupied");  // Αποθήκευση μόνο του νέου status
//            double totalPrice = calculateTotalPrice(roomNumber, fromDate, toDate);
//            saveReservationToFile(customerName, customerSurname, customerID, customerCountry, roomNumber, fromDate, toDate, totalPrice);
//            JOptionPane.showMessageDialog(this, "Reservation completed successfully!");
//        }
//    }
//
//    // Save reservation details to the reservations.txt file
//    private void saveReservationToFile(String customerName, String customerSurname, String customerID,
//                                       String customerCountry, String roomNumber, String fromDate,
//                                       String toDate, double totalPrice) {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("reservations.txt", true));
//            writer.write(customerName + "," + customerSurname + "," + customerID + "," + customerCountry + "," +
//                    roomNumber + "," + fromDate + "," + toDate + "," + totalPrice + "\n");
//            writer.close();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving reservation: " + e.getMessage());
//        }
//    }
//
//    // Calculate the total price based on the room and dates
//    private double calculateTotalPrice(String roomNumber, String fromDate, String toDate) {
//        double basePrice = getBasePriceForRoom(roomNumber);
//        int numberOfNights = calculateNumberOfNights(fromDate, toDate);
//        double totalPrice = basePrice * numberOfNights;
//
//        // Προσθήκη λογικής για γκρουπ και εκπτώσεις
//        if (numberOfNights > 20) {
//            totalPrice *= 0.75;  // 25% έκπτωση για γκρουπ
//        }
//
//        // Ειδική έκπτωση αν η πληρότητα είναι κάτω από 30%
//        if (isHotelOccupancyLow()) {
//            totalPrice *= 0.90;  // Μείωση 10% της τιμής πόρτας
//        }
//
//        return totalPrice;
//    }
//
//    // Calculate the number of nights between the fromDate and toDate
//    private int calculateNumberOfNights(String fromDate, String toDate) {
//        // Υλοποιήστε τον υπολογισμό των ημερών μεταξύ των δύο ημερομηνιών
//        // Εδώ θεωρούμε απλό υπολογισμό, μπορείτε να το αντικαταστήσετε με πραγματική υλοποίηση
//        String[] fromParts = fromDate.split("-");
//        String[] toParts = toDate.split("-");
//        int fromDay = Integer.parseInt(fromParts[2]);
//        int toDay = Integer.parseInt(toParts[2]);
//        return toDay - fromDay;
//    }
//
//    // Dummy methods for the sake of example, replace them with actual logic
//    private double getBasePriceForRoom(String roomNumber) {
//        double basePrice = 0.0;
//        try {
//            // Διαβάζουμε το αρχείο rooms.txt για να βρούμε τη βασική τιμή του δωματίου
//            BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                String[] roomData = line.split(",");
//                if (roomData[0].equals(roomNumber)) {
//                    // Επιστρέφουμε την τιμή από το αρχείο για το συγκεκριμένο δωμάτιο
//                    basePrice = Double.parseDouble(roomData[4]); // Τιμή είναι στο πέμπτο πεδίο (index 4)
//                    break;
//                }
//            }
//            reader.close();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error reading room data: " + e.getMessage());
//        }
//        return basePrice;
//    }
//
//    private boolean isHotelOccupancyLow() {
//        int totalRooms = rooms.size();
//        int occupiedRooms = (int) rooms.stream().filter(room -> "Occupied".equals(room.getRoomStatus())).count();
//        double occupancyRate = (double) occupiedRooms / totalRooms * 100;
//        return occupancyRate < 30;  // If occupancy is below 30%, apply the discount
//    }
//
//    // Save room status to a file (only changes status, not other details)
//    private void saveRoomStatusToFile(String roomNumber, String newStatus) {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"));
//            StringBuilder fileContent = new StringBuilder();
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                String[] roomData = line.split(",");
//                if (roomData[0].equals(roomNumber)) {
//                    roomData[3] = newStatus; // Update room status
//                }
//                fileContent.append(String.join(",", roomData)).append("\n");
//            }
//            reader.close();
//
//            BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"));
//            writer.write(fileContent.toString());
//            writer.close();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving room data: " + e.getMessage());
//        }
//    }
//}



//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.util.List;
//
//public class ReservationView extends JFrame {
//    private JComboBox<String> roomSelection;
//    private JTextField customerNameField, customerSurnameField, customerIDField, customerCountryField, fromDateField, toDateField;
//    private JLabel totalPriceLabel;
//    private JButton reserveButton;
//
//    private List<Room> rooms;
//
//    public ReservationView(List<Room> rooms) {
//        this.rooms = rooms;
//
//        setTitle("Room Reservation");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(800, 400);
//
//        // Δημιουργία πίνακα φόρμας για την είσοδο των δεδομένων
//        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
//
//        formPanel.add(new JLabel("Available Rooms:"));
//        roomSelection = new JComboBox<>(rooms.stream()
//                .filter(room -> "Available".equals(room.getRoomStatus()))
//                .map(Room::getRoomNumber)
//                .toArray(String[]::new));
//        formPanel.add(roomSelection);
//
//        formPanel.add(new JLabel("Customer Name:"));
//        customerNameField = new JTextField();
//        formPanel.add(customerNameField);
//
//        formPanel.add(new JLabel("Customer Surname:"));
//        customerSurnameField = new JTextField();
//        formPanel.add(customerSurnameField);
//
//        formPanel.add(new JLabel("Customer ID:"));
//        customerIDField = new JTextField();
//        formPanel.add(customerIDField);
//
//        formPanel.add(new JLabel("Customer Country:"));
//        customerCountryField = new JTextField();
//        formPanel.add(customerCountryField);
//
//        formPanel.add(new JLabel("From Date (YYYY-MM-DD):"));
//        fromDateField = new JTextField();
//        formPanel.add(fromDateField);
//
//        formPanel.add(new JLabel("To Date (YYYY-MM-DD):"));
//        toDateField = new JTextField();
//        formPanel.add(toDateField);
//
//        formPanel.add(new JLabel("Total Price:"));
//        totalPriceLabel = new JLabel("0.0");
//        formPanel.add(totalPriceLabel);
//
//        // Δημιουργία κουμπιού για την ολοκλήρωση της κράτησης
//        reserveButton = new JButton("Reserve Room");
//        reserveButton.addActionListener(e -> reserveRoom());
//        formPanel.add(reserveButton);
//
//        add(formPanel, BorderLayout.CENTER);
//        setVisible(true);
//    }
//
//    private void reserveRoom() {
//        String roomNumber = (String) roomSelection.getSelectedItem();
//        String customerName = customerNameField.getText();
//        String customerSurname = customerSurnameField.getText();
//        String customerID = customerIDField.getText();
//        String customerCountry = customerCountryField.getText();
//        String fromDate = fromDateField.getText();
//        String toDate = toDateField.getText();
//
//        if (roomNumber == null || customerName.isEmpty() || customerSurname.isEmpty() || customerID.isEmpty() || customerCountry.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "All fields are required!");
//            return;
//        }
//
//        // Εύρεση του επιλεγμένου δωματίου
//        Room selectedRoom = rooms.stream()
//                .filter(room -> room.getRoomNumber().equals(roomNumber))
//                .findFirst().orElse(null);
//
//        if (selectedRoom != null) {
//            selectedRoom.setStatus("Occupied");
//            saveRoomStatusToFile(roomNumber, "Occupied");  // Αποθήκευση μόνο του νέου status
//            JOptionPane.showMessageDialog(this, "Reservation completed successfully!");
//        }
//    }
//
//    // Save only the updated room status to the rooms.txt file
//    private void saveRoomStatusToFile(String roomNumber, String newStatus) {
//        try {
//            // Διαβάζουμε το αρχείο rooms.txt και το αποθηκεύουμε σε μια λίστα
//            BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"));
//            StringBuilder fileContent = new StringBuilder();
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                String[] roomData = line.split(",");
//                if (roomData[0].equals(roomNumber)) {
//                    roomData[3] = newStatus; // Ενημέρωση του status για το δωμάτιο
//                }
//                fileContent.append(String.join(",", roomData)).append("\n");
//            }
//            reader.close();
//
//            // Γράφουμε το νέο περιεχόμενο στο αρχείο rooms.txt
//            BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"));
//            writer.write(fileContent.toString());
//            writer.close();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving room data: " + e.getMessage());
//        }
//    }
//}
