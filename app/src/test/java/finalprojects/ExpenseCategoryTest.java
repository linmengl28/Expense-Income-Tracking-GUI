package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ExpenseCategoryTest {

    @Test
    public void testGetName() {
        // Test getName method for each enum constant
        assertEquals("HOUSING", ExpenseCategory.HOUSING.getName());
        assertEquals("HEALTHCARE", ExpenseCategory.HEALTHCARE.getName());
        assertEquals("TRANSPORTATION", ExpenseCategory.TRANSPORTATION.getName());
        assertEquals("EDUCATION", ExpenseCategory.EDUCATION.getName());
        assertEquals("CHILDCARE", ExpenseCategory.CHILDCARE.getName());
        assertEquals("GROCERY", ExpenseCategory.GROCERY.getName());
        assertEquals("ENTERTAINMENT", ExpenseCategory.ENTERTAINMENT.getName());
        assertEquals("CLOTHING", ExpenseCategory.CLOTHING.getName());
        assertEquals("PERSONALCARE", ExpenseCategory.PERSONALCARE.getName());
        assertEquals("INSURANCE", ExpenseCategory.INSURANCE.getName());
        assertEquals("UTILITIES", ExpenseCategory.UTILITIES.getName());
        assertEquals("TELECOMMUNICATION", ExpenseCategory.TELECOMMUNICATION.getName());
        assertEquals("MISCELLANEOUS", ExpenseCategory.MISCELLANEOUS.getName());
        assertEquals("OTHER", ExpenseCategory.OTHER.getName());
    }
}
