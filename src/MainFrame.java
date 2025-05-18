import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(User user) {
        setTitle("Hotel Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        if (user.getRole().equals("ADMIN")) {
            tabbedPane.addTab("Room Management", new RoomManagementPanel());
            tabbedPane.addTab("Booking Management", new BookingManagementPanel());
            tabbedPane.addTab("User Management", new UserManagementPanel()); // Chưa triển khai
        } else {
            tabbedPane.addTab("Booking Management", new BookingManagementPanel());
            tabbedPane.addTab("Room View", new RoomManagementPanel(true)); // Chỉ xem
        }

        add(tabbedPane);
    }
}