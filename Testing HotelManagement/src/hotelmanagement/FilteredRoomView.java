package hotelmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class FilteredRoomView extends JFrame {
    private JTable roomTable;
    private JComboBox<String> statusFilter;
    private List<Room> rooms;
    private DefaultTableModel tableModel;

    public FilteredRoomView(List<Room> rooms) {
        this.rooms = rooms;

        // Ρυθμίσεις παραθύρου
        setTitle("Room Overview with Filters");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // πάνελ φίλτρου
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        filterPanel.setBackground(new Color(240, 240, 240));
        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        filterLabel.setForeground(new Color(60, 60, 60));
        filterPanel.add(filterLabel);

        statusFilter = new JComboBox<>(new String[]{"All", "Available", "Occupied", "Needs Cleaning", "Out of Service"});
        statusFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        statusFilter.setBackground(new Color(255, 255, 255));
        statusFilter.setForeground(new Color(60, 60, 60));
        filterPanel.add(statusFilter);

        statusFilter.addActionListener(e -> updateTable());

        // Πίνακας δεδομένων με προσαρμοσμένο στυλ
        tableModel = new DefaultTableModel(new Object[]{"Room Number", "Type", "Amenities", "Status", "Price"}, 0);
        roomTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Επιτρέπει μόνο την αλλαγή κατάστασης
            }
        };
        roomTable.setFont(new Font("Arial", Font.PLAIN, 12));
        roomTable.setRowHeight(30);
        roomTable.setSelectionBackground(new Color(0, 122, 255));
        roomTable.setSelectionForeground(Color.WHITE);

        // Βελτιωμένη εμφάνιση του πίνακα
        roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        roomTable.getTableHeader().setBackground(new Color(40, 40, 40));
        roomTable.getTableHeader().setForeground(Color.WHITE);

        roomTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        roomTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 3) {
                String newStatus = (String) roomTable.getValueAt(row, col);
                rooms.get(row).setStatus(newStatus);
                JOptionPane.showMessageDialog(this, "Room status updated!");
            }
        });

        // Προσθήκη φίλτρου και πίνακα στο παράθυρο
        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(roomTable), BorderLayout.CENTER);

        updateTable();
        setVisible(true);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        String selectedFilter = (String) statusFilter.getSelectedItem();

        List<Room> filteredRooms = rooms.stream()
                .filter(room -> "All".equals(selectedFilter) || room.getRoomStatus().equals(selectedFilter))
                .collect(Collectors.toList());

        for (Room room : filteredRooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getRoomType(),
                    String.join(";", room.getAmenities()),
                    room.getRoomStatus(),
                    room.getPrice()
            });
        }
    }
}



//package hotelmanagement;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class FilteredRoomView extends JFrame {
//    private JTable roomTable;
//    private JComboBox<String> statusFilter;
//    private List<Room> rooms;
//    private DefaultTableModel tableModel;
//
//    public FilteredRoomView(List<Room> rooms) {
//        this.rooms = rooms;
//
//        setTitle("Room Overview with Filters");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(800, 600);
//
//        // Πάνω φίλτρο για κατάσταση δωματίου
//        JPanel filterPanel = new JPanel();
//        filterPanel.setLayout(new FlowLayout());
//        filterPanel.add(new JLabel("Filter by Status:"));
//
//        statusFilter = new JComboBox<>(new String[]{"All", "Available", "Occupied", "Needs Cleaning", "Out of Service"});
//        filterPanel.add(statusFilter);
//
//        statusFilter.addActionListener(e -> updateTable());
//
//        // Πίνακας δεδομένων
//        tableModel = new DefaultTableModel(new Object[]{"Room Number", "Type", "Amenities", "Status", "Price"}, 0);
//        roomTable = new JTable(tableModel) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 3; // Επιτρέπει μόνο την αλλαγή κατάστασης
//            }
//        };
//        roomTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        roomTable.getModel().addTableModelListener(e -> {
//            int row = e.getFirstRow();
//            int col = e.getColumn();
//            if (col == 3) {
//                String newStatus = (String) roomTable.getValueAt(row, col);
//                rooms.get(row).setStatus(newStatus);
//                JOptionPane.showMessageDialog(this, "Room status updated!");
//            }
//        });
//
//        add(filterPanel, BorderLayout.NORTH);
//        add(new JScrollPane(roomTable), BorderLayout.CENTER);
//
//        updateTable();
//        setVisible(true);
//
//    }
//
//    private void updateTable() {
//        tableModel.setRowCount(0);
//        String selectedFilter = (String) statusFilter.getSelectedItem();
//
//        List<Room> filteredRooms = rooms.stream()
//                .filter(room -> "All".equals(selectedFilter) || room.getRoomStatus().equals(selectedFilter))
//                .collect(Collectors.toList());
//
//        for (Room room : filteredRooms) {
//            tableModel.addRow(new Object[]{room.getRoomNumber(), room.getRoomType(), String.join(";", room.getAmenities()), room.getRoomStatus(), room.getPrice()});
//        }
//    }
//}
