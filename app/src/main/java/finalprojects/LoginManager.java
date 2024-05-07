package finalprojects;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

/*
 * LoginManager handles all username passed in and convert to the csvpath
 */
public class LoginManager {
    private static final String DIRECTORY_PATH = "userdata/"; 
    

    /**
     * Logs in a user by ensuring their specific CSV file exists in the userdata directory. If the file does not exist, it creates one.
     * This method is responsible for constructing the path to the user's CSV file based on their username and ensuring that all
     * necessary directories exist.
     *
     * @param username The username of the user to log in. This username is used to construct the filename of the CSV.
     * @return Path The path to the CSV file associated with the user.
     * @throws IOException If there is an issue creating the directories or the file itself.
     * @throws IllegalArgumentException If the username is null or empty.
     */
    public static Path login(String username) throws IOException {
        Path dirPath = Paths.get(DIRECTORY_PATH);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        String filename = username + ".csv";
        Path csvPath = Paths.get(DIRECTORY_PATH, filename); // Build the path object

        if (!Files.exists(csvPath)) {
            // If the file does not exist, create a new file
            Files.createFile(csvPath);
            System.out.println("New account created for username: " + username);
        } else {
            // If the file exists, just notify that we are loading the existing file
            System.out.println("Existing account loaded for username: " + username);
        }

        return csvPath;
    }



}


