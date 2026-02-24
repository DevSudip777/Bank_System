import service.BankService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService service = new BankService();
        Scanner sc = new Scanner(System.in);
        String fName = "";
        String lName = "";
        String email = "";
        String phone = "";
        String address = "";
        double amount;
        long accNumber;
        int choice;

        do {
            System.out.println("\n=============================================================");
            System.out.println("Welcome to SS_Sev Bank");
            System.out.println("=============================================================");
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
            System.out.println("=============================================================");
            System.out.print("Enter your choice : ");

            choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    System.out.print("Enter first name : ");
                    fName = sc.nextLine();
                    System.out.print("Enter last name : ");
                    lName = sc.nextLine();
                    System.out.print("Enter Email : ");
                    email = sc.nextLine();
                    while(!email.contains("@")){
                        System.out.println("Invalid email format.");
                        System.out.print("Renter your Email : ");
                        email = sc.nextLine();
                    }
                    System.out.print("Enter phone number : ");
                    phone = sc.nextLine();
                    while(!phone.matches("\\d{10}")){
                        System.out.println("Phone number must be 10 digits.");
                        System.out.print("Renter your Phone number : ");
                        phone = sc.nextLine();
                    }
                    System.out.print("Enter address : ");
                    address = sc.nextLine();
                    service.createAccount(fName, lName, email, phone, address);
                    break;
                case 2:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();
                    service.closeAccount(accNumber);
                    break;
                case 3:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();
                    System.out.print("Enter the amount to withdraw : ");
                    amount = sc.nextDouble();
                    service.withdraw(accNumber, amount);
                    break;
                case 4:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();
                    System.out.print("Enter the amount to deposit : ");
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
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    sc.nextLine();
                    int option;
                    do {
                        System.out.println("\n=============================================================");
                        System.out.println("Following options can be updated :");
                        System.out.println("1. Update FirstName\n2. Update LastName\n3. Update Email\n4. Update Phone Number\n5. Update Address\n6. Exit");
                        System.out.println("=============================================================");
                        System.out.print("Enter your choice : ");
                        option = sc.nextInt();
                        sc.nextLine();
                        switch (option) {
                            case 1:
                                System.out.print("Enter first name : ");
                                fName = sc.nextLine();
                                service.updateCustomerDetails(fName, lName, email, phone, address, accNumber);
                                break;
                            case 2:
                                System.out.print("Enter last name : ");
                                lName = sc.nextLine();
                                service.updateCustomerDetails(fName, lName, email, phone, address, accNumber);
                                break;
                            case 3:
                                System.out.print("Enter email : ");
                                email = sc.nextLine();
                                if (!email.contains("@")){
                                    System.out.println("Invalid email format.");
                                } else {
                                    service.updateCustomerDetails(fName, lName, email, phone, address, accNumber);
                                }
                                break;
                            case 4:
                                System.out.print("Enter phone number : ");
                                phone = sc.nextLine();
                                if(!phone.matches("\\d{10}")){
                                    System.out.println("Phone number must be 10 digits.");
                                }else {
                                    service.updateCustomerDetails(fName, lName, email, phone, address, accNumber);
                                }
                                break;
                            case 5:
                                System.out.print("Enter  address : ");
                                address = sc.nextLine();
                                service.updateCustomerDetails(fName, lName, email, phone, address, accNumber);
                                break;
                            case 6:
                                break;
                            default:
                                System.out.println("\nInvalid option!");
                                System.out.println("Please choose correct option which can be updated\n");
                        }


                    }while(option != 6);

                    // switch case break
                    break;
                case 9:
                    System.out.println("\n=============================================================");
                    System.out.println("Thank you for visiting SS_Sev bank. Have a nice day! 😊😊");
                    System.out.println("=============================================================");
                    break;
                default:
                    System.out.println("\nInvalid selection!");
                    System.out.println("Please choose correct option which are available\n");
            }
        }while(choice != 9);
    }
}