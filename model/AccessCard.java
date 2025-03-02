package ggnamy.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccessCard { //Singleton Pattern สามารถใช้ในกรณีที่ต้องการให้มีอินสแตนซ์เดียวของ AccessManager เพื่อจัดการการเข้าถึงทั้งหมดในระบขยจบล
    private final String cardId; //ป้องกันการสร้างหลายอินสแตนซ์
    private final String owner;
    private final Map<String, String> accessibleRooms;
    private final LocalDateTime issuedAt;

    public AccessCard(String cardId, String owner) {
        this.cardId = cardId;
        this.owner = owner;
        this.accessibleRooms = new HashMap<>();
        this.issuedAt = LocalDateTime.now();
    }

    public void grantAccess(String room, String password) {
        accessibleRooms.put(room, password);
    }

    public boolean hasAccess(String room, String password) {
        return accessibleRooms.containsKey(room) && accessibleRooms.get(room).equals(password);
    }

    public String getOwner() {
        return owner;
    }

    public Set<String> getAccessibleRooms() {
        return accessibleRooms.keySet();
    }

}

