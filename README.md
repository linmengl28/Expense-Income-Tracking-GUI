# Expense-Income-Tracking-GUI / Personal Finance Management System

Overview

The Personal Finance Management System is a Java-based application designed to help users track their income and expenses. It provides functionalities for recording various financial transactions, generating reports, and managing user settings.

Features

Income/Expense Tracking: Users can record their income and expenses, categorize transactions, and specify the date of each transaction.
Reporting: The system generates reports based on recorded transactions, allowing users to visualize their financial data over time.
Member Management: Users can manage a list of members associated with their financial transactions, enabling tracking of individual spending habits.
Settings: The application provides settings options, including the ability to add new members and configure user preferences.
Components

The system consists of several key components:

MainFrame: The main graphical user interface (GUI) window where users can navigate between different functionalities of the application.
Transaction: Represents a financial transaction, with subclasses Income and Expense to differentiate between types of transactions.
TransactionCategory: Defines categories for income and expenses, including enums such as IncomeCategory and ExpenseCategory.
MemberManager: Manages the list of members associated with financial transactions.
UserData: Handles user-specific data, such as the username.
TransactionFactory: Generates transaction objects based on input parameters.
LoginManager: Handles user login and file management for storing user data.
Usage

To use the Personal Finance Management System, follow these steps:

Launch the application by running the Main class.
Enter your username when prompted.
Use the MainFrame GUI to navigate between income/expense tracking, reporting, and settings.
Record your financial transactions, generate reports, and manage user settings as needed.
Dependencies

This project has no external dependencies beyond the standard Java Development Kit (JDK).

Contributors

[Menglin Lin] 
[Yue Yu] 
[Jingjing Ji] 
