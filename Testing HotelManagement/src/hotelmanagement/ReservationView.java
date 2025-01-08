package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationView extends JFrame {
    private JList<String> roomSelectionList;
    private JTextField customerNameField, customerSurnameField, customerIDField, customerCountryField, fromDateField, toDateField, customerAddressField;
    private JLabel totalPriceLabel;
    private JButton reserveButton, groupReserveButton;
    private JCheckBox creditCardCheckbox;
    private JTextField cardFirstNameField, cardLastNameField, cardNumberField, cardMonthField, cardYearField, cardCVCField;
    private JButton panicButton;

    private List<Room> rooms;

    public ReservationView() {
        this(new ArrayList<>());
    }

    public ReservationView(List<Room> rooms) {
        this.rooms = rooms;

        setTitle("Room Reservation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        // Main panel
        JPanel formPanel = new JPanel(new GridLayout(14, 2, 10, 10)); // Increased rows for the number of fields
        formPanel.setBackground(new Color(245, 245, 245)); // Light background color

        // Insert
        formPanel.add(new JLabel("Available Rooms:"));
        roomSelectionList = new JList<>(
                rooms.stream()
                        .filter(room -> "Available".equals(room.getRoomStatus()))
                        .map(room -> room.getRoomNumber() + " - " + room.getRoomType()) // Add room type
                        .toArray(String[]::new)
        );
        formPanel.add(new JScrollPane(roomSelectionList));

        formPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(customerNameField);

        formPanel.add(new JLabel("Customer Surname:"));
        customerSurnameField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(customerSurnameField);

        formPanel.add(new JLabel("Customer ID:"));
        customerIDField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(customerIDField);

        formPanel.add(new JLabel("Customer Country:"));
        customerCountryField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(customerCountryField);

        formPanel.add(new JLabel("Customer Address (Street):"));
        customerAddressField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(customerAddressField);

        formPanel.add(new JLabel("From Date (YYYY-MM-DD):"));
        fromDateField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(fromDateField);

        formPanel.add(new JLabel("To Date (YYYY-MM-DD):"));
        toDateField = new JTextField();
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(toDateField);

        formPanel.add(new JLabel("Total Price:"));
        totalPriceLabel = new JLabel("0.0");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalPriceLabel.setForeground(new Color(0, 128, 0)); // Green color for total price
        formPanel.add(totalPriceLabel);

        reserveButton = new JButton("Reserve Room");
        reserveButton.setFont(new Font("Arial", Font.BOLD, 14));
        reserveButton.setBackground(new Color(70, 130, 180));
        reserveButton.setForeground(Color.WHITE);
        reserveButton.addActionListener(e -> reserveRoom());
        formPanel.add(reserveButton);

        groupReserveButton = new JButton("Reserve Group");
        reserveButton.setFont(new Font("Arial", Font.BOLD, 14));
        reserveButton.setBackground(new Color(203, 110, 28));
        reserveButton.setForeground(Color.WHITE);
        groupReserveButton.addActionListener(e -> reserveGroup());
        formPanel.add(groupReserveButton);

        creditCardCheckbox = new JCheckBox("Use Credit Card");
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        creditCardCheckbox.addActionListener(e -> toggleCreditCardFields());
        formPanel.add(creditCardCheckbox);

        // Credit card fields
        formPanel.add(new JLabel("Cardholder First Name:"));
        cardFirstNameField = new JTextField();
        cardFirstNameField.setEnabled(false);
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cardFirstNameField);

        formPanel.add(new JLabel("Cardholder Last Name:"));
        cardLastNameField = new JTextField();
        cardLastNameField.setEnabled(false);
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cardLastNameField);

        formPanel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField();
        cardNumberField.setEnabled(false);
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cardNumberField);

        formPanel.add(new JLabel("Expiration Month:"));
        cardMonthField = new JTextField();
        cardMonthField.setEnabled(false);
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cardMonthField);

        formPanel.add(new JLabel("Expiration Year:"));
        cardYearField = new JTextField();
        cardYearField.setEnabled(false);
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cardYearField);

        formPanel.add(new JLabel("CVC:"));
        cardCVCField = new JTextField();
        cardCVCField.setEnabled(false);
        creditCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cardCVCField);

        // Panic button
        panicButton = new JButton("Panic Button");
        panicButton.setFont(new Font("Arial", Font.BOLD, 14));
        panicButton.setBackground(Color.RED);
        panicButton.setForeground(Color.WHITE);
        panicButton.addActionListener(e -> showPanicMessage());
        formPanel.add(panicButton);

        JLabel contactLabel = new JLabel("Having trouble? Contact Hotel Polar:\nphone: 6912345678 email: PolarHotel@gmail.com");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(contactLabel);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }



    private void toggleCreditCardFields() {
        boolean isSelected = creditCardCheckbox.isSelected();
        cardFirstNameField.setEnabled(isSelected);
        cardLastNameField.setEnabled(isSelected);
        cardNumberField.setEnabled(isSelected);
        cardMonthField.setEnabled(isSelected);
        cardYearField.setEnabled(isSelected);
        cardCVCField.setEnabled(isSelected);
    }

    private void showPanicMessage() {
        JOptionPane.showMessageDialog(this, "Alert: Policee has been notified.");
    }

    private void reserveRoom() {
        String selectedItem = roomSelectionList.getSelectedValue();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select a room!");
            return;
        }

        String roomNumber = selectedItem.split(" - ")[0]; // Extract room number
        reserve(roomNumber, false);
    }

    private void reserveGroup() {
        List<String> selectedItems = roomSelectionList.getSelectedValuesList();
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one room!");
            return;
        }

        List<String> roomNumbers = selectedItems.stream()
                .map(item -> item.split(" - ")[0]) // Extract room number
                .toList();

        int totalGuests = calculateTotalGuests(roomNumbers);
        if (totalGuests >= 20) {
            // Υπολογισμός συνολικού κόστους και προκαταβολής
            String fromDate = fromDateField.getText();
            String toDate = toDateField.getText();
            double totalPrice = calculateTotalPrice(String.join(";", roomNumbers), fromDate, toDate, true);
            double prepayment = totalPrice * 0.25;

            JOptionPane.showMessageDialog(this, String.format(
                    "The group reservation requires a prepayment of 25%%.\nPrepayment amount: %.2f", prepayment));
        }

        reserve(String.join(",", roomNumbers), true);
    }

    private void reserve(String roomNumberOrGroup, boolean isGroup) {
        String customerName = customerNameField.getText();
        String customerSurname = customerSurnameField.getText();
        String customerID = customerIDField.getText();
        String customerCountry = customerCountryField.getText();
        String customerAddress = customerAddressField.getText(); // Get address
        String fromDate = fromDateField.getText();
        String toDate = toDateField.getText();

        if (customerName.isEmpty() || customerSurname.isEmpty() || customerID.isEmpty() || customerCountry.isEmpty() || customerAddress.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (creditCardCheckbox.isSelected()) {
            String cardFirstName = cardFirstNameField.getText();
            String cardLastName = cardLastNameField.getText();
            String cardNumber = cardNumberField.getText();
            String cardMonth = cardMonthField.getText();
            String cardYear = cardYearField.getText();
            String cardCVC = cardCVCField.getText();

            if (cardFirstName.isEmpty() || cardLastName.isEmpty() || cardNumber.isEmpty() || cardMonth.isEmpty() || cardYear.isEmpty() || cardCVC.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill out all credit card details!");
                return;
            }
        }

        double totalPrice = calculateTotalPrice(roomNumberOrGroup, fromDate, toDate, isGroup);
        saveReservationToFile(customerName, customerSurname, customerID, customerCountry, customerAddress, roomNumberOrGroup, fromDate, toDate, totalPrice);

        if (!isGroup) {
            updateRoomStatus(roomNumberOrGroup, "Occupied");
        } else {
            for (String room : roomNumberOrGroup.split(",")) {
                updateRoomStatus(room, "Occupied");
            }
        }

        JOptionPane.showMessageDialog(this, "Reservation completed successfully!");
    }

    private void saveReservationToFile(String customerName, String customerSurname, String customerID,
                                       String customerCountry, String customerAddress, String roomNumberOrGroup, String fromDate,
                                       String toDate, double totalPrice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reservations.txt", true))) {
            // Checks if the reservation is a group (multiple rooms)
            if (roomNumberOrGroup.contains(",")) {
                // If it's a group, replace the comma with a semicolon
                roomNumberOrGroup = roomNumberOrGroup.replace(",", ";");
            }
            writer.write(customerName + "," + customerSurname + "," + customerID + "," + customerCountry + "," + customerAddress + "," +
                    roomNumberOrGroup + "," + fromDate + "," + toDate + "," + totalPrice + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving reservation: " + e.getMessage());
        }
    }
    private boolean isHighDemand(String date) {
        String[] parts = date.split("-");
        int month = Integer.parseInt(parts[1]);

        return month == 12 || month == 1 || month == 4 || (month >= 6 && month <= 8);
    }

    private double calculateTotalPrice(String roomNumberOrGroup, String fromDate, String toDate, boolean isGroup) {
        List<String> roomNumbers = List.of(roomNumberOrGroup.split(","));
        double totalBasePrice = roomNumbers.stream()
                .mapToDouble(this::getBasePriceForRoom)
                .sum();
        int numberOfNights = calculateNumberOfNights(fromDate, toDate);
        double totalPrice = totalBasePrice * numberOfNights;

        if (isGroup && calculateTotalGuests(roomNumbers) >= 20) {
            totalPrice *= 0.6;
        }

        // Check if any date is during high-demand periods
        if (isHighDemand(fromDate) || isHighDemand(toDate)) {
            totalPrice *= 1.2;
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
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String roomNumber = roomData[0];
                String roomType = roomData[1];
                String amenities = roomData[2];
                String status = roomData[3];
                double price = 0.0;

                try {
                    price = Double.parseDouble(roomData[4]);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping room with invalid price: " + line);
                    continue;
                }

                if (selectedRoomNumbers.contains(roomNumber) && "Available".equals(status)) {
                    status = newStatus;
                }

                updatedRooms.add(roomNumber + "," + roomType + "," + amenities + "," + status + "," + price);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading rooms file: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (String updatedRoom : updatedRooms) {
                writer.write(updatedRoom + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating room status: " + e.getMessage());
        }
    }
}



//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReservationView extends JFrame {
//    private JList<String> roomSelectionList;
//    private JTextField customerNameField, customerSurnameField, customerIDField, customerCountryField, fromDateField, toDateField, customerAddressField;
//    private JLabel totalPriceLabel;
//    private JButton reserveButton, groupReserveButton;
//    private JCheckBox creditCardCheckbox;
//    private JTextField cardFirstNameField, cardLastNameField, cardNumberField, cardMonthField, cardYearField, cardCVCField;
//    private JButton panicButton;
//
//    private List<Room> rooms;
//
//    public ReservationView() {
//        this(new ArrayList<>()); // Default constructor calls the one with the list
//    }
//
//    public ReservationView(List<Room> rooms) {
//        this.rooms = rooms;
//
//        setTitle("Room Reservation");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(800, 600);
//
//        // Main panel for form fields
//        JPanel formPanel = new JPanel(new GridLayout(14, 2, 10, 10)); // Increased rows for the number of fields
//
//        // Insert fields
//        formPanel.add(new JLabel("Available Rooms:"));
//        roomSelectionList = new JList<>(
//                rooms.stream()
//                        .filter(room -> "Available".equals(room.getRoomStatus()))
//                        .map(room -> room.getRoomNumber() + " - " + room.getRoomType()) // Add room type
//                        .toArray(String[]::new)
//        );
//        formPanel.add(new JScrollPane(roomSelectionList));
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
//        formPanel.add(new JLabel("Customer Address (Street):"));
//        customerAddressField = new JTextField();
//        formPanel.add(customerAddressField);
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
//        reserveButton = new JButton("Reserve Room");
//        reserveButton.addActionListener(e -> reserveRoom());
//        formPanel.add(reserveButton);
//
//        groupReserveButton = new JButton("Reserve Group");
//        groupReserveButton.addActionListener(e -> reserveGroup());
//        formPanel.add(groupReserveButton);
//
//        creditCardCheckbox = new JCheckBox("Use Credit Card");
//        creditCardCheckbox.addActionListener(e -> toggleCreditCardFields());
//        formPanel.add(creditCardCheckbox);
//
//        // Credit card fields
//        formPanel.add(new JLabel("Cardholder First Name:"));
//        cardFirstNameField = new JTextField();
//        cardFirstNameField.setEnabled(false);
//        formPanel.add(cardFirstNameField);
//
//        formPanel.add(new JLabel("Cardholder Last Name:"));
//        cardLastNameField = new JTextField();
//        cardLastNameField.setEnabled(false);
//        formPanel.add(cardLastNameField);
//
//        formPanel.add(new JLabel("Card Number:"));
//        cardNumberField = new JTextField();
//        cardNumberField.setEnabled(false);
//        formPanel.add(cardNumberField);
//
//        formPanel.add(new JLabel("Expiration Month:"));
//        cardMonthField = new JTextField();
//        cardMonthField.setEnabled(false);
//        formPanel.add(cardMonthField);
//
//        formPanel.add(new JLabel("Expiration Year:"));
//        cardYearField = new JTextField();
//        cardYearField.setEnabled(false);
//        formPanel.add(cardYearField);
//
//        formPanel.add(new JLabel("CVC:"));
//        cardCVCField = new JTextField();
//        cardCVCField.setEnabled(false);
//        formPanel.add(cardCVCField);
//
//        // Panic button
//        panicButton = new JButton("Panic Button");
//        panicButton.addActionListener(e -> showPanicMessage());
//        formPanel.add(panicButton);
//
//        // Contact label
//        JLabel contactLabel = new JLabel("Having trouble? Contact Hotel Polar:\nphone: 6912345678 email: PolarHotel@gmail.com");
//        formPanel.add(contactLabel);
//
//        // Add the panel to the JFrame
//        add(formPanel, BorderLayout.CENTER);
//        setVisible(true);
//    }
//
//
//
//    private void toggleCreditCardFields() {
//        boolean isSelected = creditCardCheckbox.isSelected();
//        cardFirstNameField.setEnabled(isSelected);
//        cardLastNameField.setEnabled(isSelected);
//        cardNumberField.setEnabled(isSelected);
//        cardMonthField.setEnabled(isSelected);
//        cardYearField.setEnabled(isSelected);
//        cardCVCField.setEnabled(isSelected);
//    }
//
//    private void showPanicMessage() {
//        JOptionPane.showMessageDialog(this, "Alert: Police has been notified.");
//    }
//
//    private void reserveRoom() {
//        String selectedItem = roomSelectionList.getSelectedValue();
//        if (selectedItem == null) {
//            JOptionPane.showMessageDialog(this, "Please select a room!");
//            return;
//        }
//
//        String roomNumber = selectedItem.split(" - ")[0]; // Extract room number
//        reserve(roomNumber, false);
//    }
//
//    private void reserveGroup() {
//        List<String> selectedItems = roomSelectionList.getSelectedValuesList();
//        if (selectedItems.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please select at least one room!");
//            return;
//        }
//
//        List<String> roomNumbers = selectedItems.stream()
//                .map(item -> item.split(" - ")[0]) // Extract room number
//                .toList();
//
//        int totalGuests = calculateTotalGuests(roomNumbers);
//        if (totalGuests >= 20) {
//            // Υπολογισμός συνολικού κόστους και προκαταβολής
//            String fromDate = fromDateField.getText();
//            String toDate = toDateField.getText();
//            double totalPrice = calculateTotalPrice(String.join(";", roomNumbers), fromDate, toDate, true);
//            double prepayment = totalPrice * 0.25;
//
//            // Εμφάνιση μηνύματος για την προκαταβολή
//            JOptionPane.showMessageDialog(this, String.format(
//                    "The group reservation requires a prepayment of 25%%.\nPrepayment amount: %.2f", prepayment));
//        }
//
//        reserve(String.join(",", roomNumbers), true);
//    }
//
//    private void reserve(String roomNumberOrGroup, boolean isGroup) {
//        String customerName = customerNameField.getText();
//        String customerSurname = customerSurnameField.getText();
//        String customerID = customerIDField.getText();
//        String customerCountry = customerCountryField.getText();
//        String customerAddress = customerAddressField.getText(); // Get address
//        String fromDate = fromDateField.getText();
//        String toDate = toDateField.getText();
//
//        if (customerName.isEmpty() || customerSurname.isEmpty() || customerID.isEmpty() || customerCountry.isEmpty() || customerAddress.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "All fields are required!");
//            return;
//        }
//
//        if (creditCardCheckbox.isSelected()) {
//            String cardFirstName = cardFirstNameField.getText();
//            String cardLastName = cardLastNameField.getText();
//            String cardNumber = cardNumberField.getText();
//            String cardMonth = cardMonthField.getText();
//            String cardYear = cardYearField.getText();
//            String cardCVC = cardCVCField.getText();
//
//            if (cardFirstName.isEmpty() || cardLastName.isEmpty() || cardNumber.isEmpty() || cardMonth.isEmpty() || cardYear.isEmpty() || cardCVC.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Please fill out all credit card details!");
//                return;
//            }
//        }
//
//        double totalPrice = calculateTotalPrice(roomNumberOrGroup, fromDate, toDate, isGroup);
//        saveReservationToFile(customerName, customerSurname, customerID, customerCountry, customerAddress, roomNumberOrGroup, fromDate, toDate, totalPrice);
//
//        if (!isGroup) {
//            updateRoomStatus(roomNumberOrGroup, "Occupied");
//        } else {
//            for (String room : roomNumberOrGroup.split(",")) {
//                updateRoomStatus(room, "Occupied");
//            }
//        }
//
//        JOptionPane.showMessageDialog(this, "Reservation completed successfully!");
//    }
//
//    private void saveReservationToFile(String customerName, String customerSurname, String customerID,
//                                       String customerCountry, String customerAddress, String roomNumberOrGroup, String fromDate,
//                                       String toDate, double totalPrice) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reservations.txt", true))) {
//            // Checks if the reservation is a group (multiple rooms)
//            if (roomNumberOrGroup.contains(",")) {
//                // If it's a group, replace the comma with a semicolon
//                roomNumberOrGroup = roomNumberOrGroup.replace(",", ";");
//            }
//            writer.write(customerName + "," + customerSurname + "," + customerID + "," + customerCountry + "," + customerAddress + "," +
//                    roomNumberOrGroup + "," + fromDate + "," + toDate + "," + totalPrice + "\n");
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving reservation: " + e.getMessage());
//        }
//    }
//
//    private double calculateTotalPrice(String roomNumberOrGroup, String fromDate, String toDate, boolean isGroup) {
//        List<String> roomNumbers = List.of(roomNumberOrGroup.split(","));
//        double totalBasePrice = roomNumbers.stream()
//                .mapToDouble(this::getBasePriceForRoom)
//                .sum();
//        int numberOfNights = calculateNumberOfNights(fromDate, toDate);
//        double totalPrice = totalBasePrice * numberOfNights;
//
//        if (isGroup && calculateTotalGuests(roomNumbers) >= 20) {
//            totalPrice *= 0.6;
//        }
//
//        return totalPrice;
//    }
//
//    private int calculateTotalGuests(List<String> roomNumbers) {
//        return roomNumbers.stream()
//                .mapToInt(this::getRoomCapacity)
//                .sum();
//    }
//
//    private int calculateNumberOfNights(String fromDate, String toDate) {
//        String[] fromParts = fromDate.split("-");
//        String[] toParts = toDate.split("-");
//        int fromDay = Integer.parseInt(fromParts[2]);
//        int toDay = Integer.parseInt(toParts[2]);
//        return toDay - fromDay;
//    }
//
//    private double getBasePriceForRoom(String roomNumber) {
//        return rooms.stream()
//                .filter(room -> room.getRoomNumber().equals(roomNumber))
//                .findFirst()
//                .map(Room::getPrice)
//                .orElse(0.0);
//    }
//
//    private int getRoomCapacity(String roomNumber) {
//        return rooms.stream()
//                .filter(room -> room.getRoomNumber().equals(roomNumber))
//                .findFirst()
//                .map(Room::getCapacity)
//                .orElse(1);
//    }
//
//    private void updateRoomStatus(String roomNumberOrGroup, String newStatus) {
//        List<String> selectedRoomNumbers = List.of(roomNumberOrGroup.split(","));
//        List<String> updatedRooms = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] roomData = line.split(",");
//                if (roomData.length < 5) {
//                    System.out.println("Skipping invalid line: " + line);
//                    continue;
//                }
//
//                String roomNumber = roomData[0];
//                String roomType = roomData[1];
//                String amenities = roomData[2];
//                String status = roomData[3];
//                double price = 0.0;
//
//                try {
//                    price = Double.parseDouble(roomData[4]);
//                } catch (NumberFormatException e) {
//                    System.out.println("Skipping room with invalid price: " + line);
//                    continue;
//                }
//
//                if (selectedRoomNumbers.contains(roomNumber) && "Available".equals(status)) {
//                    status = newStatus;
//                }
//
//                updatedRooms.add(roomNumber + "," + roomType + "," + amenities + "," + status + "," + price);
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error reading rooms file: " + e.getMessage());
//            return;
//        }
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
//            for (String updatedRoom : updatedRooms) {
//                writer.write(updatedRoom + "\n");
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error updating room status: " + e.getMessage());
//        }
//    }
//}
