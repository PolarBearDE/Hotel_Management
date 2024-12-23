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

        setTitle("Room Overview with Filters");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        // Πάνω φίλτρο για κατάσταση δωματίου
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.add(new JLabel("Filter by Status:"));

        statusFilter = new JComboBox<>(new String[]{"All", "Available", "Occupied", "Needs Cleaning", "Out of Service"});
        filterPanel.add(statusFilter);

        statusFilter.addActionListener(e -> updateTable());

        // Πίνακας δεδομένων
        tableModel = new DefaultTableModel(new Object[]{"Room Number", "Type", "Amenities", "Status", "Price"}, 0);
        roomTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Επιτρέπει μόνο την αλλαγή κατάστασης
            }
        };
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
            tableModel.addRow(new Object[]{room.getRoomNumber(), room.getRoomType(), String.join(";", room.getAmenities()), room.getRoomStatus(), room.getPrice()});
        }
    }
}
