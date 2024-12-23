package hotelmanagement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CostCalculator {
    public static double calculateCost(Room room, LocalDate checkIn, LocalDate checkOut, boolean isPeakSeason) {
        double basePrice = room.getPrice();
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        // Εφαρμογή εποχιακού συντελεστή
        double seasonMultiplier = isPeakSeason ? 1.5 : 1.0;

        // Επιστροφή συνολικού κόστους
        return basePrice * nights * seasonMultiplier;
    }
}
