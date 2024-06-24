import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * The LibraryApp class represents a Library Management System application with a GUI.
 */
public class LibraryApp extends JFrame {

    private static final String PASSWORD = "admin123"; // Define your password here
    private Connection connection;
    private JFrame mainFrame;

    /**
     * Constructs a LibraryApp with a specified database connection.
     *
     * @param connection the database connection to be used
     */
    public LibraryApp(Connection connection) {
        this.connection = connection;
        initUI();
    }

    /**
     * Initializes the user interface for the LibraryApp.
     */
    private void initUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    /**
     * Places the components on the given panel.
     *
     * @param panel the panel to place components on
     */
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Password:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 20, 165, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                if (PASSWORD.equals(password)) {
                    // Close the current login window
                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
                    loginFrame.dispose();

                    // Open the main menu in a new window
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    /**
     * Displays the main menu after a successful login.
     */
    private void showMainMenu() {
        mainFrame = new JFrame("Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 200);

        JPanel panel = new JPanel();
        mainFrame.add(panel);
        panel.setLayout(new GridLayout(3, 1));

        JButton loanButton = new JButton("Loan Page");
        JButton bookButton = new JButton("Book Page");
        JButton patronButton = new JButton("Patron Page");

        panel.add(loanButton);
        panel.add(bookButton);
        panel.add(patronButton);

        loanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoanPage();
            }
        });

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookPage();
            }
        });

        patronButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPatronPage();
            }
        });

        mainFrame.setVisible(true);
    }

    /**
     * Displays the loan page.
     */
    private void showLoanPage() {
        JFrame frame = new JFrame("Loan Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        LoanOperation loanOperations = new LoanOperation(connection);
        LoanPanel loanPanel = new LoanPanel(loanOperations, mainFrame);
        frame.add(loanPanel);
        frame.setVisible(true);
    }

    /**
     * Displays the book page.
     */
    private void showBookPage() {
        JFrame frame = new JFrame("Book Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        BookOperation bookOperations = new BookOperation(connection);
        BookPanel bookPanel = new BookPanel(bookOperations, mainFrame);
        frame.add(bookPanel);
        frame.setVisible(true);
    }

    /**
     * Displays the patron page.
     */
    private void showPatronPage() {
        JFrame frame = new JFrame("Patron Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        PatronOperations patronOperations = new PatronOperations(connection);
        PatronPanel patronPanelUI = new PatronPanel(patronOperations, mainFrame);
        frame.add(patronPanelUI);
        frame.setVisible(true);
    }
}
