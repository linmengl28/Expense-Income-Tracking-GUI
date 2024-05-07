package finalprojects;

/**
 * Represents a generic category for financial transactions.
 */
public interface TransactionCategory {

    /**
     * Retrieves the name of the transaction category.
     * This method is intended to provide a uniform way to access the descriptive name
     * of any transaction category, facilitating operations like display, logging, or storage
     * that require a textual representation of the category.
     *
     * @return A {@link String} representing the name of the category.
     */
    String getName(); 
}

