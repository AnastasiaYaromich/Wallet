package domain.models;

/**
 * Transaction class contains information about user transactions.
 */
public class Transaction {
    private int id;
    private String transactionId;
    private String type;
    private String condition;
    private String note;


    public Transaction(String transactionId, String type, String condition, String note) {
        this.transactionId = transactionId;
        this.type = type;
        this.condition = condition;
        this.note = note;
    }

    public Transaction() {

    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[" +
                "transactionId='" + transactionId + '\'' +
                ", type='" + type + '\'' +
                ", condition='" + condition + '\'' +
                ", note='" + note + '\'' +
                ']';
    }
}
