package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionID;
    private long accountNumber;
    private String transactionType;
    private double amount;
    private LocalDateTime transactionDate;
    private long relatedAccountNumber;
    private String description;

    // constructor-----------
    public Transaction(long accountNumber, String transactionType, double amount, LocalDateTime transactionDate, long relatedAccountNumber, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.relatedAccountNumber = relatedAccountNumber;
        this.description = description;
    }

    public long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getRelatedAccountNumber() {
        return relatedAccountNumber;
    }
    public void setRelatedAccountNumber(long relatedAccountNumber) {
        this.relatedAccountNumber = relatedAccountNumber;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
