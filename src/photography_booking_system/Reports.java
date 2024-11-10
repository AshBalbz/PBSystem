package photography_booking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Timestamp;

public class Reports {
    private Scanner sc = new Scanner(System.in);
    config conf = new config();

    public void ReportTransaction() {
        int act;
        do {
            try {
                System.out.println("\n-------------------------------------------------");
                System.out.println("     == WELCOME TO REPORT HISTORY SECTION ==");
                System.out.println("-------------------------------------------------");

                System.out.println("1. Generate Specific Customer");
                System.out.println("2. Generate Specific Photographer");
                System.out.println("3. Generate All Booking History");
                System.out.println("4. Back to Main Menu");

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine(); 

                switch (act) {
                    case 1:
                        displayCustomerBookingCounts();
                        break;
                    case 2:
                        displayPhotographerBookingCounts();
                        break;
                    case 3:
                        Booking book = new Booking();
                        book.viewBookings();
                        break;
                    case 4:
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
        } while (act != 4);
    }

    
    public void displayCustomerBookingCounts() {
        Customer cust = new Customer();
        cust.viewCustomers(); 

        System.out.print("Enter Customer ID: ");
        int cid = sc.nextInt();
        sc.nextLine();  

            String csql = "SELECT COUNT(*) FROM CUSTOMERS WHERE c_id = ?";
                while (conf.getSingleValue(csql, cid) == 0) {
                    System.out.println("Selected ID doesn't exist! ");
                    System.out.print("Select Customer ID Again: ");
                    cid = sc.nextInt();
                    sc.nextLine();  
                }

        String customerQuery = "SELECT c_id, c_name, c_email, c_pnum FROM CUSTOMERS WHERE c_id = ?";
        String bookingQuery = "SELECT BOOKINGS.b_id, PHOTOGRAPHERS.name AS photographer, PACKAGES.pname AS package, BOOKINGS.event_date, BOOKINGS.status " +
                              "FROM BOOKINGS " +
                              "JOIN PHOTOGRAPHERS ON BOOKINGS.p_id = PHOTOGRAPHERS.p_id " +
                              "JOIN PACKAGES ON BOOKINGS.pp_id = PACKAGES.pp_id " +
                              "WHERE BOOKINGS.c_id = ? " +
                              "ORDER BY BOOKINGS.event_date";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (Connection conn = conf.getConnection();
             PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

            customerStmt.setInt(1, cid);
            try (ResultSet customerResult = customerStmt.executeQuery()) {
                if (customerResult.next()) {
                    System.out.println("\n----------------------------------------------------------------------------------------------------------");
                    System.out.println("                                          == CUSTOMER DETAILS ==                                             ");
                    System.out.println("----------------------------------------------------------------------------------------------------------");

                    System.out.println("Customer Name: " + customerResult.getString("c_name"));
                    System.out.println("Email: " + customerResult.getString("c_email"));
                    System.out.println("Phone Number: " + customerResult.getString("c_pnum"));
                    System.out.println();

                    System.out.println("==========================================================================================================");
                    System.out.println("|                                        CUSTOMER BOOKING HISTORY                                        |");
                    System.out.println("==========================================================================================================");
                    System.out.printf("| %-15s | %-20s | %-20s | %-20s | %-15s |%n", "Booking ID", "Photographer", "Package", "Event Date", "Status");
                    System.out.println("----------------------------------------------------------------------------------------------------------");

                    bookingStmt.setInt(1, cid);
                    try (ResultSet bookingResult = bookingStmt.executeQuery()) {
                        while (bookingResult.next()) {
                            Object eventDateObj = bookingResult.getObject("event_date");
                            String formattedDate = "N/A"; 

                            if (eventDateObj != null) {
                                try {
                                    if (eventDateObj instanceof Timestamp) {
                                        formattedDate = sdf.format((Timestamp) eventDateObj);
                                    } else if (eventDateObj instanceof Date) {
                                        formattedDate = sdf.format((Date) eventDateObj);
                                    } else if (eventDateObj instanceof String && ((String) eventDateObj).matches("\\d{4}-\\d{2}-\\d{2}")) {
                                        formattedDate = eventDateObj.toString();
                                    } else if (eventDateObj instanceof String && ((String) eventDateObj).matches("\\d{4}")) {
                                        formattedDate = eventDateObj + "-01-01";
                                    } else {
                                        System.out.println("Warning: Invalid date format in database: " + eventDateObj);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error formatting date: " + e.getMessage());
                                }
                            }


                            System.out.printf("| %-15d | %-20s | %-20s | %-20s | %-15s |%n",
                                    bookingResult.getInt("b_id"),
                                    bookingResult.getString("photographer"),
                                    bookingResult.getString("package"),
                                    formattedDate,
                                    bookingResult.getString("status"));
                        }
                    }
                    System.out.println("----------------------------------------------------------------------------------------------------------");
                } else {
                    System.out.println("Customer not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void displayPhotographerBookingCounts(){
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


               String photographerQuery = "SELECT p_id, name, experience, specialty, contact_details FROM PHOTOGRAPHERS WHERE p_id = ?";
               String bookingQuery = "SELECT BOOKINGS.b_id, CUSTOMERS.c_name AS customer, BOOKINGS.status, BOOKINGS.event_date " +
                                     "FROM BOOKINGS " +
                                     "JOIN CUSTOMERS ON BOOKINGS.c_id = CUSTOMERS.c_id " +
                                     "WHERE BOOKINGS.p_id = ? " +
                                     "ORDER BY BOOKINGS.event_date";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        
        try (Connection conn = conf.getConnection();
             PreparedStatement photographerStmt = conn.prepareStatement(photographerQuery);
             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery)) {

             photographerStmt.setInt(1, pid);
             try (ResultSet photographerResult = photographerStmt.executeQuery()) {
                if (photographerResult.next()) {
                    
                    System.out.println("-----------------------------------------------------------------------------------");
                    System.out.println("|                          PHOTOGRAPHER BOOKING HISTORY                           |");
                    System.out.println("-----------------------------------------------------------------------------------");

                   
                    System.out.println("Photographer Name: " + photographerResult.getString("name"));
                    System.out.println("Experience: " + photographerResult.getString("experience"));
                    System.out.println("Specialty: " + photographerResult.getString("specialty"));
                    System.out.println("Contact Details: " + photographerResult.getString("contact_details"));
                    System.out.println();

                    System.out.println("===================================================================================");
                    System.out.println("|                          PHOTOGRAPHER BOOKING HISTORY                           |");
                    System.out.println("===================================================================================");
                    System.out.printf("| %-15s | %-20s | %-20s | %-15s |%n", "Booking ID", "Customer", "Booked Date", "Status");
                    System.out.println("-----------------------------------------------------------------------------------");
                    bookingStmt.setInt(1, pid);
                    try (ResultSet bookingResult = bookingStmt.executeQuery()) {
                        while (bookingResult.next()) {
                            Object eventDateObj = bookingResult.getObject("event_date");
                            String formattedDate = "N/A"; 

                            if (eventDateObj != null) {
                                try {
                                    if (eventDateObj instanceof Timestamp) {
                                        formattedDate = sdf.format((Timestamp) eventDateObj);
                                    } else if (eventDateObj instanceof Date) {
                                        formattedDate = sdf.format((Date) eventDateObj);
                                    } else if (eventDateObj instanceof String && ((String) eventDateObj).matches("\\d{4}-\\d{2}-\\d{2}")) {
                                        formattedDate = eventDateObj.toString();
                                    } else if (eventDateObj instanceof String && ((String) eventDateObj).matches("\\d{4}")) {
                                        formattedDate = eventDateObj + "-01-01";
                                    } else {
                                        System.out.println("Warning: Invalid date format in database: " + eventDateObj);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error formatting date: " + e.getMessage());
                                }
                            }


                            System.out.printf("| %-15d | %-20s | %-20s | %-15s |%n",
                                    bookingResult.getInt("b_id"),
                                    bookingResult.getString("customer"),
                                    formattedDate,
                                    bookingResult.getString("status"));
                                   
                        }
                    }
                    System.out.println("-----------------------------------------------------------------------------------");
                } else {
                    System.out.println("Customer not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

