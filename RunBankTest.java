import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * The RunBankTest class that makes test cases for different methods.
 * @author Natalia Cervantes
 * @author Tzetzaith Rivero
 * @version 3.0
 */
class RunBankTest {
    private HashMap<String, Customer> customersByName;
    private Scanner scanner;
    private Customer testCustomer;
    private Customer testCustomer2;
    private BankManager bm;

    /**
     * Initializes resources before each test.
     */
    @BeforeEach
    void setUp() {
        customersByName = new LinkedHashMap<>();
        scanner = new Scanner(System.in);

        Checking checkingAccount = new Checking(1001, 500.0);
        Saving savingsAccount = new Saving(2001, 1000.0);
        Credit creditAccount = new Credit(3001, -100.0, 500.0);
        testCustomer = new Customer(1, "John", "Doe", "01-Jan-1990", "123 Main St", "555-1234", checkingAccount, savingsAccount, creditAccount);
        customersByName.put(testCustomer.getFirstName() + " " + testCustomer.getLastName(), testCustomer);

        testCustomer2 = new Customer(2, "Ram", "Rivero", "01-Jan-1990", "123 Main St", "555-1234", checkingAccount, savingsAccount, creditAccount);
        customersByName.put(testCustomer2.getFirstName() + " " + testCustomer2.getLastName(), testCustomer2);

        bm = new BankManager(customersByName);
    }

    /**
     * Tests the deposit functionality by verifying that the balance of the checking account
     * is correctly updated after a deposit.
     * 
     * This test deposits a specified amount into the customer's checking account and
     * checks that the account balance increases by the correct amount.
     */
    @Test
    void testDepositMoney() {
        double initialBalance = testCustomer.getCheckingAccount().getBalance();
        double depositAmount = 200.0;

        testCustomer.deposit(depositAmount, "1");

        double expectedBalance = initialBalance + depositAmount;
        assertEquals(expectedBalance, testCustomer.getCheckingAccount().getBalance());
    }

    /**
     * Tests the withdrawal functionality by verifying that the balance of the checking account
     * is correctly updated after a withdrawal.
     *
     * This test withdraws a specified amount from the customer's checking account and
     * checks that the account balance decreases by the correct amount.
     */
    @Test
    void testWithdrawMoney() {
        double initialBalance = testCustomer.getCheckingAccount().getBalance();
        double withdrawAmount = 100.0;

        testCustomer.withdraw(withdrawAmount, "1");

        double expectedBalance = initialBalance - withdrawAmount;
        assertEquals(expectedBalance, testCustomer.getCheckingAccount().getBalance());
    }

    /**
     * Tests the payment functionality by verifying that the balances of both the payer
     * and recipient's checking accounts are correctly updated after a payment transaction.
     * 
     * This test simulates a payment from the test customer to a recipient customer,
     * deducting the specified amount from the payer's checking account and adding it
     * to the recipient's checking account. It verifies that both account balances
     * reflect the transaction accurately.
     */
    @Test
    void testPaySomeone() {
        Checking recipientCheckingAccount = new Checking(1002, 300.0);
        Saving recipientSavingsAccount = new Saving(2002, 600.0);
        Credit recipientCreditAccount = new Credit(3002, -50.0, 400.0);
        Customer recipientCustomer = new Customer(2, "Jane", "Smith", "02-Feb-1991", "456 Elm St", "555-5678", recipientCheckingAccount, recipientSavingsAccount, recipientCreditAccount);

        customersByName.put("Jane Smith", recipientCustomer);

        double payerInitialBalance = testCustomer.getCheckingAccount().getBalance();
        double recipientInitialBalance = recipientCustomer.getCheckingAccount().getBalance();
        double paymentAmount = 100.0;

        if (payerInitialBalance >= paymentAmount) {
            testCustomer.getCheckingAccount().setBalance(payerInitialBalance - paymentAmount);
            recipientCustomer.getCheckingAccount().setBalance(recipientInitialBalance + paymentAmount);
        }

        assertEquals(payerInitialBalance - paymentAmount, testCustomer.getCheckingAccount().getBalance());
        assertEquals(recipientInitialBalance + paymentAmount, recipientCustomer.getCheckingAccount().getBalance());
    }

