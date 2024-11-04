package photography_booking_system;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Booking {
 Scanner sc = new Scanner (System.in);
        config conf = new config();
        public void BookingTransactions() {
        int act;
        
        do {
            try {
                System.out.println("\n-----------------------------------------------");
                System.out.println("         == Welcome to Booking Section ==");
                System.out.println("-----------------------------------------------");

                System.out.println("1. Add Booking");
                System.out.println("2. View Bookings");
                System.out.println("3. Update Booking");
                System.out.println("4. Delete Booking");
                System.out.println("5. Back to Main Menu");

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine();

                switch (act) {
                    case 1:
                        addBookings();
                        break;

                    case 2:
                        viewBookings();
                        break;

                    case 3:
                        viewBookings();
                        updateBookings();
                        viewBookings();
                        break;

                    case 4:
                        viewBookings();
                        deleteBookings();
                        viewBookings();
                        break;

                    case 5:
                        System.out.println("Returning to the main menu...");
                        break;

                    default:
                        System.out.println("Invalid Option.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
                act = -1;
            }
        } while (act != 5);
    }
    
    private void addBookings(){
        Customer cust = new Customer();
        cust.viewCustomers();
        
        System.out.print("Enter Customer ID: ");
        int cid = sc.nextInt();
        
        String csql = "SELECT c_id FROM CUSTOMERS WHERE c_id = ?";
        while(conf.getSingleValue(csql, cid) == 0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Customer Id Again: ");
            cid = sc.nextInt();
            sc.nextLine();
        }
        
        
        Photographer photo = new Photographer();
        photo.viewPhotographers();
        
        System.out.print("Enter Photographer ID: ");
        int pid = sc.nextInt();
        
        String psql = "SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?";
        while(conf.getSingleValue(psql, pid) == 0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Photographer Id Again: ");
            cid = sc.nextInt();
            sc.nextLine();
        }
        
        
        Packages pack = new Packages();
        pack.viewPackages();
        
        System.out.print("Enter Package ID: ");
        int ppid = sc.nextInt();
        
        String ppsql = "SELECT pp_id FROM PACKAGES WHERE pp_id = ?";
        while(conf.getSingleValue(ppsql, ppid) == 0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Package Id Again: ");
            cid = sc.nextInt();
            sc.nextLine();
        }
        
        int e_dt = 0;
        while (true) {
            System.out.print("Enter Event Date (YYYY-MM-DD format): ");
            try {
                e_dt = sc.nextInt();
                sc.nextLine();
                if (e_dt <= 0) {
                    System.out.println("Please enter a valid date in the format YYYY-MM-DD.\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid date format.\n");
                sc.nextLine();
            }
        }

        String loc;
        while (true) {
            System.out.print("Enter Location: ");
            loc = sc.nextLine();
           
            if (loc.trim().isEmpty()) {
                System.out.println("Location cannot be empty. Please enter a valid location.\n");
            } else {
                break;
            }
        }
            
        LocalDate currdate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = currdate.format(format);
        
        
        String paymethod;
        while (true) {
            System.out.print("Enter Payment Method: ");
            paymethod = sc.nextLine();
           
            if (paymethod.trim().isEmpty()) {
                System.out.println("Payment Method cannot be empty. Please enter a valid payment method.\n");
            } else {
                break;
            }
        }
            
        String amountqry = "SELECT amount FROM PACKAGES WHERE pp_id = ?";
        double amount = conf.getSingleValue(amountqry, ppid);

       
        double downpayment = 0;
        while (true) {
            System.out.print("Enter Downpayment: ");
            try {
                downpayment = sc.nextDouble();
                sc.nextLine();
                if (downpayment < 0) {
                    System.out.println("Downpayment cannot be negative. Please enter a valid downpayment.\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for downpayment.\n");
                sc.nextLine();
            }
        }
        
        double due;
        due = amount - downpayment;
        System.out.println("Balance Due: \n"+due);
        
        String status = "Pending";
        
        String bookqry = "INSERT INTO BOOKINGS(c_id, p_id, pp_id, event_date, location, payment_date, pay_method, amount, downpayment, balance_due, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(bookqry, cid, pid, ppid, e_dt, loc, date, paymethod, amount, downpayment, due, status);
    
    }
    
    public void viewBookings(){
        String qry = "SELECT b_id, c_name, name, pname, event_date, location, payment_date, downpayment, balance_due, status FROM BOOKINGS "
                + "LEFT JOIN CUSTOMERS ON CUSTOMERS.c_id = BOOKINGS.c_id LEFT JOIN PHOTOGRAPHERS ON PHOTOGRAPHERS.p_id = BOOKINGS.p_id LEFT JOIN PACKAGES ON PACKAGES.pp_id = BOOKINGS.pp_id";
        
        String[] header = {"BID", "Customer", "Photographer", "Package Name", "Event Date", "Locatioin", "Payment Date","Downpayment", "Balance Due", "Status"};
        String[] column = {"b_id", "c_name", "name", "pname", "event_date", "location", "payment_date", "downpayment", "balance_due", "status" };
       
        conf.viewRecords(qry, header, column);
        
        
        
        }
    
    private void updateBookings(){
        System.out.print("Enter ID to Update: ");
        int bid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT b_id FROM BOOKINGS WHERE b_id = ?",bid)==0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Photographer Id Again: ");
            bid = sc.nextInt();
            sc.nextLine();
        }
        
        Customer cust = new Customer();
        cust.viewCustomers();
        
        System.out.print("Enter Customer ID: ");
        int cid = sc.nextInt();
        
        String csql = "SELECT c_id FROM CUSTOMERS WHERE c_id = ?";
        while(conf.getSingleValue(csql, cid) == 0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Customer Id Again: ");
            cid = sc.nextInt();
            sc.nextLine();
        }
        
        
        Photographer photo = new Photographer();
        photo.viewPhotographers();
        
        System.out.print("Enter Photographer ID: ");
        int pid = sc.nextInt();
        
        String psql = "SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?";
        while(conf.getSingleValue(psql, pid) == 0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Photographer Id Again: ");
            cid = sc.nextInt();
            sc.nextLine();
        }
        
        
        Packages pack = new Packages();
        pack.viewPackages();
        
        System.out.print("Enter Package ID: ");
        int ppid = sc.nextInt();
        
        String ppsql = "SELECT pp_id FROM PACKAGES WHERE pp_id = ?";
        while(conf.getSingleValue(ppsql, ppid) == 0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Package Id Again: ");
            cid = sc.nextInt();
            sc.nextLine();
        }
        
        int e_dt = 0;
        while (true) {
            System.out.print("Enter Event Date (YYYY-MM-DD format): ");
            try {
                e_dt = sc.nextInt();
                sc.nextLine();
                if (e_dt <= 0) {
                    System.out.println("Please enter a valid date in the format YYYY-MM-DD.\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid date format.\n");
                sc.nextLine();
            }
        }

        String loc;
        while (true) {
            System.out.print("Enter Location: ");
            loc = sc.nextLine();
           
            if (loc.trim().isEmpty()) {
                System.out.println("Location cannot be empty. Please enter a valid location.\n");
            } else {
                break;
            }
        }
            
        LocalDate currdate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = currdate.format(format);
        
        
        String paymethod;
        while (true) {
            System.out.print("Enter Payment Method: ");
            paymethod = sc.nextLine();
           
            if (paymethod.trim().isEmpty()) {
                System.out.println("Payment Method cannot be empty. Please enter a valid payment method.\n");
            } else {
                break;
            }
        }
            
        String amountqry = "SELECT amount FROM PACKAGES WHERE pp_id = ?";
        double amount = conf.getSingleValue(amountqry, ppid);

       
        double downpayment = 0;
        while (true) {
            System.out.print("Enter Downpayment: ");
            try {
                downpayment = sc.nextDouble();
                sc.nextLine();
                if (downpayment < 0) {
                    System.out.println("Downpayment cannot be negative. Please enter a valid downpayment.\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for downpayment.\n");
                sc.nextLine();
            }
        }
        
        double due;
        due = amount - downpayment;
        System.out.println("Balance Due: \n"+due);
        
        String status = "Pending";
        
        String qry = "UPDATE BOOKINGS SET c_id = ?, p_id = ?, pp_id = ?, event_date = ?, location = ?, pay_method = ?, downpayment = ?, balance_due = ? WHERE b_id = ?";
        conf.updateRecord(qry, cid, pid, ppid, e_dt, loc, paymethod, downpayment, due, bid);
        
    }
    
    
       private void deleteBookings(){
    
        System.out.print("Enter ID to Delete: ");
        int id = sc.nextInt();
        
        while(conf.getSingleValue("SELECT b_id FROM BOOKINGS WHERE b_id = ?",id)==0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Booking Id Again: ");
            id = sc.nextInt();
            sc.nextLine();
        }
        
        String qry = "DELETE FROM BOOKINGS WHERE b_id = ?";
        conf.deleteRecord(qry, id);
        
}
    
}