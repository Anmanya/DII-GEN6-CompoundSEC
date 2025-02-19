package ggnamy.view;

import ggnamy.controller.AccessManager;
import ggnamy.model.AccessCard;
import ggnamy.model.AccessEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class FancyAccessUI  {
    private AccessManager manager;
    private JFrame loginFrame;
    private JFrame mainFrame;
    private AccessCard currentCard;
    private int cardIdCounter = 3; // Counter for new user IDs
    private String currentRole;    // "Admin" or "User"
    private String currentFloor;   // "Low Floor", "Medium Floor", "High Floor"

    public FancyAccessUI() {
        // ใช้ Look-and-Feel ของระบบ
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) { }
        manager = new AccessManager();
        showRoleSelection();
    }

    // --- หน้าจอเลือกบทบาท ---
    private void showRoleSelection() {
        JFrame frame = new JFrame("Select Role");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("Select Your Role", SwingConstants.CENTER);
        frame.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        JButton adminBtn = new JButton("Admin");
        JButton userBtn = new JButton("User");
        adminBtn.setPreferredSize(new Dimension(100, 40));
        userBtn.setPreferredSize(new Dimension(100, 40));
        panel.add(adminBtn);
        panel.add(userBtn);
        frame.add(panel, BorderLayout.CENTER);

        adminBtn.addActionListener(e -> {
            currentRole = "Admin";
            frame.dispose();
            showAdminLogin();
        });
        userBtn.addActionListener(e -> {
            currentRole = "User";
            frame.dispose();
            showLogin();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- หน้าจอ Admin Login ---
    private void showAdminLogin() {
        JFrame frame = new JFrame("Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel label = new JLabel("Enter Admin Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        frame.add(label);
        frame.add(passwordField);
        frame.add(new JLabel("")); // ช่องว่าง
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String password = new String(passwordField.getPassword()).trim();
            if (password.equals("12345678")) {
                frame.dispose();
                showLogin();
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- หน้าจอ Login สำหรับ Admin และ User ---
    private void showLogin() {
        loginFrame = new JFrame("Secure Access System - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel dateLabel = new JLabel("Date/Time:");
        JTextField dateField = new JTextField(LocalDateTime.now().toString());
        dateField.setEditable(false);
        JButton loginBtn = new JButton("Login");

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(dateLabel);
        loginFrame.add(dateField);
        loginFrame.add(new JLabel("")); // ช่องว่าง
        loginFrame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter a username.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // ดึงหรือสร้าง AccessCard ใหม่
            currentCard = manager.getCardForUser(username);
            if (currentCard == null) {
                currentCard = new AccessCard(String.format("%03d", cardIdCounter++), username);
                if (currentRole.equals("User")) {
                    // สำหรับผู้ใช้งานทั่วไป ให้กำหนดรหัสผ่านสำหรับบางห้องเท่านั้น
                    currentCard.grantAccess("Guest Room", "1234");
                    currentCard.grantAccess("Dining Room", "1234");
                    currentCard.grantAccess("Fitness Center", "1234");
                    currentCard.grantAccess("Conference Room", "1234");
                    // ผู้ใช้ทั่วไปไม่มีสิทธิ์เข้าห้องใน High Floor
                } else { // Admin
                    // Admin เข้าถึงห้องทุกห้องได้โดยไม่ต้องใส่รหัสผ่าน
                    currentCard.grantAccess("Guest Room", "");
                    currentCard.grantAccess("Dining Room", "");
                    currentCard.grantAccess("Fitness Center", "");
                    currentCard.grantAccess("Conference Room", "");
                    currentCard.grantAccess("Executive Office", "");
                    currentCard.grantAccess("Board Room", "");
                    currentCard.grantAccess("Private Lounge", "");
                    currentCard.grantAccess("VIP Suite", "");
                }
                manager.registerCard(currentCard);
            }
            loginFrame.dispose();
            showFloorSelection();
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    // --- หน้าจอเลือกชั้น ---
    private void showFloorSelection() {
        JFrame floorFrame = new JFrame("Select Floor");
        floorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        floorFrame.setSize(350, 150);
        floorFrame.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("Select a Floor", SwingConstants.CENTER);
        floorFrame.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        JButton lowBtn = new JButton("Low Floor");
        JButton mediumBtn = new JButton("Medium Floor");
        JButton highBtn = new JButton("High Floor");
        lowBtn.setPreferredSize(new Dimension(100, 40));
        mediumBtn.setPreferredSize(new Dimension(100, 40));
        highBtn.setPreferredSize(new Dimension(100, 40));
        panel.add(lowBtn);
        panel.add(mediumBtn);
        panel.add(highBtn);
        floorFrame.add(panel, BorderLayout.CENTER);

        lowBtn.addActionListener(e -> {
            currentFloor = "Low Floor";
            floorFrame.dispose();
            showMainMenu(currentFloor);
        });
        mediumBtn.addActionListener(e -> {
            currentFloor = "Medium Floor";
            floorFrame.dispose();
            showMainMenu(currentFloor);
        });
        highBtn.addActionListener(e -> {
            currentFloor = "High Floor";
            floorFrame.dispose();
            showMainMenu(currentFloor);
        });

        floorFrame.setLocationRelativeTo(null);
        floorFrame.setVisible(true);
    }

    // --- หน้าจอ Main Menu แสดงตัวเลือกห้องตามชั้น ---
    // --- หน้าจอ Main Menu แสดงตัวเลือกห้องตามชั้น ---
    private void showMainMenu(String floor) {
        mainFrame = new JFrame("Main Menu - " + currentCard.getOwner() + " (" + floor + ")");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 400);
        mainFrame.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("Select a Room to Access on " + floor, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainFrame.add(label, BorderLayout.NORTH);

        // กำหนดรายชื่อห้อง (ทั้งหมด) สำหรับแต่ละชั้น
        String[] rooms;
        if (floor.equals("Low Floor")) {
            rooms = new String[]{"Guest Room", "Dining Room", "Restroom", "Lobby", "Storage"};
        } else if (floor.equals("Medium Floor")) {
            rooms = new String[]{"Fitness Center", "Conference Room", "Meeting Room", "Break Room", "Office"};
        } else { // High Floor
            rooms = new String[]{"Executive Office", "Board Room", "Private Lounge", "VIP Suite"};
        }

        JPanel roomPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        for (String room : rooms) {
            JButton btn = new JButton(room);
            btn.setPreferredSize(new Dimension(150, 50));
            btn.addActionListener(e -> {
                // หากเป็น Admin ให้เข้าห้องได้ทุกห้องโดยไม่ตรวจสอบ
                if (currentRole.equals("Admin") || currentCard.getAccessibleRooms().contains(room)) {
                    if (currentRole.equals("User")) {
                        openRoomWindowForUser(room);
                    } else {
                        openRoomWindowForAdmin(room);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Access restricted: You do not have permission for " + room,
                            "Access Restricted", JOptionPane.WARNING_MESSAGE);
                }
            });
            roomPanel.add(btn);
        }
        mainFrame.add(roomPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        if (currentRole.equals("Admin")) {
            JButton manageLogsBtn = new JButton("Manage Logs");
            manageLogsBtn.addActionListener(e -> showManageLogs());
            bottomPanel.add(manageLogsBtn);
        }
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            mainFrame.dispose();
            showRoleSelection();
        });
        bottomPanel.add(logoutBtn);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // --- หน้าต่างสำหรับการเข้าห้อง (สำหรับ User) ---
    private void openRoomWindowForUser(String room) {
        JFrame roomFrame = new JFrame("Access " + room);
        roomFrame.setSize(300, 150);
        roomFrame.setLayout(new GridLayout(3, 1, 5, 5));

        JLabel label = new JLabel("Enter password for " + room + ":", SwingConstants.CENTER);
        JPasswordField pwdField = new JPasswordField();
        JPanel btnPanel = new JPanel();
        JButton enterBtn = new JButton("Enter");
        JButton backBtn = new JButton("Back");
        btnPanel.add(enterBtn);
        btnPanel.add(backBtn);

        roomFrame.add(label);
        roomFrame.add(pwdField);
        roomFrame.add(btnPanel);

        enterBtn.addActionListener(e -> {
            String password = new String(pwdField.getPassword()).trim();
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(roomFrame, "Please enter a password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean granted = manager.attemptAccess(currentCard.getOwner(), room, password);
            String msg = currentCard.getOwner() + " attempted to access " + room + " -> " + (granted ? "GRANTED" : "DENIED");
            JOptionPane.showMessageDialog(roomFrame, msg, "Access Result", JOptionPane.INFORMATION_MESSAGE);
            roomFrame.dispose();
        });

        backBtn.addActionListener(e -> roomFrame.dispose());

        roomFrame.setLocationRelativeTo(mainFrame);
        roomFrame.setVisible(true);
    }

    // --- สำหรับ Admin: เข้าห้องโดยไม่ต้องใส่รหัสผ่าน ---
    private void openRoomWindowForAdmin(String room) {
        AccessEvent event = new AccessEvent(currentCard.getOwner(), room, true);
        manager.getLogs().add(event);
        JOptionPane.showMessageDialog(mainFrame,
                "Admin " + currentCard.getOwner() + " has accessed " + room,
                "Access Granted",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // --- หน้าต่างสำหรับจัดการ Log (สำหรับ Admin เท่านั้น) ---
    private void showManageLogs() {
        JFrame manageFrame = new JFrame("Manage Logs");
        manageFrame.setSize(500, 300);
        manageFrame.setLayout(new BorderLayout(10, 10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> logList = new JList<>(listModel);
        for (AccessEvent event : manager.getLogs()) {
            listModel.addElement(event.toString());
        }
        JScrollPane scrollPane = new JScrollPane(logList);
        manageFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton deleteBtn = new JButton("Delete Selected");
        JButton addBtn = new JButton("Add Log");
        JButton closeBtn = new JButton("Close");
        bottomPanel.add(deleteBtn);
        bottomPanel.add(addBtn);
        bottomPanel.add(closeBtn);
        manageFrame.add(bottomPanel, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> {
            int selectedIndex = logList.getSelectedIndex();
            if (selectedIndex != -1) {
                manager.getLogs().remove(selectedIndex);
                listModel.remove(selectedIndex);
            }
        });

        addBtn.addActionListener(e -> {
            JTextField userField = new JTextField();
            JTextField roomField = new JTextField();
            String[] options = {"GRANTED", "DENIED"};
            JComboBox<String> accessCombo = new JComboBox<>(options);
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("User:"));
            panel.add(userField);
            panel.add(new JLabel("Room:"));
            panel.add(roomField);
            panel.add(new JLabel("Access:"));
            panel.add(accessCombo);
            int result = JOptionPane.showConfirmDialog(manageFrame, panel, "Add Log", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String user = userField.getText().trim();
                String room = roomField.getText().trim();
                boolean accessGranted = accessCombo.getSelectedItem().equals("GRANTED");
                AccessEvent newEvent = new AccessEvent(user, room, accessGranted);
                manager.getLogs().add(newEvent);
                listModel.addElement(newEvent.toString());
            }
        });

        closeBtn.addActionListener(e -> manageFrame.dispose());

        manageFrame.setLocationRelativeTo(mainFrame);
        manageFrame.setVisible(true);
    }

    // --- Main Method ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FancyAccessUI::new);
    }
}
