package service;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import exception.AccountClosedException;
import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import model.Customer;
import model.Transaction;
import receipts.ReceiptGenerator;
import util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankService {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // Generating Account Number----------------------------
    private long generateAccountNumber(){
        long min = 1000000000L;
        long max = 9999999999L;

        return  min + (long) (Math.random()*(max-min));
    }
    public void createAccount(String fName, String lName, String email, String phone, String address){
        try {
            // create a new customer record
            Customer customer = new Customer(fName, lName, email, phone, address);

            // then get the customerID
            int cusId = customerDAO.createCustomer(customer);
            if(cusId == -1){
                System.out.println("Failed to create customer entry!");
                return;
            }
            // use the customerID to create a new record in BankAccounts table.
            // Generate new unique account number
            long accNumber = this.generateAccountNumber();
            Account account = new Account(accNumber, cusId, "Savings", 0.0, "Active", LocalDate.now());
            if(accountDAO.createAccount(account)){
                System.out.println("Account created successfully!.\nYour account number : " + accNumber);
            }else{
                System.out.println("Failed to create account.");
            }
        }catch (SQLException e){
            System.out.println("Error : " + e.getMessage());
        }
    }
    public void closeAccount(long accountNumber) {
        try {
            // search the specified bank account
            Account account = accountDAO.getAccount(accountNumber);
            if (account == null) {
                throw new AccountNotFoundException("Account does not exist at SS-Sev bank");
            }
            // bank account already exist, BUT. it's already closed.
            if (account.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Account already closed.");
            }

            // close the account.
            if(accountDAO.closeAccount(accountNumber)) {
                System.out.println("Account closed successfully.");
            } else {
                System.out.println("Failed to close bank account.");
            }
        }
        catch ( AccountNotFoundException | AccountClosedException | SQLException e){
            System.out.println("Error : " + e.getMessage());
        }
    }
    public void withdraw(long accNumber, double amount){
        try {
            // search the specified bank account
            if (amount <= 0) {
                throw new InvalidAmountException("Withdrawal amount must be positive greater than 0");
            }
            Account account = accountDAO.getAccount(accNumber);
            if (account == null) {
                throw new AccountNotFoundException("Account does not exist at SS-Sev bank");
            }
            // bank account already exist, BUT. it's already closed.
            else if (account.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Account already closed.");
            }else {
                // first update the balance property of account object
                account.setBalance(getCurrBalance(amount, account) - amount);
                // call accountDao object to update the account balance
                accountDAO.updateBalance(account);
                // create new transaction object
                Transaction t = new Transaction(accNumber, "Withdrawal", amount, LocalDateTime.now(), 0, "Withdrawal from account");
                // update transaction schema bt adding new transaction instance
                int transactionID = transactionDAO.addTransaction(t);
                t.setTransactionId(transactionID);
                // receipt generation
                ReceiptGenerator.generateReceipt(t, account);
                // completion the balance update message
                System.out.println("Withdrawal successful!\nWithdrawal amount : " + amount + "\nAvailable balance : " + account.getBalance());
            }
        }
        catch (InvalidAmountException | InsufficientBalanceException | SQLException | AccountClosedException | AccountNotFoundException e){
            System.out.println("Error : " + e.getMessage());
        }

    }

    // fetching current balance from account-----------------------------------
    private static double getCurrBalance(double amount, Account account) throws InsufficientBalanceException {
        double currBalance = account.getBalance();
        double overdraftLimit = 0;
        // 2accountTypes - savings, current
        // savings account minimum balance = 0
        // current account overdraftLimit = -5000, bank balance = -5000
        if(account.getAccountType().equalsIgnoreCase("savings")){
            if(currBalance - amount < overdraftLimit){
                throw new InsufficientBalanceException("Withdrawal amount exceeds the minimum balance. Cannot initiate transaction.");
            }
        }else if(account.getAccountType().equalsIgnoreCase("current")){
            overdraftLimit = -5000;
            if(currBalance - amount < overdraftLimit){
                throw new InsufficientBalanceException("Withdrawal amount exceeds the overdraft limit. Cannot initiate transaction.");
            }
        }
        // return the current balance--------------------
        return currBalance;
    }

// -----------------------------------------Deposit method----------------------------------------------------------

    public void deposit(long accNumber, double amount){
        try {
            // search the specified bank account
            if (amount <= 0) {
                throw new InvalidAmountException("Withdrawal amount must be positive or greater than 0");
            }
            // search the specified bank account.
            Account account = accountDAO.getAccount(accNumber);
            if (account == null) {
                throw new AccountNotFoundException("Account does not exist at SS-Sev bank");
            }
            // bank account already exist. But, it's already closed.
            else if (account.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Account already closed.");
            }else {
                // first update the balance property of account object
                account.setBalance(account.getBalance() + amount);
                // call accountDao object to update the account balance in database
                accountDAO.updateBalance(account);
                // create new transaction object
                Transaction t = new Transaction(accNumber, "Deposit", amount, LocalDateTime.now(), 0, "Deposit to account");
                // update transaction schema by adding new transaction instance
                int transactionID = transactionDAO.addTransaction(t);
                t.setTransactionId(transactionID);

                // receipt generation
                ReceiptGenerator.generateReceipt(t, account);
                // completion the balance update message
                System.out.println("Deposit successful! \nDeposit amount : " + amount + "\nAvailable balance : " + account.getBalance());
            }
        }
        catch (InvalidAmountException | SQLException | AccountClosedException | AccountNotFoundException e){
            System.out.println("Error : " + e.getMessage());
        }
    }

    //------------------------------------Transfer Method-----------------------------------------------------

    public void transfer(long accNumber, long receiverAccNumber, double amount){
        try (Connection conn = DBUtil.getConnection()){

            // If both accountNumber reference the same account
            if (accNumber == receiverAccNumber) {
                throw new IllegalArgumentException("Sender and receiver account cannot be same.");
            }

            // check the amount is transferable or not
            if (amount <= 0) {
                throw new InvalidAmountException("Amount must be positive or greater than 0");
            }

            // search the specified sender's bank account.
            Account account = accountDAO.getTransferAccount(accNumber, conn);
            if (account == null) {
                throw new AccountNotFoundException("Your account does not exist at SS-Sev bank");
            }
            // bank account already exist. But, it's already closed.
            else if (account.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Your account already closed.");
            }

            // search the specified receiver's bank account
            // bcs getAccount uses another connection
            Account recAccount = accountDAO.getTransferAccount(receiverAccNumber, conn);
            if (recAccount == null){
                throw new AccountNotFoundException("Receiver's account does not exist at SS-Sev bank");
            }
            // bank account already exist. But, it's already closed.
            else if (recAccount.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Receiver's account already closed.");
            }


            // // check if specified withdrawal amount is less than the permitted limits.
            double overdraftLimit = 0;
            double currentBalance = account.getBalance();
            if(account.getAccountType().equalsIgnoreCase("current")){
                overdraftLimit = -5000;
            }
            if(currentBalance - amount < overdraftLimit){
                throw new InsufficientBalanceException("Withdrawal amount exceeds the overdraft limit. Cannot initiate transaction.");
            }else{
                try{
                    // don't save anything
                    conn.setAutoCommit(false);
                    // deduct from senders account, create a transaction

                    // first update the property - balance - in the Account object.
                    account.setBalance(account.getBalance() - amount);

                    // call AccountDAO object to update the balance field in the DB by using the Account object.
                    boolean senderAccountUpdateState = accountDAO.transactionUpdateBalance(account, conn);
                    Transaction t1 = new Transaction(accNumber, "Debit", amount, LocalDateTime.now(), receiverAccNumber, "Debited from account for transfer");
                    int transactionId = transactionDAO.addTransferTransaction(t1, conn);
                    t1.setTransactionId(transactionId);



                    // Second update the property - balance - in the recAccount object
                    recAccount.setBalance(recAccount.getBalance() + amount);

                    boolean receiverAccountUpdateState = accountDAO.transactionUpdateBalance(recAccount, conn);
                    // object creation of transaction
                    Transaction t2 = new Transaction(receiverAccNumber, "Credit", amount, LocalDateTime.now(), accNumber, "Credited to account via transfer");
                     //add the object into SQL database
                    int transactionId1 = transactionDAO.addTransferTransaction(t2, conn);
                    t2.setTransactionId(transactionId1);

                    // receipt generation for transactions - t1 and t2
                    if(senderAccountUpdateState && receiverAccountUpdateState){
                        // save all changes
                        conn.commit();
                        // receipt for transaction of sender
                        ReceiptGenerator.generateReceipt(t1, account);
                        // receipt for transaction of receiver
                        ReceiptGenerator.generateReceipt(t2, recAccount);
                        System.out.println("\nTransaction Successful.\nAvailable balance : " + account.getBalance() + "\n");
                    }
                    else{
                        conn.rollback();
                        System.out.println("Transaction Failed!");
                    }
                }catch (SQLException e){
                    conn.rollback();
                    System.out.println("Transaction failed: " + e.getMessage());
                    System.out.println("All operations rolled back.");
                }
            }
        }
        catch (SQLException | AccountNotFoundException | AccountClosedException | InvalidAmountException |
               InsufficientBalanceException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
    public void transactionHistory(long accNumber){
        try {
            // check if the account exists in DB, get the bankAccount
            Account account = accountDAO.getAccount(accNumber);
            if (account == null) {
                throw new AccountNotFoundException("Your account does not exist at SS-Sev bank");
            }
            // bank account already exist. But, it's already closed.
            else if (account.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Your account already closed.");
            }
            else {
                // if account is available then, get all transaction related to this bankAccount.
                List<Transaction> transactionList = transactionDAO.getTransactions(accNumber);
                if(transactionList.isEmpty()){
                    System.out.println("No transactions were made by this account " + accNumber);
                }else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    System.out.println("\n========== All transaction histories ==========");
                    for(Transaction t : transactionList){
                        System.out.println();
                        System.out.println("Transaction Id      : " + t.getTransactionId());
                        System.out.println("Transaction Date    : " + t.getTransactionDate().format(formatter));
                        System.out.println("Transaction Type    : " + t.getTransactionType());
                        System.out.println("Transaction Amount  : " + t.getAmount());
                        if(t.getTransactionType().equalsIgnoreCase("Debit")){
                            System.out.println("From(your account)  : " + t.getAccountNumber());
                            System.out.println("To                  : " + t.getRelatedAccountNumber());
                        }else if(t.getTransactionType().equalsIgnoreCase("Credit")){
                            System.out.println("From                : " + t.getRelatedAccountNumber());
                            System.out.println("To(your account)    : " + t.getAccountNumber());
                        }
                        System.out.println("Description         : " + t.getDescription());
                        System.out.println();
                        System.out.println();
                    }
                }
            }
        }catch (AccountNotFoundException | AccountClosedException | SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }


    }
    public void accountDetails(long accountNumber) {
        try {
            Account account = accountDAO.getAccount(accountNumber);
            if (account == null) {
                // Throw exception if account is not found
                throw new AccountNotFoundException("Account does not exist at SS_Sev Bank.");
            }
            System.out.println("=============================================================");
            System.out.println("\t\t\tACCOUNT DETAILS");
            System.out.println("=============================================================");
            System.out.printf(
                    "Account Holder's Name : %s%n" +
                    "Account Number        : %d%n" +
                    "Account Type          : %s%n" +
                    "Account Status        : %s%n" +
                    "Account Opening Date  : %tF%n" +
                    "Account Balance       : %.2f%n",
                    customerDAO.getCustomerName(accountNumber),
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getStatus(),
                    account.getOpeningDate(),
                    account.getBalance()
            );
            System.out.println("============================================================");
        }catch (AccountNotFoundException  | SQLException e){
            System.out.println("Error : " + e.getMessage());
        }
    }
    public void updateCustomerDetails(String fName, String lName, String email, String phone, String address, long accNumber) {
        try {
            // check if the account is existed or not
            Account account = accountDAO.getAccount(accNumber);
            if (account == null){
                throw new AccountNotFoundException("Account does not exist at SS-Sev bank");
            }
            // bank account already exist. But, it's already closed.
            else if (account.getStatus().equalsIgnoreCase("closed")){
                throw new AccountClosedException("Account already closed.");
            }
            // Update the account
            else{
                int customerID = account.getCustomerId();
                boolean flag = false;
                if(!fName.isEmpty()){
                    flag = customerDAO.updateFirstName(fName, customerID);
                }
                if (!lName.isEmpty()) {
                    flag = customerDAO.updateLastName(lName, customerID);
                }
                if(!email.isEmpty()){
                    flag = customerDAO.updateEmail(email, customerID);
                }
                if (!phone.isEmpty()){
                    flag = customerDAO.updatePhone(phone, customerID);
                }
                if (!address.isEmpty()){
                    flag = customerDAO.updateAddress(address, customerID);
                }

                if(flag){
                    System.out.println("\nCustomer update process successful!\nWould You like to update anything else....");
                }else{
                    System.out.println("\nCustomer update process failed!");
                }

            }

        }
        catch (SQLException | AccountNotFoundException | AccountClosedException e){
            System.out.println("Error : " + e.getMessage());
        }
    }
}
