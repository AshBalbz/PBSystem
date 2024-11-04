//package photography_booking_system;
//
//import java.util.InputMismatchException;
//import java.util.Scanner;
//
//public class Payment {
//    private Scanner sc = new Scanner(System.in);
//     config conf = new config();
//    
//    public void configPayment() {
//        int act;
//
//        do {
//            try {
//                System.out.println("\n-----------------------------------------------");
//                System.out.println("        == Welcome to Payment Section == ");
//                System.out.println("-----------------------------------------------");
//                
//                System.out.println("1. Add Payment");
//                System.out.println("2. View Payment");
//                System.out.println("3. Update Payment");
//                System.out.println("4. Delete Payment");
//                System.out.println("5. Back to Main Menu");
//
//                System.out.print("\nPlease Choose an Option: ");
//                act = sc.nextInt();
//                sc.nextLine();
//
//                switch (act) {
////                    case 1:
////                        addPayments(); 
////                        break;
////                
////                    case 2:
////                        viewPayments();  
////                        break;
////
////                    case 3:
////                        updatePayments(); 
////                        break;
////            
////                    case 4:
////                        deletePayments(); 
////                        break;
////                
////                    case 5:
////                        System.out.println("Returning to the main menu...");
////                        break;
//        
//                    default:
//                        System.out.println("Invalid Option.");
//                }
//        
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input. Please enter a valid number.");
//                sc.nextLine();
//                act = -1;
//            }
//        } while (act != 5);
//    }
//    
//    public void addPayments() {
//        System.out.print("Enter Booking ID: ");
//        int bid = sc.nextInt();
//        
//        String bsql = "SELECT b_id FROM BOOKINGS WHERE b_id = ?";
//        while(conf.getSingleValue(bsql, bid) == 0){
//            System.out.println("Selected ID doesn't exist! ");
//            System.out.print("Select Booking ID Again: ");
//            bid = sc.nextInt();
//            sc.nextLine();
//        }
//        
//        int dt = 0;
//        while (true) {
//            System.out.print("Enter Payment Date (YYYY-MM-DD format): ");
//            try {
//                dt = sc.nextInt();
//                sc.nextLine();
//                if (dt <= 0) {
//                    System.out.println("Please enter a valid date in the format YYYYMMDD.\n");
//                } else {
//                    break;
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input. Please enter a valid date format.\n");
//                sc.nextLine();
//            }
//        }
//
//        double amount = 0;
//        while (true) {
//            System.out.print("Enter Amount Paid: ");
//            try {
//                amount = sc.nextDouble();
//                sc.nextLine();
//                if (amount < 0) {
//                    System.out.println("Amount cannot be negative. Please enter a valid amount.\n");
//                } else {
//                    break;
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input. Please enter a valid number for Amount.\n");
//                sc.nextLine();
//            }
//        }
//
//        String paymethod;
//        while (true) {
//            System.out.print("Enter Payment Method: ");
//            paymethod = sc.nextLine();
//           
//            if (paymethod.trim().isEmpty()) {
//                System.out.println("Payment Method cannot be empty. Please enter a valid payment method.\n");
//            } else {
//                break;
//            }
//        }
//
//      
//
//        // Insert record into database
//        String sql = "INSERT INTO PAYMENTS (b_id, downpayment, pay_date, payment_method) VALUES (?, ?, ?, ?, ?)";
//        conf.addRecord(sql, bid, amount, dt, paymethod);
//    }
//    
////    public void viewPayments() {
////       
////        String qry = "SELECT PAYMENTS.ID, amount_paid, payment_date, payment_method, status FROM PAYMENTS "
////                     + "INNER JOIN BOOKINGS ON PAYMENTS.b_id = BOOKINGS.ID";
////        String[] header = {"ID", "Amount Paid", "Payment Date", "Payment Method", "Status"};
////        String[] column = {"ID", "amount_paid", "payment_date", "payment_method", "status"};
////
////        conf.viewRecords(qry, header, column);
////    }
////
////    
////    
////    public void updatePayments() {
////        int id;
////
////        while (true) {
////            System.out.print("Enter Photographer ID to Update: ");
////            try {
////                id = sc.nextInt();
////                sc.nextLine(); 
////                
////                if (id <= 0) {
////                    System.out.println("Photographer ID must be a positive integer. Please try again.\n");
////                } else if (!conf.doesIDExist("PHOTOGRAPHERS", id)) {
////                    System.out.println("Photographer ID doesn't exist. Please enter a valid ID.\n");
////                } else {
////                    break; 
////                }
////            } catch (InputMismatchException e) {
////                System.out.println("Invalid input. Please enter a valid number for Photographer ID.\n");
////                sc.nextLine();
////            }
////        }
////        
////        System.out.print("Enter Photographer Name: ");
////        String name = sc.nextLine();
////
////        String exper;
////            while (true) {
////                System.out.print("Enter Experience: ");
////                exper = sc.nextLine();
////                if (exper.trim().isEmpty()) {
////                    System.out.println("Experience cannot be empty. Please enter a valid experience.\n");
////                } else {
////                    break;
////                }
////            }
////            
////        String sp;
////            while (true) {
////                System.out.print("Enter Specialty: ");
////                sp = sc.nextLine();
////                if (exper.trim().isEmpty()) {
////                    System.out.println("Specialty cannot be empty. Please enter a valid specialty.\n");
////                } else {
////                    break;
////                }
////            }
////
////        String con;
////        while (true) {
////            System.out.print("Contact Details: ");
////            con = sc.nextLine();
////            
////            if (isValidContactDetails(con)) {
////                break;
////            } else {
////                System.out.println("Invalid phone number format. Please enter digits only.\n");
////            }
////        }
////        
////        String qry = "UPDATE PHOTOGRAPHERS SET name = ?, experience = ?, specialty = ?, contact_details = ? WHERE ID = ?";
////        conf.updateRecord(qry, name, exper, sp, con, id);
////    }
////    
////    public void deletePayments() {
////        int id;
////
////        while (true) {
////            System.out.print("Enter Photographer ID to Delete: ");
////            try {
////                id = sc.nextInt();
////                sc.nextLine(); 
////                
////                if (id <= 0) {
////                    System.out.println("Photographer ID must be a positive integer. Please try again.\n");
////                } else if (!conf.doesIDExist("PHOTOGRAPHERS", id)) {
////                    System.out.println("Photographer ID doesn't exist. Please enter a valid ID.\n");
////                } else {
////                    break; 
////                }
////            } catch (InputMismatchException e) {
////                System.out.println("Invalid input. Please enter a valid number for Photographer ID.\n");
////                sc.nextLine();
////            }
////        }
////        
////        String qry = "DELETE FROM PHOTOGRAPHERS WHERE ID = ?";
////        conf.deleteRecord(qry, id);
////    }
////    
////    
////    
////    private boolean isValidContactDetails(String contactDetails) {
////        return contactDetails.matches("\\d+"); 
////    }
////
////    
////}
////
////
