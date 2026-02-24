package receipts;

import model.Account;
import model.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReceiptGenerator {

    private static final String path = "src/receipts/generatedReceipts/";

    public static void generateReceipt(Transaction t, Account account) {
        try{
            // provide a path for storing generated receipts
            File receiptDir = new File(path);
            if(!receiptDir.exists()){
                receiptDir.mkdirs();
            }
            // giving a unique file name
            String fileName = "Receipt_" + t.getTransactionId() + ".txt";
            File receiptFile = new File(receiptDir, fileName);
            // change the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))) {
                writer.write("=====================================");
                writer.newLine();
                writer.write("            BANK RECEIPT             ");
                writer.newLine();
                writer.write("=====================================");
                writer.newLine();
                writer.write("Transaction ID      : " + t.getTransactionId());
                writer.newLine();
                writer.write("Transaction Type    : " + t.getTransactionType());
                writer.newLine();
                writer.write("Amount              : " + String.format("%.2f", t.getAmount()));
                writer.newLine();
                if ("DEBIT".equalsIgnoreCase(t.getTransactionType())) {
                    writer.write("Transferred To      : " + t.getRelatedAccountNumber());
                    writer.newLine();
                }else if ("CREDIT".equalsIgnoreCase(t.getTransactionType())) {
                    writer.write("Received From       : " + t.getRelatedAccountNumber());
                    writer.newLine();
                }

                writer.write("Description         : " + t.getDescription());
                writer.newLine();
                writer.write("Transaction Date    : " + t.getTransactionDate().format(formatter));
                writer.newLine();
                writer.write("Available Balance   : " + account.getBalance());
                writer.newLine();
                writer.write("=====================================");
                writer.newLine();
                writer.write("        Thank You For Banking        ");
                writer.newLine();
                writer.write("=====================================");
                System.out.println("\nReceipt generated successfully: " + fileName);


            }
        }
        catch(IOException e){
            System.out.println("Error : " + e.getMessage());
        }
    }
}
