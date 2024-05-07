package finalprojects;

import java.time.LocalDate;

/*
 * Income is all income transactions
 */
public class Income extends Transaction {

    /**
     * Constructs a new Income with specified details.
     *
     * @param category the income category of this transaction, encapsulated in {@link IncomeCategory}, cannot be null.
     * @param amount   the monetary value of the income, represented as a double. This value should be positive.
     * @param date     the date on which the income was received, represented as a {@link LocalDate}.
     * @param member   the member associated with the income, encapsulated in a {@link Member} object, cannot be null.
     */
    public Income(IncomeCategory category, double amount, LocalDate date, Member member) {
        super(category, amount, date, member);
    }
}
