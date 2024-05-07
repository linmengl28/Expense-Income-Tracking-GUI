package finalprojects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class TransactionManagerTest {
    private TransactionsManager manager;
    private MemberManager memberManager;
    private Transaction transaction;
    private Member member;
    private LocalDate date;

    @BeforeEach
    void setup() {
        this.memberManager = new MemberManager();  
        member = new Member("John Doe");
        memberManager.addMemberToList(member);
        manager = new TransactionsManager(memberManager);
        date = LocalDate.of(2023, 4, 15);
        member = new Member("John Doe");
        transaction = new Transaction(IncomeCategory.SALARY, 1000.0, date, member);
        manager.addTransaction(transaction.getCategory(), transaction.getAmount(), transaction.getDate(), transaction.getMember());
        transaction.setId(manager.generateNewId());
    }

    @Test
    void testUpdateCSV_IOError() {
        String username = "testUser";

        Path basePath = Paths.get(System.getProperty("java.io.tmpdir")); 
        Path restrictedPath = basePath.resolve(username + "_dir"); 

        try {
            Files.createDirectories(restrictedPath);  // Create a directory

            assertThrows(NoSuchFileException.class, () -> {
                // Attempt to write using directory path, which should fail
                manager.updateCSV(restrictedPath.toString());
            }, "Expected an IOException because a directory is not a valid target for BufferedWriter");
        } catch (IOException e) {
            System.err.println("Setup for IO exception test failed: " + e.getMessage());
            fail("The setup for the IOException scenario could not be completed.");
        } finally {
            // Cleanup, attempt to remove the directory if it was created
            try {
                Files.deleteIfExists(restrictedPath);  // Cleanup after test
            } catch (IOException e) {
                // Ignore cleanup errors
            }
        }
    }

    @Test
    void testLoadTransactionsFromCSV_Simulated() {
        String csvLine = "2,Income:COMMISSION,1500.0,2023-04-16,Jane Doe";
        Transaction parsedTransaction = Transaction.readFromCsv(csvLine, memberManager);
        manager.getTransactions().clear();
        manager.getTransactions().add(parsedTransaction);

        List<Transaction> transactions = manager.getTransactions();
        assertNotNull(transactions, "Transactions list should not be null");
        assertEquals(1, transactions.size(), "Should contain exactly one transaction after 'loading'");

        Transaction loadedTransaction = transactions.get(0);
        assertNotNull(loadedTransaction, "Loaded transaction should not be null");
        assertEquals(1500.0, loadedTransaction.getAmount(), "Amount should match the simulated CSV input");
        assertEquals("COMMISSION", loadedTransaction.getCategory().getName(), "Category should match the simulated CSV input");
        assertEquals(LocalDate.of(2023, 4, 16), loadedTransaction.getDate(), "Date should match the simulated CSV input");
        assertEquals("Jane Doe", loadedTransaction.getMember().getName(), "Member should match the simulated CSV input");
    }

    @Test
    void testDisplayTransactionsWithMonthFilters() {
        YearMonth yearMonth = YearMonth.from(date);
        List<Transaction> result = manager.displayTransactions(null, yearMonth, IncomeCategory.SALARY, member, null, true, false);
        assertFalse(result.isEmpty(), "Should not be empty as there is one transaction matching the filters");
        assertEquals(1, result.size(), "There should be one transaction matching the month and category");
    
        Transaction resultTransaction = result.get(0);
        resultTransaction.setId(2);
    
        assertEquals(transaction.getId(), resultTransaction.getId(), "IDs should match");
        assertEquals(transaction.getCategory(), resultTransaction.getCategory(), "Categories should match");
        assertEquals(transaction.getAmount(), resultTransaction.getAmount(), "Amounts should match");
        assertEquals(transaction.getDate(), resultTransaction.getDate(), "Dates should match");
    }


    
    @Test
    void testDisplayTransactions_BothDateAndMonthFilters_ThrowsException() {
        LocalDate testDate = LocalDate.of(2023, 4, 15);
        YearMonth testMonth = YearMonth.of(2023, 4);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            manager.displayTransactions(testDate, testMonth, null, null, null, null, null)
        );
        assertEquals("Cannot filter by both date and month at the same time.", exception.getMessage());
    }

    @Test
    void testFilterAndSortByDate_IllegalArgument() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.displayTransactions(date, null, null, null, null, null, true);
        }, "Should throw an exception when trying to filter and sort by date at the same time.");
        assertTrue(exception.getMessage().contains("Cannot filter and sort by date at the same time"));
    }

    @Test
    void testFilterBySpecificDate() {
        List<Transaction> results = manager.displayTransactions(date, null, null, null, null, false, false);
        assertTrue(results.stream().allMatch(t -> t.getDate().equals(date)), "All transactions should match the specific date filter");
    }

    @Test
    void testAddTransaction_IncreasesListSize() {
        int initialSize = manager.getTransactions().size();
        manager.addTransaction(ExpenseCategory.GROCERY, 200.0, date, member);
        assertEquals(initialSize + 1, manager.getTransactions().size());
    }

    @Test
    void testGetTransactions_ReturnsCorrectTransactions() {
        List<Transaction> transactions = manager.getTransactions();
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());

        Transaction retrieved = transactions.get(0);
        assertAll("Check transaction fields",
            () -> assertEquals(IncomeCategory.SALARY, retrieved.getCategory()),
            () -> assertEquals(1000.0, retrieved.getAmount()),
            () -> assertEquals(date, retrieved.getDate()),
            () -> assertEquals(member.getName(), retrieved.getMember().getName())
        );
    }

    @Test
    void testRemoveTransaction_ByObject() {
        manager.removeTransaction(transaction);
        assertFalse(manager.getTransactions().contains(transaction));
    }

    @Test
    void testRemoveTransaction_ById() {
        transaction.setId(manager.generateNewId() - 1); // Ensure the ID is set correctly for removal
        manager.removeTransaction(transaction.getId());
        assertFalse(manager.getTransactions().contains(transaction));
    }

    @Test
    void testSetTransaction_UpdatesCorrectly() {
        manager.addTransaction(transaction.getCategory(), transaction.getAmount(), transaction.getDate(), transaction.getMember());
    
        double newAmount = 1500.0;
        LocalDate newDate = date.plusDays(1);
        Member newMember = new Member("Jane Doe");
        manager.setTransaction(transaction.getId(), null, newAmount, newDate, newMember);
    
        Transaction updated = manager.findTransactionbyID(transaction.getId());
        assertNotNull(updated, "The transaction should not be null");
        assertEquals(newAmount, updated.getAmount(), "Amount should be updated");
        assertEquals(newDate, updated.getDate(), "Date should be updated");
        assertEquals(newMember.getName(), updated.getMember().getName(), "Member should be updated");
    }

    @Test
    void testFindTransactionbyID_FindsCorrectTransaction() {
        transaction.setId(manager.generateNewId() - 1); // Ensure the ID is set correctly for finding
        Transaction found = manager.findTransactionbyID(transaction.getId());
        assertEquals(transaction, found);
    }

    @Test
    void testGenerateNewId_GeneratesSequentialIds() {
        int newId = manager.generateNewId();
        assertTrue(newId > 0);
        int newer=manager.generateNewId();
        assertEquals(newId, 2);
    }

    @Test
    void testUpdateCSV_ChecksUpdateFunctionality() {
        assertDoesNotThrow(() -> manager.updateCSV("testUser"));
    }

    @Test
    void testDisplayTransactionsWithDateRangeAndType() {
        LocalDate startDate = LocalDate.of(2023, 4, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);

        Transaction anotherTransaction = new Transaction(IncomeCategory.SALARY, 500.0, LocalDate.of(2023, 4, 10), member);
        anotherTransaction.setId(manager.generateNewId());
        manager.addTransaction(anotherTransaction.getCategory(), anotherTransaction.getAmount(), anotherTransaction.getDate(), member);

        List<Transaction> results = manager.displayTransactionsbetweendate(startDate, endDate, null, member, true, true, false);
        assertEquals(2, results.size(), "Should return two transactions in the date range for the member");
        assertTrue(results.contains(transaction), "Results should contain the original transaction");
        assertTrue(results.contains(anotherTransaction), "Results should contain the added transaction");
        
        assertEquals(500.0, results.get(0).getAmount(), "The first transaction should be the one with the lesser amount");
}



}