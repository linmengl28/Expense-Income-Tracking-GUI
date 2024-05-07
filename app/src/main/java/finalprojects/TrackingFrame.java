package finalprojects;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.YearMonth;
import java.util.List;

import org.jdesktop.swingx.JXDatePicker;

/**
 * @author Menglin Lin
 *  The TrackingFrame class represents the main GUI frame of the application.
 *  It provides table display, filter/sort and adding,updating and deleting transaction functions.
 */
public class TrackingFrame extends JFrame {
    private TransactionsManager TM;
    private MemberManager MM;
    private JTable transactionTable;
    private JTextField idTextField;
    private JButton IorEButton;
    private JButton memberButton;
    private JButton categoryButton;
    private JTextField amountTextField;
    private JButton filterButton;
    private JButton sortButton;
    private JPopupMenu popupMenuMember;
    private JPopupMenu popupMenuIorE;
    private JPopupMenu popupMenuIncomeCate;
    private JPopupMenu popupMenuExpenseCate;
    private JPopupMenu popupMenuSort;
    private JXDatePicker datePicker;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private double amount;
    private String selectedSort;
    private String selectedIorE;
    private String selectedCategory;
    private String selectedMember;
    //--------------filter & sort attribute-------
    private boolean isSortByAmount;
    private boolean isSortByDate;
    private JXDatePicker filterByDatePicker;
    private Date filterSelectedDate;
    private Date filterSelectedMonth;
    private JXDatePicker filterByMonthPicker;
    private JButton filterByIorEButton;
    private JButton filterByCateButton;
    private JButton filterByMemberButton;
    private JPopupMenu popupMenuMember2;
    private JPopupMenu popupMenuIorE2;
    private JPopupMenu popupMenuIncomeCate2;
    private JPopupMenu popupMenuExpenseCate2;
    private String filterSelectedMember = "";
    private String filterSelectedIorE = "";
    private String filterSelectedCategory = "";

