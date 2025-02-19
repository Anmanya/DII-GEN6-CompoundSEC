package ggnamy.model;

import java.time.LocalDateTime;


public class  AccessEvent {
    private String user;
    private String room;
    private boolean accessGranted;
    private LocalDateTime timestamp;

    public AccessEvent(String user, String room, boolean accessGranted) {
        this.user = user;
        this.room = room;
        this.accessGranted = accessGranted;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String  toString() {
        return timestamp + " | User: " + user + " | Room: " + room + " | Access: " + (accessGranted ? "GRANTED" : "DENIED");
    }
}

