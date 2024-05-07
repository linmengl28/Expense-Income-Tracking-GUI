package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class ExpenseTest {

    @Test
    public void testExpenseConstructorAndGetters() {
        // Create test data
        ExpenseCategory category = ExpenseCategory.HOUSING;
        double amount = 1500.0;
        LocalDate date = LocalDate.of(2024, 4, 15);
        Member member = new Member("Jane Smith");

        // Create an expense object
        Expense expense = new Expense(category, amount, date, member);

        // Test getters
        assertEquals(category, expense.getCategory());
        assertEquals(amount, expense.getAmount());
        assertEquals(date, expense.getDate());
        assertEquals(member, expense.getMember());
    }
}
