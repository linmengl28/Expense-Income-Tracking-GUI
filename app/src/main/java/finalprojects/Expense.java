package finalprojects;

import java.time.LocalDate;

/**
 * Expense extends Transaction, which is all expense activities
 */
public class Expense extends Transaction {
    
    /**
     * Constructs a new Expense with specified details.
     *
     * @param category the category of the expense, encapsulated in {@link ExpenseCategory}, cannot be null.
     * @param amount   the monetary value of the expense, represented as a double. This value should be positive.
     * @param date     the date on which the expense occurred, represented as a {@link LocalDate}.
     * @param member   the member associated with the expense, encapsulated in a {@link Member} object, cannot be null.
     */
    public Expense(ExpenseCategory category, double amount, LocalDate date, Member member) {
        super(category, amount, date, member);
    }
}
