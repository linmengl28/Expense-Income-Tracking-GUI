package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TransactionFactoryTest {

    @Test
    public void testCreateTransaction() {
        // Test creating an Income transaction
        TransactionFactory tf= new TransactionFactory();
        TransactionCategory incomeCategory = IncomeCategory.SALARY;
        Double amount = 1000.0;
        LocalDate date = LocalDate.now();
        Member member = new Member("John Doe");
        Transaction incomeTransaction = TransactionFactory.createTransaction(incomeCategory, amount, date, member);
        assertTrue(incomeTransaction instanceof Income);
        assertEquals(amount, incomeTransaction.getAmount());
        assertEquals(date, incomeTransaction.getDate());
        assertEquals(member, incomeTransaction.getMember());

        // Test creating an Expense transaction
        TransactionCategory expenseCategory = ExpenseCategory.HOUSING;
        Transaction expenseTransaction = TransactionFactory.createTransaction(expenseCategory, amount, date, member);
        assertTrue(expenseTransaction instanceof Expense);
        assertEquals(amount, expenseTransaction.getAmount());
        assertEquals(date, expenseTransaction.getDate());
        assertEquals(member, expenseTransaction.getMember());

        // Test creating transaction with invalid category
        TransactionCategory invalidCategory = null;
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionFactory.createTransaction(invalidCategory, amount, date, member);
        });
    }
}
