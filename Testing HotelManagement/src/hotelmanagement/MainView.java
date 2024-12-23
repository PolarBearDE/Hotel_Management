package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainView {
    public MainView(String username) {
        JFrame frame = new JFrame("Dashboard - Welcome, " + username);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Δημιουργία του RoomPriceManager
        RoomPriceManager priceManager = new RoomPriceManager();

        // Δημιουργία λίστας δωματίων (πχ με 60 δωμάτια)
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            String roomNumber = String.valueOf(100 + i);
            String roomType = (i <= 10) ? "Single" : (i <= 30) ? "Double" : (i <= 45) ? "Triple" : (i <= 55) ? "Suite" : "Queen";
            List<String> amenities = new ArrayList<>();
            amenities.add("Fridge");
            amenities.add("Air Conditioning");
            amenities.add("TV");

            Room room = new Room(roomNumber, roomType, true, true, true, amenities, priceManager.getPriceForRoom(roomType));
            rooms.add(room);
        }

        // Δημιουργία του RoomManagementView με την λίστα δωματίων και τον RoomPriceManager
        RoomManagementView roomManagementView = new RoomManagementView(rooms, priceManager);

        // Ρυθμίζουμε GridLayout για καλύτερη οργάνωση
        frame.setLayout(new GridLayout(7, 1, 10, 10)); // 7 σειρές, 1 στήλη, κενά 10px


        // Κουμπί για ρυθμίσεις ξενοδοχείου
        JButton hotelSettingsButton = new JButton("Hotel Settings");
        hotelSettingsButton.addActionListener(e -> new HotelSettingsView(priceManager, roomManagementView));
        frame.add(hotelSettingsButton);

        // Κουμπί για διαχείριση δωματίων
        JButton roomsManagementButton = new JButton("Rooms Management");
        roomsManagementButton.addActionListener(e -> new RoomManagementView(rooms, priceManager)); // Άνοιγμα του RoomManagementView
        frame.add(roomsManagementButton);

        // Νέα κουμπιά για τα φίλτρα δωματίων και κράτηση
        JButton filteredRoomsButton = new JButton("Filtered Room View");
        filteredRoomsButton.addActionListener(e -> new FilteredRoomView(rooms)); // Άνοιγμα του FilteredRoomView
        frame.add(filteredRoomsButton);

        JButton reservationButton = new JButton("Room Reservation");
        reservationButton.addActionListener(e -> new ReservationView(rooms)); // Άνοιγμα του ReservationView
        frame.add(reservationButton);

        JButton employeeDetailsButton = new JButton("Employee Details");
        employeeDetailsButton.addActionListener(e -> new EmployeeDetailsView());
        frame.add(employeeDetailsButton);

        // Κουμπί για αποσύνδεση
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?",
                    "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                new LoginView(); // Επιστροφή στο Login
            }
        });
        frame.add(logoutButton);

        // Εμφάνιση παραθύρου
        frame.setVisible(true);
    }
}
