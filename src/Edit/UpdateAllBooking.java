
package Edit;

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
import photography_booking_system.Booking;
import photography_booking_system.Packages;
import photography_booking_system.Photographer;
import photography_booking_system.config;

public class UpdateAllBooking {
     Scanner sc = new Scanner (System.in);
     config conf = new config();
   
     
     
     public void OverallUpdate() {
        Booking book = new Booking();
        book.viewBookings();
        
        System.out.print("Enter ID to Update: ");
        int bid = sc.nextInt();
        sc.nextLine(); 

            while (conf.getSingleValue("SELECT b_id FROM BOOKINGS WHERE b_id = ?", bid) == 0) {
                System.out.println("Selected ID doesn't exist! ");
                System.out.print("Select Booking ID Again: ");
                bid = sc.nextInt();
                sc.nextLine(); 
            }

        System.out.print("Do you want to cancel this booking? (Yes/No): ");
        String cancelResponse = sc.nextLine();

        if (cancelResponse.equalsIgnoreCase("Yes")) {
            String cancelQuery = "UPDATE BOOKINGS SET status = 'Cancelled' WHERE b_id = ?";
            conf.updateRecord(cancelQuery, bid);
            System.out.println("The booking has been successfully cancelled.");
            return; 
        }
            
        Photographer photo = new Photographer();
        photo.viewPhotographers();

        System.out.print("Enter Photographer ID: ");
        int pid = sc.nextInt();
        sc.nextLine(); 

            String psql = "SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?";
                while (conf.getSingleValue(psql, pid) == 0) {
                    System.out.println("Selected ID doesn't exist! ");
                    System.out.print("Select Photographer ID Again: ");
                    pid = sc.nextInt();
                    sc.nextLine();
                }

        Packages pack = new Packages();
        pack.viewPackages();

        System.out.print("Enter Package ID: ");
        int ppid = sc.nextInt();
        sc.nextLine();

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


            String qry = "UPDATE BOOKINGS SET p_id = ?, pp_id = ?, event_date = ?, location = ?, pay_method = ?, downpayment = ?, balance_due = ?, status = ? WHERE b_id = ?";
            conf.updateRecord(qry, pid, ppid, e_dt, loc, paymethod, downpayment, due, status, bid);
            displaySpecificBooking(bid);
  
    }
     
     private void displaySpecificBooking(int bid) {
    String query = """
        SELECT 
            b.b_id AS BookingID, 
            c.c_name AS CustomerName,
            p.name AS PhotographerName, 
            pp.pname AS PackageName, 
            b.event_date AS EventDate, 
            b.location AS Location, 
            b.pay_method AS PaymentMethod, 
            b.downpayment AS Downpayment, 
            b.status AS Status 
        FROM BOOKINGS b
        JOIN CUSTOMERS c ON b.c_id = c.c_id
        JOIN PHOTOGRAPHERS p ON b.p_id = p.p_id
        JOIN PACKAGES pp ON b.pp_id = pp.pp_id
        WHERE b.b_id = ?
    """;

    try (java.sql.Connection conn = conf.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, bid);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Table Header
            System.out.println("=====================================================================================================================================================");
            System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-15s | %-10s | %-15s | %-10s | %-10s |\n", 
                "Booking ID", "Customer", "Photographer", "Package", "Event Date", 
                "Location", "Payment Method", "Downpayment", "Status");
            System.out.println("=====================================================================================================================================================");
            
            // Booking Details
            System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-15s | %-10s | %-15s | %-10s  | %-10s |\n", 
                rs.getInt("BookingID"), 
                rs.getString("CustomerName"),
                rs.getString("PhotographerName"), 
                rs.getString("PackageName"), 
                rs.getString("EventDate"), 
                rs.getString("Location"), 
                rs.getString("PaymentMethod"), 
                rs.getDouble("Downpayment"), 
                rs.getString("Status"));
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("No details found for the selected booking.");
        }

    } catch (SQLException e) {
        System.out.println("Error fetching booking details: " + e.getMessage());
    }
}
    
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


