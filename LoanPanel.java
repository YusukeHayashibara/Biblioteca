import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The LoanPanel class represents a panel in a GUI application that displays loan data
 * in a table and allows for basic operations such as adding, deleting, updating, and
 * searching loan data.
 */
public class LoanPanel extends JPanel {

    private LoanOperation loanOperations;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Constructs a LoanPanel with the specified loan operations and main frame.
     *
     * @param loanOperations the loan operations to use for interacting with the database
     * @param mainFrame the main frame of the application
     */
    public LoanPanel(LoanOperation loanOperations, JFrame mainFrame) {
        this.loanOperations = loanOperations;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    /**
     * Initializes the user interface components of the panel.
     */
    private void initUI() {
        tableModel = new DefaultTableModel(new Object[]{"Book ID", "Patron ID", "Loan Date", "Return Date", "Returned"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        DefaultTableCellRenderer centeredCheckBoxRenderer = new DefaultTableCellRenderer() {
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                checkBox.setSelected((Boolean) value);
                checkBox.setHorizontalAlignment(JLabel.CENTER);
                if (isSelected) {
                    checkBox.setBackground(table.getSelectionBackground());
                } else {
                    checkBox.setBackground(table.getBackground());
                }
                return checkBox;
            }
        };

        loadLoanData();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton backButton = new JButton("Back");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search by book ID");

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        controlPanel.add(searchPanel, BorderLayout.NORTH);

        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewLoan();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedRow();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(LoanPanel.this)).dispose();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchLoan(searchField.getText());
            }
        });
    }

    /**
     * Loads loan data from the database into the table model.
     */
    private void loadLoanData() {
        tableModel.setRowCount(0); // Limpar dados existentes
        try {
            ResultSet rs = loanOperations.getAllLoans();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_book"),
                        rs.getInt("id_patron"),
                        rs.getDate("loan_date"),
                        rs.getDate("return_date"),
                        rs.getBoolean("returned")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected row from the table and the corresponding loan from the database.
     */
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            int patronId = (int) tableModel.getValueAt(selectedRow, 1);
            Date loanDate = (Date) tableModel.getValueAt(selectedRow, 2);
            try {
                loanOperations.deleteLoan(bookId, patronId, loanDate);
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the selected row in the table and the corresponding loan in the database.
     */
    private void updateSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int currentBookId = (int) tableModel.getValueAt(selectedRow, 0);
            int currentPatronId = (int) tableModel.getValueAt(selectedRow, 1);
            Date currentLoanDate = (Date) tableModel.getValueAt(selectedRow, 2);
            Date currentReturnDate = (Date) tableModel.getValueAt(selectedRow, 3);
            boolean currentReturned = (boolean) tableModel.getValueAt(selectedRow, 4);

            JTextField bookIdField = new JTextField(String.valueOf(currentBookId));
            JTextField patronIdField = new JTextField(String.valueOf(currentPatronId));
            JTextField loanDateField = new JTextField(String.valueOf(currentLoanDate));
            JTextField returnDateField = new JTextField(String.valueOf(currentReturnDate));
            JCheckBox returnedCheckBox = new JCheckBox("Returned", currentReturned);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Book ID:"));
            panel.add(bookIdField);
            panel.add(new JLabel("Patron ID:"));
            panel.add(patronIdField);
            panel.add(new JLabel("Loan Date:"));
            panel.add(loanDateField);
            panel.add(new JLabel("Return Date:"));
            panel.add(returnDateField);
            panel.add(returnedCheckBox);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Loan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                // Parse input fields
                int newBookId = Integer.parseInt(bookIdField.getText());
                int newPatronId = Integer.parseInt(patronIdField.getText());
                Date newLoanDate = Date.valueOf(loanDateField.getText());
                Date newReturnDate = Date.valueOf(returnDateField.getText());
                boolean newReturned = returnedCheckBox.isSelected();

                try {
                    // Check if update is for returned or return date only
                    if (newReturned != currentReturned || !newReturnDate.equals(currentReturnDate)) {
                        loanOperations.updateLoan(currentBookId, currentPatronId, currentLoanDate, newBookId, newPatronId, newLoanDate, newReturnDate, newReturned);
                        tableModel.setValueAt(newBookId, selectedRow, 0);
                        tableModel.setValueAt(newPatronId, selectedRow, 1);
                        tableModel.setValueAt(newLoanDate, selectedRow, 2);
                        tableModel.setValueAt(newReturnDate, selectedRow, 3);
                        tableModel.setValueAt(newReturned, selectedRow, 4);
                    } else {
                        JOptionPane.showMessageDialog(this, "Update restricted to 'returned' status or 'return date'.", "Update Restriction", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Adds a new loan to the database and refreshes the table data.
     */
    private void addNewLoan() {
        JTextField bookIdField = new JTextField();
        JTextField patronIdField = new JTextField();
        JTextField loanDateField = new JTextField();
        JTextField returnDateField = new JTextField();
        JCheckBox returnedCheckBox = new JCheckBox("Returned");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Book ID:"));
        panel.add(bookIdField);
        panel.add(new JLabel("Patron ID:"));
        panel.add(patronIdField);
        panel.add(new JLabel("Loan Date (YYYY-MM-DD):"));
        panel.add(loanDateField);
        panel.add(new JLabel("Return Date (YYYY-MM-DD):"));
        panel.add(returnDateField);
        panel.add(returnedCheckBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Loan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int bookId = Integer.parseInt(bookIdField.getText());
            int patronId = Integer.parseInt(patronIdField.getText());
            Date loanDate = Date.valueOf(loanDateField.getText());
            Date returnDate = returnDateField.getText().isEmpty() ? null : Date.valueOf(returnDateField.getText());
            boolean returned = returnedCheckBox.isSelected();

            try {
                loanOperations.addLoan(bookId, patronId, loanDate, returnDate, returned);
                loadLoanData();
            } catch (SQLException e) {
                if (e.getMessage().contains("The book is already loaned out and not yet returned.")) {
                    JOptionPane.showMessageDialog(this, "Error: The book is already loaned out and not yet returned.", "Loan Error", JOptionPane.ERROR_MESSAGE);
                } else if (e.getMessage().contains("loan_book_fk")) {
                    JOptionPane.showMessageDialog(this, "Error: The specified book does not exist.", "Loan Error", JOptionPane.ERROR_MESSAGE);
                } else if (e.getMessage().contains("loan_patron_fk")) {
                    JOptionPane.showMessageDialog(this, "Error: The specified patron does not exist.", "Loan Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * Searches for loans in the database matching the query and updates the table model.
     *
     * @param query the search query
     */
    private void searchLoan(String query) {
        try {
            ResultSet rs = loanOperations.searchLoan(query);
            tableModel.setRowCount(0); // Limpar dados existentes
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_book"),
                        rs.getInt("id_patron"),
                        rs.getDate("loan_date"),
                        rs.getDate("return_date"),
                        rs.getBoolean("returned")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}