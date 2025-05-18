import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookingManagementPanel extends JPanel {
    private BookingService bookingService = new BookingService();
    private RoomService roomService = new RoomService();
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public BookingManagementPanel() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"ID", "Room Number", "Customer Name", "Check-In", "Check-Out", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> roomCombo = new JComboBox<>();
        try {
            List<Room> rooms = roomService.getAllRooms();
            for (Room room : rooms) {
                roomCombo.addItem(room.getId() + " - " + room.getRoomNumber());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
        }

        JTextField customerNameField = new JTextField();
        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"PENDING", "CONFIRMED", "CANCELLED"});

        formPanel.add(new JLabel("Room:"));
        formPanel.add(roomCombo);
        formPanel.add(new JLabel("Customer Name:"));
        formPanel.add(customerNameField);
        formPanel.add(new JLabel("Check-In (yyyy-MM-dd):"));
        formPanel.add(checkInField);
        formPanel.add(new JLabel("Check-Out (yyyy-MM-dd):"));
        formPanel.add(checkOutField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo);

        JButton addButton = new JButton("Add Booking");
        addButton.addActionListener(e -> {
            try {
                int roomId = Integer.parseInt(((String) roomCombo.getSelectedItem()).split(" - ")[0]);
                Booking booking = new Booking(0, roomId, "", customerNameField.getText(),
                        dateFormat.parse(checkInField.getText()), dateFormat.parse(checkOutField.getText()),
                        (String) statusCombo.getSelectedItem());
                bookingService.addBooking(booking);
                refreshTable();
            } catch (SQLException | ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        formPanel.add(addButton);

        JButton updateButton = new JButton("Update Booking");
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                    Booking booking = new Booking(bookingId, 0, "", customerNameField.getText(),
                            dateFormat.parse(checkInField.getText()), dateFormat.parse(checkOutField.getText()),
                            (String) statusCombo.getSelectedItem());
                    bookingService.updateBooking(booking);
                    refreshTable();
                } catch (SQLException | ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking!");
            }
        });
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Booking");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                    bookingService.deleteBooking(bookingId);
                    refreshTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking!");
            }
        });
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Booking> bookings = bookingService.getAllBookings();
            for (Booking booking : bookings) {
                tableModel.addRow(new Object[]{
                        booking.getId(), booking.getRoomNumber(), booking.getCustomerName(),
                        dateFormat.format(booking.getCheckInDate()), dateFormat.format(booking.getCheckOutDate()),
                        booking.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}