package hotelmanagement;

import java.io.*;
import java.util.*;

public class UserManager {
    public static List<String[]> readUsersFromFile() {
        List<String[]> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    users.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}
