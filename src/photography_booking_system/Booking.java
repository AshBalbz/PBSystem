package photography_booking_system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Edit.UpdateAllBooking;
import Edit.SpecificBookingUpdate;

public class Booking {
        Scanner sc = new Scanner (System.in);
        config conf = new config();
        int act;

        public void BookingTransactions() {
        
        do {
            try {
                System.out.println("\n-----------------------------------------------");
                System.out.println("       == WELCOME TO BOOKING SECTION ==");
                System.out.println("-----------------------------------------------");

                System.out.println("1. Add Booking");
                System.out.println("2. View Bookings");
                System.out.println("3. Update Booking");
                System.out.println("4. Delete Booking");
                System.out.println("5. Back to Main Menu");
                System.out.println("-----------------------------------------------");

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
                        updateBookings();
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
        System.out.println("");

            String csql = "SELECT c_id FROM CUSTOMERS WHERE c_id = ?";
                while (conf.getSingleValue(csql, cid) == 0) {
                    System.out.println("Selected ID doesn't exist! ");
                    System.out.print("Select Customer Id Again: ");
                    cid = sc.nextInt();
                    sc.nextLine();
                }

        Photographer photo = new Photographer();
        photo.viewPhotographers();

        System.out.print("Enter Photographer ID: ");
        int pid = sc.nextInt();
        System.out.println("");
        
            String psql = "SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?";
                while (conf.getSingleValue(psql, pid) == 0) {
                    System.out.println("Selected ID doesn't exist! ");
                    System.out.print("Select Photographer Id Again: ");
                    pid = sc.nextInt();
                    sc.nextLine();
                }

        Packages pack = new Packages();
        pack.viewPackages();

        System.out.print("Enter Package ID: ");
        int ppid = sc.nextInt();
        sc.nextLine();
        System.out.println("");

            String ppsql = "SELECT pp_id FROM PACKAGES WHERE pp_id = ?";
                while (conf.getSingleValue(ppsql, ppid) == 0) {
                    System.out.println("Selected ID doesn't exist! ");
                    System.out.print("Select Package Id Again: ");
                    ppid = sc.nextInt();
                    sc.nextLine();
                }

            String e_dt = "";
                while (true) {
                    System.out.print("Enter Event Date (YYYY-MM-DD format): ");
                    e_dt = sc.nextLine();

                    String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
                    Pattern pattern = Pattern.compile(dateRegex);
                    Matcher matcher = pattern.matcher(e_dt);

                    if (!matcher.matches()) {
                        System.out.println("Invalid input. Please enter a valid date in the format YYYY-MM-DD.\n");
                        continue;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    try {
                        sdf.parse(e_dt);

                        if (isDateAvailable(e_dt)) {
                            break;
                        } else {
                            System.out.println("This date is not available. Please choose another date.\n");
                        }

                    } catch (ParseException e) {
                        System.out.println("Invalid date. Please enter a valid date (e.g., 2024-02-29 is not valid if it's not a leap year).\n");
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
                        sc.nextLine(); // Consume newline character

                        if (downpayment < 0) {
                            System.out.println("Downpayment cannot be negative. Please enter a valid downpayment.\n");
                        } else if (downpayment > amount) {
                            System.out.println("Invalid downpayment. Try again. Make sure that the downpayment does not exceed the total amount of " + amount + ".\n");
                        } else {
                            break; // Exit the loop when downpayment is valid
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number for downpayment.\n");
                        sc.nextLine(); // Clear invalid input
                    }
                }

                double due = amount - downpayment;
                String status = determineStatus(due, e_dt);


        String bookqry = "INSERT INTO BOOKINGS(c_id, p_id, pp_id, event_date, location, payment_date, pay_method, amount, downpayment, balance_due, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(bookqry, cid, pid, ppid, e_dt, loc, date, paymethod, amount, downpayment, due, status);
    }

  
    public void viewBookings(){
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t == BOOKING LIST ==");
                String qry = "SELECT b_id, c_name, name, pname, event_date, location, payment_date, downpayment, balance_due, status FROM BOOKINGS "
                + "LEFT JOIN CUSTOMERS ON CUSTOMERS.c_id = BOOKINGS.c_id LEFT JOIN PHOTOGRAPHERS ON PHOTOGRAPHERS.p_id = BOOKINGS.p_id LEFT JOIN PACKAGES ON PACKAGES.pp_id = BOOKINGS.pp_id";
        
                String[] header = {"BID", "Customer", "Photographer", "Package Name", "Event Date", "Location", "Payment Date","Downpayment", "Balance Due", "Status"};
                String[] column = {"b_id", "c_name", "name", "pname", "event_date", "location", "payment_date", "downpayment", "balance_due", "status" };
       
        conf.viewRecords(qry, header, column);
    }
    
    
     private void updateBookings() {
        SpecificBookingUpdate edit = new SpecificBookingUpdate();
        UpdateAllBooking all = new UpdateAllBooking();
        
        
        do {
            try {
                System.out.println("");
                System.out.println("-----------------------------------------------");
                System.out.println("   == Kindly Select From The Option Below ==         ");
                System.out.println("-----------------------------------------------");

                System.out.println("1. Update a Specific Data ");
                System.out.println("2. Back ");
                System.out.println("-----------------------------------------------");

                System.out.print("\nSelect an Option: ");
                act = sc.nextInt();
                sc.nextLine(); 

                switch (act) {
                    case 1:
                    edit.EditSpecificBooking();
                       break;

                    case 2:
                    all.OverallUpdate();
                    break;

                    case 3:
                        System.out.println("Going back....");
                        break;
               
                    default:
                        System.out.println("Invalid Option.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();  
                act = -1;  
            }
        } while (act != 3);    
        
        
        
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
        
        
        System.out.print("Are you sure you want to delete this booking? (Yes/No): ");
        String confirmation = sc.next();

            if (confirmation.equalsIgnoreCase("Yes")) {
                String qry = "DELETE FROM BOOKINGS WHERE b_id = ?";
                conf.deleteRecord(qry, id);
            System.out.println("Booking deleted successfully.");
                } else {
            System.out.println("Deletion canceled.");
            }
}
    
    
            LocalDate currdate = LocalDate.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String date = currdate.format(format);
            
     public boolean isDateAvailable(String eventDate) {
        String query = "SELECT COUNT(*) FROM BOOKINGS WHERE event_date = ?";
            try (java.sql.Connection conn = conf.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, eventDate);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) { 
                    return rs.getInt(1) == 0;
                } else {
                    return false;
                }

            } catch (SQLException e) {
                System.out.println("Error checking date availability: " + e.getMessage());
                return false;
            }
}
          
    public String determineStatus(double due, String e_dt) {
        LocalDate eventDate = LocalDate.parse(e_dt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (due > 0) {
            return "Pending";
        } else if (due == 0 && LocalDate.now().isBefore(eventDate)) {
            return "Confirmed";
        } else if (due == 0 && LocalDate.now().isAfter(eventDate)) {
            return "Completed";
        } else {
            return "Cancelled";  
        }
          }
  
}

    
