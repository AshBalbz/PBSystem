package photography_booking_system;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Customer {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
    
    
    public void CustomerTransactions() {
        int act;

        do {
            try {
                System.out.println("\n-----------------------------------------------");
                System.out.println("        == WELCOME TO CUSTOMER SECTION ==");
                System.out.println("-----------------------------------------------");
                
                System.out.println("1. Add Customer");
                System.out.println("2. View Customer");
                System.out.println("3. Update Customer");
                System.out.println("4. Delete Customer");
                System.out.println("5. Back to Main Menu");
                System.out.println("-----------------------------------------------");

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine();

                switch (act) {
                    case 1:
                        addCustomers();
                        break;
                
                    case 2:
                        viewCustomers();  
                        break;
                
                    case 3:
                        viewCustomers();
                        updateCustomers(); 
                        viewCustomers();
                        break;
            
                    case 4:
                        viewCustomers();
                        deleteCustomers(); 
                        viewCustomers();
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
    

    private void addCustomers() {
        String name;
            while (true) {
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Name cannot be empty. Please enter a valid name.\n");
                } else {
                    break;
                }
            }

        String email;
            while (true) {
                System.out.print("Enter Email: ");
                email = sc.nextLine();

                if (isValidEmail(email)) {
                    break;
                } else {
                    System.out.println("Invalid email format. Please enter a valid email.\n");
                }
            }

        String pnum;
            while (true) {
                System.out.print("Enter Phone Number: ");
                pnum = sc.nextLine();

                if (isValidPhoneNumber(pnum)) {
                    break;
                } else {
                    System.out.println("Invalid phone number format. Please enter digits only.\n");
                }
            }

            String sql = "INSERT INTO CUSTOMERS (c_name, c_email, c_pnum) VALUES (?, ?, ?)";   
            conf.addRecord(sql, name, email, pnum);
    }
    
    
    public void viewCustomers(){
        System.out.println("\n---------------------------------------------------------------------------------------------");
        System.out.println("                                    == LIST OF CUSTOMERS ==             ");
        
            String qry = "SELECT * FROM CUSTOMERS";
            String[] header = {"ID", "Name", "Email", "Contact Number"};
            String[] column = {"c_id", "c_name", "c_email", "c_pnum"};
       
        conf.viewRecords(qry, header, column);
    
    }
    
    
    private void updateCustomers(){
        System.out.print("Enter ID to Update: ");
        int cid = sc.nextInt();
        sc.nextLine();
        
            while(conf.getSingleValue("SELECT c_id FROM CUSTOMERS WHERE c_id = ?",cid)==0){
                System.out.println("Selected ID doesn't exist! ");
                System.out.print("Select Customer Id Again: ");
                cid = sc.nextInt();
                sc.nextLine();
        }
        
        String name;
            while (true) {
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Name cannot be empty. Please enter a valid name.\n");
                } else {
                    break;
                }
            }

        String email;
            while (true) {
                System.out.print("Enter Email: ");
                email = sc.nextLine();

                if (isValidEmail(email)) {
                    break;
                } else {
                    System.out.println("Invalid email format. Please enter a valid email.\n");
                }
            }

        String pnum;
            while (true) {
                System.out.print("Enter Phone Number: ");
                pnum = sc.nextLine();

                if (isValidPhoneNumber(pnum)) {
                    break;
                } else {
                    System.out.println("Invalid phone number format. Please enter digits only.\n");
                }
            }
       
                String qry = "UPDATE CUSTOMERS SET c_name = ?, c_email = ?, c_pnum = ? WHERE c_id = ?";
                conf.updateRecord(qry, name, email, pnum, cid);
    }
    
    
    private void deleteCustomers(){
        System.out.print("Enter ID to Delete: ");
        int cid = sc.nextInt();
        
            while(conf.getSingleValue("SELECT c_id FROM CUSTOMERS WHERE c_id = ?",cid)==0){
                System.out.println("Selected ID doesn't exist! ");
                System.out.print("Select Customer Id Again: ");
                cid = sc.nextInt();
                sc.nextLine();
            }
        
                String qry = "DELETE FROM CUSTOMERS WHERE c_id = ?";
                conf.deleteRecord(qry, cid);

}
 
    
        private boolean isValidEmail(String email) {
            return email.contains("@") && email.contains("."); 
        }


        private boolean isValidPhoneNumber(String phoneNumber) {
            return phoneNumber.matches("\\d+"); 
        }
    
    
}
