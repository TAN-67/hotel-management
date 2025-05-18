import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private UserService userService = new UserService();
    private DefaultTableModel tableModel;

    public UserManagementPanel() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"ID", "Username", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setName("user_management_form"); // Thay setIds bằng setName
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleCombo);

        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> {
            try {
                User user = new User(0, usernameField.getText(), passwordField.getText(), (String) roleCombo.getSelectedItem());
                userService.addUser(user);
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        formPanel.add(addButton);

        JButton updateButton = new JButton("Update User");
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int userId = (int) tableModel.getValueAt(selectedRow, 0);
                    User user = new User(userId, usernameField.getText(), passwordField.getText(), (String) roleCombo.getSelectedItem());
                    userService.updateUser(user);
                    refreshTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user!");
            }
        });
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int userId = (int) tableModel.getValueAt(selectedRow, 0);
                    userService.deleteUser(userId);
                    refreshTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user!");
            }
        });
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[]{
                        user.getId(), user.getUsername(), user.getRole()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}