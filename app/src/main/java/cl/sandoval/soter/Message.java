package cl.sandoval.soter;

import com.google.firebase.Timestamp;

public class Message {
    private String text;
    private String sender;
    private Timestamp timestamp;  // fuck google

    // Constructor vacío (necesario para Firestore)
    public Message() {
    }

    // Constructor con parámetros
    public Message(String text, String sender, Timestamp timestamp) {
        this.text = text;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Convert TS to Long???????
    public long getTimestampAsLong() {
        return timestamp != null ? timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000 : 0;
    }
}