    /**
     * Tests the determineCreditLimit method to verify that it returns a credit limit
     * within the expected range based on different credit score brackets.
     *
     * This test runs multiple iterations for various credit scores, checking that
     * the returned credit limit falls within the defined range for each score bracket.
     */
    @Test
    void testDetermineCreditLimit() {
        for (int i = 0; i < 10; i++) {
            double limit = Credit.determineCreditLimit(500);
            assertTrue(limit >= 100 && limit < 700);
        }

        for (int i = 0; i < 10; i++) {
            double limit = Credit.determineCreditLimit(650);
            assertTrue(limit >= 700 && limit < 5000);
        }

        for (int i = 0; i < 10; i++) {
            double limit = Credit.determineCreditLimit(700);
            assertTrue(limit >= 5000 && limit < 7500);
        }

        for (int i = 0; i < 10; i++) {
            double limit = Credit.determineCreditLimit(770);
            assertTrue(limit >= 7500 && limit < 16000);
        }

        for (int i = 0; i < 10; i++) {
            double limit = Credit.determineCreditLimit(820);
            assertTrue(limit >= 16000 && limit < 25000);
        }
    }

    /**
     * Tests the addTransaction and getTransactions methods to verify that transactions
     * are correctly added and retrieved for a specified account type.
     *
     * This test adds multiple transactions to the customer's account and checks that
     * the retrieved transaction history matches the expected format and content.
     */
    @Test
    void testAddAndGetTransaction() {
        String accountType = "Savings";
        double startingBalance1 = 500.0;
        double endingBalance1 = 700.0;
        String transactionDescription1 = "Deposited $200.00";
        testCustomer.addTransaction(accountType, startingBalance1, endingBalance1, transactionDescription1);

        double startingBalance2 = 700.0;
        double endingBalance2 = 500.0;
        String transactionDescription2 = "Withdrew $200.00";
        testCustomer.addTransaction(accountType, startingBalance2, endingBalance2, transactionDescription2);

        String expectedText = "Starting Balance: $500.0, Ending Balance: $700.0, Transaction: Deposited $200.00.\n" + "Starting Balance: $700.0, Ending Balance: $500.0, Transaction: Withdrew $200.00.\n";
        assertEquals(expectedText, testCustomer.getTransactions(accountType));
    }

    /**
     * Tests the BankManager's paySomeone method by verifying that a payment
     * between two customers' accounts is processed successfully.
     */
    @Test
    void testPaySomeoneManager(){
        Customer c1 = customersByName.get("John Doe");
        Customer c2 = customersByName.get("Ram Rivero");
        assertTrue(bm.paySomeone(c1, c2, "Checking", "Checking", 100));
    }

    /**
     * Tests the BankManager's transferMoney method by verifying that
     * a transfer within the same account type fails as expected.
     */
    @Test
    void testTransferMoneyManager(){
        Customer c1 = customersByName.get("John Doe");
        assertFalse(bm.transferMoney(c1, "Checking", "Checking", 100));
    }

    /**
     * Tests the BankManager's withdrawMoney method by verifying that a withdrawal
     * from a customer's account is processed successfully.
     */
    @Test
    void testWithdrawMoneyManager(){
        Customer c1 = customersByName.get("John Doe");
        assertTrue(bm.withdrawMoney(c1, "Checking", 100));
    }

    /**
     * Tests the BankManager's depositMoney method by verifying that a deposit
     * into a customer's account is processed successfully.
     */
    @Test
    void testDepositMoneyManager(){
        Customer c1 = customersByName.get("John Doe");
        assertTrue(bm.depositMoney(c1, "Checking", 100));
    }

    /**
     * Tests the BankManager's generateBankStatement method by verifying that
     * a bank statement is successfully generated for the test customer.
     */
    @Test
    void testGenerateBankStatementManager(){
        assertTrue(bm.generateBankStatement(testCustomer));
    }

    /**
     * Clears resources after each test.
     */
    @AfterEach
    void tearDown() {
        customersByName.clear();
        testCustomer = null;
        testCustomer2 = null;
        scanner.close();
    }
}

