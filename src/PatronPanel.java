import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The PatronPanel class represents a panel in a GUI application that displays patron data
 * in a table and allows for basic operations such as adding, deleting, updating, and
 * searching patron data.
 */
public class PatronPanel extends JPanel {

    private PatronOperations patronOperations;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Constructs a PatronPanel with the specified patron operations and main frame.
     *
     * @param patronOperations the patron operations to use for interacting with the database
     * @param mainFrame the main frame of the application
     */
    public PatronPanel(PatronOperations patronOperations, JFrame mainFrame) {
        this.patronOperations = patronOperations;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    /**
     * Initializes the user interface components of the panel.
     */
    private void initUI() {
        tableModel = new DefaultTableModel(new Object[]{"Patron ID", "Name", "Phone", "Address"}, 0);
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        loadPatronData();

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
        JButton searchButton = new JButton("Search");

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        controlPanel.add(searchPanel, BorderLayout.NORTH);

        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewPatron();
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
                ((JFrame) SwingUtilities.getWindowAncestor(PatronPanel.this)).dispose();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPatron(searchField.getText());
            }
        });
    }

    /**
     * Loads patron data from the database into the table model.
     */
    private void loadPatronData() {
        tableModel.setRowCount(0); // Limpar dados existentes
        try {
            ResultSet rs = patronOperations.getAllPatrons();
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_patron"), rs.getString("nome"), rs.getString("phone"), rs.getString("address")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected row from the table and the corresponding patron from the database.
     */
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int patronId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                patronOperations.deletePatron(patronId);
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                if (e.getMessage().contains("Cannot delete patron because he/she is currently loaning a book.")) {
                    JOptionPane.showMessageDialog(this, "Error: Cannot delete patron because he/she is currently loaning a book.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error: An unexpected error occurred while deleting the book.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Updates the selected row in the table and the corresponding patron in the database.
     */
    private void updateSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int patronId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            String phone = (String) tableModel.getValueAt(selectedRow, 2);
            String address = (String) tableModel.getValueAt(selectedRow, 3);
            try {
                patronOperations.updatePatron(patronId, name, phone, address);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new patron to the database and refreshes the table data.
     */
    private void addNewPatron() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Patron", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            try {
                patronOperations.addPatron(name, phone, address);
                loadPatronData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Searches for patrons in the database matching the query and updates the table model.
     *
     * @param query the search query
     */
    private void searchPatron(String query) {
        try {
            ResultSet rs = patronOperations.searchPatron(query);
            tableModel.setRowCount(0); // Limpar dados existentes
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_patron"), rs.getString("nome"), rs.getString("phone"), rs.getString("address")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}