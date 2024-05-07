package finalprojects;

/**
 * @author MenglinLin
 */

/**
 * UserDate is for storing the username infomration
 */ 
public class UserData {
    private static UserData instance;
    private String username;

    /**
     * Private constructor to prevent instantiation from outside the class. Ensures the singleton pattern is maintained.
     */
    private UserData() {
        
    }

    /**
     * Provides access to the singleton instance of the UserData class. If the instance doesn't exist, it initializes it.
     * This method is thread-safe to prevent multiple instantiation in a multi-threaded environment.
     *
     * @return The single, static instance of UserData.
     */
    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The current username stored in this UserData instance.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the username stored in the UserData instance.
     *
     * @param username The new username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
}

