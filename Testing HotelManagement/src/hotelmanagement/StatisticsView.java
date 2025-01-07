package hotelmanagement;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsView extends JFrame {

    public StatisticsView() {
        setTitle("Hotel Statistics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(statsArea), BorderLayout.CENTER);

        JButton exportButton = new JButton("Export to PDF");
        add(exportButton, BorderLayout.SOUTH);

        try {
            List<Room> rooms = loadRooms();
            Map<String, String> roomStatuses = loadRoomStatuses();
            List<Reservation> reservations = loadReservations();

            StringBuilder stats = new StringBuilder();

            stats.append("=== Hotel Statistics ===\n");
            stats.append("1. Room Occupancy Rate: ").append(calculateOccupancyRate(reservations, roomStatuses)).append("%\n");
            stats.append("2. Most Popular Room Type: ").append(findMostPopularRoomType(reservations, rooms, roomStatuses)).append("\n");
            stats.append("3. Average Stay Duration: ").append(calculateAverageStayDuration(reservations)).append(" nights\n");
            stats.append("4. Guest Frequency:\n").append(calculateGuestFrequency(reservations)).append("\n");

            statsArea.setText(stats.toString());

            exportButton.addActionListener(e -> {
                try {
                    exportStatisticsToPDF(stats.toString());
                    JOptionPane.showMessageDialog(this, "Statistics exported to PDF successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting to PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

        } catch (IOException e) {
            statsArea.setText("Error reading data files: " + e.getMessage());
        }

        setVisible(true);
    }

    private void exportStatisticsToPDF(String stats) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("HotelStatistics.pdf"));
        document.open();
        document.add(new Paragraph(stats));
        document.close();
    }

    private List<Room> loadRooms() throws IOException {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    rooms.add(new Room(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4])));
                }
            }
        }
        return rooms;
    }

    private Map<String, String> loadRoomStatuses() throws IOException {
        Map<String, String> roomStatuses = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    roomStatuses.put(parts[0], parts[3]);  // Βάζουμε την κατάσταση από τη 4η στήλη (ξεκινώντας από 0)
                }
            }
        }
        return roomStatuses;
    }

    private List<Reservation> loadReservations() throws IOException {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    reservations.add(new Reservation(parts[0], parts[1], parts[2], parts[3],
                            parts[4], parts[5], parts[6], parts[7],
                            Double.parseDouble(parts[8])));
                }
            }
        }
        return reservations;
    }

    private double calculateOccupancyRate(List<Reservation> reservations, Map<String, String> roomStatuses) {
        long occupiedRooms = reservations.stream()
                .filter(reservation -> "Occupied".equals(roomStatuses.get(reservation.getRoomNumber())))
                .count();
        return ((double) occupiedRooms / roomStatuses.size()) * 100;
    }

    private String findMostPopularRoomType(List<Reservation> reservations, List<Room> rooms, Map<String, String> roomStatuses) {
        Map<String, Long> roomTypeCounts = reservations.stream()
                .map(reservation -> rooms.stream()
                        .filter(room -> room.getRoomNumber().equals(reservation.getRoomNumber()))
                        .findFirst()
                        .map(Room::getRoomType)
                        .orElse("Unknown"))
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        return roomTypeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    private double calculateAverageStayDuration(List<Reservation> reservations) {
        return reservations.stream()
                .mapToDouble(reservation -> calculateStayDuration(reservation.getFromDate(), reservation.getToDate()))
                .average()
                .orElse(0.0);
    }

    private String calculateGuestFrequency(List<Reservation> reservations) {
        Map<String, Long> guestFrequency = reservations.stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getCustomerName() + " " + reservation.getCustomerSurname(), Collectors.counting()));

        StringBuilder result = new StringBuilder();
        guestFrequency.forEach((guest, count) -> result.append("   ").append(guest).append(": ").append(count).append(" visits\n"));
        return result.toString();
    }

    private long calculateStayDuration(String fromDate, String toDate) {
        String[] fromParts = fromDate.split("-");
        String[] toParts = toDate.split("-");
        int fromDay = Integer.parseInt(fromParts[2]);
        int toDay = Integer.parseInt(toParts[2]);
        return toDay - fromDay;
    }

    public static void main(String[] args) {
        new StatisticsView();
    }
}