    /**
     * Construct a Tracking Frame
     * @param TM Transaction Manager that keeps the list of transactions
     * @param MM Member Manager that keeps the list of members
     */
    public TrackingFrame(TransactionsManager TM, MemberManager MM) {
        this.TM = TM;
        this.MM = MM;

        // Set up main panel
        setTitle("Income/Expense Tracking");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table for data
        transactionTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        transactionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = transactionTable.getSelectedRow();
                    if (selectedRow != -1) { // If a row is selected
                        // Set corresponding data
                        idTextField.setText(transactionTable.getValueAt(selectedRow, 0).toString());
                        String dateString = transactionTable.getValueAt(selectedRow, 1).toString();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            datePicker.setDate(date);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        IorEButton.setText(transactionTable.getValueAt(selectedRow, 2).toString());
                        selectedIorE = IorEButton.getText().trim();
                        categoryButton.setText(transactionTable.getValueAt(selectedRow, 3).toString());
                        selectedCategory = categoryButton.getText().trim();
                        amountTextField.setText(transactionTable.getValueAt(selectedRow, 4).toString());
                        memberButton.setText(transactionTable.getValueAt(selectedRow, 5).toString());
                        selectedMember = memberButton.getText().trim();
                    }
                }
            }
        });

        // Add table to the EAST position
        add(scrollPane, BorderLayout.CENTER);
        updateTable();

        // ----------------------------Filter & Sort Panel --------------------------
        JPanel filterPanel = new JPanel(new FlowLayout());

        // Filter button implementation...
        JLabel filterLabel = new JLabel("Filter by");
        filterPanel.add(filterLabel);
        filterButton = new JButton("Default");
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterOptionsDialog();
                if (filterByDatePicker.getDate() == null && filterByMonthPicker.getDate() == null && filterSelectedMember.isEmpty()&& filterSelectedCategory.isEmpty()&&filterSelectedIorE.isEmpty()){
                    filterButton.setText("Default");
                }else{
                    filterButton.setText("Customized");
                }
                updateTable();
            }
        });
        filterPanel.add(filterButton);

        // Sort button implementation...
        JLabel sortLabel = new JLabel("Sort by");
        filterPanel.add(sortLabel);
        sortButton = new JButton("Default");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenuSort.show(sortButton, 0, sortButton.getHeight());
            }
        });
        filterPanel.add(sortButton);
        // Create the popup menu
        popupMenuSort = new JPopupMenu();
        JMenuItem menuDefaultSort = new JMenuItem("Default");
        JMenuItem menuDate = new JMenuItem("Date");
        JMenuItem menuAmount = new JMenuItem("Amount");

        ActionListener menuActionListenerSort = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSort = ((JMenuItem) e.getSource()).getText();
                isSortByDate = selectedSort.equals("Date");
                isSortByAmount = selectedSort.equals("Amount");
                sortButton.setText(selectedSort);
                updateTable();
            }
        };

        menuDefaultSort.addActionListener(menuActionListenerSort);
        menuDate.addActionListener(menuActionListenerSort);
        menuAmount.addActionListener(menuActionListenerSort);

        popupMenuSort.add(menuDefaultSort);
        popupMenuSort.add(menuDate);
        popupMenuSort.add(menuAmount);

        // Add filterPanel to the SOUTH position
        add(filterPanel, BorderLayout.NORTH);

        // -------------------------------Input panel----------------------------
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2)); // Vertical layout
        //For capturing ID number
        JLabel idLabel = new JLabel("     ID:");
        idTextField = new JTextField();
        idTextField.setPreferredSize(new Dimension(200, idTextField.getPreferredSize().height));
        inputPanel.add(idLabel);
        inputPanel.add(idTextField);

        //For choosing income or expense
        JLabel IorELabel = new JLabel("     Income/Expense:");
        IorEButton = new JButton("Income/Expense");
        IorEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenuIorE.show(IorEButton, 0, IorEButton.getHeight());
            }
        });
        inputPanel.add(IorELabel);
        inputPanel.add(IorEButton);

        // Create the popup menu
        popupMenuIorE = new JPopupMenu();
        JMenuItem menuIncome = new JMenuItem("Income");
        JMenuItem menuExpense = new JMenuItem("Expense");


        ActionListener menuActionListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIorE = ((JMenuItem) e.getSource()).getText();
                IorEButton.setText(selectedIorE);
            }
        };

        menuIncome.addActionListener(menuActionListener2);
        menuExpense.addActionListener(menuActionListener2);

        popupMenuIorE.add(menuIncome);
        popupMenuIorE.add(menuExpense);


        // For choosing due date
        JLabel dateLabel = new JLabel("     Date:");
        datePicker = new JXDatePicker();
        inputPanel.add(dateLabel);
        inputPanel.add(datePicker);

        // For choosing category
        JLabel categoryLabel = new JLabel("     Category:");
        categoryButton = new JButton("Choose category");
        categoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if the user has selected Income/Expense first
                if (selectedIorE == null) {
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Please choose Income or Expense first.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedIorE.equals("Income")){
                    popupMenuIncomeCate.show(categoryButton, 0, categoryButton.getHeight());
                }
                if (selectedIorE.equals("Expense")){
                    popupMenuExpenseCate.show(categoryButton, 0, categoryButton.getHeight());
                }

            }
        });
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryButton);

        // pop up menu for income category
        popupMenuIncomeCate = new JPopupMenu();
        for (IncomeCategory category : IncomeCategory.values()) {
            JMenuItem menuItem = new JMenuItem(category.toString());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCategory = ((JMenuItem) e.getSource()).getText();
                    categoryButton.setText(selectedCategory);
                }
            });
            popupMenuIncomeCate.add(menuItem);
        }

        // pop up menu for expense category
        popupMenuExpenseCate = new JPopupMenu();
        for (ExpenseCategory category : ExpenseCategory.values()) {
            JMenuItem menuItem = new JMenuItem(category.toString());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCategory = ((JMenuItem) e.getSource()).getText();
                    categoryButton.setText(selectedCategory);
                }
            });
            popupMenuExpenseCate.add(menuItem);
        }


        // For input amount
        JLabel amountLabel = new JLabel("     Amount:");
        amountTextField = new JTextField();
        amountTextField.setPreferredSize(new Dimension(200, amountTextField.getPreferredSize().height));
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        //Selection of member with popup menu
        JLabel memberLabel = new JLabel("     Select Member:");
        memberButton = new JButton("Member name");
        memberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenuMember.show(memberButton, 0, memberButton.getHeight());
            }
        });
        inputPanel.add(memberLabel);
        inputPanel.add(memberButton);

        // Create the popup menu for choosing
        popupMenuMember = new JPopupMenu();
        for (Member member : MM.getMembers()) {
            JMenuItem menuItem = new JMenuItem(member.getName());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedMember = ((JMenuItem) e.getSource()).getText();
                    memberButton.setText(selectedMember);
                }
            });
            popupMenuMember.add(menuItem);
        }

        // Button panel for Add, Update, Delete
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Add button function
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIorE == null || selectedIorE.isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing Income/Expense.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Income/Expense");
                }
                //error handling
                if (datePicker==null || datePicker.getDate() == null){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing Date.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Date.");
                }
                if (amountTextField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing amount");
                }else {
                    try {
                        amount = Double.parseDouble(amountTextField.getText().trim());
                    } catch (NumberFormatException e1){
                        JOptionPane.showMessageDialog(TrackingFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (selectedCategory == null || selectedCategory.isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing category.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing category");
                }
                if (selectedMember == null || selectedMember.isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing member.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Member");
                }
                LocalDate localDate = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                amount = Double.parseDouble(amountTextField.getText().trim());
                int selectedRow = transactionTable.getSelectedRow();
                if (selectedRow != -1) {
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Please do not select a row when using add.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectedIorE.equals("Income")){
                    TM.addTransaction(findIncomeCategory(),amount,localDate,MM.findMemberByName(selectedMember));
                }else if (selectedIorE.equals("Expense")){
                    TM.addTransaction(findExpenseCategory(),amount,localDate,MM.findMemberByName(selectedMember));
                }
                TM.updateCSV(UserData.getInstance().getUsername());
                updateTable();
                idTextField.setText("");
                IorEButton.setText("Income/Expense");
                selectedIorE = "";
                categoryButton.setText("Choose category");
                selectedCategory = "";
                amountTextField.setText("");
                datePicker.setDate(null);
                memberButton.setText("Member name");
                selectedMember = "";
            }
        });
        buttonPanel.add(addButton);

        // update button function
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIorE == null || selectedIorE.isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing Income/Expense.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Income/Expense");
                }
                //error handling
                if (datePicker == null ||datePicker.getDate() == null){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing Date.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Date.");
                }
                if (amountTextField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing amount");
                }else {
                    try {
                        amount = Double.parseDouble(amountTextField.getText().trim());
                    } catch (NumberFormatException e1){
                        JOptionPane.showMessageDialog(TrackingFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (selectedCategory == null || selectedCategory.isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing category.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Category");
                }
                if (selectedMember == null || selectedMember.isEmpty()){
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Missing member.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("Missing Member");
                }
                LocalDate localDate = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                amount = Double.parseDouble(amountTextField.getText().trim());
                int selectedRow = transactionTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(transactionTable.getValueAt(selectedRow, 0).toString());
                if (selectedIorE.equals("Income")){
                    TM.setTransaction(id,findIncomeCategory(),amount,localDate,MM.findMemberByName(selectedMember));
                }else if (selectedIorE.equals("Expense")){
                    TM.setTransaction(id,findExpenseCategory(),amount,localDate,MM.findMemberByName(selectedMember));
                }
                TM.updateCSV(UserData.getInstance().getUsername());
                updateTable();
                idTextField.setText("");
                IorEButton.setText("Income/Expense");
                selectedIorE = "";
                categoryButton.setText("Choose category");
                selectedCategory = "";
                amountTextField.setText("");
                datePicker.setDate(null);
                memberButton.setText("Member name");
                selectedMember = "";
            }
        });
        buttonPanel.add(updateButton);

        // delete button function
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = transactionTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(TrackingFrame.this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(transactionTable.getValueAt(selectedRow, 0).toString());
                TM.removeTransaction(id);
                TM.updateCSV(UserData.getInstance().getUsername());
                updateTable();
                idTextField.setText("");
                IorEButton.setText("Income/Expense");
                selectedIorE = "";
                categoryButton.setText("Choose category");
                selectedCategory = "";
                amountTextField.setText("");
                datePicker.setDate(null);
                memberButton.setText("Member name");
                selectedMember = "";
            }
        });
        buttonPanel.add(deleteButton);

        // Add inputPanel to the WEST position
        add(inputPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    /**
     * helper method for filtering
     */
    private void showFilterOptionsDialog() {
        // Create a dialog to show filter options
        JDialog dialog = new JDialog(this, "Filter Options", true);
        dialog.setLayout(new GridLayout(6, 2));

        // -------------------filter by date/month----------------------
        JLabel filterByDateLabel = new JLabel("Filter by Date");
        dialog.add(filterByDateLabel);
        filterByDatePicker = new JXDatePicker();
        if (filterSelectedDate != null){
            filterByDatePicker.setDate(filterSelectedDate);
        }
        filterByDatePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable the filterByMonthPicker when a date is selected
                filterByMonthPicker.setEnabled(false);
            }
        });
        dialog.add(filterByDatePicker);

        JLabel filterByMonthLabel = new JLabel("Filter by Month");
        dialog.add(filterByMonthLabel);
        filterByMonthPicker = new JXDatePicker();
        filterByMonthPicker.setFormats("MM-yyyy");
        if (filterSelectedMonth != null){
            filterByMonthPicker.setDate(filterSelectedMonth);
        }
        filterByMonthPicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable the filterByDatePicker when a month is selected
                filterByDatePicker.setEnabled(false);
            }
        });
        dialog.add(filterByMonthPicker);

        // --------------------filter by member ------------
        JLabel filterByMemberLabel = new JLabel("Filter by Member");
        filterByMemberButton = new JButton();
        if (filterSelectedMember.isEmpty()){
            filterByMemberButton.setText("Member name");
        } else {
            filterByMemberButton.setText(filterSelectedMember);
        }
        filterByMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenuMember2.show(filterByMemberButton, 0, filterByMemberButton.getHeight());
            }
        });
        dialog.add(filterByMemberLabel);
        dialog.add(filterByMemberButton);

        // Create the popup menu for choosing
        popupMenuMember2 = new JPopupMenu();
        for (Member member : MM.getMembers()) {
            JMenuItem menuItem = new JMenuItem(member.toString());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filterSelectedMember = ((JMenuItem) e.getSource()).getText();
                    filterByMemberButton.setText(filterSelectedMember);
                }
            });
            popupMenuMember2.add(menuItem);
        }

        // ------------------filter by Income or Expense-----------------------
        JLabel filterByIorELabel = new JLabel("Filter by Income/Expense");
        filterByIorEButton = new JButton();
        if (filterSelectedIorE.isEmpty()){
            filterByIorEButton.setText("Income/Expense");
        } else {
            filterByIorEButton.setText(filterSelectedIorE);
        }
        filterByIorEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenuIorE2.show(filterByIorEButton, 0, filterByIorEButton.getHeight());
            }
        });
        dialog.add(filterByIorELabel);
        dialog.add(filterByIorEButton);

        // Create the popup menu
        popupMenuIorE2 = new JPopupMenu();
        JMenuItem menuIncome = new JMenuItem("Income");
        JMenuItem menuExpense = new JMenuItem("Expense");


        ActionListener menuActionListener3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSelectedIorE = ((JMenuItem) e.getSource()).getText();
                filterByIorEButton.setText(filterSelectedIorE);
            }
        };

        menuIncome.addActionListener(menuActionListener3);
        menuExpense.addActionListener(menuActionListener3);

        popupMenuIorE2.add(menuIncome);
        popupMenuIorE2.add(menuExpense);


        // -----------------------------filter by category-----------------------------
        JLabel filterByCateLabel = new JLabel("Filter by Category");
        filterByCateButton = new JButton();
        if (filterSelectedCategory.isEmpty()){
            filterByCateButton.setText("Choose category");
        } else {
            filterByCateButton.setText(filterSelectedCategory);
        }
        filterByCateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterSelectedIorE.equals("Income")){
                    popupMenuIncomeCate2.show(filterByCateButton, 0, filterByCateButton.getHeight());
                }
                if (filterSelectedIorE.equals("Expense")){
                    popupMenuExpenseCate2.show(filterByCateButton, 0, filterByCateButton.getHeight());
                }

            }
        });
        dialog.add(filterByCateLabel);
        dialog.add(filterByCateButton);

        // pop up menu for income category
        popupMenuIncomeCate2 = new JPopupMenu();
        for (IncomeCategory category : IncomeCategory.values()) {
            JMenuItem menuItem = new JMenuItem(category.toString());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filterSelectedCategory = ((JMenuItem) e.getSource()).getText();
                    filterByCateButton.setText(filterSelectedCategory);
                }
            });
            popupMenuIncomeCate2.add(menuItem);
        }

        // pop up menu for expense category
        popupMenuExpenseCate2 = new JPopupMenu();
        for (ExpenseCategory category : ExpenseCategory.values()) {
            JMenuItem menuItem = new JMenuItem(category.toString());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filterSelectedCategory = ((JMenuItem) e.getSource()).getText();
                    filterByCateButton.setText(filterSelectedCategory);
                }
            });
            popupMenuExpenseCate2.add(menuItem);
        }

        // -------------------Clear button------------
        JButton clearButton = new JButton("Clear Filters");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset all filter selections to default state
                filterByDatePicker.setDate(null);
                filterByMonthPicker.setDate(null);
                filterByCateButton.setText("Choose category");
                filterSelectedCategory = "";
                filterByIorEButton.setText("Income/Expense");
                filterSelectedIorE = "";
                filterByMemberButton.setText("Member name");
                filterSelectedMember = "";
            }
        });
        dialog.add(clearButton);

        //------------------OK button-----------------
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement filtering based on user selection
                System.out.println("Filtering...");
                dialog.dispose(); // Close the dialog after selection
            }
        });
        dialog.add(okButton);

        dialog.pack();
        dialog.setLocationRelativeTo(this); // Center the dialog relative to the main frame
        dialog.setVisible(true);

        // Save selected dates when dialog is closed
        filterSelectedDate = filterByDatePicker.getDate();
        filterSelectedMonth = filterByMonthPicker.getDate();
    }


    /**
     * helper method for finding income category
     * @return IncomeCategory
     */
    private IncomeCategory findIncomeCategory(){
        for (IncomeCategory c: IncomeCategory.values()){
            if (c.toString().equals(selectedCategory)){
                return c;
            }
        }
        return null;
    }

    /**
     * helper method for finding expense category
     * @return ExpenseCategory
     */
    private ExpenseCategory findExpenseCategory(){
        for (ExpenseCategory c: ExpenseCategory.values()){
            if (c.toString().equals(selectedCategory)){
                return c;
            }
        }
        return null;
    }

    /**
     * helper method for updating display table
     */
    private void updateTable() {
        // Get all tasks from TransactionManager
        TM.loadTransactionsFromCSV();
        List<Transaction> transactions;
        LocalDate filterByDate = null;
        YearMonth filterByMonth = null;
        if (filterByDatePicker != null && filterByDatePicker.getDate() != null){
            filterByDate = filterByDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (filterByMonthPicker != null && filterByMonthPicker.getDate() != null){
            LocalDate selectedDate = filterByMonthPicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            filterByMonth = YearMonth.of(selectedDate.getYear(), selectedDate.getMonth());
        }

        if (filterSelectedIorE.equals("Income")) {
            transactions = TM.displayTransactions(filterByDate, filterByMonth, Transaction.convertStrToCategory(filterSelectedCategory), MM.findMemberByName(filterSelectedMember), Income.class, isSortByAmount, isSortByDate);
        } else if (filterSelectedIorE.equals("Expense")) {
            transactions = TM.displayTransactions(filterByDate, filterByMonth, Transaction.convertStrToCategory(filterSelectedCategory), MM.findMemberByName(filterSelectedMember), Expense.class, isSortByAmount, isSortByDate);
        } else {
            transactions = TM.displayTransactions(filterByDate, filterByMonth, null, MM.findMemberByName(filterSelectedMember), (Class<? extends Transaction>) null, isSortByAmount, isSortByDate);
        }

        // Create a 2D array to store task data
        Object[][] data = new Object[transactions.size()][6];
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction= transactions.get(i);
            data[i][0] = transaction.getId();
            data[i][1] = transaction.getDate();
            data[i][2] = transaction.getClass().getSimpleName();
            data[i][3] = transaction.getCategory();
            data[i][4] = transaction.getAmount();
            data[i][5] = transaction.getMember();
        }
        String[] columns = {"ID", "Date","Income/Expense", "Category", "Amount",  "Member"};
        // Create a table model with data and column names
        DefaultTableModel model = new DefaultTableModel(data, columns);
        transactionTable.setModel(model);
    }
}
