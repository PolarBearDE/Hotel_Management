package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

public class MainView {
    public MainView(String username) {
        JFrame frame = new JFrame("Dashboard - Welcome, " + username);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(200, 200, 255), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        frame.setContentPane(backgroundPanel);

        JLabel headerLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        backgroundPanel.add(headerLabel, BorderLayout.NORTH);

        JLabel dateLabel = new JLabel("Date and Time: " + getCurrentDateTime(), SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backgroundPanel.add(dateLabel, BorderLayout.BEFORE_FIRST_LINE);

        Timer timer = new Timer(1000, e -> dateLabel.setText("Date and Time: " + getCurrentDateTime()));
        timer.start();

        Set<String> roles = getUserRoles(username);
        if (roles.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Error: No roles found for user " + username,
                    "Permission Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RoomPriceManager priceManager = new RoomPriceManager();
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            String roomNumber = String.valueOf(100 + i);
            String roomType = (i <= 10) ? "Single" : (i <= 30) ? "Double" : (i <= 45) ? "Triple" : (i <= 55) ? "Suite" : "Queen";
            List<String> amenities = List.of("Fridge", "Air Conditioning", "TV");
            Room room = new Room(roomNumber, roomType, true, true, true, amenities, priceManager.getPriceForRoom(roomType));
            rooms.add(room);
        }

        RoomManagementView roomManagementView = new RoomManagementView(rooms, priceManager);
        JPanel buttonPanel = new JPanel(new GridLayout(9, 1, 10, 10));
        buttonPanel.setOpaque(false);

        // Buttons
        if (roles.contains("Administrator") || roles.contains("Manager")) {
            JButton hotelSettingsButton = createStyledButton("Hotel Settings");
            hotelSettingsButton.addActionListener(e -> new HotelSettingsView(priceManager, roomManagementView));
            buttonPanel.add(hotelSettingsButton);
        }
        if (roles.contains("Administrator") || roles.contains("Manager")) {
            JButton roomsManagementButton = createStyledButton("Rooms Management");
            roomsManagementButton.addActionListener(e -> new RoomManagementView(rooms, priceManager));
            buttonPanel.add(roomsManagementButton);
        }
        if (roles.contains("Administrator") || roles.contains("Manager") || roles.contains("Employee")) {
            JButton filteredRoomsButton = createStyledButton("Filtered Room View");
            filteredRoomsButton.addActionListener(e -> new FilteredRoomView(rooms));
            buttonPanel.add(filteredRoomsButton);
        }
        if (roles.contains("Administrator") || roles.contains("Manager") || roles.contains("Employee")) {
            JButton reservationButton = createStyledButton("Room Reservation");
            reservationButton.addActionListener(e -> new ReservationView(rooms));
            buttonPanel.add(reservationButton);
        }
        if (roles.contains("Administrator") || roles.contains("Manager")) {
            JButton employeeDetailsButton = createStyledButton("Employee Details");
            employeeDetailsButton.addActionListener(e -> new EmployeeDetailsView());
            buttonPanel.add(employeeDetailsButton);
        }
        if (roles.contains("Administrator")) {
            JButton statisticsButton = createStyledButton("Statistics View");
            statisticsButton.addActionListener(e -> new StatisticsView());
            buttonPanel.add(statisticsButton);
        }
        if (roles.contains("Administrator") || roles.contains("Manager")) {
            JButton checkoutButton = createStyledButton("Checkout View");
            checkoutButton.addActionListener(e -> new CheckoutView());
            buttonPanel.add(checkoutButton);
        }
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?",
                    "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                new LoginView();
            }
        });
        buttonPanel.add(logoutButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("PolarSoft Technologies Version Alpha 2.25.1", SwingConstants.RIGHT);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backgroundPanel.add(footerLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private Set<String> getUserRoles(String username) {
        Set<String> roles = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].equals(username)) {
                    roles.addAll(Arrays.asList(parts[3].split(";")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roles;
    }
}


//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//
//public class MainView {
//    public MainView(String username) {
//        JFrame frame = new JFrame("Dashboard - Welcome, " + username);
//        frame.setSize(600, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Ανάγνωση δικαιωμάτων από το αρχείο
//        Set<String> roles = getUserRoles(username);
//        if (roles.isEmpty()) {
//            JOptionPane.showMessageDialog(frame, "Error: No roles found for user " + username,
//                    "Permission Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Δημιουργία του RoomPriceManager
//        RoomPriceManager priceManager = new RoomPriceManager();
//
//        // Δημιουργία λίστας δωματίων
//        List<Room> rooms = new ArrayList<>();
//        for (int i = 1; i <= 60; i++) {
//            String roomNumber = String.valueOf(100 + i);
//            String roomType = (i <= 10) ? "Single" : (i <= 30) ? "Double" : (i <= 45) ? "Triple" : (i <= 55) ? "Suite" : "Queen";
//            List<String> amenities = new ArrayList<>();
//            amenities.add("Fridge");
//            amenities.add("Air Conditioning");
//            amenities.add("TV");
//
//            Room room = new Room(roomNumber, roomType, true, true, true, amenities, priceManager.getPriceForRoom(roomType));
//            rooms.add(room);
//        }
//
//        RoomManagementView roomManagementView = new RoomManagementView(rooms, priceManager);
//
//        frame.setLayout(new BorderLayout(10, 10)); // BorderLayout για οργάνωση
//
//        JPanel buttonPanel = new JPanel(new GridLayout(9, 1, 10, 10)); // GridLayout για κουμπιά
//
//        // Προσθήκη κουμπιών ανάλογα με τους ρόλους
//        if (roles.contains("Administrator") || roles.contains("Manager")) {
//            JButton hotelSettingsButton = new JButton("Hotel Settings");
//            hotelSettingsButton.addActionListener(e -> new HotelSettingsView(priceManager, roomManagementView));
//            buttonPanel.add(hotelSettingsButton);
//        }
//
//        if (roles.contains("Administrator") || roles.contains("Manager")) {
//            JButton roomsManagementButton = new JButton("Rooms Management");
//            roomsManagementButton.addActionListener(e -> new RoomManagementView(rooms, priceManager));
//            buttonPanel.add(roomsManagementButton);
//        }
//
//        if (roles.contains("Administrator") || roles.contains("Manager") || roles.contains("Employee")) {
//            JButton filteredRoomsButton = new JButton("Filtered Room View");
//            filteredRoomsButton.addActionListener(e -> new FilteredRoomView(rooms));
//            buttonPanel.add(filteredRoomsButton);
//        }
//
//        if (roles.contains("Administrator") || roles.contains("Manager") || roles.contains("Employee")) {
//            JButton reservationButton = new JButton("Room Reservation");
//            reservationButton.addActionListener(e -> new ReservationView(rooms));
//            buttonPanel.add(reservationButton);
//        }
//
//        if (roles.contains("Administrator") || roles.contains("Manager")) {
//            JButton employeeDetailsButton = new JButton("Employee Details");
//            employeeDetailsButton.addActionListener(e -> new EmployeeDetailsView());
//            buttonPanel.add(employeeDetailsButton);
//        }
//
//        if (roles.contains("Administrator")) {
//            JButton statisticsButton = new JButton("Statistics View");
//            statisticsButton.addActionListener(e -> new StatisticsView());
//            buttonPanel.add(statisticsButton);
//        }
//
//        if (roles.contains("Administrator") || roles.contains("Manager")) {
//            JButton checkoutButton = new JButton("Checkout View");
//            checkoutButton.addActionListener(e -> new CheckoutView());
//            buttonPanel.add(checkoutButton);
//        }
//
//        JButton logoutButton = new JButton("Logout");
//        logoutButton.addActionListener(e -> {
//            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?",
//                    "Logout Confirmation", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                frame.dispose();
//                new LoginView(); // Επιστροφή στο Login
//            }
//        });
//        buttonPanel.add(logoutButton);
//
//        JLabel dateLabel = new JLabel("Date and Time: " + getCurrentDateTime(), SwingConstants.CENTER);
//        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        JPanel datePanel = new JPanel(new BorderLayout());
//        datePanel.add(dateLabel, BorderLayout.CENTER);
//        frame.add(datePanel, BorderLayout.NORTH);
//
//        frame.add(buttonPanel, BorderLayout.CENTER);
//
//        JLabel footerLabel = new JLabel("PolarSoft Technologies Version Alpha 2.25.1", SwingConstants.RIGHT);
//        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
//        JPanel footerPanel = new JPanel(new BorderLayout());
//        footerPanel.add(footerLabel, BorderLayout.EAST);
//        frame.add(footerPanel, BorderLayout.SOUTH);
//
//        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> dateLabel.setText("Date and Time: " + getCurrentDateTime()));
//        timer.start();
//
//        frame.setVisible(true);
//    }
//
//    private String getCurrentDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return dateFormat.format(new Date());
//    }
//
//    private Set<String> getUserRoles(String username) {
//        Set<String> roles = new HashSet<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 4 && parts[0].equals(username)) {
//                    String[] roleParts = parts[3].split(";");
//                    roles.addAll(Arrays.asList(roleParts));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return roles;
//    }
//}


//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.ArrayList;
//
//
//public class MainView {
//    public MainView(String username) {
//        JFrame frame = new JFrame("Dashboard - Welcome, " + username);
//        frame.setSize(600, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Δημιουργία του RoomPriceManager
//        RoomPriceManager priceManager = new RoomPriceManager();
//
//        // Δημιουργία λίστας δωματίων (πχ με 60 δωμάτια)
//        List<Room> rooms = new ArrayList<>();
//        for (int i = 1; i <= 60; i++) {
//            String roomNumber = String.valueOf(100 + i);
//            String roomType = (i <= 10) ? "Single" : (i <= 30) ? "Double" : (i <= 45) ? "Triple" : (i <= 55) ? "Suite" : "Queen";
//            List<String> amenities = new ArrayList<>();
//            amenities.add("Fridge");
//            amenities.add("Air Conditioning");
//            amenities.add("TV");
//
//            Room room = new Room(roomNumber, roomType, true, true, true, amenities, priceManager.getPriceForRoom(roomType));
//            rooms.add(room);
//        }
//
//        // Δημιουργία του RoomManagementView με την λίστα δωματίων και τον RoomPriceManager
//        RoomManagementView roomManagementView = new RoomManagementView(rooms, priceManager);
//
//        // Ρυθμίζουμε GridLayout για καλύτερη οργάνωση
//        frame.setLayout(new BorderLayout(10, 10)); // Χρησιμοποιούμε BorderLayout με κενά 10px
//
//        // Κουμπί για ρυθμίσεις ξενοδοχείου
//        JButton hotelSettingsButton = new JButton("Hotel Settings");
//        hotelSettingsButton.addActionListener(e -> new HotelSettingsView(priceManager, roomManagementView));
//
//        // Κουμπί για διαχείριση δωματίων
//        JButton roomsManagementButton = new JButton("Rooms Management");
//        roomsManagementButton.addActionListener(e -> new RoomManagementView(rooms, priceManager));
//
//        // Νέα κουμπιά για τα φίλτρα δωματίων και κράτηση
//        JButton filteredRoomsButton = new JButton("Filtered Room View");
//        filteredRoomsButton.addActionListener(e -> new FilteredRoomView(rooms)); // Άνοιγμα του FilteredRoomView
//
//        JButton reservationButton = new JButton("Room Reservation");
//        reservationButton.addActionListener(e -> new ReservationView(rooms)); // Άνοιγμα του ReservationView
//
//        JButton employeeDetailsButton = new JButton("Employee Details");
//        employeeDetailsButton.addActionListener(e -> new EmployeeDetailsView());
//
//        // Νέα κουμπιά για τα Statistics, Customer Reservation, και Checkout Views
//        JButton statisticsButton = new JButton("Statistics View");
//        statisticsButton.addActionListener(e -> new StatisticsView());
//
//        JButton checkoutButton = new JButton("Checkout View");
//        checkoutButton.addActionListener(e -> new CheckoutView());
//
//        // Κουμπί για αποσύνδεση
//        JButton logoutButton = new JButton("Logout");
//        logoutButton.addActionListener(e -> {
//            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?",
//                    "Logout Confirmation", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                frame.dispose();
//                new LoginView(); // Επιστροφή στο Login
//            }
//        });
//
//        // Εμφάνιση της ημερομηνίας και ώρας στην κορυφή
//        JLabel dateLabel = new JLabel("Date and Time: " + getCurrentDateTime(), SwingConstants.CENTER);
//        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        JPanel datePanel = new JPanel(new BorderLayout());
//        datePanel.add(dateLabel, BorderLayout.CENTER);
//        frame.add(datePanel, BorderLayout.NORTH); // Τοποθέτηση στην κορυφή
//
//        // Εμφάνιση των υπόλοιπων κουμπιών
//        JPanel buttonPanel = new JPanel(new GridLayout(9, 1, 10, 10)); // 9 κουμπιά σε στήλες
//        buttonPanel.add(hotelSettingsButton);
//        buttonPanel.add(roomsManagementButton);
//        buttonPanel.add(filteredRoomsButton);
//        buttonPanel.add(reservationButton);
//        buttonPanel.add(employeeDetailsButton);
//        buttonPanel.add(statisticsButton);
//        buttonPanel.add(checkoutButton);
//        buttonPanel.add(logoutButton);
//        frame.add(buttonPanel, BorderLayout.CENTER); // Τα κουμπιά στο κέντρο
//
//        // Χαμηλή χάρια κάτω δεξιά
//        JLabel footerLabel = new JLabel("PolarSoft Technologies Version Alpha 2.25.1", SwingConstants.RIGHT);
//        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
//        JPanel footerPanel = new JPanel(new BorderLayout());
//        footerPanel.add(footerLabel, BorderLayout.EAST);
//        frame.add(footerPanel, BorderLayout.SOUTH); // Τοποθέτηση στην κάτω περιοχή
//
//        // Ενημέρωση ώρας κάθε 1 δευτερόλεπτο
//        Timer timer = new Timer(1000, e -> dateLabel.setText("Date and Time: " + getCurrentDateTime()));
//        timer.start();
//
//        // Εμφάνιση παραθύρου
//        frame.setVisible(true);
//    }
//
//    private String getCurrentDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return dateFormat.format(new Date());
//    }
//}
