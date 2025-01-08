package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class EmployeeDetailsView {
    private final DefaultListModel<String> employeeListModel;
    private final Map<String, Employee> employees;
    private static final String USERS_FILE = "users.txt";

    public EmployeeDetailsView() {
        employees = new HashMap<>();
        loadEmployeesFromFile(); // Φορτώνουμε τους χρήστες από το αρχείο

        JFrame frame = new JFrame("Employee Details");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Λίστα εργαζομένων
        employeeListModel = new DefaultListModel<>();
        for (Employee employee : employees.values()) {
            String displayInfo = String.format("%s | Email: %s | Roles: %s",
                    employee.getUsername(),
                    employee.getEmail(),
                    String.join(", ", employee.getRoles()));
            employeeListModel.addElement(displayInfo);
        }

        JList<String> employeeList = new JList<>(employeeListModel);
        employeeList.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeList.setBackground(new Color(255, 255, 255));
        employeeList.setSelectionBackground(new Color(70, 130, 180));
        employeeList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(employeeList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Πάνελ κουμπιών
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Κουμπί "Update Employee"
        JButton updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateEmployeeButton.setBackground(new Color(70, 130, 180));
        updateEmployeeButton.setForeground(Color.WHITE);
        updateEmployeeButton.setPreferredSize(new Dimension(180, 40));
        updateEmployeeButton.setFocusPainted(false);
        updateEmployeeButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        updateEmployeeButton.addActionListener(e -> {
            String selectedValue = employeeList.getSelectedValue();
            if (selectedValue != null) {
                String username = selectedValue.split(" \\| ")[0];
                updateEmployee(username);
            }
        });
        buttonPanel.add(updateEmployeeButton);

        // Κουμπί "Register Employee"
        JButton registerEmployeeButton = new JButton("Register Employee");
        registerEmployeeButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerEmployeeButton.setBackground(new Color(34, 139, 34));
        registerEmployeeButton.setForeground(Color.WHITE);
        registerEmployeeButton.setPreferredSize(new Dimension(180, 40));
        registerEmployeeButton.setFocusPainted(false);
        registerEmployeeButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        registerEmployeeButton.addActionListener(e -> {
            new RegisterView();
            refreshEmployeeList();
        });
        buttonPanel.add(registerEmployeeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadEmployeesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String email = parts[2].trim();
                    Set<String> roles = new HashSet<>(Arrays.asList(parts[3].trim().split(";")));
                    employees.put(username, new Employee(username, password, email, roles));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
        }
    }

    private void saveEmployeesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (Employee employee : employees.values()) {
                writer.printf("%s,%s,%s,%s%n",
                        employee.getUsername(),
                        employee.getPassword(),
                        employee.getEmail(),
                        String.join(";", employee.getRoles()));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving users: " + e.getMessage());
        }
    }

    private void refreshEmployeeList() {
        employeeListModel.clear();
        employees.clear();
        loadEmployeesFromFile();
        for (Employee employee : employees.values()) {
            String displayInfo = String.format("%s | Email: %s | Roles: %s",
                    employee.getUsername(),
                    employee.getEmail(),
                    String.join(", ", employee.getRoles()));
            employeeListModel.addElement(displayInfo);
        }
    }

    private void updateEmployee(String username) {
        if (username == null) {
            JOptionPane.showMessageDialog(null, "Please select an employee to update.");
            return;
        }

        Employee employee = employees.get(username);
        if (employee == null) {
            JOptionPane.showMessageDialog(null, "Employee not found.");
            return;
        }

        // Δημιουργία παραθύρου ενημέρωσης
        JTextField usernameField = new JTextField(employee.getUsername());
        JTextField passwordField = new JTextField(employee.getPassword());
        JTextField emailField = new JTextField(employee.getEmail());

        JCheckBox adminCheck = new JCheckBox("Administrator", employee.getRoles().contains("Administrator"));
        JCheckBox managerCheck = new JCheckBox("Manager", employee.getRoles().contains("Manager"));
        JCheckBox employeeCheck = new JCheckBox("Employee", employee.getRoles().contains("Employee"));

        JPanel rolePanel = new JPanel();
        rolePanel.setBackground(new Color(240, 240, 240));
        rolePanel.add(adminCheck);
        rolePanel.add(managerCheck);
        rolePanel.add(employeeCheck);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(new Color(240, 240, 240));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Roles:"));
        panel.add(rolePanel);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            employee.setUsername(usernameField.getText());
            employee.setPassword(passwordField.getText());
            employee.setEmail(emailField.getText());

            Set<String> updatedRoles = new HashSet<>();
            if (adminCheck.isSelected()) updatedRoles.add("Administrator");
            if (managerCheck.isSelected()) updatedRoles.add("Manager");
            if (employeeCheck.isSelected()) updatedRoles.add("Employee");
            employee.setRoles(updatedRoles);

            saveEmployeesToFile();
            refreshEmployeeList();
            JOptionPane.showMessageDialog(null, "Employee updated successfully!");
        }
    }

    public static void main(String[] args) {
        new EmployeeDetailsView();
    }
}

