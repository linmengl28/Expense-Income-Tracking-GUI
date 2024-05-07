package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class IncomeTest {

    @Test
    public void testIncomeConstructorAndGetters() {
        // Create test data
        IncomeCategory category = IncomeCategory.SALARY;
        double amount = 5000.0;
        LocalDate date = LocalDate.now();
        Member member = new Member("John Doe");

        // Create an income object
        Income income = new Income(category, amount, date, member);

        // Test getters
        assertEquals(category, income.getCategory());
        assertEquals(amount, income.getAmount());
        assertEquals(date, income.getDate());
        assertEquals(member, income.getMember());
    }
}
