import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * The FilesHandling class handles all file related functions.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class FilesHandling {
    /**
     * Loads customer data from a CSV file and populates the provided maps with customer details.
     * Also generates passwords for each customer using the PasswordManager.
     *
     * @param filePath         The path to the CSV file containing customer data.
     * @param customersByName  A map to store customers, keyed by their full name.
     * @param customersById    A map to store customers, keyed by their ID.
     */
    public static void loadCustomersFromCSV(String filePath, HashMap<String, Customer> customersByName, HashMap<Integer, Customer> customersById) {
        PasswordManager passwordManager = PasswordManager.getInstance();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            String[] headers = headerLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            int idIndex = -1, 
                firstNameIndex = -1, 
                lastNameIndex = -1, 
                dobIndex = -1, 
                addressIndex = -1,
                phoneIndex = -1, 
                checkingAccountNumberIndex = -1, 
                checkingBalanceIndex = -1,
                savingsAccountNumberIndex = -1, 
                savingsBalanceIndex = -1,
                creditAccountNumberIndex = -1, 
                creditMaxIndex = -1, 
                creditBalanceIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                switch (headers[i].trim()) {
                    case "Identification Number":
                        idIndex = i;
                        break;
                    case "First Name":
                        firstNameIndex = i;
                        break;
                    case "Last Name":
                        lastNameIndex = i;
                        break;
                    case "Date of Birth":
                        dobIndex = i;
                        break;
                    case "Address":
                        addressIndex = i;
                        break;
                    case "Phone Number":
                        phoneIndex = i;
                        break;
                    case "Checking Account Number":
                        checkingAccountNumberIndex = i;
                        break;
                    case "Checking Starting Balance":
                        checkingBalanceIndex = i;
                        break;
                    case "Savings Account Number":
                        savingsAccountNumberIndex = i;
                        break;
                    case "Savings Starting Balance":
                        savingsBalanceIndex = i;
                        break;
                    case "Credit Account Number":
                        creditAccountNumberIndex = i;
                        break;
                    case "Credit Max":
                        creditMaxIndex = i;
                        break;
                    case "Credit Starting Balance":
                        creditBalanceIndex = i;
                        break;
                }
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                int id = Integer.parseInt(values[idIndex].trim());
                String firstName = values[firstNameIndex].trim();
                String lastName = values[lastNameIndex].trim();
                String dob = values[dobIndex].trim();
                String address = values[addressIndex].trim();
                String phone = values[phoneIndex].trim();
                int checkingAccountNumber = Integer.parseInt(values[checkingAccountNumberIndex].trim());
                double checkingStartingBalance = Double.parseDouble(values[checkingBalanceIndex].trim());
                int savingsAccountNumber = Integer.parseInt(values[savingsAccountNumberIndex].trim());
                double savingsStartingBalance = Double.parseDouble(values[savingsBalanceIndex].trim());
                int creditAccountNumber = Integer.parseInt(values[creditAccountNumberIndex].trim());
                double creditMax = Double.parseDouble(values[creditMaxIndex].trim());
                double creditStartingBalance = Double.parseDouble(values[creditBalanceIndex].trim());

                Checking checkingAccount = new Checking(checkingAccountNumber, checkingStartingBalance);
                Saving savingsAccount = new Saving(savingsAccountNumber, savingsStartingBalance);
                Credit creditAccount = new Credit(creditAccountNumber, creditStartingBalance, creditMax);

                Customer customer = new Customer(id, firstName, lastName, dob, address, phone, checkingAccount, savingsAccount, creditAccount);
                customersByName.put(firstName + " " + lastName, customer);
                customersById.put(id, customer);
                passwordManager.generatePassword(firstName + " " + lastName);
            }
        } catch (Exception e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    /**
     * Updates the customer information in the CSV file after transactions are made.
     * @param customers the HashMap of customers whose information needs to be updated
     */
    public static void updateCSV(HashMap<String, Customer> customers) {
        String csvFilePath = "Updated_Bank_Users.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number,Checking Account Number,Checking Starting Balance,Savings Account Number,Savings Starting Balance,Credit Account Number,Credit Max,Credit Starting Balance");
            writer.newLine();
            for (Customer customer : customers.values()) {
                writer.write(customer.getIdentificationNumber() + "," +
                             customer.getFirstName() + "," +
                             customer.getLastName() + "," +
                             customer.getDateOfBirth() + "," +
                             customer.getAddress() + "," +
                             customer.getPhoneNumber() + "," +
                             customer.getCheckingAccount().getAccountNumber() + "," +
                             customer.getCheckingAccount().getBalance() + "," +
                             customer.getSavingsAccount().getAccountNumber() + "," +
                             customer.getSavingsAccount().getBalance() + "," +
                             customer.getCreditAccount().getAccountNumber() + "," +
                             customer.getCreditAccount().getCreditMax() + "," +
                             customer.getCreditAccount().getBalance());
                writer.newLine();
            }    
        } catch (IOException e) {
            System.out.println();
        }
    } 

    /**
     * Generates a text file containing a summary of the customer's transactions, including
     * account summaries for checking, savings, and credit accounts.
     * The file is named using the customer's first and last name, followed by "_Transactions.txt".
     * Each account type section displays the transaction history or a message indicating no transactions
     * for that account. The file also includes the statement date.
     * @param customer       The customer whose transaction summary is being generated.
     * @param startingBalance An array containing the starting balances for each account type.
     * @param endingBalance   An array containing the ending balances for each account type.
     * @param statementDate   The date of the statement to be included in the file.
     */
    public static void generateUserTransactionsFile(Customer customer, double[] startingBalance, double[] endingBalance, String statementDate) {
        String fileName = customer.getFirstName() + "_" + customer.getLastName() + "_Transactions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(customer.getFirstName() + " " + customer.getLastName() + "'s Transactions.");
            writer.newLine();
            writer.write("Date of Statement: " + statementDate);
            writer.newLine();
            writer.newLine();

            String[] accountTypes = {"Checking", "Savings", "Credit"};
            for (int i = 0; i < accountTypes.length; i++) {
                writer.write(accountTypes[i] + " Account Summary:");
                writer.newLine();
                
                String transactions = customer.getTransactions(accountTypes[i]);
                if (transactions.isEmpty()) {
                    writer.write("No transactions for this account.");
                } else {
                    writer.write(transactions);
                }
                writer.newLine();
                writer.newLine();
            }
            System.out.println("---------------------------------");
            System.out.println("\nAccount summary generated in file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error generating account summary: " + e.getMessage());
        }
    }


    /**
     * Logs the details of a transaction to a file.
     * @param transactionDetails the details of the transaction to be logged
     */
    public static void logTransaction(String transactionDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transaction_log.txt", true))) {
            writer.write(transactionDetails);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }
}
