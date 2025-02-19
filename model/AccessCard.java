package ggnamy.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccessCard {
    private String cardId;
    private String owner;
    private Map<String, String> accessibleRooms; // key: room name, value: password
    private LocalDateTime issuedAt;

    public AccessCard(String cardId, String owner) {
        this.cardId = cardId;
        this.owner = owner;
        this.accessibleRooms = new HashMap<>();
         this.issuedAt = LocalDateTime.now();
    }


    // กำหนดสิทธิ์ให้เข้าห้องพร้อมรหัสผ่าน
    public void grantAccess(String room, String password) {
        accessibleRooms.put(room, password);
    }

    // ตรวจสอบว่าผู้ใช้มีสิทธิ์เข้าห้องและรหัสผ่านตรงหรือไม่
    public boolean hasAccess(String room, String password) {
        if (!accessibleRooms.containsKey(room)) {
            return false;
        }
        return accessibleRooms.get(room).equals(password);
    }

    public String getOwner() {
        return owner;
    }

    // ดึงรายชื่อห้องที่ผู้ใช้มีสิทธิ์เข้าถึง
    public Set<String> getAccessibleRooms() {
        return accessibleRooms.keySet();
    }
}
