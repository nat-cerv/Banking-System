import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;

/**
 * The RunBank class provides a main program for simulating a banking system. 
 * It reads customer data from a CSV file, allows customers and bank managers 
 * to interact with their accounts, and logs transactions.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
public class RunBank {
    /**
     * The main method reads customer data from a CSV file and starts the 
     * banking system, allowing the user to log in as either a customer, 
     * a bank manager, or a bank teller.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Customer> customersByName = new HashMap<>();
        HashMap<Integer, Customer> customersById = new HashMap<>();
        FilesHandling.loadCustomersFromCSV("CS 3331 - Bank Users.csv", customersByName, customersById);

        boolean exit = false;
        while (!exit) {
            System.out.println("____________________________________________________________________________________");
            System.out.println("\nHello User! Please identify yourself: ");
            System.out.println("1. Customer");
            System.out.println("2. Bank Manager");
            System.out.println("3. Bank Teller");
            System.out.println("4. Exit");
            System.out.print("Select an option (1-4): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")){
                System.out.println("\nWhat is your first name?");
                String firstName = scanner.nextLine();
                System.out.println("And your last name?");
                String lastName = scanner.nextLine();
                
                PasswordManager passwordManager = PasswordManager.getInstance();
                boolean loginSuccessful = passwordManager.loginCustomer(firstName + " " + lastName, scanner);
                if (loginSuccessful) {
                    bankingMenu(customersByName, firstName, lastName, scanner);
                }
  
            } else if (choice.equals("2")){
                bankingMenu(customersByName, customersById, scanner);

            } else if (choice.equals("3")){
                bankingMenu(customersById, scanner);

            } else if (choice.equals("4") || choice.equals("EXIT")){
                exit = true;
                System.out.println("Goodbye!");
                break;
            } 
            else {
                System.out.println("\n---------------------------------");
                System.out.println("Invalid option. Please try again.");
                System.out.println("...");
            }
        }
        FilesHandling.updateCSV(customersByName);
        scanner.close();
    }

    /**
     * The banking menu for customers, allowing them to view account balances, 
     * deposit, withdraw, transfer money, pay another customer, and generate 
     * transaction summary.
     * @param customers the HashMap of customers
     * @param firstName the first name of the customer
     * @param lastName  the last name of the customer
     * @param scanner the scanner to read user inputs
     */
    private static void bankingMenu(HashMap<String, Customer> customers, String firstName, String lastName, Scanner scanner) {
        boolean exit = false;
        String nameKey = firstName + " " + lastName;
        Customer matchedCustomer = customers.get(nameKey);

        if (matchedCustomer == null) {
            System.out.println("---------------------------------");
            System.out.println("Customer not found...");
            return;
        }

        double[] startingBalance = {matchedCustomer.getCheckingAccount().getBalance(), matchedCustomer.getSavingsAccount().getBalance(), matchedCustomer.getCreditAccount().getBalance()}; 
        double[] endingBalance = {matchedCustomer.getCheckingAccount().getBalance(), matchedCustomer.getSavingsAccount().getBalance(), matchedCustomer.getCreditAccount().getBalance()};

        while (!exit) {
            startingBalance[0] = matchedCustomer.getCheckingAccount().getBalance();
            startingBalance[1] = matchedCustomer.getSavingsAccount().getBalance();
            startingBalance[2] = matchedCustomer.getCreditAccount().getBalance();
            boolean[] transactionTypes = {false, false, false, false};
            System.out.println("\n--- Banking Menu ---");
            System.out.println("1. Inquire Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money Between Accounts");
            System.out.println("5. Pay Someone");
            System.out.println("6. Generate Transaction Summary");
            System.out.println("7. Exit");
            System.out.print("Select an option (1-7): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    inquireBalance(matchedCustomer);
                    break;
                case "2":
                    depositMoney(matchedCustomer, scanner);
                    transactionTypes[0] = true;
                    break;
                case "3":
                    withdrawMoney(matchedCustomer, scanner);
                    transactionTypes[1] = true;
                    break;
                case "4":
                    transferMoney(matchedCustomer, scanner);
                    transactionTypes[2] = true;
                    break;
                case "5":
                    try{
                        System.out.println("\nWhat is the first name of the other customer?");
                        String firstNameOther = scanner.nextLine();
                        System.out.println("And their last name?");
                        String lastNameOther = scanner.nextLine();
                        
                        while (nameKey.equals(firstNameOther + " " + lastNameOther)){
                            System.out.println("---------------------------------");
                            System.out.println("You can't pay yourself! Try again...\n\nWhat is the first name of the other customer?"); 
                            firstNameOther = scanner.nextLine();
                            System.out.println("And their last name?");
                            lastNameOther = scanner.nextLine();
                        }

                        paySomeone(customers, nameKey, firstNameOther, lastNameOther, scanner);
                        transactionTypes[3] = true;
                        break;
                    } catch (Exception e) {
                        System.out.println("---------------------------------");
                        System.out.println("Customer not found...");
                    }
                case "6":
                    String dateTransaction = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    FilesHandling.generateUserTransactionsFile(matchedCustomer, startingBalance, endingBalance, dateTransaction);
                    break;   
                case "7":
                    exit = true;
                    System.out.println("Goodbye " + nameKey + "!");
                    break;
                default:
                    System.out.println("---------------------------------");
                    System.out.println("Invalid option. Please try again.");
                    break;
            }

            endingBalance[0] = matchedCustomer.getCheckingAccount().getBalance();
            endingBalance[1] = matchedCustomer.getSavingsAccount().getBalance();
            endingBalance[2] = matchedCustomer.getCreditAccount().getBalance();
            String[] accountTypes = {"Checking", "Savings", "Credit"};
            for (int i = 0; i < accountTypes.length; i++) {
                if (startingBalance[i] != endingBalance[i]) {
                    String transactionDescription = "";
                    double amount = Math.abs(startingBalance[i] - endingBalance[i]);
                    amount = Math.round(amount * 100.0) / 100.0;
                    if (transactionTypes[0]) {
                        transactionDescription = "Deposited $" + amount;
                    } else if (transactionTypes[1]) {
                        transactionDescription = "Withdrawed $" + amount;
                    } else if (transactionTypes[2]) {
                        transactionDescription = "Transfered $" + amount;
                    } else if (transactionTypes[3]) {
                        transactionDescription = "Payed Someone $" + amount;
                    }
                    matchedCustomer.addTransaction(accountTypes[i], startingBalance[i], endingBalance[i], transactionDescription);
                }
            }
            FilesHandling.updateCSV(customers);
        }
    }

    /**
     * The banking menu for the bank teller, can access a customer information or 
     * generate a bank statement by entering their full name or ID, process 
     * transactions of customers, and add a new customer.
     * @param customersName the HashMap of customers by Name
     * @param customersId the HashMap of customers by ID
     * @param identNum the identification number of the customer to look up
     * @param scanner the scanner to read user inputs
     */
    private static void bankingMenu(HashMap<String, Customer> customersName, HashMap<Integer, Customer> customersId, Scanner scanner) {
        BankManager bm = new BankManager(customersName);
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Bank Manager Menu ---");
            System.out.println("1. View Customer Information");
            System.out.println("2. Process Transactions");
            System.out.println("3. Generate Bank Statement");
            System.out.println("4. Add a new Customer");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");
            String choice = scanner.nextLine();
            Customer matchedCustomer = null;
            if (choice.equals("1") || choice.equals("3")) {
                matchedCustomer = searchCustomer(customersName, customersId, scanner);
                if (matchedCustomer == null) {
                    System.out.println("Customer lookup failed. Returning to main menu.");
                    continue;
                }
            }
            switch (choice) {
                case "1":
                    bm.viewCustomerInfo(matchedCustomer);
                    break;
                case "2":
                    bm.processTransactions("Transactions.csv");
                    break;
                case "3":
                    bm.generateBankStatement(matchedCustomer);
                    break;
                case "4":
                    addNewCustomer(customersName, scanner);
                    break;
                case "5":
                    exit = true;
                    System.out.println("Exiting Bank Manager Menu.");
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, 4, or 5:");
            }
        }
    }

    /**
     * Helper method to search a customer by their full name or ID.
     * @param customersName the HashMap of customers by Name
     * @param customersId the HashMap of customers by ID
     * @param scanner the scanner to read user inputs
     */
    private static Customer searchCustomer(HashMap<String, Customer> customersName, HashMap<Integer, Customer> customersId, Scanner scanner) {
        System.out.println("\nHow to access the customer:");
        System.out.println("1. By their full name");
        System.out.println("2. By their ID number");
        while (true) {
            String answer = scanner.nextLine();
            switch (answer) {
                case "1":
                    System.out.println("Type the full name of the customer (e.g., Sofia Hernandez):");
                    while (true) {
                        String fullName = scanner.nextLine();
                        Customer customer = customersName.get(fullName);
                        if (customer != null) {return customer;}
                        System.out.println("Customer not found. Please enter a valid full name:");
                    }
                case "2":
                    System.out.println("Type the ID number of the customer:");
                    while (true) {
                        String ID = scanner.nextLine();
                        try {
                            Customer customer = customersId.get(Integer.parseInt(ID));
                            if (customer != null) {return customer;}
    
                            System.out.println("Customer not found. Please enter a valid ID:");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format. Please enter a numeric ID:");
                        }
                    }
                default:
                    System.out.println("Invalid option. Please enter 1 or 2:");
            }
        }
    }

    /**
     * Displays detailed customer information or processes transactions
     * based on the identification number entered by a bank teller.
     * @param customersId the HashMap of customers by ID
     * @param scanner the scanner to read user inputs
     */
    private static void bankingMenu(HashMap<Integer, Customer> customersId, Scanner scanner) {
        BankTeller bt = new BankTeller();
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Bank Teller Menu ---");
            System.out.println("1. View Customer Information");
            System.out.println("2. Process Transactions");
            System.out.println("3. Exit");
            System.out.print("Select an option (1-3): ");
            String choice = scanner.nextLine();

            Customer matchedCustomer = null;
            if (!choice.equals("3")) {
                System.out.println("\nType the ID number of the customer:");
                while (true) {
                    String ID = scanner.nextLine();
                    try {
                        matchedCustomer = customersId.get(Integer.parseInt(ID));
                        if (matchedCustomer != null) {break;}
                        System.out.println("\nCustomer not found. Please enter a valid ID:");
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid ID format. Please enter a numeric ID:");
                    }
                }
            }
            switch (choice) {
                case "1":
                    bt.viewCustomerInfo(matchedCustomer);
                    break;
                case "2":
                    bt.processDeposit(matchedCustomer);
                    bt.processWithdrawal(matchedCustomer);
                    break;
                case "3":
                    exit = true;
                    System.out.println("\nExiting Bank Teller Menu.");
                    break;
                default:
                    System.out.println("\nInvalid option. Please enter 1, 2, or 3:");
            }
        }
    }

    /**
     * Displays the details of the customer's checking, savings, and credit accounts.
     * @param customer the customer whose balances are being inquired
     */
    public static void inquireBalance(Customer customer) {
        System.out.println("\n--- Account Balances ---");
        customer.getCheckingAccount().displayAccountInfo();
        customer.getSavingsAccount().displayAccountInfo();
        customer.getCreditAccount().displayAccountInfo();
    }

    /**
     * Allows the customer to deposit money into their checking, savings, or credit account.
     * @param customer the customer who is making the deposit
     * @param scanner the scanner to read user inputs
     */
    public static void depositMoney(Customer customer, Scanner scanner) {
        System.out.println("\n--- Deposit Money ---");
        try{
            double amount = getAmount("deposit", scanner);
            System.out.println("\nSelect account to deposit into:");
            System.out.println("1. Checking");
            System.out.println("2. Savings");
            System.out.println("3. Credit");
            String accountChoice; 
            while (true) { 
                accountChoice = scanner.nextLine();
                if (accountChoice.equals("3")) {
                    while (!customer.getCreditAccount().isDepositWithinLimit(amount)) {
                        System.out.print("\nThe amount you chose is too high for your Credit Account.\n");
                        customer.getCreditAccount().displayAccountInfo();
                        amount = getAmount("deposit", scanner);
                    }
                }
                double prevChecking = customer.getCheckingAccount().getBalance();
                double prevSaving = customer.getSavingsAccount().getBalance();
                double prevCredit = customer.getCreditAccount().getBalance();
                customer.deposit(amount, accountChoice);
                switch (accountChoice) {
                    case "1":
                        if (prevChecking != customer.getCheckingAccount().getBalance()) {
                            FilesHandling.logTransaction("Deposited $" + amount + " into Checking Account for " + customer.getFirstName() + " " + customer.getLastName());
                        }
                        break;
                    case "2":
                        if (prevSaving != customer.getSavingsAccount().getBalance()) {
                            FilesHandling.logTransaction("Deposited $" + amount + " into Savings Account for " + customer.getFirstName() + " " + customer.getLastName());
                        }
                        break;
                    case "3":
                        if (prevCredit != customer.getCreditAccount().getBalance()) {
                            FilesHandling.logTransaction("Deposited $" + amount + " into Credit Account for " + customer.getFirstName() + " " + customer.getLastName());
                        }
                        break;
                    default:
                        System.out.println("Invalid selection. Please enter 1, 2, or 3:");
                        continue;
                }
                break;
            }
        } catch (NumberFormatException e) {
            System.out.println("---------------------------------");
            System.out.println("Error: Invalid inputs.");
        }
    }    

    /**
     * Allows the customer to withdraw money from their checking or savings account.
     * @param customer the customer who is making the withdrawal
     * @param scanner the scanner to read user inputs
     */
    public static void withdrawMoney(Customer customer, Scanner scanner) {
        System.out.println("\n--- Withdraw Money ---");
        try{
            double amount = getAmount("withdraw", scanner);
            System.out.println("\nSelect account to withdraw from:");
            System.out.println("1. Checking");
            System.out.println("2. Savings");
            String accountChoice; 
            while (true) { 
                accountChoice = scanner.nextLine();   
                double prevChecking = customer.getCheckingAccount().getBalance();
                double prevSaving = customer.getSavingsAccount().getBalance();
                customer.withdraw(amount, accountChoice);
                switch (accountChoice) {
                    case "1":
                        if (prevChecking != customer.getCheckingAccount().getBalance()) {
                            FilesHandling.logTransaction("Withdrew $" + amount + " from Checking Account for " + customer.getFirstName() + " " + customer.getLastName());
                        }
                        break;
                    case "2":
                        if (prevSaving != customer.getSavingsAccount().getBalance()) {
                            FilesHandling.logTransaction("Withdrew $" + amount + " from Savings Account for " + customer.getFirstName() + " " + customer.getLastName());
                        }
                        break;
                    default:
                        System.out.println("Invalid selection. Please enter 1, or 2:");
                        continue;
                }
                break;
            }
        } catch (Exception e) {
            System.out.println("---------------------------------");
            System.out.println("Error: Invalid inputs.");
        }
    }
    

    /**
     * Allows the customer to transfer money between their checking and savings accounts.
     * @param customer the customer making the transfer
     * @param scanner the scanner to read user inputs
     */    
    public static void transferMoney(Customer customer, Scanner scanner) {
        System.out.println("\n--- Transfer Money ---");
        try{
            double amount = getAmount("transfer", scanner);
            System.out.println("\nSelect transfer direction:");
            System.out.println("1. From Checking to Savings");
            System.out.println("2. From Savings to Checking");
            String direction; 
            while (true) { 
                direction = scanner.nextLine();
                if (direction.equals("1") || direction.equals("2")) {break;}
                System.out.println("Invalid selection. Please enter 1, or 2:");
            }
        
            if (direction.equals("1")) {
                double checkingBalance = customer.getCheckingAccount().getBalance();
                if (checkingBalance >= amount) {
                    customer.getCheckingAccount().setBalance(checkingBalance - amount);
                    customer.getSavingsAccount().setBalance(customer.getSavingsAccount().getBalance() + amount);
                    System.out.println("Transferred $" + amount + " from Checking to Savings.");
        
                    FilesHandling.logTransaction("Transferred $" + amount + " from Checking to Savings for " + customer.getFirstName() + " " + customer.getLastName());
                } else {
                    System.out.println("Insufficient funds in Checking.");
                }
            } else if (direction.equals("2")) {
                double savingsBalance = customer.getSavingsAccount().getBalance();
                if (savingsBalance >= amount) {
                    customer.getSavingsAccount().setBalance(savingsBalance - amount);
                    customer.getCheckingAccount().setBalance(customer.getCheckingAccount().getBalance() + amount);
                    System.out.println("Transferred $" + amount + " from Savings to Checking.");
        
                    FilesHandling.logTransaction("Transferred $" + amount + " from Savings to Checking for " + customer.getFirstName() + " " + customer.getLastName());
                } else {
                    System.out.println("Insufficient funds in Savings.");
                }
            } else {
                System.out.println("Invalid transfer direction.");
            }
        } catch (Exception e) {
            System.out.println("---------------------------------");
            System.out.println("Error: Invalid inputs.");
        }
    }
    

    /**
     * Allows the customer to pay another customer by transferring money from their checking account
     * to the recipient's checking account.
     * @param customers           HashMap of all customers
     * @param payerName          index of the paying customer
     * @param firstNameRecipient  first name of the recipient
     * @param lastNameRecipient   last name of the recipient
     * @param scanner the scanner to read user inputs
     */
    public static void paySomeone(HashMap<String, Customer> customers, String payerName, String firstNameRecipient, String lastNameRecipient, Scanner scanner) {
        System.out.println("\n--- Pay Someone ---");
        Customer payer = customers.get(payerName);
        String nameKey = firstNameRecipient + " " + lastNameRecipient;
        Customer matchedRecipient = customers.get(nameKey);
        if (matchedRecipient == null) {
            System.out.println("---------------------------------");
            System.out.println("Recipient not found. Please check the name and try again.");
            return;
        }

        try{
            double amount = getAmount("pay", scanner);
            double payerCheckingBalance = payer.getCheckingAccount().getBalance();
            if (payerCheckingBalance >= amount) {
                payer.getCheckingAccount().setBalance(payerCheckingBalance - amount);
                double recipientCheckingBalance = matchedRecipient.getCheckingAccount().getBalance();
                matchedRecipient.getCheckingAccount().setBalance(recipientCheckingBalance + amount);
        
                System.out.println("Paid $" + amount + " to " + nameKey + ".");
                FilesHandling.logTransaction("Paid $" + amount + " from " + payerName + " to " + nameKey);
            } else {
                System.out.println("Insufficient funds in Checking. Payment could not be completed.");
            }
        } catch (Exception e) {
            System.out.println("---------------------------------");
            System.out.println("Error: Invalid inputs.");
        }
    }

    /**
     * Prompts the user to enter an amount for a specific transaction type, validates the input,
     * @param transactionType string indicating the type of transaction
     * @param scanner         the scanner to read user inputs
     * @return A valid transaction amount as a double
     */
    public static double getAmount(String transactionType, Scanner scanner) {
        double amount;
        while (true) {
            try {
                System.out.print("\nEnter the amount to " + transactionType + ": ");
                String amountResponse = scanner.nextLine();
                amount = Double.parseDouble(amountResponse);
                if (amount > 0 && amount <= 900000) {
                    amount = Math.round(amount * 100.0) / 100.0;
                    break;
                }
                System.out.println("\nAmount cannot be negative, zero, or bigger than 900,000. Please enter a valid amount.");
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a numeric value.");
            }
        }
        return amount;
    }
    

    /**
     * Adds a new customer to the system by prompting for personal and account information.
     * 
     * This method gathers the customer's first name, last name, date of birth, address,
     * and phone number. It then creates a password, checking, savings, and credit accounts with 
     * unique account numbers and an assigned credit limit based on a random credit score.
     * If a customer with the same name already exists, it prompts for a unique name.
     * @param customersByName A map of customer names to Customer objects, used to store and check for unique names
     * @param scanner         A Scanner object for reading user input
     */
    public static void addNewCustomer(HashMap<String, Customer> customersByName, Scanner scanner) {
        System.out.println("\n--- Add New Customer ---");
        try {   
            int customerId = Customer.lastCustomerId;
            String firstName, lastName, dateOfBirth, address, phoneNumber;
            while (true) {
                System.out.print("Enter first name: ");
                firstName = scanner.nextLine().trim();
                System.out.print("Enter last name: ");
                lastName = scanner.nextLine().trim();
                if (!customersByName.containsKey(firstName + " " + lastName)) {break;}
                System.out.println("\nCustomer with this name already exists. Please enter a unique name.");
            }
            while (true) {
                System.out.print("Enter date of birth (e.g., 01-Jan-2000): ");
                dateOfBirth = scanner.nextLine().trim();
                if (dateOfBirth.length() <= 11) {break;}
                System.out.println("\nInvalid date. Please try again.");
            }
            while (true) {
                System.out.print("Enter address: ");
                address = scanner.nextLine().trim();
                if (address.length() <= 50) {break;}
                System.out.println("\nInvalid address. Please try again.");
            }
            while (true) {
                System.out.print("Enter phone number (e.g., (915) 123-4567): ");
                phoneNumber = scanner.nextLine().trim();
                if (phoneNumber.length() <= 14) {break;}
                System.out.println("\nInvalid phone number. Please try again.");
            }
            
            int checkingAccountNumber = Checking.lastCheckingAccountNumber;
            int savingsAccountNumber = Saving.lastSavingsAccountNumber;
            int creditAccountNumber = Credit.lastCreditAccountNumber;
            Random rand = new Random();
            int creditScore = 1 + rand.nextInt(900);
            double creditMax = Credit.determineCreditLimit(creditScore);

            AccountFactory accFac = new AccountFactory();
            Checking checkingAccount = null;
            Saving savingsAccount = null;
            Credit creditAccount = null;
            try {
                checkingAccount = (Checking) accFac.createAccount("Checking", checkingAccountNumber, creditMax);
                savingsAccount = (Saving) accFac.createAccount("Saving", savingsAccountNumber, creditMax);
                creditAccount = (Credit) accFac.createAccount("Credit", creditAccountNumber, creditMax);
            } catch (InvalidAccountException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Failed to create customer accounts. Returning to main menu...");
                return;
            }
            
            Customer newCustomer = new Customer(customerId, firstName, lastName, dateOfBirth, address, phoneNumber, checkingAccount, savingsAccount, creditAccount);
            customersByName.put(firstName + " " + lastName, newCustomer);
            
            PasswordManager passwordManager = PasswordManager.getInstance();
            String password = passwordManager.generatePassword(firstName + " " + lastName);
            System.out.println("Generated password for " + firstName + " " + lastName + " --- " + password);

            System.out.println("New customer added successfully.");
        } catch (Exception e) {
            System.out.println("Error: Couldn't create new customer.");
        }
    }
}