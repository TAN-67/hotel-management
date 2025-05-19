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

    public List<User> searchUsers(String username) throws SQLException {
        return userDAO.searchUsers(username);
    }

    public void addUser(User user) throws SQLException {
        if (userDAO.isUsernameExists(user.getUsername())) {
            throw new SQLException("Username already exists!");
        }
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new SQLException("Username and password cannot be empty!");
        }
        // Kiểm tra số lượng Admin
        if (user.getRole().equals("ADMIN")) {
            List<User> users = getAllUsers();
            long adminCount = users.stream().filter(u -> u.getRole().equals("ADMIN")).count();
            if (adminCount > 0) {
                throw new SQLException("Only one Admin is allowed!");
            }
        }
        userDAO.addUser(user);
    }

    public void updateUser(User user) throws SQLException {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new SQLException("Username and password cannot be empty!");
        }
        // Ngăn chặn thay đổi vai trò thành ADMIN nếu đã có Admin
        List<User> users = getAllUsers();
        long adminCount = users.stream().filter(u -> u.getRole().equals("ADMIN")).count();
        if (user.getRole().equals("ADMIN") && adminCount > 0 && users.stream().noneMatch(u -> u.getId() == user.getId() && u.getRole().equals("ADMIN"))) {
            throw new SQLException("Only one Admin is allowed!");
        }
        userDAO.updateUser(user);
    }

    public void deleteUser(int id) throws SQLException {
        userDAO.deleteUser(id);
    }
}