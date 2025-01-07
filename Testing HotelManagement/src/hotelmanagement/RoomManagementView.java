package hotelmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomManagementView {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final List<Room> rooms;
    private final RoomPriceManager priceManager;
    private static final String ROOMS_FILE = "rooms.txt"; // Αρχείο για αποθήκευση δωματίων

    public RoomManagementView(List<Room> rooms, RoomPriceManager priceManager) {
        this.rooms = rooms;
        this.priceManager = priceManager;

        // Φορτώνουμε τα δωμάτια από το αρχείο
        loadRoomsFromFile();

        JFrame frame = new JFrame("Room Management");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String[] columns = {"Room Number", "Room Type", "Amenities", "Status", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        loadRoomData();

        JPanel buttonPanel = new JPanel();

        JButton addRoomButton = new JButton("Add Room");
        addRoomButton.addActionListener(e -> addRoom());

        JButton updateRoomButton = new JButton("Update Room");
        updateRoomButton.addActionListener(e -> updateRoom());

        JButton deleteRoomButton = new JButton("Delete Room");
        deleteRoomButton.addActionListener(e -> deleteRoom());

        JButton exportPdfButton = new JButton("Export to PDF");
        exportPdfButton.addActionListener(e -> exportToPdf());

        JButton refreshButton = new JButton("Refresh View");
        refreshButton.addActionListener(e -> refreshView());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelRoomStatus());

        buttonPanel.add(addRoomButton);
        buttonPanel.add(updateRoomButton);
        buttonPanel.add(deleteRoomButton);
        buttonPanel.add(exportPdfButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadRoomData() {
        tableModel.setRowCount(0); // Καθαρίζουμε τον πίνακα
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getRoomType(),
                    String.join(", ", room.getAmenities()),
                    room.getRoomStatus(),
                    priceManager.getPriceForRoom(room.getRoomType())
            });
        }
    }

    private void addRoom() {
        JTextField roomNumberField = new JTextField();
        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple", "Suite", "Queen"});
        JCheckBox fridgeCheck = new JCheckBox("Fridge");
        JCheckBox acCheck = new JCheckBox("Air Conditioning");
        JCheckBox balconyCheck = new JCheckBox("Balcony");
        JCheckBox hairdryerCheck = new JCheckBox("Hairdryer");
        JCheckBox tvCheck = new JCheckBox("TV");

        JPanel amenitiesPanel = new JPanel();
        amenitiesPanel.add(fridgeCheck);
        amenitiesPanel.add(acCheck);
        amenitiesPanel.add(balconyCheck);
        amenitiesPanel.add(hairdryerCheck);
        amenitiesPanel.add(tvCheck);

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("Room Number:"));
        panel.add(roomNumberField);
        panel.add(new JLabel("Room Type:"));
        panel.add(roomTypeCombo);
        panel.add(new JLabel("Amenities:"));
        panel.add(amenitiesPanel);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Room", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String roomNumber = roomNumberField.getText();
            String roomType = (String) roomTypeCombo.getSelectedItem();
            List<String> amenities = new ArrayList<>();
            if (fridgeCheck.isSelected()) amenities.add("Fridge");
            if (acCheck.isSelected()) amenities.add("Air Conditioning");
            if (balconyCheck.isSelected()) amenities.add("Balcony");
            if (hairdryerCheck.isSelected()) amenities.add("Hairdryer");
            if (tvCheck.isSelected()) amenities.add("TV");

            double price = priceManager.getPriceForRoom(roomType);

            Room newRoom = new Room(roomNumber, roomType, true, true, true, amenities, price);
            rooms.add(newRoom);
            saveRoomsToFile(); // Αποθηκεύουμε τα δωμάτια
            loadRoomData();
        }
    }

    private void updateRoom() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a room to update.");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String roomType = (String) tableModel.getValueAt(selectedRow, 1);
        String[] amenities = ((String) tableModel.getValueAt(selectedRow, 2)).split(", ");
        String status = (String) tableModel.getValueAt(selectedRow, 3);

        Room room = rooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst().orElse(null);
        if (room == null) {
            JOptionPane.showMessageDialog(null, "Selected room not found.");
            return;
        }

        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple", "Suite", "Queen"});
        roomTypeCombo.setSelectedItem(roomType);

        JCheckBox fridgeCheck = new JCheckBox("Fridge", room.getAmenities().contains("Fridge"));
        JCheckBox acCheck = new JCheckBox("Air Conditioning", room.getAmenities().contains("Air Conditioning"));
        JCheckBox balconyCheck = new JCheckBox("Balcony", room.getAmenities().contains("Balcony"));
        JCheckBox hairdryerCheck = new JCheckBox("Hairdryer", room.getAmenities().contains("Hairdryer"));
        JCheckBox tvCheck = new JCheckBox("TV", room.getAmenities().contains("TV"));

        JPanel amenitiesPanel = new JPanel();
        amenitiesPanel.add(fridgeCheck);
        amenitiesPanel.add(acCheck);
        amenitiesPanel.add(balconyCheck);
        amenitiesPanel.add(hairdryerCheck);
        amenitiesPanel.add(tvCheck);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Occupied", "Out of Service"});
        statusCombo.setSelectedItem(status);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Room Type:"));
        panel.add(roomTypeCombo);
        panel.add(new JLabel("Amenities:"));
        panel.add(amenitiesPanel);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Room", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            room.setRoomType((String) roomTypeCombo.getSelectedItem());

            List<String> updatedAmenities = new ArrayList<>();
            if (fridgeCheck.isSelected()) updatedAmenities.add("Fridge");
            if (acCheck.isSelected()) updatedAmenities.add("Air Conditioning");
            if (balconyCheck.isSelected()) updatedAmenities.add("Balcony");
            if (hairdryerCheck.isSelected()) updatedAmenities.add("Hairdryer");
            if (tvCheck.isSelected()) updatedAmenities.add("TV");
            room.setAmenities(updatedAmenities);

            room.setStatus((String) statusCombo.getSelectedItem());

            saveRoomsToFile(); // Αποθηκεύουμε τις αλλαγές
            loadRoomData();
            JOptionPane.showMessageDialog(null, "Room updated successfully!");
        }
    }

    private void deleteRoom() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a room to delete.");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        rooms.removeIf(room -> room.getRoomNumber().equals(roomNumber));

        saveRoomsToFile(); // Αποθηκεύουμε τις αλλαγές
        loadRoomData();
        JOptionPane.showMessageDialog(null, "Room deleted successfully!");
    }

    private void exportToPdf() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("rooms.pdf"));
            document.open();

            document.add(new Paragraph("Room Management Report"));
            document.add(new Paragraph(" "));
            for (Room room : rooms) {
                document.add(new Paragraph("Room Number: " + room.getRoomNumber()));
                document.add(new Paragraph("Room Type: " + room.getRoomType()));
                document.add(new Paragraph("Amenities: " + String.join(", ", room.getAmenities())));
                document.add(new Paragraph("Status: " + room.getRoomStatus()));
                document.add(new Paragraph("Price: " + room.getPrice()));
                document.add(new Paragraph(" "));
            }
            document.close();
            JOptionPane.showMessageDialog(null, "Rooms exported to PDF successfully.");
        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting to PDF: " + e.getMessage());
        }
    }

    private void refreshView() {
        loadRoomsFromFile();
        loadRoomData();
        JOptionPane.showMessageDialog(null, "View refreshed successfully.");
    }

    private void cancelRoomStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a room to cancel.");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        Room room = rooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst().orElse(null);

        if (room != null && room.getRoomStatus().equals("Occupied")) {
            room.setStatus("Available");
            saveRoomsToFile(); // Αποθηκεύουμε τις αλλαγές
            loadRoomData();
            JOptionPane.showMessageDialog(null, "Room status changed to Available.");
        }
    }

    private void saveRoomsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room room : rooms) {
                writer.printf("%s,%s,%s,%s,%.2f%n",
                        room.getRoomNumber(),
                        room.getRoomType(),
                        String.join(";", room.getAmenities()),
                        room.getRoomStatus(),
                        room.getPrice());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving rooms: " + e.getMessage());
        }
    }

    private void loadRoomsFromFile() {
        rooms.clear();
        File file = new File(ROOMS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String roomNumber = parts[0];
                String roomType = parts[1];
                List<String> amenities = List.of(parts[2].split(";"));
                String status = parts[3]; // Παίρνουμε το status από το αρχείο
                double price = Double.parseDouble(parts[4]);

                Room room = new Room(roomNumber, roomType, true, true, true, amenities, price);
                room.setStatus(status); // Ρυθμίζουμε το status στο αντικείμενο Room
                rooms.add(room);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading rooms: " + e.getMessage());
        }
    }
}




