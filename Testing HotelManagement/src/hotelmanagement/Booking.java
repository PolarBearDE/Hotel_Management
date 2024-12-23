package hotelmanagement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private String customerName;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomType;
    private boolean isPrepaid; // Προπληρωμή
    private boolean isTraditional; // Παραδοσιακή κράτηση
    private double price;
    private int groupSize; // Μέγεθος γκρουπ
    private static double hotelOccupancy = 0.25; // Για παράδειγμα, 25% πληρότητα ξενοδοχείου

    // Μέθοδος για επιστροφή της ημερομηνίας αναχώρησης
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Booking(String customerName, String roomNumber, LocalDate checkInDate, LocalDate checkOutDate, String roomType, boolean isPrepaid, boolean isTraditional, double price, int groupSize) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.isPrepaid = isPrepaid;
        this.isTraditional = isTraditional;
        this.price = price;
        this.groupSize = groupSize;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPrepaid() {
        return isPrepaid;
    }

    public boolean isTraditional() {
        return isTraditional;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public int getGroupSize() {
        return groupSize;
    }

    // Υπολογισμός προπληρωμής για την παραδοσιακή κράτηση
    public double calculateDeposit() {
        if (isTraditional) {
            return price * 0.20; // 20% για παραδοσιακή κράτηση
        }
        return 0;
    }

    // Υπολογισμός προπληρωμής για την προπληρωμένη κράτηση (50% αν είναι πάνω από 90 μέρες)
    public double calculatePrepaidAmount() {
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), checkInDate);
        if (isPrepaid && daysBeforeCheckIn >= 90) {
            return price * 0.50; // 50% αν είναι 90 μέρες πριν την άφιξη
        }
        return 0;
    }

    // Υπολογισμός για γκρουπ μεγαλύτερο από 20 άτομα
    public double calculateGroupDiscountOrPrepayment() {
        if (groupSize > 20) {
            if (isPrepaid) {
                return price * 0.25; // 25% προπληρωμή για γκρουπ
            } else if (isTraditional) {
                return price * 0.40; // 40% έκπτωση για παραδοσιακή κράτηση σε γκρουπ
            }
        }
        return 0;
    }

    // Υπολογισμός για μειωμένη τιμή αν η πληρότητα είναι κάτω από 30%
    public double calculateOccupancyDiscount() {
        if (hotelOccupancy < 0.30) {
            return price * 0.10; // 10% μείωση αν η πληρότητα είναι κάτω από 30%
        }
        return 0;
    }

    // Υπολογισμός τελικής τιμής με όλες τις εκπτώσεις και προπληρωμές
    public double calculateFinalPrice() {
        double discount = calculateGroupDiscountOrPrepayment();
        double occupancyDiscount = calculateOccupancyDiscount();
        return price - discount - occupancyDiscount;
    }

    // Έλεγχος για ακύρωση κράτησης και επιστροφή προκαταβολής
    public boolean checkCancellation(LocalDate cancellationDate) {
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(cancellationDate, checkInDate);
        if (daysBeforeCheckIn <= 15) {
            // Χάνεται η προκαταβολή
            return false;
        } else if (groupSize > 1 && daysBeforeCheckIn >= 60) {
            // Επιστροφή χρημάτων για γκρουπ
            return true;
        } else {
            // Επιστροφή χρημάτων για ιδιώτες
            return true;
        }
    }
}
