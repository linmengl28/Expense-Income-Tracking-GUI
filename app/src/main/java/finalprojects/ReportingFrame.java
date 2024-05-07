package finalprojects;

/**
 * @author Jingjing Ji
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

/**
 * Class for Reporting frame for visualized financial reporting and prediction.
 */
public class ReportingFrame extends JFrame {
    private JComboBox<String> timePeriodComboBox;
    private JTabbedPane tabbedPane;
    private JPanel historicalChartPanel;
    private JPanel predictionChartPanel;

    public MemberManager MM;
    public TransactionsManager TM;
    public Member member;

    /**
     * Constructor for Reporting frame
     * @param MM MemberManager
     */
    public ReportingFrame(MemberManager MM) {
        this.MM = MM;
        TM = new TransactionsManager(this.MM);


        setTitle("Financial Reporting");
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JPanel controlPanel = createControlPanel();


        //Create Panels within the two tabs
        historicalChartPanel = new JPanel(new BorderLayout());
        predictionChartPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Historical", historicalChartPanel);
        tabbedPane.addTab("Prediction", predictionChartPanel);

        // Add the tabbed pane to the center of the main panel
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Add the control panel to the south of the main panel
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); 
        setResizable(true);
        setMinimumSize(new Dimension(1550, 1000));

        // Initially load historical charts for default time period
        loadHistoricalCharts("3 month");

