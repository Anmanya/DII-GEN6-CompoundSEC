package view;

import ggnamy.controller.AccessManager;
import ggnamy.model.AccessCard;
import ggnamy.model.AccessEvent;
import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.swing.Timer;

public class FancyAccessUI {
    private AccessManager manager;
    private JFrame loginFrame;
    private JFrame mainFrame;
    private AccessCard currentCard;
    private int cardIdCounter = 3;
    private String currentRole;
    private String currentFloor;

    public FancyAccessUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        manager = new AccessManager();
        showRoleSelection();
    }
    private boolean isAccessAllowed(String username) { //Strategy Pattern ตรวจสอบการเข้าถึงตามผู้ใช้แต่ละคน
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (username.equalsIgnoreCase("Ping")) {
            return today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY;
        } else if (username.equalsIgnoreCase("Fai")) {
            return today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY;
        }
        return true; // ผู้ใช้คนอื่นสามารถเข้าได้ทุกวัน
    }

    // --- แก้ไขฟังก์ชันเข้าสู่ชั้นสำหรับ User ---
    private boolean canEnterFloor(String username) {
        if (!isAccessAllowed(username)) {
            JOptionPane.showMessageDialog(null,
                    "Access denied: " + username + " is not allowed today.",
                    "Access Restricted", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // --- สร้างปุ่มที่มีสไตล์ ---
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) { //  Decorator Pattern เช่นเพิ่มวัน
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 50));
        button.setBackground(bgColor);  // Background color
        button.setForeground(Color.WHITE);  // White text
        button.setFont(new Font("Arial", Font.BOLD, 16));  // ใช้ฟอนต์ตัวหนา
        button.setFocusPainted(false);  // Remove focus ring
        button.setBorder(BorderFactory.createLineBorder(new Color(30, 60, 90), 2));  // Blue border
        button.setOpaque(true);
        button.setBorderPainted(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);  // เปลี่ยนสีเมื่อเมาส์เข้า
                button.setFont(new Font("Arial", Font.BOLD, 18));  // ทำให้ปุ่มใหญ่ขึ้นเล็กน้อย
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);  // รีเซ็ตสีเมื่อเมาส์ออก
                button.setFont(new Font("Arial", Font.BOLD, 16));  // รีเซ็ตฟอนต์
            }
        });
        return button;
    }

    // --- หน้าจอเลือกบทบาท ---
    private void showRoleSelection() {
        JFrame frame = new JFrame("Select Role");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLayout(new BorderLayout(10, 10));

        // กำหนดสีพื้นหลังเป็น Gradient
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(70, 130, 180), 0, getHeight(), new Color(30, 60, 90));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        frame.add(contentPanel);
        contentPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Select Your Role", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));  // Larger font
        label.setForeground(Color.WHITE);
        contentPanel.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        JButton adminBtn = createStyledButton("Admin", new Color(70, 130, 180), new Color(100, 150, 220));
        JButton userBtn = createStyledButton("User", new Color(60, 179, 113), new Color(85, 215, 135));  // Different color for User
        panel.add(adminBtn);
        panel.add(userBtn);
        contentPanel.add(panel, BorderLayout.CENTER);

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
        frame.setSize(350, 200);
        frame.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel label = new JLabel("Enter Admin Password:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));  // Add border for input field
        JButton loginBtn = createStyledButton("Login", new Color(70, 130, 180), new Color(100, 150, 220));

        frame.add(label);
        frame.add(passwordField);
        frame.add(new JLabel("")); // ช่องว่าง
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String password = new String(passwordField.getPassword()).trim();
            if (password.equals("1234")) {
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
        loginFrame.setSize(350, 250);
        loginFrame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 16));
        userField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2)); // Add border
        JLabel dateLabel = new JLabel("Date/Time:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField dateField = new JTextField(LocalDateTime.now().toString());
        dateField.setFont(new Font("Arial", Font.PLAIN, 16));
        dateField.setEditable(false);
        dateField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2)); // Add border for date field
        JButton loginBtn = createStyledButton("Login", new Color(70, 130, 180), new Color(100, 150, 220));

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
            currentCard = manager.getCardForUser(username);
            if (currentCard == null) {
                currentCard = new AccessCard(String.format("%03d", cardIdCounter++), username);
                if (currentRole.equals("User")) {
                    currentCard.grantAccess("Guest Room", "1234");
                    currentCard.grantAccess("Dining Room", "1234");
                    currentCard.grantAccess("Fitness Center", "1234");
                    currentCard.grantAccess("Conference Room", "1234");
                } else {
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
        floorFrame.setSize(400, 450);
        floorFrame.setLayout(new BorderLayout(10, 10));
        floorFrame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label = new JLabel("Select Floor", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        floorFrame.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton lowBtn = createStyledButton("Low Floor", new Color(70, 130, 180), new Color(100, 150, 220));
        JButton mediumBtn = createStyledButton("Medium Floor", new Color(60, 179, 113), new Color(85, 215, 135));
        JButton highBtn = createStyledButton("High Floor", new Color(255, 69, 0), new Color(255, 99, 71));
        panel.add(lowBtn);
        panel.add(mediumBtn);
        panel.add(highBtn);

        if (currentRole.equals("Admin")) {
            JButton modifyBtn = createStyledButton("Modify Card", new Color(186, 85, 211), new Color(117, 115, 115));
            JButton addBtn = createStyledButton("Add Card", new Color(255, 215, 0), new Color(255, 239, 88));
            JButton revokeBtn = createStyledButton("Revoke Card", new Color(255, 105, 180), new Color(255, 182, 193));
            panel.add(modifyBtn);
            panel.add(addBtn);
            panel.add(revokeBtn);

            modifyBtn.addActionListener(e -> modifyAccess());
            addBtn.addActionListener(e -> addCard());
            revokeBtn.addActionListener(e -> revokeCard());
        }

        floorFrame.add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backBtn = createStyledButton("Back", new Color(169, 169, 169), new Color(192, 192, 192));
        bottomPanel.add(backBtn);
        floorFrame.add(bottomPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> {
            floorFrame.dispose();
            showLogin();
        });

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
    private void showMainMenu(String floor) {
        mainFrame = new JFrame("Main Menu - " + currentCard.getOwner() + " (" + floor + ")");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 500);
        mainFrame.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("Select a Room to Access on " + floor, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainFrame.add(label, BorderLayout.NORTH);

        String[] rooms = floor.equals("Low Floor") ? new String[]{"Guest Room", "Dining Room", "Restroom", "Lobby", "Storage"}
                : floor.equals("Medium Floor") ? new String[]{"Fitness Center", "Conference Room", "Meeting Room", "Break Room", "Office"}
                : new String[]{"Executive Office", "Board Room", "Private Lounge", "VIP Suite"};

        JPanel roomPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        for (String room : rooms) {
            JButton btn = createStyledButton(room, new Color(70, 130, 180), new Color(100, 150, 220));
            btn.setPreferredSize(new Dimension(150, 60));
            btn.addActionListener(e -> {
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
            JButton manageLogsBtn = createStyledButton("Manage Logs", new Color(70, 130, 180), new Color(100, 150, 220));
            manageLogsBtn.addActionListener(e -> showManageLogs());
            bottomPanel.add(manageLogsBtn);
        }
        JButton logoutBtn = createStyledButton("Logout", new Color(255, 69, 0), new Color(255, 99, 71));
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
        if (!canEnterFloor(currentCard.getOwner())) {
            return;
        }

        JFrame roomFrame = new JFrame("Access " + room);
        roomFrame.setSize(350, 200);
        roomFrame.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Enter password for " + room + ":", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        JPasswordField pwdField = new JPasswordField();
        pwdField.setFont(new Font("Arial", Font.PLAIN, 16));
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
        manageFrame.setSize(600, 400);
        manageFrame.setLayout(new BorderLayout(10, 10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> logList = new JList<>(listModel);
        for (AccessEvent event : manager.getLogs()) {
            listModel.addElement(event.toString());
        }
        JScrollPane scrollPane = new JScrollPane(logList);
        manageFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton closeBtn = createStyledButton("Close", new Color(255, 69, 0), new Color(255, 99, 71));
        bottomPanel.add(closeBtn);
        manageFrame.add(bottomPanel, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> manageFrame.dispose());

        manageFrame.setLocationRelativeTo(mainFrame);
        manageFrame.setVisible(true);
    }

    private void modifyAccess(){
        String username = JOptionPane.showInputDialog("Enter username to modify access:");
        if (username != null && !username.trim().isEmpty()) {
            AccessCard card = manager.getCardForUser(username);
            if (card != null) {
                String[] floors = {"Low Floor", "Medium Floor", "High Floor"};
                String selectedFloor = (String) JOptionPane.showInputDialog(
                        null, "Select Floor:", "Floor Selection",
                        JOptionPane.QUESTION_MESSAGE, null, floors, floors[0]);

                if (selectedFloor != null) {
                    String[] rooms;
                    switch (selectedFloor) {
                        case "Low Floor":
                            rooms = new String[]{"Guest Room", "Dining Room", "Restroom", "Lobby", "Storage"};
                            break;
                        case "Medium Floor":
                            rooms = new String[]{"Fitness Center", "Conference Room", "Meeting Room", "Break Room", "Office"};
                            break;
                        case "High Floor":
                            rooms = new String[]{"Executive Office", "Board Room", "Private Lounge", "VIP Suite", "Sky Lounge"};
                            break;
                        default:
                            return;
                    }

                    JList<String> roomList = new JList<>(rooms);
                    roomList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    JScrollPane scrollPane = new JScrollPane(roomList);
                    int option = JOptionPane.showConfirmDialog(null, scrollPane, "Modify Accessible Rooms", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        if (card.getAccessibleRooms() != null) {
                            card.getAccessibleRooms().clear();
                        }
                        for (String room : roomList.getSelectedValuesList()) {
                            card.grantAccess(room, "1234");
                        }
                        JOptionPane.showMessageDialog(null, "Access permissions updated successfully.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        }
    }

    private void addCard(){
        String username = JOptionPane.showInputDialog("Enter new username:");
        if (username != null && !username.trim().isEmpty()) {
            String[] floors = {"Low Floor", "Medium Floor", "High Floor"};
            String selectedFloor = (String) JOptionPane.showInputDialog(
                    null, "Select Floor:", "Floor Selection",
                    JOptionPane.QUESTION_MESSAGE, null, floors, floors[0]);

            if (selectedFloor != null) {
                String[] rooms;
                switch (selectedFloor) {
                    case "Low Floor":
                        rooms = new String[]{"Guest Room", "Dining Room", "Restroom", "Lobby", "Storage"};
                        break;
                    case "Medium Floor":
                        rooms = new String[]{"Fitness Center", "Conference Room", "Meeting Room", "Break Room", "Office"};
                        break;
                    case "High Floor":
                        rooms = new String[]{"Executive Office", "Board Room", "Private Lounge", "VIP Suite", "Sky Lounge"};
                        break;
                    default:
                        return;
                }

                JList<String> roomList = new JList<>(rooms);
                roomList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                JScrollPane scrollPane = new JScrollPane(roomList);
                int option = JOptionPane.showConfirmDialog(null, scrollPane, "Select Accessible Rooms", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    AccessCard newCard = new AccessCard(String.format("%03d", cardIdCounter++), username);
                    for (String room : roomList.getSelectedValuesList()) {
                        newCard.grantAccess(room, "1234");
                    }
                    manager.registerCard(newCard);
                    JOptionPane.showMessageDialog(null, "Card added successfully for " + username);
                }
            }
        }
    }

    private void revokeCard(){
        String username = JOptionPane.showInputDialog("Enter username to revoke access card:");
        if (username != null && !username.trim().isEmpty()) {
            AccessCard card = manager.getCardForUser(username);
            if (card != null) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to revoke the access permissions for " + username + "?",
                        "Confirm Revocation",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    card.getAccessibleRooms().clear();
                    JOptionPane.showMessageDialog(null, "Access permissions for " + username + " have been revoked.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FancyAccessUI::new);
    }

}

