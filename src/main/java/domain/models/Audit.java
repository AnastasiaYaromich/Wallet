package domain.models;

/**
 * Audit class contains information about user actions.
 */
public class Audit {
    private String information;
    private int id;
    private String type;
    private String status;
    private Double balance;
    private String dateTime;
    private String note;

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "[" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", status='" + status + '\'' +
                ", balance='" + balance + '\'' +
                ", note='" + note + '\'' +
                ']';
    }
}
