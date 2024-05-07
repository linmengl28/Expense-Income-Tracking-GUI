package finalprojects;


import java.time.LocalDate;



/**
 * Factory class for creating transactions.
 * This class encapsulates the logic for creating specific types of transactions based on the category,
 * simplifying the creation process elsewhere in the application. It supports the creation of both income
 * and expense transaction types.
 */
public class TransactionFactory {

    /**
     * Creates a transaction object based on the specified parameters. The type of transaction
     * (Income or Expense) is determined by the category provided.
     *
     * @param category The category of the transaction which determines the type of transaction
     *                 to create. This must be an instance of {@link IncomeCategory} or {@link ExpenseCategory}.
     * @param amount   The amount of money involved in the transaction. This value should be non-null
     *                 and positive.
     * @param date     The date when the transaction occurred. This value must not be null.
     * @param member   The member associated with the transaction. This value must not be null.
     * @return A new instance of {@link Transaction}, specifically either an {@link Income} or {@link Expense}
     *         depending on the category type.
     * @throws IllegalArgumentException If the category is neither an instance of {@link IncomeCategory}
     *                                  nor {@link ExpenseCategory}, or other required parameters are null.
     */
    public static Transaction createTransaction(TransactionCategory category, Double amount, LocalDate date, Member member){
        Transaction transaction = null;
        if (category instanceof IncomeCategory){
            transaction= new Income((IncomeCategory) category, amount, date, member);
        } else if (category instanceof ExpenseCategory) {
            transaction= new Expense((ExpenseCategory) category, amount, date, member);
        } else {
            throw new IllegalArgumentException("Invalid Category input");
        }
        return transaction;
    }
    
}



