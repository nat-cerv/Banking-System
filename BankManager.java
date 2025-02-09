import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The BankManager class represents a bank manager.
 * It extends the Person class to inherit personal information.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class BankManager extends Person implements Staff {
    /** A map of customer names to Customer objects, representing the customers assigned to this bank manager. */
    private HashMap<String, Customer> assignedCustomers;

    /**
     * Creates a new BankManager with the given assigned customers.
     * @param assignedCustomers A map of customer names to Customer objects assigned to this bank manager.
     */
    public BankManager(HashMap<String, Customer> assignedCustomers) {
        super();
        this.assignedCustomers = assignedCustomers;
    }

    /** 
     * Displays detailed information about a customer, including personal details,
     * address, phone number, and account balances.
     * 
     * This method prints the customer's identification number, full name,
     * date of birth, address, and phone number. Additionally, it displays
     * information for the customer's checking, savings, and credit accounts,
     * including account numbers, current balances, and credit limit.
     * @param customer The customer object whose information is to be displayed.
     */
    @Override
    public void viewCustomerInfo(Customer customer) {
        System.out.println("\n\nCustomer Information:");
        System.out.println("---------------------");
        System.out.println("Identification Number: " + customer.getIdentificationNumber());
        System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Date of Birth: " + customer.getDateOfBirth()); 
        System.out.println("Address: " + customer.getAddress());
        System.out.println("Phone Number: " + customer.getPhoneNumber());
    
        System.out.println("\nAccount Information:");
        System.out.println("---------------------");
        System.out.println("Checking Account Number: " + customer.getCheckingAccount().getAccountNumber());
        System.out.println("Checking Account Balance: $" + customer.getCheckingAccount().getBalance());
    
        System.out.println("Savings Account Number: " + customer.getSavingsAccount().getAccountNumber());
        System.out.println("Savings Account Balance: $" + customer.getSavingsAccount().getBalance());
    
        System.out.println("Credit Account Number: " + customer.getCreditAccount().getAccountNumber());
        System.out.println("Credit Account Balance: $" + customer.getCreditAccount().getBalance());
        System.out.println("Credit Limit: $" + customer.getCreditAccount().getCreditMax());
    }


    /**
     * Processes a CSV file containing a list of transactions and performs actions
     * such as payments, transfers, inquiries, withdrawals, and deposits based on
     * the transaction data.
     *
     * This method reads each line from the provided CSV file, parses the transaction
     * details, identifies the action to be performed, and invokes the corresponding
     * method. It supports the following actions:
     * 
     * <ul>
     *   <li><strong>pays</strong>: One customer pays another customer.</li>
     *   <li><strong>transfers</strong>: A customer transfers money between their own accounts.</li>
     *   <li><strong>inquires</strong>: A customer inquires about their account balance.</li>
     *   <li><strong>withdraws</strong>: A customer withdraws money from an account.</li>
     *   <li><strong>deposits</strong>: A customer deposits money into an account.</li>
     * </ul>
     *
     * @param csvFilePath The file path to the CSV file containing transaction data.
     *                    Each line in the file should represent a transaction with
     *                    the required fields separated by commas.
     */
    public void processTransactions(String csvFilePath) {
        System.out.println("---------------------------------");
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String fromFirstName = values[0].trim();
                String fromLastName = values[1].trim();
                String fromWhere = values[2].trim();
                String action = values[3].trim();
                String toFirstName = values.length > 4 ? values[4].trim() : "";
                String toLastName = values.length > 5 ? values[5].trim() : "";
                String toWhere = values.length > 6 ? values[6].trim() : "";
                double amount = values.length > 7 && !values[7].trim().isEmpty() ? Double.parseDouble(values[7].trim()) : 0.0;
    
                Customer fromCustomer = this.assignedCustomers.get(fromFirstName + " " + fromLastName);
                Customer toCustomer = toFirstName.isEmpty() || toLastName.isEmpty() ? null : this.assignedCustomers.get(toFirstName + " " + toLastName);
    
                switch (action) {
                    case "pays":
                        if (fromCustomer != null && toCustomer != null) {
                            System.out.println(fromFirstName + fromLastName + ",,," + toFirstName + toLastName + fromWhere+ toWhere+ amount);
                            paySomeone(fromCustomer, toCustomer, fromWhere, toWhere, amount);
                        } else {
                            System.out.println("Pay action failed: Customer not found.");
                        }
                        break;
    
                    case "transfers":
                        if (fromCustomer != null) {
                            transferMoney(fromCustomer, fromWhere, toWhere, amount);
                        } else {
                            System.out.println("Transfer action failed: Customer not found.");
                        }
                        break;
    
                    case "inquires":
                        if (fromCustomer != null) {
                            inquireBalance(fromCustomer, fromWhere);
                        } else {
                            System.out.println("Inquire action failed: Customer not found.");
                        }
                        break;
    
                    case "withdraws":
                        if (fromCustomer != null) {
                            withdrawMoney(fromCustomer, fromWhere, amount);
                        } else {
                            System.out.println("Withdraw action failed: Customer not found.");
                        }
                        break;
    
                    case "deposits":
                        if (toCustomer != null) {
                            depositMoney(toCustomer, toWhere, amount);
                        } else {
                            System.out.println("Deposit action failed: Customer not found.");
                        }
                        break;
    
                    default:
                        System.out.println("Unknown action: " + action);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing amount: " + e.getMessage());
        }
    }
    

     /**
     * Handles a payment transaction from one customer to another.
     * @param fromCustomer The customer making the payment.
     * @param toCustomer The customer receiving the payment.
     * @param fromWhere The account type from which funds are drawn ("Checking").
     * @param toWhere The account type to which funds are deposited ("Checking").
     * @param amount The amount to be transferred.
     * @return True if the transaction was successful; false otherwise.
     */
    public boolean paySomeone(Customer fromCustomer, Customer toCustomer, String fromWhere, String toWhere, double amount) {
        double payerCheckingBalance = fromCustomer.getCheckingAccount().getBalance();
        double recipientCheckingBalance = toCustomer.getCheckingAccount().getBalance();
        if (fromWhere.equals("Checking") && toWhere.equals("Checking")) {
            if (payerCheckingBalance >= amount) {
                fromCustomer.getCheckingAccount().setBalance(payerCheckingBalance - amount);
                toCustomer.getCheckingAccount().setBalance(recipientCheckingBalance + amount);

                fromCustomer.addTransaction(fromWhere, payerCheckingBalance, fromCustomer.getCheckingAccount().getBalance(), "Paid $" + amount + " to " + toCustomer.getFirstName() + " " + toCustomer.getLastName());
                toCustomer.addTransaction(toWhere, recipientCheckingBalance, toCustomer.getCheckingAccount().getBalance(), "Received $" + amount + " from " + fromCustomer.getFirstName() + " " + fromCustomer.getLastName());
        
                System.out.println("Transaction successful: $" + amount + " paid from " + fromCustomer.getFirstName() + " to " + toCustomer.getFirstName());
                return true;
            } else {
                System.out.println("Transaction failed: Insufficient funds in Checking account.");
                return false;
            }
        }
        else {
            System.out.println("Transaction failed: Invalid account specified.");
            return false;
        }
    }
    

    /**
     * Transfers a specified amount between a customer's checking and savings accounts.
     * @param customer  The customer initiating the transfer.
     * @param fromWhere Source account type ("Checking" or "Savings").
     * @param toWhere   Destination account type ("Checking" or "Savings").
     * @param amount    The amount to transfer.
     * @return true if the statement is successfully generated, false otherwise.
     */
    public boolean transferMoney(Customer customer, String fromWhere, String toWhere, double amount) {
        double checkingBalance = customer.getCheckingAccount().getBalance();
        double savingsBalance = customer.getSavingsAccount().getBalance();
    
        if (fromWhere.equals("Checking") && toWhere.equals("Savings")) {
            if (checkingBalance >= amount) {
                customer.getCheckingAccount().setBalance(checkingBalance - amount);
                customer.getSavingsAccount().setBalance(savingsBalance + amount);
    
                customer.addTransaction("Checking", checkingBalance, customer.getCheckingAccount().getBalance(), "Transferred $" + amount + " to Savings");
                customer.addTransaction("Savings", savingsBalance, customer.getSavingsAccount().getBalance(), "Received $" + amount + " from Checking");
    
                System.out.println("Transaction successful: $" + amount + " transferred from Checking to Savings.");
                return true;
            } else {
                System.out.println("Transaction failed: Insufficient funds in Checking account.");
                return false;
            }
        } else if (fromWhere.equals("Savings") && toWhere.equals("Checking")) {
            if (savingsBalance >= amount) {
                customer.getSavingsAccount().setBalance(savingsBalance - amount);
                customer.getCheckingAccount().setBalance(checkingBalance + amount);
    
                customer.addTransaction("Savings", savingsBalance, customer.getSavingsAccount().getBalance(), "Transferred $" + amount + " to Checking");
                customer.addTransaction("Checking", checkingBalance, customer.getCheckingAccount().getBalance(), "Received $" + amount + " from Savings");
    
                System.out.println("Transaction successful: $" + amount + " transferred from Savings to Checking.");
                return true;
            } else {
                System.out.println("Transaction failed: Insufficient funds in Savings account.");
                return false;
            }
        } else {
            System.out.println("Transaction failed: Unsupported account types specified for transfer.");
            return false;
        }
    }
    

    /**
     * Displays the balance and account details for a specified account type
     * (checking, savings, or credit) of a customer.
     * @param customer  The customer whose account balance is being inquired.
     * @param fromWhere The account type to check ("Checking", "Savings", or "Credit").
     */
    public void inquireBalance(Customer customer, String fromWhere) {
        if (fromWhere.equals("Checking")){
            System.out.println("Checking Account Number: " + customer.getCheckingAccount().getAccountNumber());
            System.out.println("Checking Account Balance: $" + customer.getCheckingAccount().getBalance());
        } else if (fromWhere.equals("Savings")){
            System.out.println("Savings Account Number: " + customer.getSavingsAccount().getAccountNumber());
            System.out.println("Savings Account Balance: $" + customer.getSavingsAccount().getBalance());
        } else if (fromWhere.equals("Credit")){
            System.out.println("Credit Account Number: " + customer.getCreditAccount().getAccountNumber());
            System.out.println("Credit Account Balance: $" + customer.getCreditAccount().getBalance());
            System.out.println("Credit Limit: $" + customer.getCreditAccount().getCreditMax());
        }
    }


    /**
     * Withdraws a specified amount of money from a customer's chosen account type
     * (checking, savings, or credit) if sufficient funds are available.
     * @param customer  The {@link Customer} from whose account the money is to be withdrawn.
     * @param fromWhere The account type to withdraw from ("Checking", "Savings", or "Credit").
     * @param amount    The amount of money to withdraw.
     * @return  true if the withdrawal is successful, false otherwise.
     */
    public boolean withdrawMoney(Customer customer, String fromWhere, double amount) {
        if (fromWhere.equals("Checking")) {
            double checkingBalance = customer.getCheckingAccount().getBalance();
            if (checkingBalance >= amount) {
                customer.getCheckingAccount().setBalance(checkingBalance - amount);
                customer.addTransaction("Checking", checkingBalance, customer.getCheckingAccount().getBalance(), "Withdrew $" + amount);
                System.out.println("Withdrawal successful: $" + amount + " withdrawn from Checking account.");
                return true;
            } else {
                System.out.println("Withdrawal failed: Insufficient funds in Checking account.");
                return false;
            }
        } else if (fromWhere.equals("Savings")) {
            double savingsBalance = customer.getSavingsAccount().getBalance();
            if (savingsBalance >= amount) {
                customer.getSavingsAccount().setBalance(savingsBalance - amount);
                customer.addTransaction("Savings", savingsBalance, customer.getSavingsAccount().getBalance(), "Withdrew $" + amount);
                System.out.println("Withdrawal successful: $" + amount + " withdrawn from Savings account.");
                return true;
            } else {
                System.out.println("Withdrawal failed: Insufficient funds in Savings account.");
                return false;
            }
        } else if (fromWhere.equals("Credit")) {
            double creditBalance = customer.getCreditAccount().getBalance();
            if (creditBalance >= amount) {
                customer.getCreditAccount().setBalance(creditBalance - amount);
                customer.addTransaction("Credit", creditBalance, customer.getCreditAccount().getBalance(), "Withdrew $" + amount);
                System.out.println("Withdrawal successful: $" + amount + " withdrawn from Credit account.");
                return true;
            } else {
                System.out.println("Withdrawal failed: Insufficient funds in Credit account.");
                return false;
            }
        } else {
            System.out.println("Withdrawal failed: Invalid account specified.");
            return false;
        }
    }
    

    /**
     * Deposits a specified amount of money into a customer's chosen account type
     * (checking, savings, or credit).
     * @param customer  The {@link Customer} whose account will receive the deposit.
     * @param toWhere   The account type to deposit into ("Checking", "Savings", or "Credit").
     * @param amount    The amount of money to deposit.
     * @return true if the deposit is successful, false otherwise.
     */
    public boolean depositMoney(Customer customer, String toWhere, double amount) {
        if (toWhere.equals("Checking")) {
            double checkingBalance = customer.getCheckingAccount().getBalance();
            customer.getCheckingAccount().setBalance(checkingBalance + amount);
            customer.addTransaction("Checking", checkingBalance, customer.getCheckingAccount().getBalance(), "Deposited $" + amount);
            System.out.println("Deposit successful: $" + amount + " deposited into Checking account.");
            return true;
        } else if (toWhere.equals("Savings")) {
            double savingsBalance = customer.getSavingsAccount().getBalance();
            customer.getSavingsAccount().setBalance(savingsBalance + amount);
            customer.addTransaction("Savings", savingsBalance, customer.getSavingsAccount().getBalance(), "Deposited $" + amount);
            System.out.println("Deposit successful: $" + amount + " deposited into Savings account.");
            return true;
        } else if (toWhere.equals("Credit")) {
            double creditBalance = customer.getCreditAccount().getBalance();
            customer.getCreditAccount().setBalance(creditBalance + amount);
            customer.addTransaction("Credit", creditBalance, customer.getCreditAccount().getBalance(), "Deposited $" + amount);
            System.out.println("Deposit successful: $" + amount + " deposited into Credit account.");
            return true;
        } else {
            System.out.println("Deposit failed: Invalid account specified.");
            return false;
        }
    }
    

    /**
     * Generates a bank statement for a specific customer.
     * @param customer The customer for whom the statement is generated.
     * @return true if the statement is successfully generated, false otherwise.
     */
    public boolean generateBankStatement(Customer customer) {
        String fileName = "BankStatement_" + customer.getIdentificationNumber() + "_" + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Bank Statement for " + customer.getFirstName() + " " + customer.getLastName());
            writer.newLine();
            writer.write("Date of Statement: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            writer.newLine();
            writer.newLine();
            writer.write("Customer Information:");
            writer.newLine();
            writer.write("ID: " + customer.getIdentificationNumber());
            writer.newLine();
            writer.write("Name: " + customer.getFirstName() + " " + customer.getLastName());
            writer.newLine();
            writer.write("Address: " + customer.getAddress());
            writer.newLine();
            writer.write("Phone Number: " + customer.getPhoneNumber());
            writer.newLine();
            writer.newLine();
            writer.write("Account Information:");
            writer.newLine();
            writer.write("Checking Account Balance: $" + customer.getCheckingAccount().getBalance());
            writer.newLine();
            writer.write("Savings Account Balance: $" + customer.getSavingsAccount().getBalance());
            writer.newLine();
            writer.write("Credit Account Balance: $" + customer.getCreditAccount().getBalance());
            writer.newLine();
            writer.newLine();
            writer.write("Transaction History:");
            writer.newLine();
            writer.write("Checking Account Transactions:");
            writer.newLine();
            writer.write(customer.getTransactions("Checking"));
            writer.newLine();
            writer.newLine();
            writer.write("Savings Account Transactions:");
            writer.newLine();
            writer.write(customer.getTransactions("Savings"));
            writer.newLine();
            writer.newLine();
            writer.write("Credit Account Transactions:");
            writer.newLine();
            writer.write(customer.getTransactions("Credit"));
            writer.newLine();
            writer.newLine();
            System.out.println("---------------------------------");
            System.out.println("Bank statement generated successfully for " + customer.getFirstName() + " " + customer.getLastName());
            return true;
        } catch (IOException e) {
            System.out.println("Error generating bank statement: " + e.getMessage());
            return false;
        }
    }
}