        // Load prediction chart
        loadPredictionChart();

    }

    /**
     * Create the panel with drop-down list of date range
     * @return panel
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] timePeriods = {"3 months", "6 months", "annual"};
        timePeriodComboBox = new JComboBox<>(timePeriods);
        timePeriodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPeriod = (String) timePeriodComboBox.getSelectedItem();
                loadHistoricalCharts(selectedPeriod);
            }
        });

        return panel;
    }
    
    /**
     * load or refresh charts for historical reporting tab.
     * @param timePeriod user-selected time range.
     */
    private void loadHistoricalCharts(String timePeriod) {

        historicalChartPanel.removeAll();
        historicalChartPanel.setLayout(new BorderLayout()); 

        // Create a panel for the drop-down button at the top center
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(new JLabel("Select Time Period:"));
        controlPanel.add(timePeriodComboBox);

        // Add the control panel to the top center of the historicalChartPanel
        historicalChartPanel.add(controlPanel, BorderLayout.NORTH);

        // Create a panel for the charts
        JPanel chartPanel = new JPanel(new GridLayout(2, 2)); // Set the layout to a 2x2 grid

        // Add the charts to the chart panel
        chartPanel.add(newLineChart(timePeriod, "Historical Financial Trend", "MonthYear", "Amount ($)"));
        chartPanel.add(newPieChart("Income", timePeriod));
        chartPanel.add(newPieChart("Expense", timePeriod));
        chartPanel.add(newBarChart(timePeriod,"Income vs Expense By Member", "Member", "Amount($)"));

        // Add the chart panel to the center of the historicalChartPanel
        historicalChartPanel.add(chartPanel, BorderLayout.CENTER);

        // Update UI
        historicalChartPanel.revalidate();
        historicalChartPanel.repaint();
    }

    /**
     * Load or refresh the prediction bar chart in prediction tab
     */
    private void loadPredictionChart() {
        // Clear existing prediction chart
        predictionChartPanel.removeAll();

        // Generate/refresh prediction chart
        predictionChartPanel.add(newBarChart("future 1 Month Prediction", "MonthYear", "Amount ($)"));

        // Update UI
        predictionChartPanel.revalidate();
        predictionChartPanel.repaint();
    }

    
    /**
     * Create a line chart for reporting by income and expense for user-selected time range.
     * @param timePeriod user-selected time range
     * @param title Historical trend
     * @param xaxis Amount of expense and income
     * @param yaxis Month Year
     * @return line chart
     */
    private JPanel newLineChart(String timePeriod, String title, String xaxis, String yaxis) {
        JPanel panel = new JPanel();
        JFreeChart chart = ChartFactory.createLineChart(title, xaxis, yaxis, createLineDataset(timePeriod));
        ChartPanel chartPanel = new ChartPanel(chart) { 
            public Dimension getPreferredSize() {
                return new Dimension(850, 400);
            }
        };
        panel.add(chartPanel);
        return panel;
    }
    
    /**
     * Prepare dataset for creating a line chart to report for user-selected time range.
     * @param timePeriod user-selected time range
     * @return line chart
     */
    private DefaultCategoryDataset createLineDataset(String timePeriod) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        // Define start date and end date based on the selected time period
        LocalDate[] range = getDateRange(timePeriod);
        LocalDate startDate = range[0], endDate = range[1];
    
        // Initialize maps to store aggregated amounts for income and expenses
        TreeMap<YearMonth, Double> incomeMap = new TreeMap<>();
        TreeMap<YearMonth, Double> expenseMap = new TreeMap<>();
    
        // Display income transactions and aggregate by month-year
        TM.loadTransactionsFromCSV();
        List<Transaction> incomes = TM.displayTransactionsbetweendate(startDate, endDate, null, null, true, false, true);

        for (Transaction income : incomes) {
            YearMonth yearMonth = YearMonth.from(income.getDate());
            // String monthYear = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
            double amount = income.getAmount();
            incomeMap.put(yearMonth, incomeMap.getOrDefault(yearMonth, 0.0) + amount);
        }
    
        // Display expense transactions and aggregate by month-year
        TM.loadTransactionsFromCSV();
        List<Transaction> expenses = TM.displayTransactionsbetweendate(startDate, endDate, null, null, false, false, true);

        for (Transaction expense : expenses) {
            YearMonth yearMonth = YearMonth.from(expense.getDate());
            // String monthYear = yearMonth.getMonth().toString() + " " + yearMonth.getYear();
            double amount = expense.getAmount();
            expenseMap.put(yearMonth, expenseMap.getOrDefault(yearMonth, 0.0) + amount);
        }

        YearMonth start = incomeMap.firstKey();
        if(start.isAfter(expenseMap.firstKey())) {
            start = expenseMap.firstKey();
        }
        YearMonth end = incomeMap.lastKey();
        if(end.isBefore(expenseMap.lastKey())) {
            end = expenseMap.lastKey();
        }
    
        for(YearMonth curMonth = start; curMonth.isBefore(end.plusMonths(1));) {
            if(incomeMap.containsKey(curMonth)) {
                dataset.addValue(incomeMap.get(curMonth), "Income", curMonth);
            }
            else {
                dataset.addValue(0.0, "Income", curMonth);
            }

            if(expenseMap.containsKey(curMonth)) {
                dataset.addValue(expenseMap.get(curMonth), "Expense", curMonth);
            }
            else {
                dataset.addValue(0.0, "Expense", curMonth);
            }

            curMonth = curMonth.plusMonths(1);
        }
    
        return dataset;
    }    


    /**
     * Create a Pie chart for reporting by category for user-selected time range.
     * @param type Income or Expense
     * @param timePeriod user-selected time range
     * @return pie chart
     */
    private JPanel newPieChart(String type, String timePeriod) {
        JPanel panel = new JPanel();
        JFreeChart chart = ChartFactory.createPieChart("Breakdown by " + type,
            createPieDataset(timePeriod, type),
            true, // lengend
            true, // tootips
            false // URL
        );

        ChartPanel chartPanel = new ChartPanel(chart) { 
            public Dimension getPreferredSize() {
                return new Dimension(500, 400);
            }
        };

        panel.add(chartPanel);
        return panel;
    }
    
    /**
     * Prepare dataset for pie chart to report breakdown of expense or income by category.
     * @param timePeriod user-selected time range.
     * @param type category type
     * @return pie chart
     */
    private PieDataset createPieDataset(String timePeriod, String type) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Calculate the start date and end date based on the selected time period
        LocalDate[] range = getDateRange(timePeriod);
        LocalDate startDate = range[0], endDate = range[1];
    
        // Fetch expense transactions within the selected time period
        List<Transaction> transactions;
        if (type.equals("Expense")) {
            TM.loadTransactionsFromCSV();
            transactions = TM.displayTransactionsbetweendate(startDate, endDate, null, null, false, false, true);
            // Group expenses by category and sum the amounts
            Map<ExpenseCategory, Double> categoryAmountMap = new HashMap<>();
            for (Transaction expense : transactions) {
                ExpenseCategory category = (ExpenseCategory) expense.getCategory();
                double amount = expense.getAmount();
                categoryAmountMap.put(category, categoryAmountMap.getOrDefault(category, 0.0) + amount);
            }

            // Add data to the dataset
            for (Map.Entry<ExpenseCategory, Double> entry : categoryAmountMap.entrySet()) {
                dataset.setValue(entry.getKey().toString(), entry.getValue());
            }
        }
        else if(type.equals("Income")) {
            TM.loadTransactionsFromCSV();
            transactions = TM.displayTransactionsbetweendate(startDate, endDate, null, null, true, false, true);
            // Group expenses by category and sum the amounts
            Map<IncomeCategory, Double> categoryAmountMap = new HashMap<>();
            for (Transaction expense : transactions) {
                IncomeCategory category = (IncomeCategory) expense.getCategory();
                double amount = expense.getAmount();
                categoryAmountMap.put(category, categoryAmountMap.getOrDefault(category, 0.0) + amount);
            }

            // Add data to the dataset
            for (Map.Entry<IncomeCategory, Double> entry : categoryAmountMap.entrySet()) {
                dataset.setValue(entry.getKey().toString(), entry.getValue());
            }
        }
        else {
            throw new IllegalArgumentException("Type must be 'Income' or 'Expense'");
        }    
        return dataset;
    }
     
    /**
     * Creating a bar chart to REPORT on income/expense for each member f
     * @param title Income vs Expense by Member
     * @param xaxis Member
     * @param yaxis Amount of income and expense
     * @return bar chart
     */
    private JPanel newBarChart(String timePeriod, String title, String xaxis, String yaxis) {
        JPanel panel = new JPanel();
        // Create a bar chart
        JFreeChart chart = ChartFactory.createBarChart(title, xaxis, yaxis,createBarDataset(timePeriod));
    
        ChartPanel chartPanel = new ChartPanel(chart) {
            public Dimension getPreferredSize() {
                return new Dimension(500, 400); 
            }
        };
        panel.add(chartPanel);
        return panel;
    }
    
    /**
     * Creating a bar chart to PREDICT on income/expense for each member f
     * @param title Income vs Expense by Member
     * @param xaxis Member
     * @param yaxis Amount of income and expense
     * @return bar chart
     */
    private JPanel newBarChart(String title, String xaxis, String yaxis) {
        JPanel panel = new JPanel();
        // Create a bar chart
        JFreeChart chart = ChartFactory.createBarChart(title, xaxis, yaxis,createPredictionBarDataset() // Make sure this method returns a CategoryDataset
        );
    
        ChartPanel chartPanel = new ChartPanel(chart) {
            public Dimension getPreferredSize() {
                return new Dimension(600, 600); 
            }
        };
        panel.add(chartPanel);
        return panel;
    }

    /**
     * Prepare dataset for creating bar chart in reporting tab.
     * @param timePeriod use-selected time range.
     * @return compatible dataset for bar chart in reporting tab.
     */
    private CategoryDataset createBarDataset(String timePeriod) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
        // Determine the start date and end date based on the user-selected time period
        LocalDate[] range = getDateRange(timePeriod);
        LocalDate startDate = range[0]; 
        LocalDate endDate = range[1];
        
        Map<String, Double> incomeMap = new HashMap<>();
        Map<String, Double> expenseMap = new HashMap<>();
        
        TM.loadTransactionsFromCSV();
        List<Transaction> incomes = TM.displayTransactionsbetweendate(startDate, endDate, null, null, true, false, true);
        TM.loadTransactionsFromCSV();
        List<Transaction> expenses = TM.displayTransactionsbetweendate(startDate, endDate, null, null, false, false, true);
    
        // Summing  income transaction amounts for each member.
        for (Transaction income : incomes) {
            String memberName = income.getMember().getName();
            double amount = income.getAmount();
            incomeMap.put(memberName, incomeMap.getOrDefault(memberName, 0.0) + amount);
        }
    
        // Summing expense transaction amounts for each member.
        for (Transaction expense : expenses) {
            String memberName = expense.getMember().getName();
            double amount = expense.getAmount();
            expenseMap.put(memberName, expenseMap.getOrDefault(memberName, 0.0) + amount);
        }
    
        // Populate the dataset with income and expense for each member
        for (Map.Entry<String, Double> entry : incomeMap.entrySet()) {
            String memberName = entry.getKey();
            double incomeAmount = entry.getValue();
            double expenseAmount = expenseMap.getOrDefault(memberName, 0.0);
            
            // Add data to the dataset
            dataset.addValue(incomeAmount, "Income", memberName);
            dataset.addValue(expenseAmount, "Expense", memberName);
        }    
        return dataset;
    }

    /**
     * To prepare dataset for creating the bar chart in the prediction tab using data from the past three months.
     * @return compatible dataset for prediction bar chart.
     */
    public CategoryDataset createPredictionBarDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Calculate the date range for the past three months
        LocalDate[] range = getDateRangeForPastThreeMonths();
        LocalDate startDate = range[0];
        LocalDate endDate = range[1];

        // Calculate average income and expenses for each member
        List<Member> members = MM.getMembers();
        for (Member member : members) {
            double avgIncome = calculateAverage("Income",startDate, endDate, member);
            double avgExpenses = calculateAverage("Expense", startDate, endDate, member);
            dataset.addValue(avgIncome, "Predicted Income", member.getName());
            dataset.addValue(avgExpenses, "Predicted Expenses", member.getName());
        }
        return dataset;
        
    }

    /**
     * Helper method to get date range for the past three months, this is used for prediction chart.
     * @return date range for the past three months since month.
     */
    static LocalDate[] getDateRangeForPastThreeMonths() {
        LocalDate currentDate = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(currentDate);
        LocalDate firstDayOfThisMonth = currentYearMonth.atDay(1);
        LocalDate endDate = firstDayOfThisMonth.minusDays(1);
        LocalDate startDate = firstDayOfThisMonth.minusMonths(3);
        return new LocalDate[]{startDate, endDate};
    }


    /**
     * Helper method to calculate average of total transaction amount for user-selected time range.
     * @param type
     * @param startDate
     * @param endDate
     * @param member
     * @return average of expense or income
     */
    private double calculateAverage(String type, LocalDate startDate, LocalDate endDate, Member member) {
        List<Transaction> transactions;
        if (type.equals("Expense")) {
            TM.loadTransactionsFromCSV();
            transactions = TM.displayTransactionsbetweendate(startDate, endDate, null, null, false, false, true);

        } else if (type.equals("Income")) {
            TM.loadTransactionsFromCSV();
            transactions = TM.displayTransactionsbetweendate(startDate, endDate, null, null, true, false, true);
        } else {
            throw new IllegalArgumentException("Type must be 'Income' or 'Expense'");
        }
    
        // Calculate sum for transactions amount within the user-selected date range
        double total = transactions.stream()
                .filter(transaction -> !transaction.getDate().isBefore(startDate) &&
                        !transaction.getDate().isAfter(endDate) &&
                        transaction.getMember().equals(member))
                .mapToDouble(Transaction::getAmount)
                .sum();
    
        // Calculate # months from startDate to endDate
        long numberOfMonths = startDate.until(endDate.plusMonths(1)).toTotalMonths();
    
        // Calculate average
        return total / numberOfMonths;
    }

    /**
     * Calculate date range based on user input-selected time range from drop down button.
     * @param timePeriod
     * @return date range for which data will be reported in charts.
     */
    LocalDate[] getDateRange(String timePeriod) {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(now);
        LocalDate firstDayOfThisMonth = currentYearMonth.atDay(1);
        LocalDate endDate = firstDayOfThisMonth.minusDays(1);
        LocalDate startDate;
        switch (timePeriod) {
            case "3 months":
                startDate = firstDayOfThisMonth.minusMonths(3);
                break;
            case "6 months":
                startDate = firstDayOfThisMonth.minusMonths(6);
                break;
            case "annual":
                startDate = firstDayOfThisMonth.minusYears(1);
                break;
            default:
                startDate = firstDayOfThisMonth.minusMonths(3); // Default is 3 months
                break;
        }

        return new LocalDate[]{startDate, endDate};
    }

}
