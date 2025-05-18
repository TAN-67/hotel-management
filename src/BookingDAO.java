import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.room_number FROM bookings b JOIN rooms r ON b.room_id = r.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("customer_name"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"),
                        rs.getString("status")
                ));
            }
        }
        return bookings;
    }

    public void addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (room_id, customer_name, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getRoomId());
            stmt.setString(2, booking.getCustomerName());
            stmt.setDate(3, new java.sql.Date(booking.getCheckInDate().getTime()));
            stmt.setDate(4, new java.sql.Date(booking.getCheckOutDate().getTime()));
            stmt.setString(5, booking.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET customer_name = ?, check_in_date = ?, check_out_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getCustomerName());
            stmt.setDate(2, new java.sql.Date(booking.getCheckInDate().getTime()));
            stmt.setDate(3, new java.sql.Date(booking.getCheckOutDate().getTime()));
            stmt.setString(4, booking.getStatus());
            stmt.setInt(5, booking.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}