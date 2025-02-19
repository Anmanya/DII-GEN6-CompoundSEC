package ggnamy.controller;

import ggnamy.model.AccessCard;
import ggnamy.model.AccessEvent;

import java.util.ArrayList;
import java.util.List;

public class AccessManager {
    private List<AccessCard> cards;
    private List<AccessEvent> logs;

    public AccessManager() {
        this.cards = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    public void registerCard(AccessCard card) {
        cards.add(card);
    }

    // ตรวจสอบการเข้าถึงโดยใช้ชื่อ, ห้อง และรหัสผ่าน
    public boolean attemptAccess(String owner, String room, String password) {
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

    // ค้นหาบัตรของผู้ใช้ตามชื่อ
    public AccessCard getCardForUser(String owner) {
        for (AccessCard card : cards) {
            if (card.getOwner().equals(owner)) {
                return card;
            }
        }
        return null;
    }

    public List<AccessEvent> getLogs() {
        return logs;
    }
}
