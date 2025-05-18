import java.sql.SQLException;
import java.util.List;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();

    public List<Booking> getAllBookings() throws SQLException {
        return bookingDAO.getAllBookings();
    }

    public void addBooking(Booking booking) throws SQLException {
        bookingDAO.addBooking(booking);
    }

    public void updateBooking(Booking booking) throws SQLException {
        bookingDAO.updateBooking(booking);
    }

    public void deleteBooking(int id) throws SQLException {
        bookingDAO.deleteBooking(id);
    }
}