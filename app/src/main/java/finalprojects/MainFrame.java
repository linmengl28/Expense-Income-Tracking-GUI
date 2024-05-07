package finalprojects;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Menglin Lin
 * The MainFrame class represents the main GUI frame of the application.
 * It provides buttons for accessing income/expense tracking, generating reports, and accessing settings.
 */

public class MainFrame extends JFrame {
    private MemberManager MM;

    /**
     * Constructs a new MainFrame with the specified MemberManager.
     * Initializes the frame and its components.
     *
     * @param MM The MemberManager instance to be used by the MainFrame.
     */
    public MainFrame(MemberManager MM) {
        this.MM = MM;
        setTitle("Main Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);


        // Create buttons
        JButton leftButton = new JButton("Income/Expense Tracking");
        leftButton.setPreferredSize(new Dimension(150,50));
        JButton rightButton = new JButton("Report");
        rightButton.setPreferredSize(new Dimension(150,50));

        // Add action listeners
        leftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open new frame on left button click
                openTrackingFrame();
            }
        });
        rightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open new frame on right button click
                openReportFrame();
            }
        });

        // Create panel to hold buttons
        JPanel panel = new JPanel(new GridLayout(1,2));
        panel.add(leftButton);
        panel.add(rightButton);

        // Add panel to the frame
        getContentPane().add(panel, BorderLayout.CENTER);

        // Create and add the Settings button
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSettingsPopup();
            }
        });
        getContentPane().add(settingsButton, BorderLayout.SOUTH);
    }

    /**
     * Helper method for opening tracking frame
     */
    private void openTrackingFrame() {
        // Create instances of required managers
        TransactionsManager transactionsManager = new TransactionsManager(MM);

        // Create the tracking frame
        TrackingFrame trackingGui = new TrackingFrame(transactionsManager, MM);

        // Set the default close operation to dispose the frame instead of exiting
        trackingGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make the tracking frame visible
        trackingGui.setVisible(true);
    }

    /**
     * Helper method for opening report frame
     */
    private void openReportFrame(){
        // Create an instance of ReportingFrame
        ReportingFrame reportingFrame = new ReportingFrame(MM);

        // Set the default close operation to dispose the frame instead of exiting
        reportingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make the FinReporting frame visible
        reportingFrame.setVisible(true);
    }

    /**
     * helper method for opening settings
     */
    private void openSettingsPopup() {
        // Create a popup window for settings
        JDialog settingsDialog = new JDialog(this, "Settings", true);
        settingsDialog.setSize(300, 150);
        settingsDialog.setLocationRelativeTo(this);

        // Create button to add member
        JButton addButton = new JButton("Add Member");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddMemberPopup(settingsDialog);
            }
        });
        settingsDialog.add(addButton, BorderLayout.NORTH);

        // Show the dialog
        settingsDialog.setVisible(true);
    }

    /**
     * helper method for settings: add member
     * @param parentDialog new JDialog
     */
    private void openAddMemberPopup(JDialog parentDialog) {
        // Create a popup window for adding a member
        JDialog addMemberDialog = new JDialog(parentDialog, "Add Member", true);
        addMemberDialog.setSize(300, 100);
        addMemberDialog.setLocationRelativeTo(parentDialog);

        // Create text field for user input
        JTextField memberNameField = new JTextField(20);
        addMemberDialog.add(memberNameField);

        // Create button to save member
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve member name from text field
                String memberName = memberNameField.getText().trim();
                if (!memberName.isEmpty()) {
                    // Check if member already exists
                    if (MM.findMemberByName(memberName) == null){
                        MM.addMemberToList(new Member(memberName));
                        addMemberDialog.dispose();
                    }else {
                        JOptionPane.showMessageDialog(addMemberDialog, "Existing member name!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(addMemberDialog, "Please enter a member name!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addMemberDialog.add(saveButton, BorderLayout.SOUTH);

        // Show the dialog
        addMemberDialog.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemberManager memberManager = new MemberManager();
            // Ask user to input username
            String username = JOptionPane.showInputDialog(null, "Enter your username:");

            // If username is not empty and user clicked OK
            if (username != null && !username.isEmpty()) {
                UserData.getInstance().setUsername(username);
                // Switch to main frame
                MainFrame mainFrame = new MainFrame(memberManager);
                mainFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Username input cancelled. Exiting application.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
