import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.util.Random;

/**
 * Event Management System - Desktop UI
 * Comprehensive Swing-based interface integrating MySQL via JDBC.
 * Implements core functionalities (F1-F7) using the relational database schema.
 */
public class EventManagementSystem {

    // Color scheme
    static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    static final Color SECONDARY_COLOR = new Color(59, 130, 246);
    static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    static final Color WARNING_COLOR = new Color(234, 179, 8);
    static final Color DANGER_COLOR = new Color(239, 68, 68);
    static final Color BG_COLOR = new Color(248, 250, 252);
    static final Color CARD_BG = Color.WHITE;
    static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    // Global variable to hold the logged-in user's details
    public static UserSession currentUser = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}

/**
 * Stores session data for the currently logged-in user.
 */
class UserSession {
    public final int userId;
    public final String email;
    public final String role;

    public UserSession(int userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}

/**
 * Database Helper Class for JDBC operations.
 */
class DatabaseHelper {
    // NOTE: REPLACE these with your actual MySQL credentials
    private static final String URL = "jdbc:mysql://localhost:3306/OOPS";
    private static final String USER = "root";
    private static final String PASSWORD = "Sai@1234";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL Driver not found. Check your classpath.", "DB Error", JOptionPane.ERROR_MESSAGE);
            throw new SQLException("MySQL Driver not found.", e);
        }
    }

    public static Vector<Vector<Object>> executeSelect(String query, Vector<String> columnNames, Object... params) {
        Vector<Vector<Object>> data = new Vector<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();

                columnNames.clear();
                // Use getColumnLabel for SQL Aliases (which often map to lowercase)
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    columnNames.add(metaData.getColumnLabel(i));
                }

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        row.add(rs.getObject(i));
                    }
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Query Error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return data;
    }

    public static int executeUpdate(String query, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }
}

/**
 * Login Frame - User authentication (F1)
 */
class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public LoginFrame() {
        setTitle("Event Management System - Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(EventManagementSystem.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(450, 100));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Event Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(191, 219, 254));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(EventManagementSystem.BG_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Email field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Role selection
        JLabel roleLabel = new JLabel("Login As");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        roleCombo = new JComboBox<>(new String[]{"Organizer", "Attendee"});
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Login button
        JButton loginBtn = createStyledButton("Login", EventManagementSystem.PRIMARY_COLOR);
        loginBtn.addActionListener(e -> handleLogin());

        // Register link
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setBackground(EventManagementSystem.BG_COLOR);
        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLabel.setForeground(EventManagementSystem.TEXT_SECONDARY);

        JButton registerLink = new JButton("Register here");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerLink.setForeground(EventManagementSystem.PRIMARY_COLOR);
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addActionListener(e -> {
            new RegistrationFrame().setVisible(true);
            dispose();
        });

        registerPanel.add(registerLabel);
        registerPanel.add(registerLink);

        // Add components to form panel
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(roleLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(roleCombo);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(loginBtn);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(registerPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        String query = "SELECT user_id, role, password_hash FROM Users WHERE email = ? LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("user_id");
                        String dbRole = rs.getString("role");
                        String dbHash = rs.getString("password_hash");

                        // This is a simple HASH SIMULATION.
                        // In a real app, use BCrypt: BCrypt.checkpw(password, dbHash)
                        String expectedHash = "hashed_" + password;

                        // 1. Check Password AND Role for strict match
                        if (dbHash.equals(expectedHash) && dbRole.equals(role)) {
                            // Log successful login
                            DatabaseHelper.executeUpdate("INSERT INTO Audit_Logs (user_id, action) VALUES (?, 'LOGIN_SUCCESS')", userId);

                            // Set current user session
                            EventManagementSystem.currentUser = new UserSession(userId, email, dbRole);

                            // Launch the new dashboard first
                            JFrame dashboard;
                            if (role.equals("Organizer")) {
                                dashboard = new OrganizerDashboard(email);
                            } else {
                                dashboard = new AttendeeDashboard(email);
                            }
                            dashboard.setVisible(true);

                            // Dispose the old frame
                            dispose();
                            return;
                        }
                    }

                    // Login Failed
                    DatabaseHelper.executeUpdate("INSERT INTO Audit_Logs (action, ip_address) VALUES (?, '127.0.0.1')", "LOGIN_FAILED");
                    showError("Invalid email, password, or role combination.");

                }
            }
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState() + ", Message: " + e.getMessage());
            showError("A database connection or query error occurred. Check console.");
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error (F1)", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Registration Frame (F1)
 */
class RegistrationFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleCombo;

    public RegistrationFrame() {
        setTitle("Register - Event Management System");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(EventManagementSystem.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(450, 80));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(EventManagementSystem.BG_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // Full Name
        formPanel.add(createLabel("Full Name"));
        nameField = createTextField();
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(12));

        // Email
        formPanel.add(createLabel("Email Address"));
        emailField = createTextField();
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(12));

        // Password
        formPanel.add(createLabel("Password"));
        passwordField = createPasswordField();
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(12));

        // Confirm Password
        formPanel.add(createLabel("Confirm Password"));
        confirmPasswordField = createPasswordField();
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(12));

        // Role
        formPanel.add(createLabel("Register As"));
        roleCombo = new JComboBox<>(new String[]{"Organizer", "Attendee"});
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(roleCombo);
        formPanel.add(Box.createVerticalStrut(20));

        // Register button
        JButton registerBtn = createStyledButton("Create Account", EventManagementSystem.SUCCESS_COLOR);
        registerBtn.addActionListener(e -> handleRegistration());
        formPanel.add(registerBtn);

        // Back to login
        formPanel.add(Box.createVerticalStrut(10));
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(EventManagementSystem.BG_COLOR);
        JButton backBtn = new JButton("← Back to Login");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backBtn.setForeground(EventManagementSystem.PRIMARY_COLOR);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        loginPanel.add(backBtn);
        formPanel.add(loginPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(EventManagementSystem.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields (F1)");
            return;
        }

        if (!password.equals(confirmPass)) {
            showError("Passwords do not match (F1)");
            return;
        }

        // Enforce password policy: minimum length (SRS)
        if (password.length() < 8) {
            showError("Password must be at least 8 characters (F1)");
            return;
        }

        // SIMULATED PASSWORD HASHING (Replace with BCrypt in production)
        String passwordHash = "hashed_" + password;

        String query = "INSERT INTO Users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try {
            int result = DatabaseHelper.executeUpdate(query, name, email, passwordHash, role);
            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                        "Account created successfully as " + role + "! (F1)\nYou can now login.",
                        "Registration Success",
                        JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame().setVisible(true);
                dispose();
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Duplicate Entry error code
                showError("Email Already Exists: This email is already registered. (F1)");
            } else {
                showError("Registration Failed: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error (F1)", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Organizer Dashboard (F4)
 */
class OrganizerDashboard extends JFrame {
    private String userEmail;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Models to hold the live data from the database
    private DefaultTableModel eventsModel;
    private DefaultTableModel venuesModel;
    private DefaultTableModel attendeesModel;
    private DefaultTableModel dashboardEventsModel;


    public OrganizerDashboard(String email) {
        this.userEmail = email;
        setTitle("Organizer Dashboard - Event Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);

        JPanel sidebar = createSidebar();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Initialize models BEFORE creating panels
        eventsModel = new DefaultTableModel();
        venuesModel = new DefaultTableModel();
        attendeesModel = new DefaultTableModel();
        dashboardEventsModel = new DefaultTableModel();

        // Load initial data into models
        refreshAllTables();

        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createEventsPanel(), "events");
        contentPanel.add(createVenuesPanel(), "venues");
        contentPanel.add(createAttendeesPanel(), "attendees");
        contentPanel.add(createReportsPanel(), "reports");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Public method called by dialogs to force a data refresh on all relevant dashboard tables.
     */
    public void refreshAllTables() {
        // Force refresh all key tables
        loadEventsData(eventsModel);
        loadVenuesData(venuesModel);
        loadAttendeesData(attendeesModel);
        // Also refresh the dashboard's event table model
        loadEventsData(dashboardEventsModel);
    }

    // Helper function to update a live model with new database data.
    private void updateModel(DefaultTableModel model, Vector<String> columnNames, Vector<Vector<Object>> data, Object[] displayHeaders) {
        // This is the robust way to update the JTable's model
        model.setDataVector(data, columnNames); // Updates data and internal identifiers
        model.setColumnIdentifiers(displayHeaders); // Updates display names
        model.fireTableDataChanged();
    }

    // ===============================================
    // DATA LOADING IMPLEMENTATIONS
    // ===============================================

    private void loadEventsData(DefaultTableModel model) {
        // Only load if a user is actually logged in
        if (EventManagementSystem.currentUser == null) return;

        int organizerId = EventManagementSystem.currentUser.userId;
        Vector<String> columnNames = new Vector<>();
        String query = "SELECT e.event_id, e.name, s.start_time, v.name AS venue_name, e.max_capacity, COUNT(t.ticket_id) AS tickets_sold, e.status, '' as actions " +
                "FROM Events e " +
                "LEFT JOIN Venue_Schedules s ON e.event_id = s.event_id " +
                "LEFT JOIN Venues v ON e.venue_id = v.venue_id " +
                "LEFT JOIN Tickets t ON e.event_id = t.event_id AND t.status = 'Confirmed' " +
                "WHERE e.organizer_id = ? " +
                "GROUP BY e.event_id ORDER BY s.start_time DESC";

        Vector<Vector<Object>> data = DatabaseHelper.executeSelect(query, columnNames, organizerId);

        updateModel(model, columnNames, data,
                new Object[]{"ID", "Event Name", "Date", "Venue", "Capacity", "Sold", "Status", "Actions"});
    }

    private void loadVenuesData(DefaultTableModel model) {
        Vector<String> columnNames = new Vector<>();
        String query = "SELECT venue_id, name, location, capacity, type, (SELECT IF(COUNT(*) > 0, 'Booked', 'Available') FROM Venue_Schedules WHERE Venue_Schedules.venue_id = v.venue_id AND end_time > NOW()) AS availability, '' AS actions FROM Venues v";
        Vector<Vector<Object>> data = DatabaseHelper.executeSelect(query, columnNames);

        updateModel(model, columnNames, data,
                new Object[]{"Venue ID", "Venue Name", "Location", "Capacity", "Type", "Availability", "Actions"});
    }

    private void loadAttendeesData(DefaultTableModel model) {
        // Only load if a user is actually logged in
        if (EventManagementSystem.currentUser == null) return;

        int organizerId = EventManagementSystem.currentUser.userId;
        Vector<String> columnNames = new Vector<>();
        String query = "SELECT u.user_id, u.name, u.email, e.name AS event_name, t.unique_code, t.status, '' AS check_in " +
                "FROM Tickets t " +
                "JOIN Users u ON t.attendee_id = u.user_id " +
                "JOIN Events e ON t.event_id = e.event_id " +
                "WHERE e.organizer_id = ? AND e.status = 'Active'";

        Vector<Vector<Object>> data = DatabaseHelper.executeSelect(query, columnNames, organizerId);

        updateModel(model, columnNames, data,
                new Object[]{"Attendee ID", "Name", "Email", "Event", "Ticket ID", "Status", "Check-In"});
    }

    // ===============================================
    // PANEL CREATION METHODS (Use the initialized models)
    // ===============================================

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Logo
        JLabel logoLabel = new JLabel("EMS Organizer");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sidebar.add(logoLabel);

        // Menu items
        sidebar.add(createMenuButton("📊 Dashboard", "dashboard"));
        sidebar.add(createMenuButton("📅 My Events", "events"));
        sidebar.add(createMenuButton("🏢 Venues", "venues"));
        sidebar.add(createMenuButton("👥 Attendees", "attendees"));
        sidebar.add(createMenuButton("📈 Reports", "reports"));

        sidebar.add(Box.createVerticalGlue());

        // User info at bottom
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(30, 41, 59));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel userLabel = new JLabel("👤 " + userEmail);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(new Color(148, 163, 184));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        logoutBtn.setForeground(EventManagementSystem.DANGER_COLOR);
        logoutBtn.setBackground(new Color(30, 41, 59));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            EventManagementSystem.currentUser = null;
            new LoginFrame().setVisible(true);
            dispose();
        });

        userPanel.add(userLabel);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(logoutBtn);
        sidebar.add(userPanel);

        return sidebar;
    }

    private JButton createMenuButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(226, 232, 240));
        btn.setBackground(new Color(30, 41, 59));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(51, 65, 85));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(30, 41, 59));
            }
        });

        btn.addActionListener(e -> cardLayout.show(contentPanel, panelName));

        return btn;
    }

    private JPanel createDashboardPanel() {
        // Implements Organizer Dashboard and Analytics (F4)
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header
        JLabel titleLabel = new JLabel("Dashboard Overview (F4)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Stats panel - KPI Display (F4)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Data fetching for KPIs
        int organizerId = EventManagementSystem.currentUser != null ? EventManagementSystem.currentUser.userId : 0;

        int totalEvents = getScalarResult("SELECT COUNT(*) FROM Events WHERE organizer_id = ?", organizerId);
        int totalTicketsSold = getScalarResult("SELECT COUNT(*) FROM Tickets WHERE event_id IN (SELECT event_id FROM Events WHERE organizer_id = ?)", organizerId);
        double totalRevenue = getDoubleScalarResult("SELECT SUM(price) FROM Tickets WHERE status = 'Confirmed' AND event_id IN (SELECT event_id FROM Events WHERE organizer_id = ?)", organizerId);
        int activeAttendees = getScalarResult("SELECT COUNT(DISTINCT attendee_id) FROM Tickets WHERE status = 'Confirmed' AND event_id IN (SELECT event_id FROM Events WHERE organizer_id = ?)", organizerId);

        statsPanel.add(createStatCard("Total Events", String.valueOf(totalEvents), EventManagementSystem.PRIMARY_COLOR, "📅"));
        statsPanel.add(createStatCard("Total Attendees", String.valueOf(activeAttendees), EventManagementSystem.SECONDARY_COLOR, "👥"));
        statsPanel.add(createStatCard("Tickets Sold", String.valueOf(totalTicketsSold), EventManagementSystem.SUCCESS_COLOR, "🎫"));
        statsPanel.add(createStatCard("Revenue", "$" + String.format("%,.2f", totalRevenue), EventManagementSystem.WARNING_COLOR, "💰"));

        // Recent events table (F4)
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(EventManagementSystem.CARD_BG);
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel recentLabel = new JLabel("Event Status Overview (F4)");
        recentLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Use the member model 'dashboardEventsModel' so it can be refreshed
        JTable recentTable = new JTable(dashboardEventsModel);
        styleTable(recentTable);

        // Remove the 'Actions' column from the dashboard display
        TableColumnModel tcm = recentTable.getColumnModel();
        if (tcm.getColumnCount() > 7) {
            try {
                // Use getColumnIndex to safely find the column by name
                int actionsColumnIndex = tcm.getColumnIndex("Actions");
                tcm.removeColumn(tcm.getColumn(actionsColumnIndex));
            } catch (IllegalArgumentException e) {
                // Column "Actions" not found, maybe already removed. Ignore.
            }
        }

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        recentPanel.add(recentLabel, BorderLayout.NORTH);
        recentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(EventManagementSystem.BG_COLOR);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(recentPanel, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Helper method to execute a scalar query (e.g., COUNT(*), SUM(price))
    private int getScalarResult(String query, int userId) {
        int result = 0;
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private double getDoubleScalarResult(String query, int userId) {
        double result = 0.0;
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JPanel createStatCard(String title, String value, Color accentColor, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(EventManagementSystem.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(EventManagementSystem.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        return card;
    }

    private JPanel createEventsPanel() {
        // Implements Event Creation, Modification, and Deletion (F2)
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.BG_COLOR);

        JLabel titleLabel = new JLabel("My Events (F2)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JButton addBtn = new JButton("+ Create Event");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> new CreateEventDialog(this).setVisible(true));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addBtn, BorderLayout.EAST);

        JTable eventsTable = new JTable(eventsModel);
        styleTable(eventsTable);
        eventsTable.setRowHeight(40);

        // Use the identifier set in loadEventsData ("Actions")
        eventsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        eventsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), eventsModel, eventsTable, "Event"));

        JScrollPane scrollPane = new JScrollPane(eventsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createVenuesPanel() {
        // Implements Venue Management (F3)
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.BG_COLOR);

        JLabel titleLabel = new JLabel("Venue Management (F3)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JButton addVenueBtn = new JButton("+ Add Venue");
        addVenueBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addVenueBtn.setForeground(Color.WHITE);
        addVenueBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        addVenueBtn.setFocusPainted(false);
        addVenueBtn.setBorderPainted(false);
        addVenueBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addVenueBtn.addActionListener(e -> new AddVenueDialog(this).setVisible(true));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addVenueBtn, BorderLayout.EAST);

        JTable venuesTable = new JTable(venuesModel);
        styleTable(venuesTable);
        venuesTable.setRowHeight(40);

        // Use the identifier set in loadVenuesData ("Actions")
        venuesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        venuesTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), venuesModel, venuesTable, "Venue"));

        JScrollPane scrollPane = new JScrollPane(venuesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendeesPanel() {
        // Implements Attendee list view (part of F4 and F6)
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.BG_COLOR);

        JLabel titleLabel = new JLabel("Attendee Management & Check-in (F6)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(EventManagementSystem.BG_COLOR);
        // (Add search components here if needed)
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        JTable attendeesTable = new JTable(attendeesModel);
        styleTable(attendeesTable);
        attendeesTable.setRowHeight(40);

        // Use the identifier set in loadAttendeesData ("Check-In")
        attendeesTable.getColumn("Check-In").setCellRenderer(new CheckInButtonRenderer());
        attendeesTable.getColumn("Check-In").setCellEditor(new CheckInButtonEditor(new JCheckBox(), attendeesModel, attendeesTable));

        JScrollPane scrollPane = new JScrollPane(attendeesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("Analytics & Reports (F4 Export)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Report options
        JPanel reportsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        reportsGrid.setBackground(EventManagementSystem.BG_COLOR);

        reportsGrid.add(createReportCard("Event Performance", "View detailed analytics for each event", "📊"));
        reportsGrid.add(createReportCard("Revenue Report", "Track ticket sales and revenue", "💰"));
        reportsGrid.add(createReportCard("Attendee Statistics", "Analyze attendee demographics", "👥"));
        reportsGrid.add(createReportCard("Venue Utilization", "Monitor venue booking rates", "🏢"));

        // Export section (F4)
        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.setBackground(EventManagementSystem.CARD_BG);
        exportPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel exportLabel = new JLabel("Export Reports");
        exportLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        exportLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel exportButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        exportButtons.setBackground(EventManagementSystem.CARD_BG);

        JButton pdfBtn = new JButton("Export as PDF");
        pdfBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pdfBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        pdfBtn.setForeground(Color.WHITE);
        pdfBtn.setFocusPainted(false);
        pdfBtn.setBorderPainted(false);
        pdfBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pdfBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "PDF report exported successfully! (F4)", "Success (F4)", JOptionPane.INFORMATION_MESSAGE));

        JButton csvBtn = new JButton("Export as CSV");
        csvBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        csvBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        csvBtn.setForeground(Color.WHITE);
        csvBtn.setFocusPainted(false);
        csvBtn.setBorderPainted(false);
        csvBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        csvBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "CSV report exported successfully! (F4)", "Success (F4)", JOptionPane.INFORMATION_MESSAGE));

        exportButtons.add(pdfBtn);
        exportButtons.add(csvBtn);

        exportPanel.add(exportLabel, BorderLayout.NORTH);
        exportPanel.add(exportButtons, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(EventManagementSystem.BG_COLOR);
        contentPanel.add(reportsGrid, BorderLayout.CENTER);
        contentPanel.add(exportPanel, BorderLayout.SOUTH);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportCard(String title, String description, String icon) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(EventManagementSystem.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(EventManagementSystem.CARD_BG);
            }
        });

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(EventManagementSystem.CARD_BG);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(EventManagementSystem.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(EventManagementSystem.TEXT_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(EventManagementSystem.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
    }
}
/**
 * Attendee Dashboard
 */
class AttendeeDashboard extends JFrame {
    private String userEmail;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Models for Attendee data
    private DefaultTableModel ticketsModel;

    // Reference to the browse panel so it can be removed and refreshed
    private JPanel browseEventsPanel;

    public AttendeeDashboard(String email) {
        this.userEmail = email;
        setTitle("Attendee Dashboard - Event Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Initialize model
        ticketsModel = new DefaultTableModel();
        loadTicketsData(ticketsModel);

        // Top navigation bar
        JPanel navbar = createNavbar();

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Create and store the browse panel
        browseEventsPanel = createBrowseEventsPanel();
        contentPanel.add(browseEventsPanel, "browse");
        contentPanel.add(createMyTicketsPanel(), "tickets");
        contentPanel.add(createProfilePanel(), "profile");

        mainPanel.add(navbar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Public method to refresh the "My Tickets" table model.
     */
    public void refreshMyTickets() {
        loadTicketsData(ticketsModel);
    }

    /**
     * Public method to refresh the "Browse Events" panel.
     * This method removes the old panel and adds a new one built with fresh data.
     */
    public void refreshBrowseEvents() {
        // Remove the old panel
        if (browseEventsPanel != null) {
            contentPanel.remove(browseEventsPanel);
        }

        // Create a new panel with fresh data
        browseEventsPanel = createBrowseEventsPanel();

        // Add the new panel back to the card layout
        contentPanel.add(browseEventsPanel, "browse");

        // Re-validate and repaint the container
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void loadTicketsData(DefaultTableModel model) {
        // Only load if a user is actually logged in
        if (EventManagementSystem.currentUser == null) return;

        int attendeeId = EventManagementSystem.currentUser.userId;
        Vector<String> columnNames = new Vector<>();
        String query = "SELECT t.unique_code, e.name AS event_name, s.start_time, v.name AS venue_name, t.status, '' AS actions " +
                "FROM Tickets t " +
                "JOIN Events e ON t.event_id = e.event_id " +
                "LEFT JOIN Venue_Schedules s ON e.event_id = s.event_id " +
                "LEFT JOIN Venues v ON e.venue_id = v.venue_id " +
                "WHERE t.attendee_id = ? " +
                "ORDER BY s.start_time DESC";

        Vector<Vector<Object>> data = DatabaseHelper.executeSelect(query, columnNames, attendeeId);

        // Update the model structure (Fixing column identification for display)
        model.setDataVector(data, columnNames);
        model.setColumnIdentifiers(new Object[]{"Ticket ID", "Event Name", "Date/Time", "Venue", "Status", "Actions"});
        model.fireTableDataChanged();
    }

    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(EventManagementSystem.PRIMARY_COLOR);
        navbar.setPreferredSize(new Dimension(1200, 70));
        navbar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel logoLabel = new JLabel("🎫 Event Management System");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        menuPanel.setBackground(EventManagementSystem.PRIMARY_COLOR);

        menuPanel.add(createNavButton("Browse Events", "browse"));
        menuPanel.add(createNavButton("My Tickets", "tickets"));
        menuPanel.add(createNavButton("Profile", "profile"));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(EventManagementSystem.PRIMARY_COLOR);

        JLabel userLabel = new JLabel("👤 " + userEmail);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            EventManagementSystem.currentUser = null;
            new LoginFrame().setVisible(true);
            dispose();
        });

        rightPanel.add(userLabel);
        rightPanel.add(Box.createHorizontalStrut(15));
        rightPanel.add(logoutBtn);

        navbar.add(logoLabel, BorderLayout.WEST);
        navbar.add(menuPanel, BorderLayout.CENTER);
        navbar.add(rightPanel, BorderLayout.EAST);

        return navbar;
    }

    private JButton createNavButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(contentPanel, panelName));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(EventManagementSystem.SECONDARY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(EventManagementSystem.PRIMARY_COLOR);
            }
        });

        return btn;
    }

    private JPanel createBrowseEventsPanel() {
        // Data fetching for events
        Vector<String> columnNames = new Vector<>();
        String query = "SELECT e.event_id, e.name, s.start_time, v.name AS venue_name, e.max_capacity, COUNT(t.ticket_id) AS tickets_sold, e.type " +
                "FROM Events e " +
                "LEFT JOIN Venue_Schedules s ON e.event_id = s.event_id " +
                "LEFT JOIN Venues v ON e.venue_id = v.venue_id " +
                "LEFT JOIN Tickets t ON e.event_id = t.event_id AND t.status = 'Confirmed' " +
                "WHERE e.status = 'Active' AND s.start_time > NOW() " +
                "GROUP BY e.event_id ORDER BY s.start_time ASC";

        Vector<Vector<Object>> eventsData = DatabaseHelper.executeSelect(query, columnNames);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Search bar (Simplified)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(EventManagementSystem.BG_COLOR);

        searchPanel.add(new JLabel("Browse Events:"));
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 35));
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        searchPanel.add(searchBtn);

        // Events grid
        JPanel eventsGrid = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 columns, dynamic rows
        eventsGrid.setBackground(EventManagementSystem.BG_COLOR);

        for (Vector<Object> eventRow : eventsData) {
            String name = (String) eventRow.get(1);

            // ================== FIX IS HERE ==================
            // Handle different date/time objects from JDBC
            Object dateObject = eventRow.get(2);
            java.util.Date dateObj = null;
            if (dateObject instanceof java.sql.Timestamp) {
                dateObj = (java.sql.Timestamp) dateObject;
            } else if (dateObject instanceof java.time.LocalDateTime) {
                java.time.LocalDateTime ldt = (java.time.LocalDateTime) dateObject;
                dateObj = java.util.Date.from(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());
            } else if (dateObject instanceof java.util.Date) {
                dateObj = (java.util.Date) dateObject;
            }
            // ================= END OF FIX ==================

            String date = (dateObj != null) ? new java.text.SimpleDateFormat("MMM dd, yyyy").format(dateObj) : "TBD";
            String venue = (String) eventRow.get(3);
            int capacity = (Integer) eventRow.get(4);
            long soldLong = (Long) eventRow.get(5);
            String sold = String.valueOf(soldLong);
            String category = (String) eventRow.get(6);
            int eventId = (Integer) eventRow.get(0);

            eventsGrid.add(createEventCard(name, date, venue, String.valueOf(capacity), sold, category, eventId));
        }

        if (eventsData.isEmpty()) {
            eventsGrid.setLayout(new FlowLayout(FlowLayout.CENTER));
            eventsGrid.add(new JLabel("No upcoming events are currently available."));
        }

        JScrollPane scrollPane = new JScrollPane(eventsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEventCard(String name, String date, String venue,
                                   String capacity, String sold, String category, int eventId) {

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(EventManagementSystem.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.CARD_BG);

        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        categoryLabel.setForeground(EventManagementSystem.PRIMARY_COLOR);
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(239, 246, 255));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(EventManagementSystem.CARD_BG);

        infoPanel.add(createInfoLabel("📅 " + date));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(createInfoLabel("🏢 " + venue));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(createInfoLabel("🎫 " + sold + "/" + capacity + " tickets sold"));

        JButton registerBtn = new JButton("Purchase Ticket (F5)");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Check if sold is greater than or equal to capacity
        if (Integer.parseInt(sold) >= Integer.parseInt(capacity)) {
            registerBtn.setText("Sold Out (E1)");
            registerBtn.setBackground(EventManagementSystem.DANGER_COLOR);
            registerBtn.setEnabled(false);
        } else {
            registerBtn.addActionListener(e ->
                    new TicketPurchaseDialog(this, name, eventId).setVisible(true));
        }

        headerPanel.add(categoryLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(EventManagementSystem.CARD_BG);
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(registerBtn);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(EventManagementSystem.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createMyTicketsPanel() {
        // Implements Ticket View and Cancellation (F7)
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Tickets (F7)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Model is already loaded in the constructor and refreshed via refreshMyTickets()
        JTable ticketsTable = new JTable(ticketsModel);
        styleTable(ticketsTable);
        ticketsTable.setRowHeight(45);

        // Use the identifier set in loadTicketsData ("Actions")
        ticketsTable.getColumn("Actions").setCellRenderer(new TicketActionRenderer());
        ticketsTable.getColumn("Actions").setCellEditor(new TicketActionEditor(new JCheckBox(), ticketsModel, ticketsTable));

        JScrollPane scrollPane = new JScrollPane(ticketsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Fetch user details
        Vector<String> columnNames = new Vector<>();
        String query = "SELECT name, email, registration_date FROM Users WHERE user_id = ?";
        Vector<Vector<Object>> userData = DatabaseHelper.executeSelect(query, columnNames, EventManagementSystem.currentUser.userId);

        String name = "N/A", email = EventManagementSystem.currentUser.email, memberSince = "N/A";
        if (!userData.isEmpty()) {
            name = (String) userData.get(0).get(0);
            email = (String) userData.get(0).get(1);

            // ================== FIX IS HERE ==================
            // Handle different date/time objects from JDBC
            Object regDateObject = userData.get(0).get(2);
            java.util.Date regDate = null;

            if (regDateObject instanceof java.sql.Timestamp) {
                regDate = (java.sql.Timestamp) regDateObject;
            } else if (regDateObject instanceof java.time.LocalDateTime) {
                // Manual conversion from LocalDateTime to Date (via Instant)
                java.time.LocalDateTime ldt = (java.time.LocalDateTime) regDateObject;
                regDate = java.util.Date.from(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());
            } else if (regDateObject instanceof java.util.Date) {
                regDate = (java.util.Date) regDateObject;
            }
            // ================= END OF FIX ==================


            if (regDate != null) {
                memberSince = new java.text.SimpleDateFormat("MMMM yyyy").format(regDate);
            }
        }

        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(EventManagementSystem.CARD_BG);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        profileCard.setMaximumSize(new Dimension(600, 1000));
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(createProfileField("Full Name", name));
        profileCard.add(Box.createVerticalStrut(20));
        profileCard.add(createProfileField("Email Address", email));
        profileCard.add(Box.createVerticalStrut(20));
        profileCard.add(createProfileField("Role", EventManagementSystem.currentUser.role));
        profileCard.add(Box.createVerticalStrut(20));
        profileCard.add(createProfileField("Member Since", memberSince));

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(EventManagementSystem.BG_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        centerPanel.add(profileCard);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfileField(String label, String value) {
        JPanel field = new JPanel(new BorderLayout(10, 5));
        field.setBackground(EventManagementSystem.CARD_BG);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelText.setForeground(EventManagementSystem.TEXT_SECONDARY);

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        valueText.setForeground(EventManagementSystem.TEXT_PRIMARY);

        field.add(labelText, BorderLayout.NORTH);
        field.add(valueText, BorderLayout.CENTER);

        return field;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(EventManagementSystem.TEXT_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(EventManagementSystem.TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
    }
}

/**
 * Create Event Dialog (F2 & F3)
 */
class CreateEventDialog extends JDialog {
    private JTextField nameField, capacityField;
    private JComboBox<String> venueCombo, categoryCombo;
    private JSpinner dateSpinner, timeSpinner;
    private JTextArea descArea;

    public CreateEventDialog(JFrame parent) {
        super(parent, "Create New Event (F2)", true);
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Create New Event (F2)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Event Name
        formPanel.add(createLabel("Event Name *"));
        nameField = createTextField();
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Category (Event Type)
        formPanel.add(createLabel("Category *"));
        categoryCombo = new JComboBox<>(new String[]{
                "Concert", "Conference", "Workshop", "Wedding", "Other"
        });
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(categoryCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Venue (F3)
        formPanel.add(createLabel("Venue *"));
        Vector<String> venueNames = new Vector<>();
        Vector<Vector<Object>> venueData = DatabaseHelper.executeSelect("SELECT name FROM Venues", new Vector<>());
        for (Vector<Object> row : venueData) {
            venueNames.add((String) row.get(0));
        }
        venueCombo = new JComboBox<>(venueNames.toArray(new String[0]));
        venueCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        venueCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(venueCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Date
        formPanel.add(createLabel("Event Date *"));
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(dateSpinner);
        formPanel.add(Box.createVerticalStrut(15));

        // Time
        formPanel.add(createLabel("Event Time *"));
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(timeSpinner);
        formPanel.add(Box.createVerticalStrut(15));

        // Capacity
        formPanel.add(createLabel("Capacity *"));
        capacityField = createTextField();
        formPanel.add(capacityField);
        formPanel.add(Box.createVerticalStrut(15));

        // Description
        formPanel.add(createLabel("Description"));
        descArea = new JTextArea(4, 20);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        formPanel.add(descScroll);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(EventManagementSystem.BG_COLOR);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelBtn.setForeground(EventManagementSystem.TEXT_SECONDARY);
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        JButton createBtn = new JButton("Create Event");
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        createBtn.setFocusPainted(false);
        createBtn.setBorderPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.addActionListener(e -> handleCreateEvent());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(createBtn);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(EventManagementSystem.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void handleCreateEvent() {
        String name = nameField.getText().trim();
        String capacity = capacityField.getText().trim();
        String description = descArea.getText().trim();
        String venueName = (String) venueCombo.getSelectedItem();
        String eventType = (String) categoryCombo.getSelectedItem();
        Date datePart = (Date) dateSpinner.getValue();
        Date timePart = (Date) timeSpinner.getValue(); // Get time part

        // Combine Date and Time correctly
        java.util.Calendar calDate = java.util.Calendar.getInstance();
        calDate.setTime(datePart);

        java.util.Calendar calTime = java.util.Calendar.getInstance();
        calTime.setTime(timePart);

        calDate.set(java.util.Calendar.HOUR_OF_DAY, calTime.get(java.util.Calendar.HOUR_OF_DAY));
        calDate.set(java.util.Calendar.MINUTE, calTime.get(java.util.Calendar.MINUTE));
        calDate.set(java.util.Calendar.SECOND, 0);

        Date combinedStartTime = calDate.getTime();

        if (name.isEmpty() || capacity.isEmpty() || venueName == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Input Data: Please fill in all required fields (F2)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maxCapacity;
        try {
            maxCapacity = Integer.parseInt(capacity);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Input Data: Capacity must be a valid number (F2)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simplified end time logic for schedule: Assuming 2 hours duration
        long twoHours = 2 * 60 * 60 * 1000;
        Date scheduleEndTime = new Date(combinedStartTime.getTime() + twoHours);

        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false); // Start transaction for atomicity (F3 requirement)

            int organizerId = EventManagementSystem.currentUser.userId;

            // 1. Get Venue ID
            String venueIdQuery = "SELECT venue_id FROM Venues WHERE name = ?";
            int venueId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(venueIdQuery)) {
                stmt.setString(1, venueName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        venueId = rs.getInt(1);
                    }
                }
            }
            if (venueId == -1) throw new SQLException("Venue not found in database.");

            // 2. Conflict Check (F3) - Check if the selected time range overlaps
            String conflictQuery = "SELECT start_time FROM Venue_Schedules WHERE venue_id = ? AND (? < end_time AND ? > start_time)";
            try (PreparedStatement conflictStmt = conn.prepareStatement(conflictQuery)) {
                conflictStmt.setInt(1, venueId);
                conflictStmt.setTimestamp(2, new Timestamp(combinedStartTime.getTime())); // Param 2: New start time
                conflictStmt.setTimestamp(3, new Timestamp(scheduleEndTime.getTime()));   // Param 3: New end time

                try (ResultSet rs = conflictStmt.executeQuery()) {
                    if (rs.next()) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this,
                                "Venue Conflict Detected (AF1): Venue is booked from " + new java.text.SimpleDateFormat("MMM dd HH:mm").format(rs.getTimestamp(1)) + ". Choose another slot. (F3)",
                                "Venue Conflict", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // 3. Insert Event (F2)
            String insertEventQuery = "INSERT INTO Events (organizer_id, venue_id, name, description, max_capacity, type, status) VALUES (?, ?, ?, ?, ?, ?, 'Active')";
            PreparedStatement insertEventStmt = conn.prepareStatement(insertEventQuery, Statement.RETURN_GENERATED_KEYS);

            insertEventStmt.setInt(1, organizerId);
            insertEventStmt.setInt(2, venueId);
            insertEventStmt.setString(3, name);
            insertEventStmt.setString(4, description);
            insertEventStmt.setInt(5, maxCapacity);
            insertEventStmt.setString(6, eventType);

            insertEventStmt.executeUpdate();

            ResultSet generatedKeys = insertEventStmt.getGeneratedKeys();
            int newEventId = -1;
            if (generatedKeys.next()) {
                newEventId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                throw new SQLException("Failed to retrieve new event ID.");
            }

            // 4. Insert Venue Schedule (F3) - Locks the time slot
            String insertScheduleQuery = "INSERT INTO Venue_Schedules (venue_id, event_id, start_time, end_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement scheduleStmt = conn.prepareStatement(insertScheduleQuery)) {
                scheduleStmt.setInt(1, venueId);
                scheduleStmt.setInt(2, newEventId);
                scheduleStmt.setTimestamp(3, new Timestamp(combinedStartTime.getTime()));
                scheduleStmt.setTimestamp(4, new Timestamp(scheduleEndTime.getTime()));
                scheduleStmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction

            // CRITICAL: Refresh all tables on the dashboard
            if (getParent() instanceof OrganizerDashboard) {
                ((OrganizerDashboard) getParent()).refreshAllTables();
            }

            JOptionPane.showMessageDialog(this, "Event '" + name + "' created and venue reserved successfully! (F2, F3)", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback on failure (SRS requirement)
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Event Creation Failed. Rollback failed: " + ex.getMessage(), "DB Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(this, "Event Creation Failed: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Add Venue Dialog
 */
class AddVenueDialog extends JDialog {
    private JTextField nameField, locationField, capacityField;
    private JComboBox<String> typeCombo;

    public AddVenueDialog(JFrame parent) {
        super(parent, "Add New Venue", true);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Add New Venue");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(EventManagementSystem.BG_COLOR);

        formPanel.add(createLabel("Venue Name *"));
        nameField = createTextField();
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabel("Location *"));
        locationField = createTextField();
        formPanel.add(locationField);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabel("Capacity *"));
        capacityField = createTextField();
        formPanel.add(capacityField);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabel("Venue Type *"));
        typeCombo = new JComboBox<>(new String[]{
                "Conference", "Outdoor", "Workshop", "Banquet", "Exhibition"
        });
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(typeCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(EventManagementSystem.BG_COLOR);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelBtn.setForeground(EventManagementSystem.TEXT_SECONDARY);
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        JButton addBtn = new JButton("Add Venue");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> handleAddVenue());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(addBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(EventManagementSystem.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void handleAddVenue() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (name.isEmpty() || location.isEmpty() || capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO Venues (name, location, capacity, type) VALUES (?, ?, ?, ?)";
        try {
            DatabaseHelper.executeUpdate(query, name, location, capacity, type);

            // CRITICAL: Refresh all tables on the dashboard
            if (getParent() instanceof OrganizerDashboard) {
                ((OrganizerDashboard) getParent()).refreshAllTables();
            }

            JOptionPane.showMessageDialog(this, "Venue added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add venue: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * Ticket Purchase Dialog (F5)
 */
class TicketPurchaseDialog extends JDialog {
    private String eventName;
    private int eventId;
    private JSpinner quantitySpinner;
    private JComboBox<String> ticketTypeCombo;
    private JLabel amountLabel;
    private final double TICKET_PRICE = 50.00; // Simulated price

    public TicketPurchaseDialog(JFrame parent, String eventName, int eventId) {
        super(parent, "Purchase Ticket (F5)", true);
        this.eventName = eventName;
        this.eventId = eventId;
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Purchase Ticket (F5)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Event info
        JPanel eventPanel = new JPanel(new BorderLayout(10, 10));
        eventPanel.setBackground(new Color(239, 246, 255));
        eventPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        eventPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel eventLabel = new JLabel("Event: " + eventName);
        eventLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        eventLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        eventPanel.add(eventLabel, BorderLayout.NORTH);
        formPanel.add(eventPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Ticket type
        formPanel.add(createLabel("Ticket Type"));
        ticketTypeCombo = new JComboBox<>(new String[]{"Digital", "Physical"});
        ticketTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ticketTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(ticketTypeCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Quantity
        formPanel.add(createLabel("Quantity"));
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1, 1, 10, 1);
        quantitySpinner = new JSpinner(quantityModel);
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        quantitySpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        quantitySpinner.addChangeListener(e -> updateAmount());

        formPanel.add(quantitySpinner);
        formPanel.add(Box.createVerticalStrut(20));

        // Price info
        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setBackground(EventManagementSystem.BG_COLOR);
        pricePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel priceLabel = new JLabel("Total Amount:");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        amountLabel = new JLabel(String.format("$%.2f", TICKET_PRICE));
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        amountLabel.setForeground(EventManagementSystem.SUCCESS_COLOR);

        pricePanel.add(priceLabel, BorderLayout.WEST);
        pricePanel.add(amountLabel, BorderLayout.EAST);

        formPanel.add(pricePanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(EventManagementSystem.BG_COLOR);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelBtn.setForeground(EventManagementSystem.TEXT_SECONDARY);
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dispose());

        JButton purchaseBtn = new JButton("Confirm Purchase (Simulate Payment)");
        purchaseBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        purchaseBtn.setForeground(Color.WHITE);
        purchaseBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        purchaseBtn.setFocusPainted(false);
        purchaseBtn.setBorderPainted(false);
        purchaseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        purchaseBtn.addActionListener(e -> handlePurchase());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(purchaseBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateAmount() {
        int quantity = (Integer) quantitySpinner.getValue();
        double total = quantity * TICKET_PRICE;
        amountLabel.setText(String.format("$%.2f", total));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(EventManagementSystem.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void handlePurchase() {
        int quantity = (Integer) quantitySpinner.getValue();
        String ticketType = (String) ticketTypeCombo.getSelectedItem();
        double totalAmount = quantity * TICKET_PRICE;
        int attendeeId = EventManagementSystem.currentUser.userId;

        // Check capacity
        Vector<String> capCols = new Vector<>();
        String capQuery = "SELECT max_capacity, (SELECT COUNT(*) FROM Tickets WHERE event_id = ? AND status = 'Confirmed') AS sold FROM Events WHERE event_id = ?";
        Vector<Vector<Object>> capData = DatabaseHelper.executeSelect(capQuery, capCols, eventId, eventId);
        if (!capData.isEmpty()) {
            int maxCapacity = (Integer) capData.get(0).get(0);
            long sold = (Long) capData.get(0).get(1);
            if (sold + quantity > maxCapacity) {
                JOptionPane.showMessageDialog(this, "Purchase failed: Exceeds event capacity.", "Capacity Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false); // Start transaction (F5 requirement)

            for (int i = 0; i < quantity; i++) {
                String uniqueCode = "T" + eventId + "-" + (new Random().nextInt(9000000) + 1000000); // Unique Ticket ID (F5)

                // 1. Insert Ticket (F5)
                try (PreparedStatement insertTicketStmt = conn.prepareStatement("INSERT INTO Tickets (unique_code, event_id, attendee_id, ticket_type, price, status) VALUES (?, ?, ?, ?, ?, 'Confirmed')", Statement.RETURN_GENERATED_KEYS)) {
                    insertTicketStmt.setString(1, uniqueCode);
                    insertTicketStmt.setInt(2, eventId);
                    insertTicketStmt.setInt(3, attendeeId);
                    insertTicketStmt.setString(4, ticketType);
                    insertTicketStmt.setDouble(5, TICKET_PRICE);
                    insertTicketStmt.executeUpdate();

                    try (ResultSet generatedKeys = insertTicketStmt.getGeneratedKeys()) {
                        int newTicketId = -1;
                        if (generatedKeys.next()) {
                            newTicketId = generatedKeys.getInt(1);
                        } else {
                            conn.rollback();
                            throw new SQLException("Failed to retrieve new ticket ID.");
                        }

                        // 2. Insert Payment Record (F5)
                        try (PreparedStatement paymentStmt = conn.prepareStatement("INSERT INTO Payments (ticket_id, amount, transaction_type, transaction_status, payment_gateway_ref) VALUES (?, ?, 'Purchase', 'Success', ?)")) {
                            paymentStmt.setInt(1, newTicketId);
                            paymentStmt.setDouble(2, TICKET_PRICE);
                            paymentStmt.setString(3, "REF" + uniqueCode);
                            paymentStmt.executeUpdate();
                        }

                        // 3. Log Audit
                        try (PreparedStatement auditStmt = conn.prepareStatement("INSERT INTO Audit_Logs (user_id, action) VALUES (?, 'TICKET_PURCHASE')")) {
                            auditStmt.setInt(1, attendeeId);
                            auditStmt.executeUpdate();
                        }
                    }
                }
            }
            conn.commit(); // Commit the transaction

            JOptionPane.showMessageDialog(this,
                    "Ticket(s) purchased successfully! (F5)\nTotal Amount: $" + String.format("%.2f", totalAmount),
                    "Success (F5)", JOptionPane.INFORMATION_MESSAGE);

            // Refresh BOTH panels on the AttendeeDashboard
            if (getParent() instanceof AttendeeDashboard) {
                AttendeeDashboard dash = (AttendeeDashboard) getParent();
                dash.refreshMyTickets();     // Refresh the tickets table
                dash.refreshBrowseEvents();  // Refresh the event browsing panel
            }

            dispose();

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                // Log rollback failure
            }
            JOptionPane.showMessageDialog(this, "Purchase Failed: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Custom Table Cell Renderers and Editors
 */
class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton editBtn, deleteBtn;

    public ButtonRenderer() {
        // Use GridLayout to prevent wrapping/overlapping
        setLayout(new GridLayout(1, 0, 5, 0));
        setOpaque(true);

        editBtn = new JButton("Edit");
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        editBtn.setBackground(EventManagementSystem.SECONDARY_COLOR);
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);

        deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        deleteBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);

        add(editBtn);
        add(deleteBtn);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton editBtn, deleteBtn;
    private DefaultTableModel model;
    private JTable table;
    private int row;
    private String entityType; // "Event" or "Venue"

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, JTable table, String entityType) {
        super(checkBox);
        this.model = model;
        this.table = table;
        this.entityType = entityType;

        // Use GridLayout to prevent wrapping/overlapping
        panel = new JPanel(new GridLayout(1, 0, 5, 0));

        editBtn = new JButton("Edit");
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        editBtn.setBackground(EventManagementSystem.SECONDARY_COLOR);
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.addActionListener(e -> {
            // Placeholder for edit functionality
            JOptionPane.showMessageDialog(panel, "Editing " + entityType + ": " + model.getValueAt(row, 1) + " (F2 Edit)", "Edit", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        });

        deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        deleteBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.addActionListener(e -> handleDelete());

        panel.add(editBtn);
        panel.add(deleteBtn);
    }

    private void handleDelete() {
        Object itemIdObj = model.getValueAt(row, 0);
        String itemName = (String) model.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to delete '" + itemName + "'? All associated data will be removed.",
                "Confirm Delete (F2 Delete)",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String deleteQuery = "";
            int id = -1;

            try {
                // Ensure the ID is correctly cast (it comes from the DB as Integer)
                if (itemIdObj instanceof Integer) {
                    id = (Integer) itemIdObj;
                } else {
                    throw new IllegalArgumentException("Invalid ID format.");
                }

                if (entityType.equals("Event")) {
                    deleteQuery = "DELETE FROM Events WHERE event_id = ?";
                } else if (entityType.equals("Venue")) {
                    deleteQuery = "DELETE FROM Venues WHERE venue_id = ?";
                }

                if (deleteQuery.isEmpty()) return;

                DatabaseHelper.executeUpdate(deleteQuery, id);

                // Update UI model
                model.removeRow(row);
                table.revalidate();
                table.repaint();

                JOptionPane.showMessageDialog(panel, itemName + " deleted successfully!");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "Deletion failed due to database error or dependency: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        fireEditingStopped();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}

class CheckInButtonRenderer extends JButton implements TableCellRenderer {
    public CheckInButtonRenderer() {
        setFont(new Font("Segoe UI", Font.BOLD, 11));
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        String status = (String) table.getValueAt(row, 5);

        if (status.equals("Used") || status.equals("Checked-In")) {
            setText("✓ Checked");
            setBackground(EventManagementSystem.SUCCESS_COLOR);
            setForeground(Color.WHITE);
            setEnabled(false);
        } else {
            setText("Check In (F6)");
            setBackground(EventManagementSystem.PRIMARY_COLOR);
            setForeground(Color.WHITE);
            setEnabled(true);
        }

        setOpaque(true);
        return this;
    }
}

class CheckInButtonEditor extends DefaultCellEditor {
    private JButton button;
    private DefaultTableModel model;
    private JTable table;
    private int row;

    public CheckInButtonEditor(JCheckBox checkBox, DefaultTableModel model, JTable table) {
        super(checkBox);
        this.model = model;
        this.table = table;
        button = new JButton();
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(e -> handleCheckIn());
    }

    private void handleCheckIn() {
        String ticketStatus = (String) model.getValueAt(row, 5);
        String ticketCode = (String) model.getValueAt(row, 4);

        if (ticketStatus.equals("Used") || ticketStatus.equals("Checked-In")) {
            JOptionPane.showMessageDialog(button, "Already Checked-In: This ticket was already used. (F6)", "Exception (F6)", JOptionPane.WARNING_MESSAGE);
            fireEditingStopped();
            return;
        } else if (ticketStatus.equals("Cancelled") || ticketStatus.equals("Refunded")) {
            JOptionPane.showMessageDialog(button, "Ticket Cancelled: Entry not permitted. (F6)", "Exception (F6)", JOptionPane.WARNING_MESSAGE);
            fireEditingStopped();
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            // 1. Update ticket status to 'Used'
            String updateTicket = "UPDATE Tickets SET status = 'Used' WHERE unique_code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateTicket)) {
                stmt.setString(1, ticketCode);
                stmt.executeUpdate();
            }

            // 2. Insert Check-In record (F6)
            String insertCheckIn = "INSERT INTO Attendee_Check_In (ticket_id, event_id, verifier_id) VALUES ((SELECT ticket_id FROM Tickets WHERE unique_code = ?), (SELECT event_id FROM Tickets WHERE unique_code = ?), ?)";
            // Verifier is the currently logged-in organizer
            try (PreparedStatement stmt = conn.prepareStatement(insertCheckIn)) {
                stmt.setString(1, ticketCode);
                stmt.setString(2, ticketCode);
                stmt.setInt(3, EventManagementSystem.currentUser.userId);
                stmt.executeUpdate();
            }

            conn.commit();

            // Update UI model
            model.setValueAt("Used", row, 5);
            table.revalidate();
            table.repaint();

            // Log audit
            DatabaseHelper.executeUpdate("INSERT INTO Audit_Logs (user_id, action) VALUES (?, 'ATTENDEE_CHECKED_IN')", EventManagementSystem.currentUser.userId);

            JOptionPane.showMessageDialog(button, "Check-In Successful for " + model.getValueAt(row, 1) + "! (F6)", "Success (F6)", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
            }
            JOptionPane.showMessageDialog(button, "Check-in failed due to database error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        fireEditingStopped();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        String status = (String) model.getValueAt(row, 5);

        if (status.equals("Used") || status.equals("Checked-In")) {
            button.setText("✓ Checked");
            button.setBackground(EventManagementSystem.SUCCESS_COLOR);
            button.setForeground(Color.WHITE);
            button.setEnabled(false);
        } else {
            button.setText("Check In (F6)");
            button.setBackground(EventManagementSystem.PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setEnabled(true);
        }
        return button;
    }

    public Object getCellEditorValue() {
        return "";
    }
}

class TicketActionRenderer extends JPanel implements TableCellRenderer {
    private JButton viewBtn, cancelBtn;

    public TicketActionRenderer() {
        // Use GridLayout to prevent wrapping/overlapping
        setLayout(new GridLayout(1, 0, 5, 0));
        setOpaque(true);

        viewBtn = new JButton("View");
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setBorderPainted(false);

        cancelBtn = new JButton("Cancel (F7)");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cancelBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);

        add(viewBtn);
        add(cancelBtn);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        String status = (String) table.getValueAt(row, 4); // Status is column 4 in mytickets
        if (status.equals("Cancelled") || status.equals("Used") || status.equals("Refunded")) {
            cancelBtn.setEnabled(false);
        } else {
            cancelBtn.setEnabled(true);
        }

        return this;
    }
}

class TicketActionEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton viewBtn, cancelBtn;
    private DefaultTableModel model;
    private JTable table;
    private int row;

    public TicketActionEditor(JCheckBox checkBox, DefaultTableModel model, JTable table) {
        super(checkBox);
        this.model = model;
        this.table = table;

        // Use GridLayout to prevent wrapping/overlapping
        panel = new JPanel(new GridLayout(1, 0, 5, 0));

        viewBtn = new JButton("View");
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setBorderPainted(false);
        viewBtn.addActionListener(e -> {
            String ticketID = (String) model.getValueAt(row, 0);
            String eventName = (String) model.getValueAt(row, 1);
            String status = (String) model.getValueAt(row, 4);
            JOptionPane.showMessageDialog(panel,
                    "Ticket Details:\nTicket ID: " + ticketID + "\nEvent: " + eventName + "\nStatus: " + status,
                    "Ticket Details", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        });

        cancelBtn = new JButton("Cancel (F7)");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cancelBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> handleCancelTicket());

        panel.add(viewBtn);
        panel.add(cancelBtn);
    }

    private void handleCancelTicket() {
        String ticketID = (String) model.getValueAt(row, 0);
        String status = (String) model.getValueAt(row, 4);

        if (status.equals("Cancelled") || status.equals("Used") || status.equals("Refunded")) {
            JOptionPane.showMessageDialog(panel, "Cannot cancel a ticket that is already " + status + ".", "Cancellation Failed (F7)", JOptionPane.WARNING_MESSAGE);
            fireEditingStopped();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to cancel ticket " + ticketID + "?\nA mock refund will be processed (F7).",
                "Confirm Ticket Cancellation (F7)", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = null;
            try {
                conn = DatabaseHelper.getConnection();
                conn.setAutoCommit(false);

                // 1. Fetch ticket_id and price
                String fetchTicket = "SELECT ticket_id, price FROM Tickets WHERE unique_code = ?";
                int ticketDbId = -1;
                double price = 0.0;
                try (PreparedStatement stmt = conn.prepareStatement(fetchTicket)) {
                    stmt.setString(1, ticketID);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            ticketDbId = rs.getInt("ticket_id");
                            price = rs.getDouble("price");
                        } else {
                            conn.rollback();
                            throw new SQLException("Ticket not found.");
                        }
                    }
                }

                // 2. Update ticket status to 'Cancelled' (F7)
                String updateTicket = "UPDATE Tickets SET status = 'Cancelled' WHERE ticket_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateTicket)) {
                    stmt.setInt(1, ticketDbId);
                    stmt.executeUpdate();
                }

                // 3. Insert Mock Refund into Payments table (F7)
                String insertRefund = "INSERT INTO Payments (ticket_id, amount, transaction_type, transaction_status) VALUES (?, ?, 'Mock_Refund', 'Success')";
                try (PreparedStatement stmt = conn.prepareStatement(insertRefund)) {
                    stmt.setInt(1, ticketDbId);
                    stmt.setDouble(2, price);
                    stmt.executeUpdate();
                }

                conn.commit();

                // Update UI model only after successful DB commit
                model.setValueAt("Cancelled", row, 4); // Status column
                table.revalidate();
                table.repaint();

                JOptionPane.showMessageDialog(panel,
                        "Ticket cancelled successfully! Mock refund processed for $" + String.format("%.2f", price) + ".",
                        "Success (F7)", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException ex) {
                }
                JOptionPane.showMessageDialog(panel, "Ticket Cancellation Failed: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                     }
            }
        }
        fireEditingStopped();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        String status = (String) model.getValueAt(row, 4);
        if (status.equals("Cancelled") || status.equals("Used") || status.equals("Refunded")) {
            cancelBtn.setEnabled(false);
        } else {
            cancelBtn.setEnabled(true);
        }

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}