//package hotelmanagement;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.util.*;
//
//public class EmployeeDetailsView {
//    private final DefaultListModel<String> employeeListModel; // Μοντέλο για τη λίστα εργαζομένων
//    private final Map<String, Employee> employees; // Λίστα εργαζομένων με τα στοιχεία τους
//    private static final String USERS_FILE = "users.txt"; // Το αρχείο με τους χρήστες
//
//    public EmployeeDetailsView() {
//        employees = new HashMap<>();
//        loadEmployeesFromFile(); // Φορτώνουμε τους χρήστες από το αρχείο
//
//        JFrame frame = new JFrame("Employee Details");
//        frame.setSize(600, 500);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//
//        // Λίστα εργαζομένων
//        employeeListModel = new DefaultListModel<>();
//        for (Employee employee : employees.values()) {
//            String displayInfo = String.format("%s | Email: %s | Roles: %s",
//                    employee.getUsername(),
//                    employee.getEmail(),
//                    String.join(", ", employee.getRoles()));
//            employeeListModel.addElement(displayInfo);
//        }
//
//        JList<String> employeeList = new JList<>(employeeListModel);
//        JScrollPane scrollPane = new JScrollPane(employeeList);
//        frame.add(scrollPane, BorderLayout.CENTER);
//
//        // Πάνελ κουμπιών
//        JPanel buttonPanel = new JPanel();
//
//        // Κουμπί "Update Employee"
//        JButton updateEmployeeButton = new JButton("Update Employee");
//        updateEmployeeButton.addActionListener(e -> {
//            String selectedValue = employeeList.getSelectedValue();
//            if (selectedValue != null) {
//                String username = selectedValue.split(" \\| ")[0]; // Απομόνωση του username
//                updateEmployee(username);
//            }
//        });
//        buttonPanel.add(updateEmployeeButton);
//
//        // Κουμπί "Register Employee"
//        JButton registerEmployeeButton = new JButton("Register Employee");
//        registerEmployeeButton.addActionListener(e -> {
//            new RegisterView(); // Άνοιγμα του RegisterView
//            refreshEmployeeList(); // Ενημέρωση της λίστας μετά την εγγραφή
//        });
//        buttonPanel.add(registerEmployeeButton);
//
//        frame.add(buttonPanel, BorderLayout.SOUTH);
//
//        frame.setVisible(true);
//    }
//
//    private void loadEmployeesFromFile() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 4) {
//                    String username = parts[0].trim();
//                    String password = parts[1].trim();
//                    String email = parts[2].trim();
//                    Set<String> roles = new HashSet<>(Arrays.asList(parts[3].trim().split(";")));
//                    employees.put(username, new Employee(username, password, email, roles));
//                }
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
//        }
//    }
//
//    private void saveEmployeesToFile() {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
//            for (Employee employee : employees.values()) {
//                writer.printf("%s,%s,%s,%s%n",
//                        employee.getUsername(),
//                        employee.getPassword(),
//                        employee.getEmail(),
//                        String.join(";", employee.getRoles()));
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error saving users: " + e.getMessage());
//        }
//    }
//
//    private void refreshEmployeeList() {
//        employeeListModel.clear();
//        employees.clear();
//        loadEmployeesFromFile();
//        for (Employee employee : employees.values()) {
//            String displayInfo = String.format("%s | Email: %s | Roles: %s",
//                    employee.getUsername(),
//                    employee.getEmail(),
//                    String.join(", ", employee.getRoles()));
//            employeeListModel.addElement(displayInfo);
//        }
//    }
//
//    private void updateEmployee(String username) {
//        if (username == null) {
//            JOptionPane.showMessageDialog(null, "Please select an employee to update.");
//            return;
//        }
//
//        Employee employee = employees.get(username);
//        if (employee == null) {
//            JOptionPane.showMessageDialog(null, "Employee not found.");
//            return;
//        }
//
//        // Δημιουργία παραθύρου ενημέρωσης
//        JTextField usernameField = new JTextField(employee.getUsername());
//        JTextField passwordField = new JTextField(employee.getPassword());
//        JTextField emailField = new JTextField(employee.getEmail());
//
//        JCheckBox adminCheck = new JCheckBox("Administrator", employee.getRoles().contains("Administrator"));
//        JCheckBox managerCheck = new JCheckBox("Manager", employee.getRoles().contains("Manager"));
//        JCheckBox employeeCheck = new JCheckBox("Employee", employee.getRoles().contains("Employee"));
//
//        JPanel rolePanel = new JPanel();
//        rolePanel.add(adminCheck);
//        rolePanel.add(managerCheck);
//        rolePanel.add(employeeCheck);
//
//        JPanel panel = new JPanel(new GridLayout(5, 2));
//        panel.add(new JLabel("Username:"));
//        panel.add(usernameField);
//        panel.add(new JLabel("Password:"));
//        panel.add(passwordField);
//        panel.add(new JLabel("Email:"));
//        panel.add(emailField);
//        panel.add(new JLabel("Roles:"));
//        panel.add(rolePanel);
//
//        int result = JOptionPane.showConfirmDialog(null, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
//
//        if (result == JOptionPane.OK_OPTION) {
//            employee.setUsername(usernameField.getText());
//            employee.setPassword(passwordField.getText());
//            employee.setEmail(emailField.getText());
//
//            Set<String> updatedRoles = new HashSet<>();
//            if (adminCheck.isSelected()) updatedRoles.add("Administrator");
//            if (managerCheck.isSelected()) updatedRoles.add("Manager");
//            if (employeeCheck.isSelected()) updatedRoles.add("Employee");
//            employee.setRoles(updatedRoles);
//
//            saveEmployeesToFile(); // Αποθηκεύουμε τις αλλαγές στο αρχείο
//            refreshEmployeeList(); // Ενημέρωση της λίστας
//            JOptionPane.showMessageDialog(null, "Employee updated successfully!");
//        }
//    }
//
//    public static void main(String[] args) {
//        new EmployeeDetailsView();
//    }
//}
