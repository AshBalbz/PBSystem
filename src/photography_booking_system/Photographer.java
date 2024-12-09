package photography_booking_system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Photographer {
     private Scanner sc = new Scanner(System.in);
     config conf = new config();
     int act;

    public void PhotographerTransactions() {

        do {
            try {
                System.out.println("\n-----------------------------------------------");
                System.out.println("     == WELCOME TO PHOTOGRAPHER SECTION ==");
                System.out.println("-----------------------------------------------");
                
                System.out.println("1. Add Photographer");
                System.out.println("2. View Photographer");
                System.out.println("3. Update Photographer");
                System.out.println("4. Delete Photographer");
                System.out.println("5. Back to Main Menu");
                System.out.println("-------------------------------------------------");

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine();

                switch (act) {
                    case 1:
                        addPhotographers(); 
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
                if (sp.trim().isEmpty()) {
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
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.println("| \t\t\t\t\t    == LIST OF PHOTOGRAPHERS ==    \t\t\t\t\t     |");
            String qry = "SELECT * FROM PHOTOGRAPHERS";
            String[] header = {"ID", "Photographer Name", "Experience", "Specialty", "Contact Details"};
            String[] column = {"p_id", "name", "experience", "specialty", "contact_details"};
       
        conf.viewRecords(qry, header, column);
    }
    
    
    private void updatePhotographers(){
        String Query;
        
        System.out.print("Enter ID to Update: ");
        int pid = sc.nextInt();
        sc.nextLine();
        
            while(conf.getSingleValue("SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?",pid)==0){
                System.out.println("Selected ID doesn't exist! ");
                System.out.print("Select Photographer Id Again: ");
                pid = sc.nextInt();
                sc.nextLine();
            }
            
      char act;  
      do {
                try {
                    System.out.println("\n-----------------------------------------------");
                    System.out.println("  == SELECT AN OPTION YOU WANT TO UPDATE ==");
                    System.out.println("-----------------------------------------------");

                    System.out.println("A. Photographer Name");
                    System.out.println("B. Experience");
                    System.out.println("C. Specialty ");
                    System.out.println("D. Contact Number ");
                    System.out.println("E. Back");
                    System.out.println("-----------------------------------------------"); 

                    System.out.print("\nPlease Choose an Option: ");
                    act = sc.nextLine().trim().toUpperCase().charAt(0);


                    switch (act) {
                        case 'A':
                            String name;
                                while (true) {
                                    System.out.print("Enter New Name: ");
                                    name = sc.nextLine();
                                    if (name.trim().isEmpty()) {
                                        System.out.println("Name cannot be empty. Please enter a valid name.\n");
                                    } else {
                                        break;
                                    }
                                }
                            Query = "UPDATE PHOTOGRAPHERS SET name = ? WHERE p_id = ?";
                            conf.updateRecord(Query, name, pid);
                            displaySpecificPhotographer(pid);
                            break;

                        case 'B':
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
                            Query = "UPDATE PHOTOGRAPHERS SET experience = ? WHERE p_id = ?";
                            conf.updateRecord(Query, exper, pid);
                            displaySpecificPhotographer(pid);
                            break;

                        case 'C':
                            String sp;
                                while (true) {
                                    System.out.print("Enter Specialty: ");
                                    sp = sc.nextLine();
                                    if (sp.trim().isEmpty()) {
                                        System.out.println("Specialty cannot be empty. Please enter a valid specialty.\n");
                                    } else {
                                        break;
                                    }
                                }
                            Query = "UPDATE PHOTOGRAPHERS SET specialty = ? WHERE p_id = ?";
                            conf.updateRecord(Query, sp, pid);
                            displaySpecificPhotographer(pid);
                            break;
                        
                        case 'D':
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
                            Query = "UPDATE PHOTOGRAPHERS SET contact_details = ? WHERE p_id = ?";
                            conf.updateRecord(Query, con, pid);
                            displaySpecificPhotographer(pid);
                            break;

                        case 'E':
                            System.out.println("Going back to the photographer main menu.");
                            return;

                        default: {
                                System.out.println("Invalid Option. Please select a valid option.");
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter valid data.");
                        sc.nextLine(); 
                    }
                } while (true);
                }




private void displaySpecificPhotographer(int pid) {
        String query = """
            SELECT 
                p.p_id AS PhotographerID, 
                p.name AS PhotographerName,
                p.experience AS PhotographerExper,
                p.specialty AS PhotographerSpec,
                p.contact_details AS PhotographerContact
            FROM PHOTOGRAPHERS p
            WHERE p.p_id = ?
        """;

        try (java.sql.Connection conn = conf.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Table Header
                System.out.println("================================================================================================");
                System.out.printf("| %-15s | %-15s | %-15s | %-20s | %15s |\n", 
                    "Photographer ID", "Name", "Experience", "Specialty", "Contact Details");
                System.out.println("================================================================================================");

                System.out.printf("| %-15s | %-15s | %-15s | %-20s | %15s |\n", 
                    rs.getInt("PhotographerID"), 
                    rs.getString("PhotographerName"),
                    rs.getString("PhotographerExper"), 
                    rs.getString("PhotographerSpec"),
                    rs.getString("PhotographerContact")); 
                System.out.println("------------------------------------------------------------------------------------------------");
            } else {
                System.out.println("No details found for the selected booking.");
            }


        } catch (SQLException e) {
            System.out.println("Error fetching booking details: " + e.getMessage());
        }
}

    
    
    private void deletePhotographers(){
        System.out.print("Enter ID to Delete: ");
        int pid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT p_id FROM PHOTOGRAPHERS WHERE p_id = ?",pid)==0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Photographer Id Again: ");
            pid = sc.nextInt();
            sc.nextLine();
        }
        
        String qry = "DELETE FROM PHOTOGRAPHERS WHERE p_id = ?";
        conf.deleteRecord(qry, pid);
        
}

    private boolean isValidContactDetails(String contactDetails) {
        return contactDetails.matches("\\d+"); 
    }

}
