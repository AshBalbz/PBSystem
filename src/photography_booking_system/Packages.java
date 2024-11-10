package photography_booking_system;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Packages {
    private Scanner sc = new Scanner(System.in);
    config conf = new config();
    
    public void PackageTransactions() {
        int act;

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

                System.out.print("\nPlease Choose an Option: ");
                act = sc.nextInt();
                sc.nextLine();

                switch (act) {
                    case 1:
                        addPackages(); 
                        viewPackages();

                        break;
                
                    case 2:
                        viewPackages();  
                        break;
                
                    case 3:
                        viewPackages();
                        updatePackages();
                        viewPackages();
                        break;
            
                    case 4:
                        viewPackages();
                        deletePackages(); 
                        viewPackages();
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
        System.out.println("| \t\t\t\t\t       == PACKAGES LIST == \t\t\t\t\t\t |");
        
            String qry = "SELECT * FROM PACKAGES";
            String[] header = {"ID", "Package Name", "Duration [in hours]", "Amount", "Included Services" };
            String[] column = {"pp_id", "pname", "duration", "amount", "service"};
       
        conf.viewRecords(qry, header, column);
    }
    
    
    private void updatePackages(){
       System.out.print("Enter ID to Update: ");
       int id = sc.nextInt();
       sc.nextLine();

            while(conf.getSingleValue("SELECT pp_id FROM PACKAGES WHERE pp_id = ?", id) == 0){
                System.out.println("Selected ID doesn't exist! ");
                System.out.print("Select Package ID Again: ");
                id = sc.nextInt();
                sc.nextLine(); 
            }

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
                    System.out.println("Duration cannot be empty. Please enter a valid duration.\n");
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
                System.out.print("Enter Services Included: ");
                services = sc.nextLine();
                if (services.trim().isEmpty()) {
                    System.out.println("Services Included cannot be empty. Please enter valid services.\n");
                } else {
                    break;
                }
            }

                String qry = "UPDATE PACKAGES SET pname = ?, duration = ?, amount = ?, service = ? WHERE pp_id = ?";
                conf.updateRecord(qry, name, dt, amount, services, id);
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
