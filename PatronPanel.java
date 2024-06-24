import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronPanel extends JPanel {

    private PatronOperations patronOperations;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaCombo;

    public PatronPanel(PatronOperations patronOperations, JFrame mainFrame) {
        this.patronOperations = patronOperations;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initUI();
    }

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

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        searchField = new JTextField();
        searchCriteriaCombo = new JComboBox<>(new String[]{"Name", "Phone", "Address", "ID"});
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchCriteriaCombo, BorderLayout.WEST);
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
                performSearch();
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

            JTextField nameField = new JTextField(name);
            JTextField phoneField = new JTextField(phone);
            JTextField addressField = new JTextField(address);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Phone:"));
            panel.add(phoneField);
            panel.add(new JLabel("Address:"));
            panel.add(addressField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Patron", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                String newPhoneText = phoneField.getText().trim();
                String newAddress = addressField.getText().trim();

                // Check if any field is empty
                if (newName.isEmpty() || newAddress.isEmpty() || newPhoneText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validate phone number
                int newPhone;
                try {
                    newPhone = Integer.parseInt(newPhoneText);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(panel, "Invalid phone number. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    patronOperations.updatePatron(patronId, newName, newPhone, newAddress);
                    tableModel.setValueAt(newName, selectedRow, 1);
                    tableModel.setValueAt(String.valueOf(newPhone), selectedRow, 2);
                    tableModel.setValueAt(newAddress, selectedRow, 3);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(panel, "Error occurred while updating patron: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
            String name = nameField.getText().trim();
            String phoneText = phoneField.getText().trim();
            String address = addressField.getText().trim();

            // Check if any field is empty
            if (name.isEmpty() || address.isEmpty() || phoneText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number
            int phone;
            try {
                phone = Integer.parseInt(phoneText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panel, "Invalid phone number. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                patronOperations.addPatron(name, phone, address);
                loadPatronData(); // Refresh patron data
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(panel, "Error occurred while adding patron: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Searches for patrons in the database matching the query and updates the table model.
     *
     */
    private void performSearch() {
        String query = searchField.getText().trim();
        String selectedCriteria = (String) searchCriteriaCombo.getSelectedItem();

        try {
            ResultSet rs = patronOperations.searchPatron(query, selectedCriteria);
            displaySearchResults(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displaySearchResults(ResultSet rs) throws SQLException {
        tableModel.setRowCount(0); // Clear existing table data
        while (rs.next()) {
            tableModel.addRow(new Object[]{
                    rs.getInt("id_patron"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("address")
            });
        }
    }
}