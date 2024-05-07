package finalprojects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
 * Transaction is all money-related event
 */
public class Transaction {
    private int id=0; 
    private TransactionCategory category;
    private double amount;
    private LocalDate date;
    private Member member;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String CSV_SEPARATOR = ",";

    /**
     * Constructs a new Transaction with specified category, amount, date, and member.
     *
     * @param category the category of the transaction, cannot be null
     * @param amount   the amount of the transaction
     * @param date     the date of the transaction, cannot be null
     * @param member   the member associated with the transaction, cannot be null
     */
    public Transaction(TransactionCategory category, double amount, LocalDate date, Member member) {
        setCategory(category);
        setAmount(amount);
        setDate(date);
        setMember(member);
    }

    /**
     * Creates a Transaction from a CSV string.
     *
     * @param line          the CSV line representing a transaction
     * @param memberManager the member manager to manage member data
     * @return a new Transaction created from the CSV line
     * @throws IllegalArgumentException if the CSV line is invalid
     */
    public static Transaction readFromCsv(String line, MemberManager memberManager) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV line cannot be null or empty.");
        }

        String[] parts = line.split(CSV_SEPARATOR, -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("data is malformed and does not contain enough data elements.");
        }

        int id = Integer.parseInt(parts[0]); 

        String categoryType = parts[1];  
        String[] categoryDetails = getCategoryArr(categoryType);
        TransactionCategory category= convertStrToCategory(categoryDetails[1]);

        double amount = setStrAmount(parts[2]);

        LocalDate date = setStrDate(parts[3]);  

        Member member=setStrMember(parts[4]);
        memberManager.addMemberToList(member);

        Transaction transaction = TransactionFactory.createTransaction(category, amount, date, member);
        transaction.setId(id);

        return transaction;
    }

    /**
     * Helper method to extract category type and name from a string.
     * 
     * @param categoryType the combined category type and name string, expected format "Type:Name"
     * @return String[] array where first element is type and second is name
     * @throws IllegalArgumentException if the input is null, empty, or not in expected format
     */
    public static String[] getCategoryArr(String categoryType){
        if (categoryType == null || categoryType.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        String[] categoryDetails = categoryType.split(":");
        if (categoryDetails.length != 2) {
            throw new IllegalArgumentException("Categorytype in csv must be in the format 'Type:Category', received: " + categoryType);
        }
        return categoryDetails;
    }

    /**
     * Converts a category string into a TransactionCategory object.
     * 
     * @param categoryStr the category name to be converted into a TransactionCategory
     * @return TransactionCategory the corresponding TransactionCategory object
     * @throws IllegalArgumentException if the category string is invalid or not recognized
     */
    public static TransactionCategory convertStrToCategory(String categoryStr){
        TransactionCategory category = null;
        try {
            category = IncomeCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                category = ExpenseCategory.valueOf(categoryStr.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid category: " + categoryStr, ex);
            }
        }
        return category;
    }

    /**
     * Determines the category type as a string based on the category object.
     *
     * @param category the transaction category to evaluate
     * @return String representing the type of the category ("Income" or "Expense:")
     *         or an empty string if the category does not match expected types.
     */
    public String getCategoryTypeStr(TransactionCategory category) {
        if (category instanceof IncomeCategory) {
            return "Income";
        } else if (category instanceof ExpenseCategory) {
            return "Expense:";
        }
        return "";
    }

    /**
     * Converts a string amount into a double.
     * 
     * @param amountStr the amount string
     * @return double the converted double amount
     * @throws IllegalArgumentException if the amount string is null, empty, or not a valid double
     */
    public static Double setStrAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Amount cannot be null or empty.");
        }
        try {
            return Double.parseDouble(amountStr); 
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format.", e);
        }
    }

    /**
     * Converts a string representing a member's name into a Member object.
     *
     * @param name the name of the member
     * @return Member a new Member object initialized with the provided name
     * @throws IllegalArgumentException if the name is null or empty
     */
    public static Member setStrMember(String name) {
        if (name == "" || name == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        return new Member(name);
    }

    /**
     * Converts a string date into a LocalDate.
     * 
     * @param dateStr the date string in the format "yyyy-MM-dd"
     * @return LocalDate the LocalDate object representing the date
     * @throws IllegalArgumentException if the date string is null, empty, or not in a valid format
     */
    public static LocalDate setStrDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty.");
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format. Expected format: yyyy-MM-dd. " + e.getParsedString(), e.getParsedString(), e.getErrorIndex(), e);
        }
    }

    /**
     * Sets the ID for this transaction.
     *
     * @param id the ID of the transaction
     * @throws IllegalArgumentException if the ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID must be a positive value.");
        }
        this.id = id;
    }

    /**
     * Returns the ID of this transaction.
     *
     * @return int the ID of the transaction
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the amount associated with this transaction.
     *
     * @param amount the member to associate with this transaction
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Retrieves the amount of this transaction.
     *
     * @return double the amount of this transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the member associated with this transaction.
     *
     * @param member the member to associate with this transaction, cannot be null
     * @throws IllegalArgumentException if the member is null
     */
    public void setMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        this.member = member;
    }

    /**
     * Returns the member associated with this transaction.
     *
     * @return Member the member associated with the transaction
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the category of this transaction.
     *
     * @param category the transaction category, cannot be null
     * @throws NullPointerException if the category is null
     */
    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    /**
     * Sets the date of the transaction.
     *
     * @param date the date of the transaction, cannot be null
     * @throws NullPointerException if the date is null
     */
    public void setDate(LocalDate date) {

        this.date = date;
    }

    /**
     * Retrieves the date of this transaction.
     *
     * @return LocalDate the date of this transaction
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves the category associated with this transaction.
     *
     * @return TransactionCategory the category of this transaction
     */
    public TransactionCategory getCategory() {
        return category;
    }

    /**
     * Transfers and joins attributes to csvline format.
     *
     * @return A string in CSV line format. It considers the null or empty condition.
     */
    public String toCSVLine() {
        String categoryTypeStr =  getCategoryTypeStr(category);
        String categorydetails = categoryTypeStr + ":" + category.getName();
        String dateStr = (date == null) ? "" : date.format(DATE_FORMATTER);
        String memberName = member.getName();

        return id + CSV_SEPARATOR + categorydetails + CSV_SEPARATOR + amount + CSV_SEPARATOR + dateStr + CSV_SEPARATOR + memberName;
    }

    /**
     * Provides a CSV string representation of this transaction.
     *
     * @return String a CSV formatted string representing this transaction
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "Id=" + id +
                ", Category='" + category + '\'' +
                ", Amount=" + amount +
                ", Date='" + date + '\'' +
                ", Member='" + member + '\'' +
                '}';
    }

    /**
     * Compares this transaction to another object for equality.
     *
     * @param o the object to be compared with this transaction
     * @return boolean true if the other object is a transaction with the same ID, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getId() == that.getId();
    }

}

