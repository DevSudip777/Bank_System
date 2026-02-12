import service.BankService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService service = new BankService();
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n==============================");
            System.out.println("Welcome to SS_Sev Bank");
            System.out.println("==============================");
            System.out.println("Choose one of the following options : ");
            System.out.println("1. Create new account");
            System.out.println("2. Close account");
            System.out.println("3. Withdraw money");
            System.out.println("4. Deposit money");
            System.out.println("5. Transfer Money to another account");
            System.out.println("6. View transaction history");
            System.out.println("7. View account details");
            System.out.println("8. Update account details");
            System.out.println("9. Exit");
            System.out.println("==============================");
            System.out.print("Enter your choice : ");

            choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    System.out.println("Enter Account holder's first name : ");
                    String fName = sc.nextLine();
                    System.out.println("Enter Account holder's last name : ");
                    String lName = sc.nextLine();
                    System.out.println("Enter Account holder's Email : ");
                    String email = sc.nextLine();
                    System.out.println("Enter Account holder's phone number : ");
                    String phone = sc.nextLine();
                    System.out.println("Enter Account holder's address : ");
                    String address = sc.nextLine();
                    service.createAccount(fName, lName, email, phone, address);
                    break;
                case 2:
                    System.out.print("Enter bank account number : ");
                    long accNumber = sc.nextLong();
                    service.closeAccount(accNumber);
                    break;
                case 3:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();
                    System.out.print("Enter the amount to withdraw :");
                    double amount = sc.nextDouble();
                    service.withdraw(accNumber, amount);
                    break;
                case 4:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();
                    System.out.print("Enter the amount to deposit :");
                    amount = sc.nextDouble();
                    service.deposit(accNumber, amount);
                    break;
                case 5:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    System.out.print("Enter receiver's bank account number : ");
                    long receiverAccNumber = sc.nextLong();
                    System.out.print("Enter amount : ");
                    amount = sc.nextDouble();
                    service.transfer(accNumber, receiverAccNumber, amount);
                    break;
                case 6:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    service.transactionHistory(accNumber);
                    break;
                case 7:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    service.accountDetails(accNumber);
                    break;
                case 8:
                    System.out.println("Enter  first name : ");
                     fName = sc.nextLine();
                    System.out.println("Enter  last name : ");
                     lName = sc.nextLine();
                    System.out.println("Enter  Email : ");
                     email = sc.nextLine();
                    System.out.println("Enter  phone number : ");
                     phone = sc.nextLine();
                    System.out.println("Enter  address : ");
                     address = sc.nextLine();
                     service.updateCustomerDetails(fName, lName, email, phone, address);
                     break;
                case 9:
                    System.out.println("Thank you for visiting SS_Sev bank!");
                    System.out.println();
                    System.out.println("==============================");
                    break;
                default:
                    System.out.println("Invalid selection!");
                    System.out.println("Please choose correct option which are available ");
            }
        }while(choice != 9);
    }
}