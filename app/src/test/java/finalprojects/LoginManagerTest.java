package finalprojects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class LoginManagerTest {

    @Test
    public void testLogin() throws IOException {
        LoginManager lm = new LoginManager();
        String username = "testuser";
        Path csvPath = LoginManager.login(username);

        // Check if the csvPath exists
        assertTrue(Files.exists(csvPath));

        // Check if the csvPath points to the correct directory
        Path expectedDirPath = Paths.get("userdata");
        assertEquals(expectedDirPath, csvPath.getParent());

        // Check if the csvPath points to the correct filename
        Path expectedFilePath = Paths.get("userdata", username + ".csv");
        assertEquals(expectedFilePath, csvPath);

        Path csvPath2 = LoginManager.login(username);
        // Clean up: Delete the created file
        Files.deleteIfExists(csvPath);
    }
}
