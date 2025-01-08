package hotelmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LoginView {
    public LoginView() {
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);


        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, getHeight(), Color.WHITE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);
        frame.setContentPane(backgroundPanel);

        JLabel titleLabel = new JLabel("Welcome to Polar Hotel");
        titleLabel.setBounds(50, 10, 300, 40);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 30);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backgroundPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 70, 200, 30);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backgroundPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 30);
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backgroundPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 200, 30);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backgroundPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 180, 200, 35);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        backgroundPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticateUser(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    frame.dispose();
                    new MainView(username); // Άνοιγμα του MainView
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new LoginView();
    }
}
