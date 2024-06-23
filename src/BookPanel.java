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
 * The BookPanel class represents a panel in a GUI application that displays book data
 * in a table and allows for basic operations such as adding, deleting, updating, and
 * searching book data.
 */
public class BookPanel extends JPanel {

    private BookOperation bookOperations;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Constructs a BookPanel with the specified book operations and main frame.
     *
     * @param bookOperations the book operations to use for interacting with the database
     * @param mainFrame the main frame of the application
     */
    public BookPanel(BookOperation bookOperations, JFrame mainFrame) {
        this.bookOperations = bookOperations;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initUI();
    }

    /**
     * Initializes the user interface components of the panel.
     */
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

    /**
     * Loads book data from the database into the table model.
     */
    private void loadBookData() {
        tableModel.setRowCount(0); // Clear existing data
        bookOperations.loadBookData(tableModel);
    }

    /**
     * Deletes the selected row from the table and the corresponding book from the database.
     */
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                bookOperations.deleteBook(bookId);
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                //Trigger activation
                if (e.getMessage().contains("Cannot delete book because it is currently on loan and not yet returned.")) {
                    JOptionPane.showMessageDialog(this, "Error: Cannot delete book because it is currently on loan and not yet returned.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                } else {//unknown error
                    JOptionPane.showMessageDialog(this, "Error: An unexpected error occurred while deleting the book.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Updates the selected row in the table and the corresponding book in the database.
     */
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
                bookOperations.updateBook(bookId, newTitle, newAuthor, newGenre);
                tableModel.setValueAt(newTitle, selectedRow, 1);
                tableModel.setValueAt(newAuthor, selectedRow, 2);
                tableModel.setValueAt(newGenre, selectedRow, 3);
            }
        }
    }

    /**
     * Adds a new book to the database and refreshes the table data.
     */
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
            bookOperations.addBook(title, author, genre);
            loadBookData(); // Refresh table data
        }
    }

    /**
     * Searches for books in the database matching the query and updates the table model.
     *
     * @param query the search query
     */
    private void searchBook(String query) {
        tableModel.setRowCount(0); // Clear existing data
        bookOperations.searchBook(query, tableModel);
    }
}