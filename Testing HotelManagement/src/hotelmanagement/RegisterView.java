package hotelmanagement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterView {
    public RegisterView() {
        JFrame frame = new JFrame("Register");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 30);
        frame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);
        frame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        frame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        frame.add(passwordField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 100, 30);
        frame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(150, 150, 200, 30);
        frame.add(emailField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 200, 100, 30);
        frame.add(roleLabel);

        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Administrator", "Manager", "Employee"});
        roleComboBox.setBounds(150, 200, 200, 30);
        frame.add(roleComboBox);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 250, 90, 30);
        frame.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String role = (String) roleComboBox.getSelectedItem();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!");
                    return;
                }

                if (saveUser(username, password, email, role)) {
                    JOptionPane.showMessageDialog(frame, "Registration successful!");
                    frame.dispose();
                    new LoginView(); // Επιστροφή στο Login (ή στο EmployeeDetailsView αν προτιμάς)
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration failed. Try again.");
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean saveUser(String username, String password, String email, String role) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + password + "," + email + "," + role);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
