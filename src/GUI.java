import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class GUI extends JFrame {
    private Connection conn;
    private DBOperation dbOperations;

    public GUI(Connection conn) {
        this.conn = conn;
        this.dbOperations = new DBOperation();
        initComponents();
    }

    private void initComponents() {
        setTitle("Biblioteca Database Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        JPanel mainMenu = new JPanel(new GridLayout(0, 1));
        JButton patronButton = new JButton("Patron Operations");
        JButton bookButton = new JButton("Book Operations");
        JButton loanButton = new JButton("Loan Operations");

        mainMenu.add(patronButton);
        mainMenu.add(bookButton);
        mainMenu.add(loanButton);

        cardPanel.add(mainMenu, "Main Menu");

        // Patron Operations Panel
        JPanel patronOperations = new JPanel(new GridLayout(0, 1));
        JButton addPatronButton = new JButton("Add Patron");
        JButton searchPatronButton = new JButton("Search Patron");
        JButton deletePatronButton = new JButton("Delete Patron");
        JButton backPatronButton = new JButton("Back");

        patronOperations.add(addPatronButton);
        patronOperations.add(searchPatronButton);
        patronOperations.add(deletePatronButton);
        patronOperations.add(backPatronButton);

        cardPanel.add(patronOperations, "Patron Operations");

        // Book Operations Panel
        JPanel bookOperations = new JPanel(new GridLayout(0, 1));
        JButton addBookButton = new JButton("Add Book");
        JButton searchBookButton = new JButton("Search Book");
        JButton deleteBookButton = new JButton("Delete Book");
        JButton backBookButton = new JButton("Back");

        bookOperations.add(addBookButton);
        bookOperations.add(searchBookButton);
        bookOperations.add(deleteBookButton);
        bookOperations.add(backBookButton);

        cardPanel.add(bookOperations, "Book Operations");

        // Loan Operations Panel
        JPanel loanOperations = new JPanel(new GridLayout(0, 1));
        JButton addLoanButton = new JButton("Add Loan");
        JButton searchLoanButton = new JButton("Search Loan");
        JButton backLoanButton = new JButton("Back");

        loanOperations.add(addLoanButton);
        loanOperations.add(searchLoanButton);
        loanOperations.add(backLoanButton);

        cardPanel.add(loanOperations, "Loan Operations");

        // Individual Operation Panels
        JPanel addPatronPanel = createAddPatronPanel(cardLayout, cardPanel);
        JPanel searchPatronPanel = createSearchPatronPanel(cardLayout, cardPanel);
        JPanel deletePatronPanel = createDeletePatronPanel(cardLayout, cardPanel);

        JPanel addBookPanel = createAddBookPanel(cardLayout, cardPanel);
        JPanel searchBookPanel = createSearchBookPanel(cardLayout, cardPanel);
        JPanel deleteBookPanel = createDeleteBookPanel(cardLayout, cardPanel);

        JPanel addLoanPanel = createAddLoanPanel(cardLayout, cardPanel);
        JPanel searchLoanPanel = createSearchLoanPanel(cardLayout, cardPanel);

        cardPanel.add(addPatronPanel, "Add Patron");
        cardPanel.add(searchPatronPanel, "Search Patron");
        cardPanel.add(deletePatronPanel, "Delete Patron");

        cardPanel.add(addBookPanel, "Add Book");
        cardPanel.add(searchBookPanel, "Search Book");
        cardPanel.add(deleteBookPanel, "Delete Book");

        cardPanel.add(addLoanPanel, "Add Loan");
        cardPanel.add(searchLoanPanel, "Search Loan");

        // Add Action Listeners for Main Menu Buttons
        patronButton.addActionListener(e -> cardLayout.show(cardPanel, "Patron Operations"));
        bookButton.addActionListener(e -> cardLayout.show(cardPanel, "Book Operations"));
        loanButton.addActionListener(e -> cardLayout.show(cardPanel, "Loan Operations"));

        // Add Action Listeners for Patron Operations
        addPatronButton.addActionListener(e -> cardLayout.show(cardPanel, "Add Patron"));
        searchPatronButton.addActionListener(e -> cardLayout.show(cardPanel, "Search Patron"));
        deletePatronButton.addActionListener(e -> cardLayout.show(cardPanel, "Delete Patron"));
        backPatronButton.addActionListener(e -> cardLayout.show(cardPanel, "Main Menu"));

        // Add Action Listeners for Book Operations
        addBookButton.addActionListener(e -> cardLayout.show(cardPanel, "Add Book"));
        searchBookButton.addActionListener(e -> cardLayout.show(cardPanel, "Search Book"));
        deleteBookButton.addActionListener(e -> cardLayout.show(cardPanel, "Delete Book"));
        backBookButton.addActionListener(e -> cardLayout.show(cardPanel, "Main Menu"));

        // Add Action Listeners for Loan Operations
        addLoanButton.addActionListener(e -> cardLayout.show(cardPanel, "Add Loan"));
        searchLoanButton.addActionListener(e -> cardLayout.show(cardPanel, "Search Loan"));
        backLoanButton.addActionListener(e -> cardLayout.show(cardPanel, "Main Menu"));

        add(cardPanel);
    }

    private JPanel createAddPatronPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField patronNameField = new JTextField();
        JTextField patronPhoneField = new JTextField();
        JTextField patronAddressField = new JTextField();
        JButton addPatronButton = new JButton("Add Patron");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Name:"));
        panel.add(patronNameField);
        panel.add(new JLabel("Phone:"));
        panel.add(patronPhoneField);
        panel.add(new JLabel("Address:"));
        panel.add(patronAddressField);
        panel.add(addPatronButton);
        panel.add(backButton);

        addPatronButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = patronNameField.getText();
                String phone = patronPhoneField.getText();
                String address = patronAddressField.getText();
                boolean success = dbOperations.insertPatron(conn, name, phone, address);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Patron added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    patronNameField.setText("");
                    patronPhoneField.setText("");
                    patronAddressField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add patron.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Patron Operations"));

        return panel;
    }

    private JPanel createSearchPatronPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField patronNameField = new JTextField();
        JButton searchPatronButton = new JButton("Search Patron");
        JTextArea patronTextArea = new JTextArea();
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Name:"));
        panel.add(patronNameField);
        panel.add(searchPatronButton);
        panel.add(new JScrollPane(patronTextArea));
        panel.add(backButton);

        searchPatronButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patronTextArea.setText("");
                dbOperations.searchPatronByName(conn, patronNameField.getText());
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Patron Operations"));

        return panel;
    }

    private JPanel createDeletePatronPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField patronIdField = new JTextField();
        JButton deletePatronButton = new JButton("Delete Patron");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Patron ID:"));
        panel.add(patronIdField);
        panel.add(deletePatronButton);
        panel.add(backButton);

        deletePatronButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int patronId = Integer.parseInt(patronIdField.getText());
                    boolean success = dbOperations.deletePatronById(conn, patronId);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Patron deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        patronIdField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete patron.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid integer for the patron ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Patron Operations"));

        return panel;
    }

    private JPanel createAddBookPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField bookTitleField = new JTextField();
        JTextField bookAuthorField = new JTextField();
        JTextField bookCategoryField = new JTextField();
        JButton addBookButton = new JButton("Add Book");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Title:"));
        panel.add(bookTitleField);
        panel.add(new JLabel("Author:"));
        panel.add(bookAuthorField);
        panel.add(new JLabel("Category:"));
        panel.add(bookCategoryField);
        panel.add(addBookButton);
        panel.add(backButton);

        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = bookTitleField.getText();
                String author = bookAuthorField.getText();
                String category = bookCategoryField.getText();
                boolean success = dbOperations.insertBook(conn, title, author, category);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    bookTitleField.setText("");
                    bookAuthorField.setText("");
                    bookCategoryField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Book Operations"));

        return panel;
    }

    private JPanel createSearchBookPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField bookTitleField = new JTextField();
        JButton searchBookButton = new JButton("Search Book");
        JTextArea bookTextArea = new JTextArea();
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Title:"));
        panel.add(bookTitleField);
        panel.add(searchBookButton);
        panel.add(new JScrollPane(bookTextArea));
        panel.add(backButton);

        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookTextArea.setText("");
                dbOperations.searchBookByTitle(conn, bookTitleField.getText());
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Book Operations"));

        return panel;
    }

    private JPanel createDeleteBookPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField bookIdField = new JTextField();
        JButton deleteBookButton = new JButton("Delete Book");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Book ID:"));
        panel.add(bookIdField);
        panel.add(deleteBookButton);
        panel.add(backButton);

        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookIdField.getText());
                    boolean success = dbOperations.deleteBookById(conn, bookId);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        bookIdField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid integer for the book ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Book Operations"));

        return panel;
    }

    private JPanel createAddLoanPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField loanISBNField = new JTextField();
        JTextField loanPatronIdField = new JTextField();
        JTextField loanDateField = new JTextField();
        JButton addLoanButton = new JButton("Add Loan");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("ISBN:"));
        panel.add(loanISBNField);
        panel.add(new JLabel("Patron ID:"));
        panel.add(loanPatronIdField);
        panel.add(new JLabel("Loan Date (YYYY-MM-DD):"));
        panel.add(loanDateField);
        panel.add(addLoanButton);
        panel.add(backButton);

        addLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ISBN = Integer.parseInt(loanISBNField.getText());
                int patronId = Integer.parseInt(loanPatronIdField.getText());
                String loanDate = loanDateField.getText();
                boolean success = dbOperations.insertLoan(conn, ISBN, patronId, loanDate);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Loan added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loanISBNField.setText("");
                    loanPatronIdField.setText("");
                    loanDateField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add loan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Loan Operations"));

        return panel;
    }

    private JPanel createSearchLoanPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextArea loanTextArea = new JTextArea();
        JButton searchLoanButton = new JButton("Search Loan");
        JButton backButton = new JButton("Back");

        panel.add(searchLoanButton);
        panel.add(new JScrollPane(loanTextArea));
        panel.add(backButton);

        searchLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loanTextArea.setText("");
                dbOperations.readLoanData(conn);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Loan Operations"));

        return panel;
    }
}
