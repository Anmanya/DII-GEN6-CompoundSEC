
package ggnamy.model;

import java.time.LocalDateTime;

public class AccessEvent { // Factory Pattern ใช้ในการสร้าง AccessEvent ซึ่งจะใช้ในการสร้างอ็อบเจ็กต์ AccessEvent ทุกครั้งเมื่อเกิดเหตุการณ์การเข้าถึงห้อง
    private final String user;
    private final String room;
    private final boolean accessGranted;
    private final LocalDateTime timestamp;

    public AccessEvent(String user, String room, boolean accessGranted) {
        this.user = user;
        this.room = room;
        this.accessGranted = accessGranted;
        this.timestamp = LocalDateTime.now();
    }
//สร้าง Factory Method ที่ช่วยในการสร้าง AccessEvent โดยเราไม่ต้องมีการกำหนดโครงสร้างในที่เดียว
    @Override
    public String toString() {
        return timestamp + " | User: " + user + " | Room: " + room + " | Access: " + (accessGranted ? "GRANTED" : "DENIED");
    }
}
