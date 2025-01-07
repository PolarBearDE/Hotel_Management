package hotelmanagement;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CheckoutView extends JFrame {
    private JComboBox<String> reservationComboBox;
    private JTextArea receiptTextArea;
    private JButton generateReceiptButton, sendToPoliceButton, generatePDFButton;
    private List<String[]> reservations;

    public CheckoutView() {
        reservations = new ArrayList<>();
        loadReservationsFromFile();

        setTitle("Checkout");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());

        // Combo box για τις κρατήσεις
        reservationComboBox = new JComboBox<>();
        for (int i = 0; i < reservations.size(); i++) {
            String[] reservation = reservations.get(i);
            String customerName = reservation[0] + " " + reservation[1];
            reservationComboBox.addItem(customerName + " (" + i + ")");
        }
        panel.add(reservationComboBox, BorderLayout.NORTH);

        // Πεδίο για την αποδείξη
        receiptTextArea = new JTextArea();
        receiptTextArea.setEditable(false);
        panel.add(new JScrollPane(receiptTextArea), BorderLayout.CENTER);

        // Panel για τα κουμπιά με FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Κουμπί για την εκτύπωση αποδείξεων
        generateReceiptButton = new JButton("Generate Receipt");
        generateReceiptButton.addActionListener(e -> generateReceipt());
        buttonPanel.add(generateReceiptButton);

        // Κουμπί για την αποστολή στην αστυνομία
        sendToPoliceButton = new JButton("Send to Police");
        sendToPoliceButton.addActionListener(e -> sendToPolice());
        buttonPanel.add(sendToPoliceButton);

        // Κουμπί για την δημιουργία PDF
        generatePDFButton = new JButton("Generate PDF");
        generatePDFButton.addActionListener(e -> generatePDF());
        buttonPanel.add(generatePDFButton);

        // Προσθήκη του panel με τα κουμπιά
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void loadReservationsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] reservationDetails = line.split(",");
                reservations.add(reservationDetails);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage());
        }
    }

    private void generateReceipt() {
        String selectedReservation = (String) reservationComboBox.getSelectedItem();
        String[] reservationDetails = getReservationDetails(selectedReservation);

        if (reservationDetails != null) {
            String customerName = reservationDetails[0] + " " + reservationDetails[1];
            String roomNumbers = reservationDetails[5];
            String fromDate = reservationDetails[6];
            String toDate = reservationDetails[7];
            double totalPrice = Double.parseDouble(reservationDetails[8]);

            // Υπολογισμός ημερών παραμονής
            int numberOfNights = calculateNumberOfNights(fromDate, toDate);

            // Διαχείριση ομάδας δωματίων
            String roomList = formatRoomList(roomNumbers);

            // Δημιουργία αποδείξεως
            String receipt = "Receipt for " + customerName + "\n";
            receipt += "Rooms: " + roomList + "\n";
            receipt += "Stay Period: " + fromDate + " to " + toDate + "\n";
            receipt += "Number of Nights: " + numberOfNights + "\n";
            receipt += "Total Price: " + totalPrice + "\n";
            receipt += "\nThank you for staying at the Polar Hotel!";
            receipt += "\nAddress: Xolomontos 22";
            receipt += "\nPowered by PolarSoft Technologies by Mpalitsas";

            receiptTextArea.setText(receipt);
        } else {
            JOptionPane.showMessageDialog(this, "No reservation selected.");
        }
    }

    private String[] getReservationDetails(String selectedReservation) {
        int startIndex = selectedReservation.lastIndexOf("(");
        int endIndex = selectedReservation.lastIndexOf(")");
        if (startIndex != -1 && endIndex != -1) {
            try {
                int index = Integer.parseInt(selectedReservation.substring(startIndex + 1, endIndex));
                return reservations.get(index);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private int calculateNumberOfNights(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Διασφάλιση ότι οι ημερομηνίες έχουν πάντα δύο ψηφία για μήνα και ημέρα
        fromDate = formatDateString(fromDate);
        toDate = formatDateString(toDate);

        LocalDate start = LocalDate.parse(fromDate, formatter);
        LocalDate end = LocalDate.parse(toDate, formatter);
        return (int) ChronoUnit.DAYS.between(start, end);
    }

    // Συνάρτηση για να μορφοποιεί την ημερομηνία
    private String formatDateString(String date) {
        String[] dateParts = date.split("-");
        if (dateParts[1].length() == 1) {
            dateParts[1] = "0" + dateParts[1]; // Προσθήκη μηδενικού μπροστά στον μήνα
        }
        if (dateParts[2].length() == 1) {
            dateParts[2] = "0" + dateParts[2]; // Προσθήκη μηδενικού μπροστά στην ημέρα
        }
        return String.join("-", dateParts);
    }

    private String formatRoomList(String roomNumbers) {
        if (roomNumbers == null || roomNumbers.isEmpty()) {
            return "No rooms assigned";
        }
        String[] rooms = roomNumbers.split(";");
        StringBuilder roomList = new StringBuilder();
        for (int i = 0; i < rooms.length; i++) {
            roomList.append("Room ").append(rooms[i].trim());
            if (i < rooms.length - 1) {
                roomList.append(", ");
            }
        }
        return roomList.toString();
    }

    private void generatePDF() {
        String selectedReservation = (String) reservationComboBox.getSelectedItem();
        String[] reservationDetails = getReservationDetails(selectedReservation);

        if (reservationDetails != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream("receipt.pdf"));
                document.open();

                String customerName = reservationDetails[0] + " " + reservationDetails[1];
                String roomNumbers = reservationDetails[5];
                String fromDate = reservationDetails[6];
                String toDate = reservationDetails[7];
                double totalPrice = Double.parseDouble(reservationDetails[8]);

                // Υπολογισμός ημερών παραμονής
                int numberOfNights = calculateNumberOfNights(fromDate, toDate);

                // Δημιουργία αποδείξεως PDF
                document.add(new Paragraph("Receipt for " + customerName));
                document.add(new Paragraph("Rooms: " + formatRoomList(roomNumbers)));
                document.add(new Paragraph("Stay Period: " + fromDate + " to " + toDate));
                document.add(new Paragraph("Number of Nights: " + numberOfNights));
                document.add(new Paragraph("Total Price: " + totalPrice));
                document.add(new Paragraph("\nThank you for staying at the Polar Hotel!"));
                document.add(new Paragraph("\nAddress: Xolomontos 22"));
                document.add(new Paragraph("\nGreece Kozani"));
                document.add(new Paragraph("\nPowered by PolarSoft Technologies by Mpalitsas"));

                document.close();

                JOptionPane.showMessageDialog(this, "PDF generated successfully!");
            } catch (DocumentException | FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No reservation selected.");
        }
    }

    private void sendToPolice() {
        String emergencyNumbers = """
            The customer details have been sent to the nearest police station.

            In case of an emergency, please contact:
            - Police: 100
            - Fire Department: 199
            - Ambulance (EKAB): 166
            - European Emergency Number: 112
        """;

        JOptionPane.showMessageDialog(this, emergencyNumbers, "Emergency Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new CheckoutView();
    }
}



//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CheckoutView extends JFrame {
//    private JComboBox<String> reservationComboBox;
//    private JTextArea receiptTextArea;
//    private JButton generateReceiptButton;
//    private List<String[]> reservations;
//
//    public CheckoutView() {
//        reservations = new ArrayList<>();
//        loadReservationsFromFile();
//
//        setTitle("Checkout");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(600, 400);
//
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Combo box για τις κρατήσεις
//        reservationComboBox = new JComboBox<>();
//        for (int i = 0; i < reservations.size(); i++) {
//            String[] reservation = reservations.get(i);
//            String customerName = reservation[0] + " " + reservation[1];
//            reservationComboBox.addItem(customerName + " (" + i + ")");
//        }
//        panel.add(reservationComboBox, BorderLayout.NORTH);
//
//        // Πεδίο για την αποδείξη
//        receiptTextArea = new JTextArea();
//        receiptTextArea.setEditable(false);
//        panel.add(new JScrollPane(receiptTextArea), BorderLayout.CENTER);
//
//        // Κουμπί για την εκτύπωση αποδείξεων
//        generateReceiptButton = new JButton("Generate Receipt");
//        generateReceiptButton.addActionListener(e -> generateReceipt());
//        panel.add(generateReceiptButton, BorderLayout.SOUTH);
//
//        add(panel);
//        setVisible(true);
//    }
//
//    private void loadReservationsFromFile() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] reservationDetails = line.split(",");
//                reservations.add(reservationDetails);
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage());
//        }
//    }
//
//    private void generateReceipt() {
//        String selectedReservation = (String) reservationComboBox.getSelectedItem();
//        String[] reservationDetails = getReservationDetails(selectedReservation);
//
//        if (reservationDetails != null) {
//            String customerName = reservationDetails[0] + " " + reservationDetails[1];
//            String roomNumbers = reservationDetails[5];
//            String fromDate = reservationDetails[6];
//            String toDate = reservationDetails[7];
//            double totalPrice = Double.parseDouble(reservationDetails[8]);
//
//            // Υπολογισμός ημερών παραμονής
//            int numberOfNights = calculateNumberOfNights(fromDate, toDate);
//
//            // Διαχείριση ομάδας δωματίων
//            String roomList = formatRoomList(roomNumbers);
//
//            // Δημιουργία αποδείξεως
//            String receipt = "Receipt for " + customerName + "\n";
//            receipt += "Rooms: " + roomList + "\n";
//            receipt += "Stay Period: " + fromDate + " to " + toDate + "\n";
//            receipt += "Number of Nights: " + numberOfNights + "\n";
//            receipt += "Total Price: " + totalPrice + "\n";
//            receipt += "\nThank you for staying at the Polar Hotel!";
//            receipt += "\nAddress: Xolomontos 22";
//            receipt += "\nPowered by PolarSoft Technologies by Mpalitsas";
//
//            receiptTextArea.setText(receipt);
//        } else {
//            JOptionPane.showMessageDialog(this, "No reservation selected.");
//        }
//    }
//
//    private String[] getReservationDetails(String selectedReservation) {
//        int startIndex = selectedReservation.lastIndexOf("(");
//        int endIndex = selectedReservation.lastIndexOf(")");
//        if (startIndex != -1 && endIndex != -1) {
//            try {
//                int index = Integer.parseInt(selectedReservation.substring(startIndex + 1, endIndex));
//                return reservations.get(index);
//            } catch (NumberFormatException e) {
//                return null;
//            }
//        }
//        return null;
//    }
//
//    private int calculateNumberOfNights(String fromDate, String toDate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate start = LocalDate.parse(fromDate, formatter);
//        LocalDate end = LocalDate.parse(toDate, formatter);
//        return (int) ChronoUnit.DAYS.between(start, end);
//    }
//
//    private String formatRoomList(String roomNumbers) {
//        if (roomNumbers == null || roomNumbers.isEmpty()) {
//            return "No rooms assigned";
//        }
//        String[] rooms = roomNumbers.split(";");
//        StringBuilder roomList = new StringBuilder();
//        for (int i = 0; i < rooms.length; i++) {
//            roomList.append("Room ").append(rooms[i].trim());
//            if (i < rooms.length - 1) {
//                roomList.append(", ");
//            }
//        }
//        return roomList.toString();
//    }
//
//    public static void main(String[] args) {
//        new CheckoutView();
//    }
//}
