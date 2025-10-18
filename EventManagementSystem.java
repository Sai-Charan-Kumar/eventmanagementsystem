import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Event Management System - Desktop UI
 * Comprehensive Swing-based interface for managing events, venues, and attendees
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
 * Login Frame - User authentication
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

        // Simulate login - in real app, validate against database
        if (role.equals("Organizer")) {
            new OrganizerDashboard(email).setVisible(true);
        } else {
            new AttendeeDashboard(email).setVisible(true);
        }
        dispose();
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
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Registration Frame
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

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPass)) {
            showError("Passwords do not match");
            return;
        }

        if (password.length() < 8) {
            showError("Password must be at least 8 characters");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Account created successfully!\nYou can now login.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        new LoginFrame().setVisible(true);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Organizer Dashboard
 */
class OrganizerDashboard extends JFrame {
    private String userEmail;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public OrganizerDashboard(String email) {
        this.userEmail = email;
        setTitle("Organizer Dashboard - Event Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Sidebar
        JPanel sidebar = createSidebar();

        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(EventManagementSystem.BG_COLOR);

        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createEventsPanel(), "events");
        contentPanel.add(createVenuesPanel(), "venues");
        contentPanel.add(createAttendeesPanel(), "attendees");
        contentPanel.add(createReportsPanel(), "reports");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

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
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header
        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(EventManagementSystem.BG_COLOR);

        statsPanel.add(createStatCard("Total Events", "12", EventManagementSystem.PRIMARY_COLOR, "📅"));
        statsPanel.add(createStatCard("Active Events", "5", EventManagementSystem.SUCCESS_COLOR, "✓"));
        statsPanel.add(createStatCard("Total Attendees", "458", EventManagementSystem.SECONDARY_COLOR, "👥"));
        statsPanel.add(createStatCard("Revenue", "$12,450", EventManagementSystem.WARNING_COLOR, "💰"));

        // Recent events
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(EventManagementSystem.CARD_BG);
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel recentLabel = new JLabel("Recent Events");
        recentLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        String[] columns = {"Event Name", "Date", "Venue", "Attendees", "Status"};
        Object[][] data = {
                {"Tech Conference 2025", "2025-11-15", "Convention Center", "120/150", "Active"},
                {"Music Festival", "2025-12-01", "City Stadium", "500/500", "Sold Out"},
                {"Workshop Series", "2025-10-25", "Training Room A", "25/30", "Active"}
        };

        JTable recentTable = new JTable(data, columns);
        styleTable(recentTable);
        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        recentPanel.add(recentLabel, BorderLayout.NORTH);
        recentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(EventManagementSystem.BG_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(recentPanel, BorderLayout.SOUTH);

        return panel;
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
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header with add button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.BG_COLOR);

        JLabel titleLabel = new JLabel("My Events");
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

        // Events table
        String[] columns = {"ID", "Event Name", "Date", "Time", "Venue", "Capacity", "Sold", "Status", "Actions"};
        Object[][] data = {
                {"E001", "Tech Conference 2025", "2025-11-15", "09:00 AM", "Convention Center", "150", "120", "Active", ""},
                {"E002", "Music Festival", "2025-12-01", "06:00 PM", "City Stadium", "500", "500", "Sold Out", ""},
                {"E003", "Workshop Series", "2025-10-25", "02:00 PM", "Training Room A", "30", "25", "Active", ""},
                {"E004", "Annual Gala", "2025-11-30", "07:00 PM", "Grand Ballroom", "200", "185", "Active", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only actions column editable
            }
        };

        JTable eventsTable = new JTable(model);
        styleTable(eventsTable);
        eventsTable.setRowHeight(40);

        // Add action buttons in last column
        eventsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        eventsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(eventsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createVenuesPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.BG_COLOR);

        JLabel titleLabel = new JLabel("Venue Management");
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

        // Venues table
        String[] columns = {"Venue ID", "Venue Name", "Location", "Capacity", "Type", "Availability", "Actions"};
        Object[][] data = {
                {"V001", "Convention Center", "Downtown, Main Street", "500", "Conference", "Available", ""},
                {"V002", "City Stadium", "Sports Complex", "1000", "Outdoor", "Booked", ""},
                {"V003", "Training Room A", "Business Park, Block B", "50", "Workshop", "Available", ""},
                {"V004", "Grand Ballroom", "Luxury Hotel", "300", "Banquet", "Available", ""},
                {"V005", "Open Air Theater", "Central Park", "800", "Outdoor", "Available", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        JTable venuesTable = new JTable(model);
        styleTable(venuesTable);
        venuesTable.setRowHeight(40);

        venuesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        venuesTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(venuesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendeesPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header with search
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(EventManagementSystem.BG_COLOR);

        JLabel titleLabel = new JLabel("Attendee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(EventManagementSystem.BG_COLOR);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(EventManagementSystem.SECONDARY_COLOR);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        // Attendees table
        String[] columns = {"Attendee ID", "Name", "Email", "Event", "Ticket ID", "Status", "Check-In"};
        Object[][] data = {
                {"A001", "John Smith", "john@example.com", "Tech Conference", "T12345", "Confirmed", ""},
                {"A002", "Sarah Johnson", "sarah@example.com", "Music Festival", "T12346", "Checked-In", ""},
                {"A003", "Mike Williams", "mike@example.com", "Workshop Series", "T12347", "Confirmed", ""},
                {"A004", "Emily Brown", "emily@example.com", "Tech Conference", "T12348", "Confirmed", ""},
                {"A005", "David Lee", "david@example.com", "Annual Gala", "T12349", "Confirmed", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        JTable attendeesTable = new JTable(model);
        styleTable(attendeesTable);
        attendeesTable.setRowHeight(40);

        attendeesTable.getColumn("Check-In").setCellRenderer(new CheckInButtonRenderer());
        attendeesTable.getColumn("Check-In").setCellEditor(new CheckInButtonEditor(new JCheckBox()));

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

        JLabel titleLabel = new JLabel("Analytics & Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Report options
        JPanel reportsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        reportsGrid.setBackground(EventManagementSystem.BG_COLOR);

        reportsGrid.add(createReportCard("Event Performance", "View detailed analytics for each event", "📊"));
        reportsGrid.add(createReportCard("Revenue Report", "Track ticket sales and revenue", "💰"));
        reportsGrid.add(createReportCard("Attendee Statistics", "Analyze attendee demographics", "👥"));
        reportsGrid.add(createReportCard("Venue Utilization", "Monitor venue booking rates", "🏢"));

        // Export section
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
                "PDF report exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE));

        JButton csvBtn = new JButton("Export as CSV");
        csvBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        csvBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        csvBtn.setForeground(Color.WHITE);
        csvBtn.setFocusPainted(false);
        csvBtn.setBorderPainted(false);
        csvBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        csvBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "CSV report exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE));

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

    public AttendeeDashboard(String email) {
        this.userEmail = email;
        setTitle("Attendee Dashboard - Event Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Top navigation bar
        JPanel navbar = createNavbar();

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(EventManagementSystem.BG_COLOR);

        contentPanel.add(createBrowseEventsPanel(), "browse");
        contentPanel.add(createMyTicketsPanel(), "tickets");
        contentPanel.add(createProfilePanel(), "profile");

        mainPanel.add(navbar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
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
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(EventManagementSystem.BG_COLOR);

        JTextField searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
                "All Categories", "Conference", "Concert", "Workshop", "Festival", "Sports"
        });
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(new JLabel("Search Events:"));
        searchPanel.add(searchField);
        searchPanel.add(categoryCombo);
        searchPanel.add(searchBtn);

        // Events grid
        JPanel eventsGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        eventsGrid.setBackground(EventManagementSystem.BG_COLOR);

        eventsGrid.add(createEventCard("Tech Conference 2025", "Nov 15, 2025",
                "Convention Center", "150", "120", "Conference"));
        eventsGrid.add(createEventCard("Music Festival", "Dec 01, 2025",
                "City Stadium", "500", "500", "Concert"));
        eventsGrid.add(createEventCard("Workshop Series", "Oct 25, 2025",
                "Training Room A", "30", "25", "Workshop"));
        eventsGrid.add(createEventCard("Annual Gala", "Nov 30, 2025",
                "Grand Ballroom", "200", "185", "Festival"));
        eventsGrid.add(createEventCard("Sports Championship", "Dec 15, 2025",
                "City Stadium", "1000", "750", "Sports"));
        eventsGrid.add(createEventCard("Art Exhibition", "Oct 28, 2025",
                "Gallery Hall", "100", "45", "Exhibition"));

        JScrollPane scrollPane = new JScrollPane(eventsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEventCard(String name, String date, String venue,
                                   String capacity, String sold, String category) {
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

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (sold.equals(capacity)) {
            registerBtn.setText("Sold Out");
            registerBtn.setBackground(EventManagementSystem.DANGER_COLOR);
            registerBtn.setEnabled(false);
        } else {
            registerBtn.addActionListener(e ->
                    new TicketPurchaseDialog(this, name).setVisible(true));
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
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Tickets");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Tickets table
        String[] columns = {"Ticket ID", "Event Name", "Date", "Time", "Venue", "Status", "Actions"};
        Object[][] data = {
                {"T12345", "Tech Conference 2025", "Nov 15, 2025", "09:00 AM", "Convention Center", "Confirmed", ""},
                {"T12348", "Annual Gala", "Nov 30, 2025", "07:00 PM", "Grand Ballroom", "Confirmed", ""},
                {"T12350", "Art Exhibition", "Oct 28, 2025", "10:00 AM", "Gallery Hall", "Confirmed", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        JTable ticketsTable = new JTable(model);
        styleTable(ticketsTable);
        ticketsTable.setRowHeight(45);

        ticketsTable.getColumn("Actions").setCellRenderer(new TicketActionRenderer());
        ticketsTable.getColumn("Actions").setCellEditor(new TicketActionEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(ticketsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(EventManagementSystem.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Profile card
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(EventManagementSystem.CARD_BG);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        profileCard.setMaximumSize(new Dimension(600, 500));

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileCard.add(avatarLabel);
        profileCard.add(Box.createVerticalStrut(20));

        profileCard.add(createProfileField("Email", userEmail));
        profileCard.add(Box.createVerticalStrut(15));
        profileCard.add(createProfileField("Name", "John Doe"));
        profileCard.add(Box.createVerticalStrut(15));
        profileCard.add(createProfileField("Phone", "+1 234-567-8900"));
        profileCard.add(Box.createVerticalStrut(15));
        profileCard.add(createProfileField("Member Since", "September 2025"));

        profileCard.add(Box.createVerticalStrut(25));

        JButton editBtn = new JButton("Edit Profile");
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editBtn.setForeground(Color.WHITE);
        editBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileCard.add(editBtn);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(EventManagementSystem.BG_COLOR);
        centerPanel.add(profileCard);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfileField(String label, String value) {
        JPanel field = new JPanel(new BorderLayout(10, 5));
        field.setBackground(EventManagementSystem.CARD_BG);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

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
 * Create Event Dialog
 */
class CreateEventDialog extends JDialog {
    private JTextField nameField, capacityField;
    private JComboBox<String> venueCombo, categoryCombo;
    private JSpinner dateSpinner, timeSpinner;
    private JTextArea descArea;

    public CreateEventDialog(JFrame parent) {
        super(parent, "Create New Event", true);
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JLabel titleLabel = new JLabel("Create New Event");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(EventManagementSystem.BG_COLOR);

        // Event Name
        formPanel.add(createLabel("Event Name *"));
        nameField = createTextField();
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Category
        formPanel.add(createLabel("Category *"));
        categoryCombo = new JComboBox<>(new String[]{
                "Conference", "Concert", "Workshop", "Festival", "Sports", "Exhibition"
        });
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(categoryCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Venue
        formPanel.add(createLabel("Venue *"));
        venueCombo = new JComboBox<>(new String[]{
                "Convention Center", "City Stadium", "Training Room A",
                "Grand Ballroom", "Open Air Theater"
        });
        venueCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        venueCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(venueCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Date
        formPanel.add(createLabel("Event Date *"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(dateSpinner);
        formPanel.add(Box.createVerticalStrut(15));

        // Time
        formPanel.add(createLabel("Event Time *"));
        SpinnerDateModel timeModel = new SpinnerDateModel();
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

        if (name.isEmpty() || capacity.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Integer.parseInt(capacity);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Capacity must be a valid number",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Event '" + name + "' created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
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
        addBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Venue added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

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
}

/**
 * Ticket Purchase Dialog
 */
class TicketPurchaseDialog extends JDialog {
    private String eventName;
    private JSpinner quantitySpinner;
    private JComboBox<String> ticketTypeCombo;

    public TicketPurchaseDialog(JFrame parent, String eventName) {
        super(parent, "Purchase Ticket", true);
        this.eventName = eventName;
        setSize(450, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(EventManagementSystem.BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Purchase Ticket");
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
        ticketTypeCombo = new JComboBox<>(new String[]{"Digital Ticket", "Physical Ticket"});
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
        formPanel.add(quantitySpinner);
        formPanel.add(Box.createVerticalStrut(20));

        // Price info
        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setBackground(EventManagementSystem.BG_COLOR);
        pricePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel priceLabel = new JLabel("Total Amount:");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(EventManagementSystem.TEXT_PRIMARY);

        JLabel amountLabel = new JLabel("$50.00");
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

        JButton purchaseBtn = new JButton("Confirm Purchase");
        purchaseBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        purchaseBtn.setForeground(Color.WHITE);
        purchaseBtn.setBackground(EventManagementSystem.SUCCESS_COLOR);
        purchaseBtn.setFocusPainted(false);
        purchaseBtn.setBorderPainted(false);
        purchaseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        purchaseBtn.addActionListener(e -> {
            String ticketId = "T" + (int)(Math.random() * 100000);
            JOptionPane.showMessageDialog(this,
                    "Ticket purchased successfully!\nTicket ID: " + ticketId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(purchaseBtn);

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
}

/**
 * Custom Table Cell Renderers and Editors
 */
class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton editBtn, deleteBtn;

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
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

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        editBtn = new JButton("Edit");
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        editBtn.setBackground(EventManagementSystem.SECONDARY_COLOR);
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.addActionListener(e -> fireEditingStopped());

        deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        deleteBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to delete this item?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(panel, "Item deleted successfully!");
            }
            fireEditingStopped();
        });

        panel.add(editBtn);
        panel.add(deleteBtn);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
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

        if (status.equals("Checked-In")) {
            setText("✓ Checked");
            setBackground(EventManagementSystem.SUCCESS_COLOR);
            setEnabled(false);
        } else {
            setText("Check In");
            setBackground(EventManagementSystem.PRIMARY_COLOR);
            setEnabled(true);
        }

        setForeground(Color.WHITE);
        setOpaque(true);
        return this;
    }
}

class CheckInButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int row;

    public CheckInButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(button,
                    "Attendee checked in successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        button.setText("Check In");
        button.setBackground(EventManagementSystem.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        return button;
    }

    public Object getCellEditorValue() {
        return "";
    }
}

class TicketActionRenderer extends JPanel implements TableCellRenderer {
    private JButton viewBtn, cancelBtn;

    public TicketActionRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        setOpaque(true);

        viewBtn = new JButton("View");
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setBorderPainted(false);

        cancelBtn = new JButton("Cancel");
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
        return this;
    }
}

class TicketActionEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton viewBtn, cancelBtn;

    public TicketActionEditor(JCheckBox checkBox) {
        super(checkBox);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        viewBtn = new JButton("View");
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewBtn.setBackground(EventManagementSystem.PRIMARY_COLOR);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setBorderPainted(false);
        viewBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel,
                    "Ticket Details:\nTicket ID: T12345\nStatus: Confirmed\nEvent: Tech Conference 2025",
                    "Ticket Details",
                    JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cancelBtn.setBackground(EventManagementSystem.DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to cancel this ticket?\nA refund will be processed.",
                    "Cancel Ticket",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(panel,
                        "Ticket cancelled successfully!\nRefund will be processed within 5-7 business days.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            fireEditingStopped();
        });

        panel.add(viewBtn);
        panel.add(cancelBtn);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        return panel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}