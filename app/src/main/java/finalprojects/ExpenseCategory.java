package finalprojects;

/**
 * All expense category extends TransactionCategory
 */
public enum ExpenseCategory implements TransactionCategory {
    HOUSING,
    HEALTHCARE,
    TRANSPORTATION,
    EDUCATION,
    CHILDCARE,
    GROCERY,
    ENTERTAINMENT,
    CLOTHING,
    PERSONALCARE,
    INSURANCE,
    UTILITIES,
    TELECOMMUNICATION,
    MISCELLANEOUS,
    OTHER;

    /**
     * Gets the name of the expense category.
     * This method overrides the {@link TransactionCategory#getName()} method to provide the name
     * of the enum constant, which is the same as the name of the category.
     *
     * @return the name of the expense category as defined by the enum constant.
     */
    @Override
    public String getName() {
        return this.name();  
    }

}
