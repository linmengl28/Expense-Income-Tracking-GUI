package finalprojects;

import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.nio.file.*;
import java.util.stream.Stream;

/*
 * TransactionsManager handles all executives on Transaction
 */
public class TransactionsManager {
    protected List<Transaction> transactions;
    public MemberManager memberManager;

    /**
     * Constructor that initializes the TransactionsManager with a MemberManager.
     * @param memberManager the MemberManager to associate with this TransactionsManager.
     */
    public TransactionsManager(MemberManager memberManager) { 
        transactions = new ArrayList<>();
        this.memberManager=memberManager;
    }

    /**
     * Adds a new transaction to the list using given transaction details.
     * @param category the category of the transaction.
     * @param amount the monetary amount of the transaction.
     * @param date the date of the transaction.
     * @param member the member associated with the transaction.
     */
    public void addTransaction(TransactionCategory category, double amount, LocalDate date, Member member) {
        int id = generateNewId();
        Transaction transaction=TransactionFactory.createTransaction(category, amount, date, member);
        transaction.setId(id);
        transactions.add(transaction);
    } 
    
    /**
     * Retrieves all stored transactions.
     * @return a list of all transactions.
     */
    public List<Transaction> getTransactions() {
       return transactions;
    }

    /**
     * Removes a specific transaction from the list.
     * @param transaction the transaction to remove.
     */                
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    /**
     * Removes a specific transaction from the list.
     * @param id the transaction to remove.
     */
    public void removeTransaction(int id) {
        Transaction transaction=findTransactionbyID(id);
        transactions.remove(transaction);
    }

    /**
     * Updates a transaction identified by ID with new details. Null values are not updated.
     * @param id the ID of the transaction to update.
     * @param category the new category of the transaction, or null to keep the original.
     * @param amount the new amount of the transaction, or null to keep the original.
     * @param date the new date of the transaction, or null to keep the original.
     * @param member the new member associated with the transaction, or null to keep the original.
     */
    public void setTransaction(int id, TransactionCategory category, Double amount, LocalDate date, Member member) {
        Transaction transaction = findTransactionbyID(id);
        if (transaction != null) {
            if (category != null) transaction.setCategory(category);
            if (amount != null) transaction.setAmount(amount);
            if (date != null) transaction.setDate(date);
            if (member != null) transaction.setMember(member);
        }
    }

