package hotelmanagement;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String name;
    private String phone;
    private String email;

    // Constructor
    public Customer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String name, String contactInfo) {
    }

    // Getters
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Στατική μέθοδος για αποθήκευση πελατών
    private static Map<String, Customer> customerDatabase = new HashMap<>();

    public static void saveCustomerDetails(Customer customer) {
        customerDatabase.put(customer.getPhone(), customer); // Αποθήκευση με το τηλέφωνο ως κλειδί
    }

    // Μέθοδος για ενημέρωση στοιχείων πελάτη
    public static void updateCustomerDetails(String phone, String newName, String newEmail) {
        Customer customer = customerDatabase.get(phone);
        if (customer != null) {
            customer.name = newName != null ? newName : customer.name;
            customer.email = newEmail != null ? newEmail : customer.email;
            System.out.println("Customer details updated.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    // Μέθοδος για ανάκτηση πελάτη βάσει τηλεφώνου
    public static Customer getCustomerByPhone(String phone) {
        return customerDatabase.get(phone);
    }

    // Μέθοδος για εκτύπωση όλων των πελατών
    public static void printAllCustomers() {
        for (Customer customer : customerDatabase.values()) {
            System.out.println("Name: " + customer.getName() + ", Phone: " + customer.getPhone() + ", Email: " + customer.getEmail());
        }
    }

    // Μέθοδος για αποστολή στοιχείων πελάτη στο τοπικό αστυνομικό τμήμα
    public static void sendDetailsToPolice(String phone) {
        Customer customer = customerDatabase.get(phone);
        if (customer != null) {
            System.out.println("Sending customer details to the local police station: " + customer.getName() + ", " + customer.getPhone() + ", " + customer.getEmail());
        } else {
            System.out.println("Customer not found to send to police.");
        }
    }
}
