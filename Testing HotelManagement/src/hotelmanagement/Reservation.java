    package hotelmanagement;

    public class Reservation {
        private String customerName;
        private String customerSurname;
        private String customerPhone;
        private String customerCountry;
        private String customerAddress; // Άλλαξα το hotelName σε customerAddress
        private String roomNumber;
        private String fromDate;
        private String toDate;
        private double amount;

        public Reservation(String customerName, String customerSurname, String customerPhone,
                           String customerCountry, String customerAddress, String roomNumber,
                           String fromDate, String toDate, double amount) {
            this.customerName = customerName;
            this.customerSurname = customerSurname;
            this.customerPhone = customerPhone;
            this.customerCountry = customerCountry;
            this.customerAddress = customerAddress; // Ενημέρωση πεδίου
            this.roomNumber = roomNumber;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.amount = amount;
        }

        // Getters and Setters
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getCustomerSurname() { return customerSurname; }
        public void setCustomerSurname(String customerSurname) { this.customerSurname = customerSurname; }

        public String getCustomerPhone() { return customerPhone; }
        public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

        public String getCustomerCountry() { return customerCountry; }
        public void setCustomerCountry(String customerCountry) { this.customerCountry = customerCountry; }

        public String getCustomerAddress() { return customerAddress; } // Getter για address
        public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; } // Setter

        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

        public String getFromDate() { return fromDate; }
        public void setFromDate(String fromDate) { this.fromDate = fromDate; }

        public String getToDate() { return toDate; }
        public void setToDate(String toDate) { this.toDate = toDate; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }
