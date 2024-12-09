package photography_booking_system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Packages {
    private Scanner sc = new Scanner(System.in);
    config conf = new config();
    int act;

    public void PackageTransactions() {

        do {
            try {
                System.out.println("\n-------------------------------------------------");
                System.out.println("  == WELCOME TO PHOTOGRAPHY PACKAGES SECTION ==");
                System.out.println("-------------------------------------------------");
                
                System.out.println("1. Add Packages");
                System.out.println("2. View Packages");
                System.out.println("3. Update Packages");
                System.out.println("4. Delete Packages");
                System.out.println("5. Back to Main Menu");
                System.out.println("-------------------------------------------------");

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine();

                switch (act) {
                    case 1:
                        addPackages(); 
                        break;
                
                    case 2:
                        viewPackages();  
                        break;
                
                    case 3:
                        viewPackages();
                        updatePackages();
                        break;
            
                    case 4:
                        viewPackages();
                        deletePackages(); 
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
    
    
    private void addPackages(){
        String name;
            while (true) {
                System.out.print("Enter Package Name: ");
                name = sc.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Package Name cannot be empty. Please enter a valid name.\n");
                } else {
                    break;
                }
            }

        String dt;
            while (true) {
                System.out.print("Enter Duration: ");
                dt = sc.nextLine();
                if (dt.trim().isEmpty()) {
                    System.out.println("Experience cannot be empty. Please enter a valid experience.\n");
                } else {
                    break;
                }
            }

        double amount = 0;
            while (true) {
                System.out.print("Enter Price: ");
                try {
                    amount = sc.nextDouble();
                    sc.nextLine(); 
                    if (amount < 0) {
                        System.out.println("Price cannot be negative. Please enter a valid price.\n");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number for Price.\n");
                    sc.nextLine(); 
                }
            }


        String services;
            while (true) {
                System.out.print("Enter Service Included: ");
                services = sc.nextLine();
                if (services.trim().isEmpty()) {
                    System.out.println("Services Included cannot be empty. Please enter valid services.\n");
                } else {
                    break;
                }
            }

                String sql = "INSERT INTO PACKAGES (pname, duration, amount, service) VALUES (?, ?, ?, ?)";
                conf.addRecord(sql, name, dt, amount, services);
    }
    
    
    public void viewPackages(){
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.println("| \t\t\t\t\t       == PACKAGES LIST == \t\t\t\t\t\t     |");
        
            String qry = "SELECT * FROM PACKAGES";
            String[] header = {"ID", "Package Name", "Duration [in hours]", "Amount", "Included Services" };
            String[] column = {"pp_id", "pname", "duration", "amount", "service"};
       
        conf.viewRecords(qry, header, column);
    }
    
    
    private void updatePackages(){
       String Query;
       
       System.out.print("Enter ID to Update: ");
       int id = sc.nextInt();
       sc.nextLine();

            while(conf.getSingleValue("SELECT pp_id FROM PACKAGES WHERE pp_id = ?", id) == 0){
                System.out.println("Selected ID doesn't exist! ");
                System.out.print("Select Package ID Again: ");
                id = sc.nextInt();
                sc.nextLine(); 
            }

            char act;
                do {
                    try {
                        System.out.println("\n-----------------------------------------------");
                        System.out.println("  == SELECT AN OPTION YOU WANT TO UPDATE ==");
                        System.out.println("-----------------------------------------------");

                        System.out.println("A. Package Name");
                        System.out.println("B. Duration");
                        System.out.println("C. Amount ");
                        System.out.println("D. Services");
                        System.out.println("E. Back");
                        System.out.println("-----------------------------------------------");

                        System.out.print("\nPlease Choose an Option: ");
                        act = sc.nextLine().trim().toUpperCase().charAt(0);

                        switch (act) {
                            case 'A': {
                                String name;
                                while (true) {
                                    System.out.print("Enter Package Name: ");
                                    name = sc.nextLine();
                                    if (name.trim().isEmpty()) {
                                        System.out.println("Package Name cannot be empty. Please enter a valid name.\n");
                                    } else {
                                        break;
                                    }
                                }
                                String query = "UPDATE PACKAGES SET pname = ? WHERE pp_id = ?";
                                conf.updateRecord(query, name, id);
                                displaySpecificPackage(id);
                                break;
                            }

                            case 'B': {
                                String dt;
                                while (true) {
                                    System.out.print("Enter Duration: ");
                                    dt = sc.nextLine();
                                    if (dt.trim().isEmpty()) {
                                        System.out.println("Duration cannot be empty. Please enter a valid duration.\n");
                                    } else {
                                        break;
                                    }
                                }
                                String query = "UPDATE PACKAGES SET duration = ? WHERE pp_id = ?";
                                conf.updateRecord(query, dt, id);
                                displaySpecificPackage(id);
                                break;
                            }

                            case 'C': {
                                double amount = 0;
                                while (true) {
                                    System.out.print("Enter Price: ");
                                    try {
                                        amount = sc.nextDouble();
                                        sc.nextLine();
                                        if (amount < 0) {
                                            System.out.println("Price cannot be negative. Please enter a valid price.\n");
                                        } else {
                                            break;
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid input. Please enter a valid number for Price.\n");
                                        sc.nextLine();
                                    }
                                }
                                String query = "UPDATE PACKAGES SET amount = ? WHERE pp_id = ?";
                                conf.updateRecord(query, amount, id);
                                displaySpecificPackage(id);
                                break;
                            }

                            case 'D': {
                                String services;
                                while (true) {
                                    System.out.print("Enter Services Included: ");
                                    services = sc.nextLine();
                                    if (services.trim().isEmpty()) {
                                        System.out.println("Services Included cannot be empty. Please enter valid services.\n");
                                    } else {
                                        break;
                                    }
                                }
                                String query = "UPDATE PACKAGES SET service = ? WHERE pp_id = ?";
                                conf.updateRecord(query, services, id);
                                displaySpecificPackage(id);
                                break;
                            }

                            case 'E': {
                                System.out.println("Going back to the packages main menu...");
                                return; 
                            }

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


    private void displaySpecificPackage(int id) {
            String query = """
                SELECT 
                    pc.pp_id AS PackageID, 
                    pc.pname AS PackageName,
                    pc.duration AS PackageDur,
                    pc.amount AS PackageAmt,
                    pc.service AS PackageServ
                FROM PACKAGES pc
                WHERE pc.pp_id = ?
            """;

            try (java.sql.Connection conn = conf.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Table Header
                    System.out.println("================================================================================================");
                    System.out.printf("| %-15s | %-15s | %-15s | %-15s | %20s |\n", 
                        "Package ID", "Package Name", "Duration", "Amount","Services");
                    System.out.println("================================================================================================");

                    System.out.printf("| %-15s | %-15s | %-15s | %-15s | %20s |\n", 
                        rs.getInt("PackageID"), 
                        rs.getString("PackageName"),
                        rs.getString("PackageDur"), 
                        rs.getString("PackageAmt"),
                        rs.getString("PackageServ")); 
                    System.out.println("------------------------------------------------------------------------------------------------");
                } else {
                    System.out.println("No details found for the selected booking.");
                }


            } catch (SQLException e) {
                System.out.println("Error fetching booking details: " + e.getMessage());
            }
       }

    
    private void deletePackages(){
        System.out.print("Enter ID to Delete: ");
        int id = sc.nextInt();
        
        while(conf.getSingleValue("SELECT pp_id FROM PACKAGES WHERE pp_id = ?",id)==0){
            System.out.println("Selected ID doesn't exist! ");
            System.out.print("Select Package Id Again: ");
            id = sc.nextInt();
            sc.nextLine();
        }
        
        String qry = "DELETE FROM PACKAGES WHERE pp_id = ?";
        conf.deleteRecord(qry, id);
        
}
}
