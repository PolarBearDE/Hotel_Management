package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelSettingsView extends JFrame {
    private JTextField singleRoomPriceField;
    private JTextField doubleRoomPriceField;
    private JTextField tripleRoomPriceField;
    private JTextField suiteRoomPriceField;
    private JTextField queenRoomPriceField;
    private JButton updateButton;

    private RoomPriceManager priceManager;
    private RoomManagementView roomManagementView;

    public HotelSettingsView(RoomPriceManager priceManager, RoomManagementView roomManagementView) {
        this.priceManager = priceManager;
        this.roomManagementView = roomManagementView;

        setTitle("Hotel Settings");
        setSize(400, 400);
        setLayout(new GridLayout(7, 2, 15, 15));


        getContentPane().setBackground(new Color(240, 240, 240));

        // Δημιουργία των πεδίων εισαγωγής για τις τιμές
        JLabel singleRoomLabel = new JLabel("Single Room Price (EUR):");
        singleRoomLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        singleRoomPriceField = new JTextField(10);
        singleRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Single")));

        JLabel doubleRoomLabel = new JLabel("Double Room Price (EUR):");
        doubleRoomLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        doubleRoomPriceField = new JTextField(10);
        doubleRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Double")));

        JLabel tripleRoomLabel = new JLabel("Triple Room Price (EUR):");
        tripleRoomLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tripleRoomPriceField = new JTextField(10);
        tripleRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Triple")));

        JLabel suiteRoomLabel = new JLabel("Suite Room Price (EUR):");
        suiteRoomLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        suiteRoomPriceField = new JTextField(10);
        suiteRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Suite")));

        JLabel queenRoomLabel = new JLabel("Queen Room Price (EUR):");
        queenRoomLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        queenRoomPriceField = new JTextField(10);
        queenRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Queen")));

        // ενημέρωση των τιμών
        updateButton = new JButton("Update Prices");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        updateButton.setOpaque(true);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoomPrices();
            }
        });

        // Προσθήκη των στοιχείων παράθυρο
        add(singleRoomLabel);
        add(singleRoomPriceField);
        add(doubleRoomLabel);
        add(doubleRoomPriceField);
        add(tripleRoomLabel);
        add(tripleRoomPriceField);
        add(suiteRoomLabel);
        add(suiteRoomPriceField);
        add(queenRoomLabel);
        add(queenRoomPriceField);
        add(new JLabel());
        add(updateButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void updateRoomPrices() {
        try {
            // Παίρνουμε τις τιμές από τα πεδία εισαγωγής
            double singleRoomPrice = Double.parseDouble(singleRoomPriceField.getText());
            double doubleRoomPrice = Double.parseDouble(doubleRoomPriceField.getText());
            double tripleRoomPrice = Double.parseDouble(tripleRoomPriceField.getText());
            double suiteRoomPrice = Double.parseDouble(suiteRoomPriceField.getText());
            double queenRoomPrice = Double.parseDouble(queenRoomPriceField.getText());

            // Ενημέρωση των τιμών μέσω του RoomPriceManager
            priceManager.updatePriceForRoom("Single", singleRoomPrice);
            priceManager.updatePriceForRoom("Double", doubleRoomPrice);
            priceManager.updatePriceForRoom("Triple", tripleRoomPrice);
            priceManager.updatePriceForRoom("Suite", suiteRoomPrice);
            priceManager.updatePriceForRoom("Queen", queenRoomPrice);

            // Αποθήκευση των τιμών στο αρχείο
            priceManager.savePricesToFile();

            JOptionPane.showMessageDialog(this, "Prices updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for prices.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}




//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class HotelSettingsView extends JFrame {
//    private JTextField singleRoomPriceField;
//    private JTextField doubleRoomPriceField;
//    private JTextField tripleRoomPriceField;
//    private JTextField suiteRoomPriceField;
//    private JTextField queenRoomPriceField;
//    private JButton updateButton;
//
//    private RoomPriceManager priceManager;  // Χρήση του RoomPriceManager αντί της λίστας rooms
//    private RoomManagementView roomManagementView;
//
//    public HotelSettingsView(RoomPriceManager priceManager, RoomManagementView roomManagementView) {
//        this.priceManager = priceManager;
//        this.roomManagementView = roomManagementView;
//
//        setTitle("Hotel Settings");
//        setSize(400, 400);
//        setLayout(new GridLayout(7, 2));
//
//        // Δημιουργία των πεδίων εισαγωγής για τις τιμές
//        JLabel singleRoomLabel = new JLabel("Single Room Price (EUR):");
//        singleRoomPriceField = new JTextField(10);
//        singleRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Single")));
//
//        JLabel doubleRoomLabel = new JLabel("Double Room Price (EUR):");
//        doubleRoomPriceField = new JTextField(10);
//        doubleRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Double")));
//
//        JLabel tripleRoomLabel = new JLabel("Triple Room Price (EUR):");
//        tripleRoomPriceField = new JTextField(10);
//        tripleRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Triple")));
//
//        JLabel suiteRoomLabel = new JLabel("Suite Room Price (EUR):");
//        suiteRoomPriceField = new JTextField(10);
//        suiteRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Suite")));
//
//        JLabel queenRoomLabel = new JLabel("Queen Room Price (EUR):");
//        queenRoomPriceField = new JTextField(10);
//        queenRoomPriceField.setText(String.valueOf(priceManager.getPriceForRoom("Queen")));
//
//        updateButton = new JButton("Update Prices");
//        updateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                updateRoomPrices();
//            }
//        });
//
//        add(singleRoomLabel);
//        add(singleRoomPriceField);
//        add(doubleRoomLabel);
//        add(doubleRoomPriceField);
//        add(tripleRoomLabel);
//        add(tripleRoomPriceField);
//        add(suiteRoomLabel);
//        add(suiteRoomPriceField);
//        add(queenRoomLabel);
//        add(queenRoomPriceField);
//        add(new JLabel());
//        add(updateButton);
//
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setVisible(true);
//    }
//
//    private void updateRoomPrices() {
//        try {
//            // Παίρνουμε τις τιμές από τα πεδία εισαγωγής
//            double singleRoomPrice = Double.parseDouble(singleRoomPriceField.getText());
//            double doubleRoomPrice = Double.parseDouble(doubleRoomPriceField.getText());
//            double tripleRoomPrice = Double.parseDouble(tripleRoomPriceField.getText());
//            double suiteRoomPrice = Double.parseDouble(suiteRoomPriceField.getText());
//            double queenRoomPrice = Double.parseDouble(queenRoomPriceField.getText());
//
//            // Ενημέρωση των τιμών μέσω του RoomPriceManager
//            priceManager.updatePriceForRoom("Single", singleRoomPrice);
//            priceManager.updatePriceForRoom("Double", doubleRoomPrice);
//            priceManager.updatePriceForRoom("Triple", tripleRoomPrice);
//            priceManager.updatePriceForRoom("Suite", suiteRoomPrice);
//            priceManager.updatePriceForRoom("Queen", queenRoomPrice);
//
//            // Αποθήκευση των τιμών στο αρχείο
//            priceManager.savePricesToFile();
//
//            JOptionPane.showMessageDialog(this, "Prices updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Please enter valid numbers for prices.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//}
