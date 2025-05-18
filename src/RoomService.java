import java.sql.SQLException;
import java.util.List;

public class RoomService {
    private RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public void addRoom(Room room) throws SQLException {
        roomDAO.addRoom(room);
    }

    public void updateRoomStatus(int id, String status) throws SQLException {
        roomDAO.updateRoomStatus(id, status);
    }
}