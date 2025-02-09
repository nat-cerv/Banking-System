# Banking-System

1. Program Overview: The banking system is built with a modular structure, allowing different roles to perform specific functions...
- Customers can view their accounts, perform financial transactions, and review transaction logs.
- Bank Managers oversee customer accounts, process transactions, and generate bank statements.
- Bank Tellers assist customers by handling deposits, withdrawals, and simple operations.
The system is designed with secure password management and file handling mechanisms for data persistence, logging all transactions for auditing purposes.

2. Core Functionalities
- Role-Based Access Control: Users log in as...
    - Customers: Perform banking operations (view balance, deposit, withdraw, transfer, and pay someone).
    - Bank Managers: Manage customer data, process CSV-based transactions, and generate bank statements.
    - Bank Tellers: Handle customer transactions and provide account support.
- Account Management: The system manages three types of bank accounts...
    - Checking: Used for everyday transactions.
    - Savings: Helps users save money while limiting access to funds.
    - Credit: Manages credit-based transactions within predefined credit limits.
- Financial Transactions: Users can perform several key transactions...
    - Deposits
    - Withdrawals
    - Transfers between accounts
    - Payments to other customers
- File I/O for Persistence:
    - Customer details and account data are loaded from and saved to CSV files.
    - A transaction log (transaction_log.txt) records all transactions.
- Password Management: Passwords are securely handled using the singleton design pattern in the PasswordManager class.
- Factory Design for Account Creation: The system uses an AccountFactory to dynamically create different types of bank accounts.
- Custom Exception Handling: The InvalidAccountException ensures that only valid account types can be created.
- Transaction Logging and Statements:
    - Every financial transaction is logged.
    - Bank statements summarize recent transactions and account balances.
- Unit Testing: The project includes JUnit tests to ensure the reliability of key functionalities.
- Javadoc: It also includes comprehensive Javadoc documentation for long-term maintainability.

3. Key Files and Their Roles
- RunBank.java: The main program that manages user interactions and menu-based navigation. It handles role selection and directs users to the corresponding functionalities.
- Customer.java: Defines customer objects, including their personal information and associated checking, savings, and credit accounts.
- Account.java (and its subclasses Checking.java, Saving.java, and Credit.java): Base class and specific implementations for different account types, managing balances, credit limits, and transactions.
- AccountFactory.java: Implements the factory design pattern to create different types of bank accounts dynamically.
- BankManager.java and BankTeller.java: These classes represent the roles of bank employees, with different access to customer information and transactions.
- FilesHandling.java: Manages reading from and writing to CSV files, ensuring data persistence across sessions.
- PasswordManager.java: Handles password-related tasks, such as generating, updating, and validating passwords.
- transaction_log.txt and Transactions.csv: The transaction log records all user transactions for review, while the CSV file stores and updates customer details.
- RunBankTest.java: JUnit test cases to validate core functionalities, such as deposits, withdrawals, payments, and transfers.

4. Technical Details
- Object-Oriented Design: Classes such as Person, Customer, and BankTeller utilize inheritance, encapsulation, and polymorphism.
- Factory and Singleton Patterns:
    - Factory Design: The AccountFactory dynamically creates checking, savings, or credit accounts based on input parameters.
    - Singleton Design: The PasswordManager ensures a single instance for password management.
- Exception Handling: Custom exceptions, such as the InvalidAccountException, ensure robustness and input validation during runtime.
- File-Based Data Storage: The system uses CSV files to store customer data and text files for transaction logs.
