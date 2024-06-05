import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryApp extends JFrame {

    private static final String PASSWORD = "admin123"; // Set your password here
    private Connection connection;
    private JFrame mainFrame;

    public LibraryApp(Connection connection) {
        this.connection = connection;
        initUI();
    }

    private void initUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

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
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

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

    private void showLoanPage() {
        JFrame frame = new JFrame("Loan Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        LoanPanel loanPanel = new LoanPanel(connection, mainFrame);
        frame.add(loanPanel);
        frame.setVisible(true);
    }

    private void showBookPage() {
        JFrame frame = new JFrame("Book Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        BookPanel bookPanel = new BookPanel(connection, mainFrame);
        frame.add(bookPanel);
        frame.setVisible(true);
    }

    private void showPatronPage() {
        JFrame frame = new JFrame("Patron Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        PatronPanel patronPanel = new PatronPanel(connection, mainFrame);
        frame.add(patronPanel);
        frame.setVisible(true);
    }
}
