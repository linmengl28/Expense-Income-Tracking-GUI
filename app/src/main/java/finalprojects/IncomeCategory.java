package finalprojects;

/*
 * IncomeCategory included all income category
 */
public enum IncomeCategory implements TransactionCategory {
    SALARY,
    COMMISSION,
    SELFEMPLOYMENT,
    INVESTMENT,
    INTEREST,
    RETIREMENT,
    BUSINESS,
    RENTAL,
    SUBSIDARY,
    CAPITALGAIN,
    ROYALTIES,
    SIDEHUSTLE,
    MISCELLANEOUS,
    OTHER;

    /**
     * Gets the name of the income category.
     * This method overrides the {@link TransactionCategory#getName()} method to provide the name of the category.
     *
     * @return the name of the income category as defined by the enum constant.
     */
    @Override
    public String getName() {
        return this.name(); 
    }


}