    /**
     * Finds a transaction by its ID.
     * @param id the ID of the transaction to find.
     * @return the transaction with the specified ID, or null if not found.
     */
    public Transaction findTransactionbyID(int id){ 
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }
        return null;
    }

    /**
     * Generates a new unique ID for a transaction.
     * @return a new unique ID.
     */
    public int generateNewId() {
        int maxId = getCurrentMaxID();
        int newId = maxId + 1;
        return newId;
    }

    /**.
     * Gets current largest ID
     * 
     * @return current largest ID.
     */
    public int getCurrentMaxID(){ 
        int maxId = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getId() > maxId) {
                maxId = transaction.getId();
            }
        }
        return maxId;
    }

    /**
     * Loads all transactions from a CSV file associated with the current user.
     * Clears existing transactions before loading new ones.
     */
    public void loadTransactionsFromCSV() {
        String username=UserData.getInstance().getUsername();
        transactions.clear();
        Path csvPath;
        try {
            csvPath= LoginManager.login(username);
            try (BufferedReader br = Files.newBufferedReader(csvPath)) {
                String line;
                while ((line = br.readLine()) != null) {
                    Transaction transaction = Transaction.readFromCsv(line, memberManager);  
                    if (transaction != null) { 
                        transactions.add(transaction);
                    }
                }
            }    
        } catch (IOException e) {
            System.err.println("An error occurred while reading transactions from the CSVFile: " + e.getMessage()+ ",Please check the file path and permissions again");
            e.printStackTrace();
        }
    }
  
    /**
     * Writes all current transactions into a CSV file associated with the specified username.
     * @param username the username associated with the CSV file.
     */
    public void updateCSV(String username) {
        Path csvPath;
        try {
            csvPath= LoginManager.login(username);
            try (BufferedWriter bw = Files.newBufferedWriter(csvPath)) {
                for (Transaction transaction : transactions) {
                    bw.write(transaction.toCSVLine()+"\n");
                }
            }    
        } catch (IOException e) {
            System.err.println("An error occurred while updating the CSV file: " + e.getMessage());
            e.printStackTrace();
            
        }
    }
    
    /**
     * Enhanced displayTransactions method to filter and sort transaction records based on class type, date, category, and member.
     * @param filterByDate Filter transactions by specific date.
     * @param filterByMonth Filter transactions by specific month (as LocalDate).
     * @param showCategory Filter transactions by specific category.
     * @param showMember Filter transactions by specific member.
     * @param sortByAmount Sort transactions by amount.
     * @param sortByDate Sort transactions by date in descending order.
     * @param transactionClass Filter by transaction class (Income.class or Expense.class).
     * @return List of filtered and sorted transactions.
     */
    public List<Transaction> displayTransactions(LocalDate filterByDate, YearMonth filterByMonth,
                                                 TransactionCategory showCategory, Member showMember,
                                                Class<? extends Transaction> transactionClass, Boolean sortByAmount, Boolean sortByDate) {
        if (filterByDate != null && filterByMonth != null) {
            throw new IllegalArgumentException("Cannot filter by both date and month at the same time.");
        }
        if ((filterByDate != null || filterByMonth != null) && sortByDate) {
            throw new IllegalArgumentException("Cannot filter and sort by date at the same time.");
        }
    
        Stream<Transaction> filteredStream = transactions.stream();

        if (transactionClass != null) {
            filteredStream = filteredStream.filter(t -> transactionClass.isAssignableFrom(t.getClass()));
        }
    
        if (filterByDate != null) {
            filteredStream = filteredStream.filter(t -> t.getDate().equals(filterByDate));
        }
    
        if (filterByMonth != null) {
            filteredStream = filteredStream.filter(t -> YearMonth.from(t.getDate()).equals(filterByMonth));
        }
    
        if (showCategory != null) {
            filteredStream = filteredStream.filter(t -> t.getCategory().equals(showCategory));
        }
    
        if (showMember != null) {
            filteredStream = filteredStream.filter(t -> t.getMember().equals(showMember));
        }
    
        if (sortByAmount != null && sortByAmount) {
            filteredStream = filteredStream.sorted(Comparator.comparing(Transaction::getAmount));
        } else if (sortByDate != null && sortByDate) {
            filteredStream = filteredStream.sorted(Comparator.comparing(Transaction::getDate).reversed());
        }
    
        return filteredStream.collect(Collectors.toList());
    }
 
    /**
     * Displays transactions filtered by a date range, category, member, and transaction type (income/expense).
     * Transactions can also be sorted by amount or date.
     *
     * @param startDate Start date for the filter.
     * @param endDate End date for the filter.
     * @param showCategory Category to filter transactions.
     * @param showMember Member to filter transactions.
     * @param isIncome If true, filters for income transactions; if false, filters for expense transactions.
     * @param sortByAmount If true, sorts the result by transaction amount.
     * @param sortByDate If true, sorts the result by transaction date in descending order.
     * @return A list of filtered and optionally sorted transactions.
     */
    public List<Transaction> displayTransactionsbetweendate(LocalDate startDate, LocalDate endDate,
                                                 TransactionCategory showCategory, Member showMember,
                                                 Boolean isIncome, Boolean sortByAmount, Boolean sortByDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        Stream<Transaction> filteredStream = transactions.stream();

        if (isIncome != null) {
            Class<? extends Transaction> transactionClass = isIncome ? Income.class : Expense.class;
            filteredStream = filteredStream.filter(t -> transactionClass.isAssignableFrom(t.getClass()));
        }

        if (startDate != null && endDate != null) {
            filteredStream = filteredStream.filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate));
        }

        if (showCategory != null) {
            filteredStream = filteredStream.filter(t -> t.getCategory().equals(showCategory));
        }

        if (showMember != null) {
            filteredStream = filteredStream.filter(t -> t.getMember().equals(showMember));
        }

        if (sortByAmount != null && sortByAmount) {
            filteredStream = filteredStream.sorted(Comparator.comparing(Transaction::getAmount));
        }
        if (sortByDate != null && sortByDate) {
            filteredStream = filteredStream.sorted(Comparator.comparing(Transaction::getDate).reversed());
        }

        return filteredStream.collect(Collectors.toList());
    }


}















