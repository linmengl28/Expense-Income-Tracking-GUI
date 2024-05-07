package finalprojects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserDataTest {

    @Test
    public void testGetInstance() {
        // Test if getInstance() method returns the same instance
        UserData instance1 = UserData.getInstance();
        UserData instance2 = UserData.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetSetUsername() {
        // Test getUsername() and setUsername() methods
        UserData userData = UserData.getInstance();
        String username = "testUser";
        userData.setUsername(username);
        assertEquals(username, userData.getUsername());
    }
}
