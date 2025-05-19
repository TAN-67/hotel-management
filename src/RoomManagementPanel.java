import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RoomManagementPanel extends JPanel {
    private RoomService roomService = new RoomService();
    private DefaultTableModel tableModel;
    private boolean viewOnly;

    public RoomManagementPanel() {
        this(false);
    }

    public RoomManagementPanel(boolean viewOnly) {
        this.viewOnly = viewOnly;
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"ID", "Room Number", "Type", "Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form and Buttons
        if (!viewOnly) {
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField roomNumberField = new JTextField();
            JTextField typeField = new JTextField();
            JTextField priceField = new JTextField();
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"AVAILABLE", "OCCUPIED", "MAINTENANCE"});

            formPanel.add(new JLabel("Room Number:"));
            formPanel.add(roomNumberField);
            formPanel.add(new JLabel("Type:"));
            formPanel.add(typeField);
            formPanel.add(new JLabel("Price:"));
            formPanel.add(priceField);
            formPanel.add(new JLabel("Status:"));
            formPanel.add(statusCombo);

            JButton addButton = new JButton("Add Room");
            addButton.addActionListener(e -> {
                try {
                    Room room = new Room(0, roomNumberField.getText(), typeField.getText(),
                            Double.parseDouble(priceField.getText()), (String) statusCombo.getSelectedItem());
                    roomService.addRoom(room);
                    refreshTable();
                } catch (SQLException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            formPanel.add(addButton);

            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        int roomId = (int) tableModel.getValueAt(selectedRow, 0);
                        String newRoomNumber = roomNumberField.getText();
                        String newType = typeField.getText();
                        double newPrice = Double.parseDouble(priceField.getText());
                        String newStatus = (String) statusCombo.getSelectedItem();
                        // Cập nhật toàn bộ thông tin phòng
                        Room updatedRoom = new Room(roomId, newRoomNumber, newType, newPrice, newStatus);
                        updateRoomInDatabase(roomId, updatedRoom);
                        refreshTable();
                    } catch (SQLException | NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a room to update!");
                }
            });
            formPanel.add(updateButton);

            add(formPanel, BorderLayout.SOUTH);
        }

        refreshTable();
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Room> rooms = roomService.getAllRooms();
            for (Room room : rooms) {
                tableModel.addRow(new Object[]{
                        room.getId(), room.getRoomNumber(), room.getType(), room.getPrice(), room.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateRoomInDatabase(int id, Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_number = ?, type = ?, price = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getType());
            stmt.setDouble(3, room.getPrice());
            stmt.setString(4, room.getStatus());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        }
    }
}