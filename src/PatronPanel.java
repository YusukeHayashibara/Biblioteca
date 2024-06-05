import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PatronPanel extends JPanel {

    private DBOperation dbOperations;
    private Connection connection;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    private TableRowSorter<DefaultTableModel> sorter;

    public PatronPanel(Connection connection, JFrame mainFrame) {
        this.connection = connection;
        this.mainFrame = mainFrame;
        this.dbOperations = new DBOperation();
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

    private void loadPatronData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patron")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_patron"), rs.getString("nome"), rs.getString("phone"), rs.getString("address")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int patronId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Statement stmt = connection.createStatement()) {
                String query = String.format("DELETE FROM patron WHERE id_patron=%d", patronId);
                stmt.executeUpdate(query);
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

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

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Patron", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText();
                String newPhone = phoneField.getText();
                String newAddress = addressField.getText();
                try (Statement stmt = connection.createStatement()) {
                    String query = String.format("UPDATE patron SET nome='%s', phone='%s', address='%s' WHERE id_patron=%d", newName, newPhone, newAddress, patronId);
                    stmt.executeUpdate(query);
                    tableModel.setValueAt(newName, selectedRow, 1);
                    tableModel.setValueAt(newPhone, selectedRow, 2);
                    tableModel.setValueAt(newAddress, selectedRow, 3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Patron", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            try (Statement stmt = connection.createStatement()) {
                String query = String.format("INSERT INTO patron (nome, phone, address) VALUES ('%s', '%s', '%s')", name, phone, address);
                stmt.executeUpdate(query);
                loadPatronData(); // Refresh table data
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchPatron(String query) {
        tableModel.setRowCount(0); // Clear existing data

        String sql = "SELECT * FROM patron WHERE nome ILIKE '%" + query + "%' OR phone ILIKE '%" + query + "%' OR address ILIKE '%" + query + "%'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_patron"), rs.getString("name"), rs.getString("phone"), rs.getString("address")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