//
//package hotelmanagement;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class RoomManagementView {
//
//    private final JTable table;
//    private final DefaultTableModel tableModel;
//    private final List<Room> rooms;
//    private final RoomPriceManager priceManager;
//    private static final String ROOMS_FILE = "rooms.txt"; // Αρχείο για αποθήκευση δωματίων
//
//    public RoomManagementView(List<Room> rooms, RoomPriceManager priceManager) {
//        this.rooms = rooms;
//        this.priceManager = priceManager;
//
//        // Φορτώνουμε τα δωμάτια από το αρχείο
//        loadRoomsFromFile();
//
//        JFrame frame = new JFrame("Room Management");
//        frame.setSize(900, 600);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//
//        String[] columns = {"Room Number", "Room Type", "Amenities", "Status", "Price"};
//        tableModel = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 1 || column == 2 || column == 3;
//            }
//        };
//
//        table = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(table);
//        frame.add(scrollPane, BorderLayout.CENTER);
//
//        loadRoomData();
//
//        JPanel buttonPanel = new JPanel();
//
//        JButton addRoomButton = new JButton("Add Room");
//        addRoomButton.addActionListener(e -> addRoom());
//
//        JButton updateRoomButton = new JButton("Update Room");
//        updateRoomButton.addActionListener(e -> updateRoom());
//
//        JButton deleteRoomButton = new JButton("Delete Room");
//        deleteRoomButton.addActionListener(e -> deleteRoom());
//
//        buttonPanel.add(addRoomButton);
//        buttonPanel.add(updateRoomButton);
//        buttonPanel.add(deleteRoomButton);
//
//        frame.add(buttonPanel, BorderLayout.SOUTH);
//        frame.setVisible(true);
//    }
//
//    private void loadRoomData() {
//        tableModel.setRowCount(0); // Καθαρίζουμε τον πίνακα
//        for (Room room : rooms) {
//            tableModel.addRow(new Object[]{
//                    room.getRoomNumber(),
//                    room.getRoomType(),
//                    String.join(", ", room.getAmenities()),
//                    room.getRoomStatus(), // Χρησιμοποιούμε το room.getRoomStatus() για το status
//                    priceManager.getPriceForRoom(room.getRoomType())
//            });
//        }
//    }
//
//    private void addRoom() {
//        JTextField roomNumberField = new JTextField();
//        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple", "Suite", "Queen"});
//        JCheckBox fridgeCheck = new JCheckBox("Fridge");
//        JCheckBox acCheck = new JCheckBox("Air Conditioning");
//        JCheckBox balconyCheck = new JCheckBox("Balcony");
//        JCheckBox hairdryerCheck = new JCheckBox("Hairdryer");
//        JCheckBox tvCheck = new JCheckBox("TV");
//
//        JPanel amenitiesPanel = new JPanel();
//        amenitiesPanel.add(fridgeCheck);
//        amenitiesPanel.add(acCheck);
//        amenitiesPanel.add(balconyCheck);
//        amenitiesPanel.add(hairdryerCheck);
//        amenitiesPanel.add(tvCheck);
//
//        JPanel panel = new JPanel(new GridLayout(7, 2));
//        panel.add(new JLabel("Room Number:"));
//        panel.add(roomNumberField);
//        panel.add(new JLabel("Room Type:"));
//        panel.add(roomTypeCombo);
//        panel.add(new JLabel("Amenities:"));
//        panel.add(amenitiesPanel);
//
//        int result = JOptionPane.showConfirmDialog(null, panel, "Add Room", JOptionPane.OK_CANCEL_OPTION);
//
//        if (result == JOptionPane.OK_OPTION) {
//            String roomNumber = roomNumberField.getText();
//            String roomType = (String) roomTypeCombo.getSelectedItem();
//            List<String> amenities = new ArrayList<>();
//            if (fridgeCheck.isSelected()) amenities.add("Fridge");
//            if (acCheck.isSelected()) amenities.add("Air Conditioning");
//            if (balconyCheck.isSelected()) amenities.add("Balcony");
//            if (hairdryerCheck.isSelected()) amenities.add("Hairdryer");
//            if (tvCheck.isSelected()) amenities.add("TV");
//
//            double price = priceManager.getPriceForRoom(roomType);
//
//            Room newRoom = new Room(roomNumber, roomType, true, true, true, amenities, price);
//            rooms.add(newRoom);
//            saveRoomsToFile(); // Αποθηκεύουμε τα δωμάτια
//            loadRoomData();
//        }
//    }
//
//    private void updateRoom() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(null, "Please select a room to update.");
//            return;
//        }
//
//        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
//        String roomType = (String) tableModel.getValueAt(selectedRow, 1);
//        String[] amenities = ((String) tableModel.getValueAt(selectedRow, 2)).split(", ");
//        String status = (String) tableModel.getValueAt(selectedRow, 3);
//
//        Room room = rooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst().orElse(null);
//        if (room == null) {
//            JOptionPane.showMessageDialog(null, "Selected room not found.");
//            return;
//        }
//
//        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Triple", "Suite", "Queen"});
//        roomTypeCombo.setSelectedItem(roomType);
//
//        JCheckBox fridgeCheck = new JCheckBox("Fridge", room.getAmenities().contains("Fridge"));
//        JCheckBox acCheck = new JCheckBox("Air Conditioning", room.getAmenities().contains("Air Conditioning"));
//        JCheckBox balconyCheck = new JCheckBox("Balcony", room.getAmenities().contains("Balcony"));
//        JCheckBox hairdryerCheck = new JCheckBox("Hairdryer", room.getAmenities().contains("Hairdryer"));
//        JCheckBox tvCheck = new JCheckBox("TV", room.getAmenities().contains("TV"));
//
//        JPanel amenitiesPanel = new JPanel();
//        amenitiesPanel.add(fridgeCheck);
//        amenitiesPanel.add(acCheck);
//        amenitiesPanel.add(balconyCheck);
//        amenitiesPanel.add(hairdryerCheck);
//        amenitiesPanel.add(tvCheck);
//
//        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Occupied", "Out of Service"});
//        statusCombo.setSelectedItem(status);
//
//        JPanel panel = new JPanel(new GridLayout(5, 2));
//        panel.add(new JLabel("Room Type:"));
//        panel.add(roomTypeCombo);
//        panel.add(new JLabel("Amenities:"));
//        panel.add(amenitiesPanel);
//        panel.add(new JLabel("Status:"));
//        panel.add(statusCombo);
//
//        int result = JOptionPane.showConfirmDialog(null, panel, "Update Room", JOptionPane.OK_CANCEL_OPTION);
//
//        if (result == JOptionPane.OK_OPTION) {
//            room.setRoomType((String) roomTypeCombo.getSelectedItem());
//
//            List<String> updatedAmenities = new ArrayList<>();
//            if (fridgeCheck.isSelected()) updatedAmenities.add("Fridge");
//            if (acCheck.isSelected()) updatedAmenities.add("Air Conditioning");
//            if (balconyCheck.isSelected()) updatedAmenities.add("Balcony");
//            if (hairdryerCheck.isSelected()) updatedAmenities.add("Hairdryer");
//            if (tvCheck.isSelected()) updatedAmenities.add("TV");
//            room.setAmenities(updatedAmenities);
//
//            room.setStatus((String) statusCombo.getSelectedItem());
//
//            saveRoomsToFile(); // Αποθηκεύουμε τις αλλαγές
//            loadRoomData();
//            JOptionPane.showMessageDialog(null, "Room updated successfully!");
//        }
//    }
//
//    private void deleteRoom() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(null, "Please select a room to delete.");
//            return;
//        }
//
//        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
//        rooms.removeIf(room -> room.getRoomNumber().equals(roomNumber));
//
//        saveRoomsToFile(); // Αποθηκεύουμε τις αλλαγές
//        loadRoomData();
//        JOptionPane.showMessageDialog(null, "Room deleted successfully!");
//    }
//
//    private void saveRoomsToFile() {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(ROOMS_FILE))) {
//            for (Room room : rooms) {
//                writer.printf("%s,%s,%s,%s,%.2f%n",
//                        room.getRoomNumber(),
//                        room.getRoomType(),
//                        String.join(";", room.getAmenities()),
//                        room.getRoomStatus(),
//                        room.getPrice());
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error saving rooms: " + e.getMessage());
//        }
//    }
//
//    private void loadRoomsFromFile() {
//        rooms.clear();
//        File file = new File(ROOMS_FILE);
//        if (!file.exists()) return;
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length < 5) continue;
//
//                String roomNumber = parts[0];
//                String roomType = parts[1];
//                List<String> amenities = List.of(parts[2].split(";"));
//                String status = parts[3]; // Παίρνουμε το status από το αρχείο
//                double price = Double.parseDouble(parts[4]);
//
//                Room room = new Room(roomNumber, roomType, true, true, true, amenities, price);
//                room.setStatus(status); // Ρυθμίζουμε το status στο αντικείμενο Room
//                rooms.add(room);
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error loading rooms: " + e.getMessage());
//        }
//    }
//}
