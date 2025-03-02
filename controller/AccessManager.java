package ggnamy.controller;

import ggnamy.model.AccessCard;
import ggnamy.model.AccessEvent;

import java.util.ArrayList;
import java.util.List;

public class AccessManager {
    private final List<AccessCard> cards;
    private final List<AccessEvent> logs;

    public AccessManager() { //Strategy Pattern มันช่วยให้สามารถแยกการเข้าถึงออกจากกันโดยไม่ต้องแก้ไขโค้ดเดิมใน AccessCard หรือ AccessManager
        this.cards = new ArrayList<>(); //ลิสเพื่อเกบบ
        this.logs = new ArrayList<>();
    }

    public void registerCard(AccessCard card) {
        cards.add(card);
    }

    public boolean attemptAccess(String owner, String room, String password) { //Strategy Pattern ตรวจสอบสิทธิ์การเข้าถึงห้องช่วยให้สามารถสร้าง Strategy สำหรับการตรวจสอบการเข้าถึงประเภทต่าง ๆ ได้ โดยไม่ต้องแก้ไขโค้ดที่มีอยู่ใน AccessCard
        for (AccessCard card : cards) {
            if (card.getOwner().equals(owner)) {
                boolean granted = card.hasAccess(room, password);
                logs.add(new AccessEvent(owner, room, granted));
                return granted;
            }
        }
        logs.add(new AccessEvent(owner, room, false));
        return false;
    }

    public AccessCard getCardForUser(String owner) {
        for (AccessCard card : cards) {
            if (card.getOwner().equals(owner)) {
                return card;
            }
        }
        return null;
    }

    public List<AccessEvent> getLogs() { //คืนค่าลิสต์ของ AccessEvent
        return logs;
    }
}
