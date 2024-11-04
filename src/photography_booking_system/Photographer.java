package photography_booking_system;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Photographer {
     private Scanner sc = new Scanner(System.in);
     config conf = new config();
    
    public void PhotographerTransactions() {
        int act;

        do {
            try {
                System.out.println("\n-----------------------------------------------");
                System.out.println("     == Welcome to Photographer Section ==");
                System.out.println("-----------------------------------------------");
                
                System.out.println("1. Add Photographer");
                System.out.println("2. View Photographer");
                System.out.println("3. Update Photographer");
                System.out.println("4. Delete Photographer");
                System.out.println("5. Back to Main Menu");

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine();

                switch (act) {
                    case 1:
                        addPhotographers(); 
                        viewPhotographers();
                        break;
                
                    case 2:
                        viewPhotographers();  
                        break;

                    case 3:
                        viewPhotographers();
                        updatePhotographers();
                        viewPhotographers();
                        break;
            
                    case 4:
                        viewPhotographers();
                        deletePhotographers(); 
                        viewPhotographers();
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
    
    private void addPhotographers(){
        
        System.out.print("Enter Photographer Name: ");
        String name = sc.nextLine();

        String exper;
            while (true) {
                System.out.print("Enter Experience: ");
                exper = sc.nextLine();
                if (exper.trim().isEmpty()) {
                    System.out.println("Experience cannot be empty. Please enter a valid experience.\n");
                } else {
                    break;
                }
            }
            
        String sp;
            while (true) {
                System.out.print("Enter Specialty: ");
                sp = sc.nextLine();
                if (exper.trim().isEmpty()) {
                    System.out.println("Specialty cannot be empty. Please enter a valid specialty.\n");
                } else {
                    break;
                }
            }

        String con;
        while (true) {
            System.out.print("Contact Details: ");
            con = sc.nextLine();
            
            if (isValidContactDetails(con)) {
                break;
            } else {
                System.out.println("Invalid phone number format. Please enter digits only.\n");
            }
        }
        
        String sql = "INSERT INTO PHOTOGRAPHERS (name, experience, specialty, contact_details) VALUES (?, ?, ?, ?)";
        conf.addRecord(sql, name, exper, sp, con);
    }
    
    
    public void viewPhotographers(){
        String qry = "SELECT * FROM PHOTOGRAPHERS";
        String[] header = {"ID", "Photographer Name", "Experience", "Specialty", "Contact Details"};
        String[] column = {"p_id", "name", "experience", "specialty", "contact_details"};
       
        conf.viewRecords(qry, header, column);
    }
    
    private void updatePhotographers(){
        System.out.print("Enter ID to Update: ");
        int pid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?",pid)==0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Photographer Id Again: ");
            pid = sc.nextInt();
            sc.nextLine();
        }
        
         System.out.print("Enter Photographer Name: ");
        String name = sc.nextLine();

        String exper;
            while (true) {
                System.out.print("Enter Experience: ");
                exper = sc.nextLine();
                if (exper.trim().isEmpty()) {
                    System.out.println("Experience cannot be empty. Please enter a valid experience.\n");
                } else {
                    break;
                }
            }
            
        String sp;
            while (true) {
                System.out.print("Enter Specialty: ");
                sp = sc.nextLine();
                if (exper.trim().isEmpty()) {
                    System.out.println("Specialty cannot be empty. Please enter a valid specialty.\n");
                } else {
                    break;
                }
            }

        String con;
        while (true) {
            System.out.print("Contact Details: ");
            con = sc.nextLine();
            
            if (isValidContactDetails(con)) {
                break;
            } else {
                System.out.println("Invalid phone number format. Please enter digits only.\n");
            }
        }
        
        String qry = "UPDATE PHOTOGRAPHERS SET name = ?, experience = ?, specialty = ?, contact_details = ?";
        conf.updateRecord(qry, name, exper, sp, con);
        
    }
    
    private void deletePhotographers(){
    
        System.out.print("Enter ID to Delete: ");
        int pid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT c_id FROM PHOTOGRAPHERS WHERE c_id = ?",pid)==0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Photographer Id Again: ");
            pid = sc.nextInt();
            sc.nextLine();
        }
        
        String qry = "DELETE FROM PHOTOGRPAHERS WHERE p_id = ?";
        conf.deleteRecord(qry, pid);
        
}

    private boolean isValidContactDetails(String contactDetails) {
        return contactDetails.matches("\\d+"); 
    }

   
    
    
}
