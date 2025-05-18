import java.util.Date;

public class Booking {
    private int id;
    private int roomId;
    private String roomNumber;
    private String customerName;
    private Date checkInDate;
    private Date checkOutDate;
    private String status;

    public Booking(int id, int roomId, String roomNumber, String customerName, Date checkInDate, Date checkOutDate, String status) {
        this.id = id;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public String getCustomerName() { return customerName; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public String getStatus() { return status; }
}