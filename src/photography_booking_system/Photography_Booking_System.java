package photography_booking_system;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Photography_Booking_System {


    public static void main(String[] args) {
            
        Scanner sc = new Scanner(System.in);
        
        Customer cust = new Customer();
        Packages pack = new Packages();
        Booking book = new Booking();
        Photographer photo = new Photographer();
        Reports rec = new Reports();
        
        
        int op = -1; 
        
        do {
            try {
                System.out.println("\n================================================");
                System.out.println("     WELCOME TO PHOTOGRAPHY BOOKING SYSTEM      ");
                System.out.println("================================================");
                System.out.println("         == Kindly Select an Entity ==         ");
                System.out.println("------------------------------------------------");

                System.out.println("1. CUSTOMER ");
                System.out.println("2. PHOTOGRAPHY PACKAGES ");
                System.out.println("3. BOOKING ");
                System.out.println("4. PHOTOGRAPHER ");
                System.out.println("5. REPORTS ");
                System.out.println("6. EXIT ");
                System.out.println("------------------------------------------------");

                System.out.print("\nSelect an Option: ");
                op = sc.nextInt();
                sc.nextLine(); 

                switch (op) {
                    case 1:
                        cust.CustomerTransactions();
                        break;

                    case 2:
                        pack.PackageTransactions();
                        break;

                    case 3:
                        book.BookingTransactions();
                        break;

                    case 4:
                        photo.PhotographerTransactions();
                        break;

                    case 5:
                        rec.ReportTransaction();
                        break;
                    
                     

                    default:
                        System.out.println("Invalid Option.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();  
                op = -1;  
            }
        } while (op != 6);

        sc.close(); 
    }

}
