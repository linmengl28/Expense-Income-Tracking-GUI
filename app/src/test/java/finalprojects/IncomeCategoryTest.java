package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IncomeCategoryTest {

    @Test
    public void testGetName() {
        // Test getName method for each enum constant
        assertEquals("SALARY", IncomeCategory.SALARY.getName());
        assertEquals("COMMISSION", IncomeCategory.COMMISSION.getName());
        assertEquals("SELFEMPLOYMENT", IncomeCategory.SELFEMPLOYMENT.getName());
        assertEquals("INVESTMENT", IncomeCategory.INVESTMENT.getName());
        assertEquals("INTEREST", IncomeCategory.INTEREST.getName());
        assertEquals("RETIREMENT", IncomeCategory.RETIREMENT.getName());
        assertEquals("BUSINESS", IncomeCategory.BUSINESS.getName());
        assertEquals("RENTAL", IncomeCategory.RENTAL.getName());
        assertEquals("SUBSIDARY", IncomeCategory.SUBSIDARY.getName());
        assertEquals("CAPITALGAIN", IncomeCategory.CAPITALGAIN.getName());
        assertEquals("ROYALTIES", IncomeCategory.ROYALTIES.getName());
        assertEquals("SIDEHUSTLE", IncomeCategory.SIDEHUSTLE.getName());
        assertEquals("MISCELLANEOUS", IncomeCategory.MISCELLANEOUS.getName());
        assertEquals("OTHER", IncomeCategory.OTHER.getName());
    }
}
