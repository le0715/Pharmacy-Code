package pharmacy.app;

import pharmacy.domain.*;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

//admin dashboard
/**
 *  "Manage user access" (UC08)
 *  "Generate report" (UC07)
 */
public class AdministratorDashboardFrame extends JFrame {

    private static final String[] USER_TABLE_COLUMN_NAMES =
            {"User ID", "Full Name", "Email", "Role", "Status"};

    private final PharmacyDataStore pharmacyDataStore = PharmacyDataStore.getInstance();
    private final AdministratorUser loggedInAdministrator;

    private final DefaultTableModel userTableModel;
    private JTable userAccountTable;
    private final JTextArea reportOutputArea = new JTextArea();

    public AdministratorDashboardFrame(AdministratorUser loggedInAdministrator) {
        super("Administrator Dashboard - " + loggedInAdministrator.getFullName());
        this.loggedInAdministrator = loggedInAdministrator;
        this.userTableModel = new DefaultTableModel(USER_TABLE_COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        buildUserInterface();
        refreshUserAccountTable();
    }

    private void buildUserInterface() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headingLabel = new JLabel("Welcome, " + loggedInAdministrator.getFullName(), JLabel.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headingLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage User Access", buildManageUserAccessPanel());
        tabbedPane.addTab("Generate Reports", buildGenerateReportsPanel());
        add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(event -> handleLogoutButtonClicked());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel buildManageUserAccessPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        userAccountTable = new JTable(userTableModel);
        userAccountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(userAccountTable), BorderLayout.CENTER);

        JButton activateButton = new JButton("Activate Selected");
        activateButton.addActionListener(event -> handleSetUserActiveStatus(true));

        JButton deactivateButton = new JButton("Deactivate Selected");
        deactivateButton.addActionListener(event -> handleSetUserActiveStatus(false));

        JPanel actionPanel = new JPanel();
        actionPanel.add(activateButton);
        actionPanel.add(deactivateButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildGenerateReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        reportOutputArea.setEditable(false);
        reportOutputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        panel.add(new JScrollPane(reportOutputArea), BorderLayout.CENTER);

        JButton inventoryReportButton = new JButton("Generate Inventory Report");
        inventoryReportButton.addActionListener(event ->
                reportOutputArea.setText(pharmacyDataStore.generateInventoryReport()));

        JButton salesReportButton = new JButton("Generate Sales Report");
        salesReportButton.addActionListener(event ->
                reportOutputArea.setText(pharmacyDataStore.generateSalesReport()));

        JPanel actionPanel = new JPanel();
        actionPanel.add(inventoryReportButton);
        actionPanel.add(salesReportButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshUserAccountTable() {
        userTableModel.setRowCount(0);
        List<SystemUser> allUsers = pharmacyDataStore.getAllRegisteredUsers();
        for (SystemUser user : allUsers) {
            userTableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getFullName(),
                    user.getEmailAddress(),
                    user.getUserRole(),
                    user.isActiveAccount() ? "Active" : "Inactive"
            });
        }
    }

    private void handleSetUserActiveStatus(boolean shouldBeActive) {
        int selectedRowIndex = userAccountTable.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user account first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String userId = (String) userTableModel.getValueAt(selectedRowIndex, 0);
        SystemUser targetUser = findUserById(userId);
        if (targetUser != null) {
            pharmacyDataStore.setUserAccountActive(targetUser, shouldBeActive);
            refreshUserAccountTable();
        }
    }

    private SystemUser findUserById(String userId) {
        for (SystemUser user : pharmacyDataStore.getAllRegisteredUsers()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    private void handleLogoutButtonClicked() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
