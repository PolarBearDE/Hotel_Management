package hotelmanagement;

public class User {
    private String username;
    private String role; // π.χ., "Admin", "Receptionist"

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public boolean hasAccess(String viewName) {
        if (role.equals("Admin")) return true; // Ο Admin έχει πρόσβαση παντού
        if (role.equals("Receptionist")) return !viewName.equals("RevenueReportView");
        return false;
    }
}
