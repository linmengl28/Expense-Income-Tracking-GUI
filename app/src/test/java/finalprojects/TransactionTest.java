package finalprojects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransactionTest {
    private Transaction transaction;
    private Member member;
    private LocalDate date;

    @BeforeEach
    void setup() {
        member = new Member("John Doe");
        date = LocalDate.of(2023, 4, 15); 
        transaction = new Transaction(IncomeCategory.SALARY, 1000.0, date, member);
    }

    @Test
    void constructor_CorrectInitialization() {
        assertEquals(IncomeCategory.SALARY, transaction.getCategory());
        assertEquals(1000.0, transaction.getAmount());
        assertEquals(date, transaction.getDate());
        assertEquals(member, transaction.getMember());
    }

    @Test
    void readFromCsv_ValidInput_CreatesCorrectTransaction() {
        MemberManager memberManager = new MemberManager();
        member = new Member("John Doe");
        memberManager.addMemberToList(member);
        String csvLine = "1,Income:SALARY,1000.0,2023-04-15,John Doe";
        Transaction result = Transaction.readFromCsv(csvLine, memberManager);
        assertNotNull(result);
        assertEquals(IncomeCategory.SALARY, result.getCategory());
        assertEquals(1000.0, result.getAmount());
        assertEquals(LocalDate.of(2023, 4, 15), result.getDate());
        assertEquals("John Doe", result.getMember().getName());
    }

    @Test
    void readFromCsv_NullCsvLine_ThrowsIllegalArgumentException() {
        MemberManager memberManager = new MemberManager();
        member = new Member("John Doe");
        memberManager.addMemberToList(member);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Transaction.readFromCsv(null, memberManager));
        assertEquals("CSV line cannot be null or empty.", exception.getMessage());
    }

    @Test
    void readFromCsv_MalformedCsvLine_ThrowsIllegalArgumentException() {
        MemberManager memberManager = new MemberManager();
        member = new Member("John Doe");
        memberManager.addMemberToList(member);
        String malformedCsv = "1,Income:SALARY,1000.0";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Transaction.readFromCsv(malformedCsv, memberManager));
        assertEquals("data is malformed and does not contain enough data elements.", exception.getMessage());
    }

    @Test
    void getCategoryTypeStr_IncomeCategory_ReturnsIncome() {
        String result = transaction.getCategoryTypeStr(IncomeCategory.SALARY);
        assertEquals("Income", result);
    }

    @Test
    void getCategoryTypeStr_ExpenseCategory_ReturnsExpense() {
        String result = transaction.getCategoryTypeStr(ExpenseCategory.GROCERY);
        assertEquals("Expense:", result);
    }

    @Test
    void getCategoryArr_NullCategoryType_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Transaction.getCategoryArr(null));
        assertEquals("Category cannot be null or empty.", exception.getMessage());
    }

    @Test
    void getCategoryArr_EmptyStringCategoryType_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Transaction.getCategoryArr(""));
        assertEquals("Category cannot be null or empty.", exception.getMessage());
    }

    @Test
    void getCategoryArr_WhitespaceCategoryType_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> Transaction.getCategoryArr("   "));
        assertEquals("Category cannot be null or empty.", exception.getMessage());
    }

    @Test
    void getCategoryTypeStr_NullCategory_ReturnsEmptyString() {
        String result = transaction.getCategoryTypeStr(null);
        assertEquals("", result);
    }

    @Test
    void setId_NegativeId_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transaction.setId(-1));
        assertEquals("ID must be a positive value.", exception.getMessage());
    }

    @Test
    void setStrAmount_NullString_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.setStrAmount(null));
        assertEquals("Amount cannot be null or empty.", exception.getMessage());
    }

    @Test
    void setStrAmount_EmptyString_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.setStrAmount(""));
        assertEquals("Amount cannot be null or empty.", exception.getMessage());
    }

    @Test
    void setStrAmount_WhitespaceString_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.setStrAmount("  "));
        assertEquals("Amount cannot be null or empty.", exception.getMessage());
    }

    @Test
    void setStrMember_NullName_ThrowsIllegalArgumentException2() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.setStrMember(null));
        assertEquals("Member cannot be null.", exception.getMessage());
    }


    @Test
    void setStrMember_WhitespaceStringName_ReturnsMember() {
        Member result = Transaction.setStrMember("   ");
        assertNotNull(result);
        assertEquals("   ", result.getName());
    }
    
    @Test
    void toCSVLine_CorrectFormat() {
        // Setting an ID for consistent output
        transaction.setId(1);
        String expectedCSV = "1,Income:SALARY,1000.0,2023-04-15,John Doe";
        assertEquals(expectedCSV, transaction.toCSVLine());
    }

    @Test
    void toCSVLine_WithAllFieldsPresent() {
        String expectedCSV = "0,Income:SALARY,1000.0,2023-04-15,John Doe";
        assertEquals(expectedCSV, transaction.toCSVLine(), "CSV line should match the expected format with all fields.");
    }

    @Test
    void toCSVLine_WithNullDate() {
        transaction.setDate(null);
        String expectedCSV = "0,Income:SALARY,1000.0,,John Doe";
        assertEquals(expectedCSV, transaction.toCSVLine(), "CSV line should handle null date gracefully.");
    }


    @Test
    void equals_SameReference_ReturnsTrue() {
        assertTrue(transaction.equals(transaction));
    }

    @Test
    void equals_DifferentType_ReturnsFalse() {
        assertFalse(transaction.equals(new String("Not a transaction")));
    }

    @Test
    void equals_Null_ReturnsFalse() {
        assertFalse(transaction.equals(null));
    }

    @Test
    void equals_DifferentData_ReturnsFalse() {
        transaction.setId(1);
        Transaction other = new Transaction(IncomeCategory.COMMISSION, 1000.0, date, member);
        other.setId(2);
        assertFalse(transaction.equals(other));
    }

    @Test
    void equals_SameData_ReturnsTrue() {
        Transaction other = new Transaction(IncomeCategory.SALARY, 1000.0, date, member);
        transaction.setId(1);
        other.setId(1);
        assertTrue(transaction.equals(other));
    }

    @Test
    void setAmount_InvalidFormat_ThrowsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Transaction.setStrAmount("invalid"));
        assertEquals("Invalid amount format.", e.getMessage());
    }

    @Test
    void testTransactionConstructor_ValidInputs_CorrectInitialization() {
        assertEquals(1000.0, transaction.getAmount());
        assertEquals(IncomeCategory.SALARY, transaction.getCategory());
        assertEquals(date, transaction.getDate());
        assertEquals(member, transaction.getMember());
    }

    @Test
    void testSetCategory_ValidCategory_SetsCategory() {
        transaction.setCategory(ExpenseCategory.UTILITIES);
        assertEquals(ExpenseCategory.UTILITIES, transaction.getCategory());
    }

    @Test
    void testSetAmount_NegativeAmount_SetsAmount() {
        transaction.setAmount(-500.0);
        assertEquals(-500.0, transaction.getAmount());
    }

    @Test
    void testSetMember_Null_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transaction.setMember(null));
        assertEquals("Member cannot be null.", exception.getMessage());
    }

    @Test
    void testEquals_SameReference_ReturnsTrue() {
        assertTrue(transaction.equals(transaction));
    }

    @Test
    void testEquals_DifferentType_ReturnsFalse() {
        assertFalse(transaction.equals(new Object()));
    }

    @Test
    void testEquals_SameFields_ReturnsTrue() {
        Transaction anotherTransaction = new Transaction(IncomeCategory.SALARY, 1000.0, date, member);
        anotherTransaction.setId(transaction.getId());
        assertTrue(transaction.equals(anotherTransaction));
    }

    @Test
    void testEquals_DifferentId_ReturnsFalse() {
        Transaction anotherTransaction = new Transaction(IncomeCategory.SALARY, 1000.0, date, member);
        anotherTransaction.setId(transaction.getId() + 1);
        assertFalse(transaction.equals(anotherTransaction));
    }
    
    @Test
    void testToCSVLine_CorrectFormat() {
        String expectedCSV = transaction.getId() + "," + "Income:SALARY" + "," + transaction.getAmount() + "," + transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "," + transaction.getMember().getName();
        assertEquals(expectedCSV, transaction.toCSVLine());
    }

    @Test
    void testToString_ContainsCorrectInformation() {
        String toStringOutput = transaction.toString();
        assertTrue(toStringOutput.contains("Transaction{"));
        assertTrue(toStringOutput.contains("Id=" + transaction.getId()));
        assertTrue(toStringOutput.contains("Category='SALARY'"));
        assertTrue(toStringOutput.contains("Amount=1000.0"));
        assertTrue(toStringOutput.contains("Date='" + date.toString() + "'"));
        assertTrue(toStringOutput.contains("Member='" + member.getName() + "'"));
    }

    @Test
    void getCategoryArr_ValidInput_ReturnsCorrectArray() {
        String[] result = Transaction.getCategoryArr("Income:SALARY");
        assertArrayEquals(new String[]{"Income", "SALARY"}, result);
    }

    @Test
    void getCategoryArr_NullInput_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.getCategoryArr(null));
        assertEquals("Category cannot be null or empty.", exception.getMessage());
    }

    @Test
    void getCategoryArr_ImproperFormat_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.getCategoryArr("IncomeSalary"));
        assertEquals("Categorytype in csv must be in the format 'Type:Category', received: IncomeSalary", exception.getMessage());
    }

    @Test
    void convertStrToCategory_ValidIncomeCategory_ReturnsCategory() {
        TransactionCategory result = Transaction.convertStrToCategory("SALARY");
        assertEquals(IncomeCategory.SALARY, result);
    }

    @Test
    void convertStrToCategory_InvalidCategory_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.convertStrToCategory("UNKNOWN"));
        assertEquals("Invalid category: UNKNOWN", exception.getMessage());
    }

    @Test
    void setStrAmount_ValidAmount_ReturnsDouble() {
        double result = Transaction.setStrAmount("123.45");
        assertEquals(123.45, result);
    }

    @Test
    void setStrAmount_InvalidAmount_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.setStrAmount("abc"));
        assertEquals("Invalid amount format.", exception.getMessage());
    }

    @Test
    void setStrMember_ValidName_ReturnsMember() {
        Member result = Transaction.setStrMember("John Doe");
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void setStrMember_NullName_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> Transaction.setStrMember(null));
        assertEquals("Member cannot be null.", exception.getMessage());
    }

    @Test
    void setStrDate_ValidDate_ReturnsLocalDate() {
        LocalDate result = Transaction.setStrDate("2023-04-15");
        assertEquals(LocalDate.of(2023, 4, 15), result);
    }

    @Test
    public void testSetStrDate_ValidDate() {
        String validDateStr = "2023-04-15";
        LocalDate expectedDate = LocalDate.of(2023, 4, 15);
        LocalDate actualDate = Transaction.setStrDate(validDateStr);
        assertEquals(expectedDate, actualDate, "The parsed date should match the expected date.");
    }

    /**
     * Test setStrDate with a null string.
     */
    @Test
    public void testSetStrDate_NullString() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Transaction.setStrDate(null);
        });
        assertEquals("Date cannot be null or empty.", exception.getMessage(), "Expected an IllegalArgumentException for null input.");
    }

    @Test
    public void testSetStrDate_EmptyString() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Transaction.setStrDate("");
        });
        assertEquals("Date cannot be null or empty.", exception.getMessage(), "Expected an IllegalArgumentException for empty input.");
    }

    // @Test
    // public void testSetStrDate_InvalidFormat() {
    //     String invalidDateStr = "15-04-2023";
    //     DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> {Transaction.setStrDate(invalidDateStr);});
    //     assertTrue(exception.getMessage().contains("Invalid date format. Expected format: yyyy-MM-dd."), "Expected a DateTimeParseException for a date with incorrect format.");
    // }

    @Test
    void toCSVLine_CorrectOutput() {
        transaction.setId(1); // Ensure the ID is set for a consistent output
        String expected = "1,Income:SALARY,1000.0,2023-04-15,John Doe";
        assertEquals(expected, transaction.toCSVLine());
    }

    @Test
    void equals_SameTransaction_ReturnsTrue() {
        Transaction another = new Transaction(IncomeCategory.SALARY, 1000.0, date, member);
        another.setId(transaction.getId());
        assertTrue(transaction.equals(another));
    }

    @Test
    void equals_DifferentTransaction_ReturnsFalse() {
        Transaction another = new Transaction(ExpenseCategory.ENTERTAINMENT, 500.0, date, member);
        another.setId(transaction.getId() + 1); // Different ID
        assertFalse(transaction.equals(another));
    }

}








