import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws SQLException {
        return userDAO.login(username, password);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }

    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public void deleteUser(int id) throws SQLException {
        userDAO.deleteUser(id);
    }
}