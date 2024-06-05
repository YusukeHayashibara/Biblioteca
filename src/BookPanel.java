//
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

public class BookPanel extends JPanel {

    private Connection connection;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    private TableRowSorter<DefaultTableModel> sorter;

    public BookPanel(Connection connection, JFrame mainFrame) {
        this.connection = connection;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        tableModel = new DefaultTableModel(new Object[]{"Book ID", "Title", "Author", "Genre"}, 0);
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        loadBookData();

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
                addNewBook();
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
                ((JFrame) SwingUtilities.getWindowAncestor(BookPanel.this)).dispose();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook(searchField.getText());
            }
        });
    }

    private void loadBookData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_book"), rs.getString("title"), rs.getString("author"), rs.getString("genre")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Statement stmt = connection.createStatement()) {
                String query = String.format("DELETE FROM book WHERE id_book=%d", bookId);
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
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String author = (String) tableModel.getValueAt(selectedRow, 2);
            String genre = (String) tableModel.getValueAt(selectedRow, 3);

            JTextField titleField = new JTextField(title);
            JTextField authorField = new JTextField(author);
            JTextField genreField = new JTextField(genre);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Title:"));
            panel.add(titleField);
            panel.add(new JLabel("Author:"));
            panel.add(authorField);
            panel.add(new JLabel("Genre:"));
            panel.add(genreField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newTitle = titleField.getText();
                String newAuthor = authorField.getText();
                String newGenre = genreField.getText();
                try (Statement stmt = connection.createStatement()) {
                    String query = String.format("UPDATE book SET title='%s', author='%s', genre='%s' WHERE id_book=%d", newTitle, newAuthor, newGenre, bookId);
                    stmt.executeUpdate(query);
                    tableModel.setValueAt(newTitle, selectedRow, 1);
                    tableModel.setValueAt(newAuthor, selectedRow, 2);
                    tableModel.setValueAt(newGenre, selectedRow, 3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addNewBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();

            try (Statement stmt = connection.createStatement()) {
                String query = String.format("INSERT INTO book (title, author, genre) VALUES ('%s', '%s', '%s')", title, author, genre);
                stmt.executeUpdate(query);
                loadBookData(); // Refresh table data
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchBook(String query) {
        tableModel.setRowCount(0); // Clear existing data

        String sql = "SELECT * FROM book WHERE title ILIKE '%" + query + "%' OR author ILIKE '%" + query + "%' OR genre ILIKE '%" + query + "%'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id_book"), rs.getString("title"), rs.getString("author"), rs.getString("genre")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
