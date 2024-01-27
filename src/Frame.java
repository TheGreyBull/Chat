import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Frame extends JFrame implements ActionListener {

    private Container mainPanel;
    protected static final Color darkBgColor = new Color(0x202020);
    protected static final Color lightBgColor = new Color(0x2c2c2c);
    private JPanelCustom leftMainPanel;
    private JPanelCustom chatButtonsPanel;
    private JPanelCustom messagesPanel;
    private JScrollPane scrollLeftMainPanel;
    private JScrollPane scrollCenterMainPanel;
    private JPanelCustom centerMainPanel;
    private JLabelCustom selectChatLabel;
    private JButtonCustom addNewChat;
    private JLabelCustom chatSelectedLabel;
    private JPanelCustom sendMessagePanel;
    private PlaceholderTextField messageField;
    private JButtonCustom sendButton;
    private JPanelCustom createChatPanel;
    private JPanelCustom chatFormPanel;
    private JLabelCustom createChatLabel;
    private JLabelCustom insertIpAddressLabel;
    private JLabelCustom insertPortLabel;
    private JLabelCustom insertNameLabel;
    private JTextField ipAddressField;
    private JTextField portField;
    private JTextField nameField;
    private JButtonCustom sendChatInformationButton;
    private final String username;
    private final int PORT;
    private Communication currentChatSelected;
    private ArrayList<Communication> communications;
    private ChatNameButton[] allChatButtons;

    protected Frame() {
        String input = "";
        while (input.isEmpty()) {
            input = JOptionPane.showInputDialog(this, "Inserire il nome", "Nome utente", JOptionPane.QUESTION_MESSAGE);
            if (input == null)
                System.exit(1);
        }
        username = input;
        PORT = 1200;
        settings();
        declaration();
        listenConnections();
        setVisible(true);
    }

    private void settings() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        mainPanel = this.getContentPane();
        mainPanel.setBackground(lightBgColor);

        communications = new ArrayList<>();
    }

    private void declaration() {
        leftMainPanel = new JPanelCustom();
        leftMainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0,0 ,2, Frame.darkBgColor),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        leftMainPanel.setLayout(new BorderLayout());

        centerMainPanel = new JPanelCustom();
        centerMainPanel.setLayout(new BorderLayout());
        centerMainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        selectChatLabel = new JLabelCustom("Seleziona una chat. Porta: " + PORT);
        selectChatLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0,2,0, Frame.darkBgColor),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        addNewChat = new JButtonCustom("Nuova chat");
        addNewChat.addActionListener(this);
        addNewChat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0,0, Frame.darkBgColor),
                BorderFactory.createEmptyBorder(16, 10, 16, 10)
        ));

        chatButtonsPanel = new JPanelCustom();
        chatButtonsPanel.setLayout(new BoxLayout(chatButtonsPanel, BoxLayout.PAGE_AXIS));

        scrollLeftMainPanel = new JScrollPane(chatButtonsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollLeftMainPanel.setBorder(null);
        scrollLeftMainPanel.setBackground(Frame.lightBgColor);
        scrollLeftMainPanel.getVerticalScrollBar().addAdjustmentListener(e ->
                e.getAdjustable().setValue(e.getAdjustable().getMaximum()));

        leftMainPanel.add(selectChatLabel, BorderLayout.NORTH);
        leftMainPanel.add(scrollLeftMainPanel, BorderLayout.CENTER);
        leftMainPanel.add(addNewChat, BorderLayout.SOUTH);

        // Starting to make the central part

        chatSelectedLabel = new JLabelCustom("Stai scrivendo a TheGreyBull");
        chatSelectedLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0,2,0, Frame.darkBgColor),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Main message viewer

        messagesPanel = new JPanelCustom();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.PAGE_AXIS));

        scrollCenterMainPanel = new JScrollPane(messagesPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCenterMainPanel.setBorder(null);
        scrollCenterMainPanel.setBackground(Frame.lightBgColor);

        sendMessagePanel = new JPanelCustom();
        sendMessagePanel.setLayout(new BorderLayout());
        sendMessagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0,0, Frame.darkBgColor),
                BorderFactory.createEmptyBorder(10, 10, 10, 0)
        ));

        messageField = new PlaceholderTextField();
        messageField.setPlaceholder("Scrivi un messaggio");
        messageField.setBackground(Frame.lightBgColor);
        messageField.setForeground(Color.WHITE);
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 26));
        messageField.setCaretColor(Color.LIGHT_GRAY);
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        messageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!e.isControlDown() && !e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        sendButton = new JButtonCustom(">");
        sendButton.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
        sendButton.setFont(new Font("SansSerif", Font.PLAIN, 26));
        sendButton.addActionListener(this);

        sendMessagePanel.add(messageField, BorderLayout.CENTER);
        sendMessagePanel.add(sendButton, BorderLayout.EAST);

        centerMainPanel.add(chatSelectedLabel, BorderLayout.NORTH);
        centerMainPanel.add(scrollCenterMainPanel, BorderLayout.CENTER);
        centerMainPanel.add(sendMessagePanel, BorderLayout.SOUTH);

        // Starting to make the part where the user writes information of a new chat

        createChatPanel = new JPanelCustom();
        createChatPanel.setLayout(new BorderLayout());
        createChatPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        createChatLabel = new JLabelCustom("Crea una nuova chat");
        createChatLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0,2,0, Frame.darkBgColor),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        chatFormPanel = new JPanelCustom();
        chatFormPanel.setBackground(Frame.lightBgColor);
        chatFormPanel.setLayout(new GridLayout(5, 1));
        chatFormPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));

        insertIpAddressLabel = new JLabelCustom("Indirizzo IP");
        insertIpAddressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
        insertPortLabel = new JLabelCustom("Porta");
        insertPortLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
        insertNameLabel = new JLabelCustom("Nome");
        insertNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        ipAddressField = new JTextField(20);
        textFieldSettings(ipAddressField);

        portField = new JTextField(5);
        textFieldSettings(portField);

        nameField = new JTextField(username, 20);
        nameField.setEditable(false);
        textFieldSettings(nameField);

        sendChatInformationButton = new JButtonCustom("Crea nuova chat");
        sendChatInformationButton.addActionListener(this);
        sendChatInformationButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanelCustom ipAddressPanel = new JPanelCustom();
        ipAddressPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ipAddressPanel.add(insertIpAddressLabel);
        ipAddressPanel.add(ipAddressField);
        JPanelCustom portPanel = new JPanelCustom();
        portPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        portPanel.add(insertPortLabel);
        portPanel.add(portField);
        JPanelCustom namePanel = new JPanelCustom();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(insertNameLabel);
        namePanel.add(nameField);
        JPanelCustom sendPanel = new JPanelCustom();
        sendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sendPanel.add(sendChatInformationButton);

        // The empty panel is used to leave a space between the title and the form
        chatFormPanel.add(new EmptyPanel());
        chatFormPanel.add(ipAddressPanel);
        chatFormPanel.add(portPanel);
        chatFormPanel.add(namePanel);
        chatFormPanel.add(sendPanel);

        createChatPanel.add(createChatLabel, BorderLayout.NORTH);
        createChatPanel.add(chatFormPanel, BorderLayout.CENTER);

        // mainPanel initial contents

        mainPanel.add(leftMainPanel, BorderLayout.WEST);
    }

    private void textFieldSettings(JTextField field) {
        field.setBackground(Frame.lightBgColor);
        field.setForeground(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 26));
        field.setCaretColor(Color.LIGHT_GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
    }

    private String getCurrentTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }

    private void connectToNewHost(String ipAddress, int port) {
        Socket connection;
        DataOutputStream out;
        BufferedReader in;
        String readUsername;
        try {
            connection = new Socket(ipAddress, port);
            out = new DataOutputStream(connection.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // The client sends its username. Finally, the client listens the server's username
            out.writeBytes(username + "\n");
            readUsername = in.readLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nella connessione");
            return;
        }

        Communication newCommunication = new Communication(readUsername, connection, out, in);
        if (checkUsername(newCommunication.getUsername()) == 1) {
            JOptionPane.showMessageDialog(null, "Hai gia' aggiunto questa chat!");
            return;
        }
        communications.add(newCommunication);
        updateChatButtons();
        createMessageListener(newCommunication);
    }

    private void listenConnections() {
        new Thread(() -> {
            ServerSocket welcomeSocket;
            try {
                welcomeSocket = new ServerSocket(PORT);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Errore nell'avvio del serverSocket");
                System.exit(1);
                return;
            }
            while (true) {
                try {
                    Socket newConnection = welcomeSocket.accept();
                    Communication newCommunication = getNewCommunication(newConnection);
                    if (checkUsername(newCommunication.getUsername()) == 1)
                        continue;
                    communications.add(newCommunication);
                    updateChatButtons();
                    createMessageListener(newCommunication);
                } catch (IOException ignore) {}
            }
        }).start();
    }

    private Communication getNewCommunication(Socket newConnection) throws IOException {
        DataOutputStream out = new DataOutputStream(newConnection.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(newConnection.getInputStream()));

        // The server listens to client's username. Finally, the server writes its username
        String readUsername = in.readLine();
        out.writeBytes(username + "\n");

        return new Communication(readUsername, newConnection, out, in);
    }

    private void updateChatButtons() {
        chatButtonsPanel.removeAll();
        allChatButtons = new ChatNameButton[communications.size()];
        // Adds a spacing between the border of the top JLabelCustom and the buttons
        chatButtonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        for (int i = 0; i < communications.size(); i++) {
            Communication communication = communications.get(i);
            String currentUsername = communication.getUsername();
            allChatButtons[i] = new ChatNameButton(currentUsername);
            allChatButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            allChatButtons[i].addActionListener(this);

            chatButtonsPanel.add(allChatButtons[i]);
            chatButtonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        chatButtonsPanel.revalidate();
        chatButtonsPanel.repaint();
    }

    private void createMessageListener(Communication communication) {
        new Thread(() -> {
            BufferedReader in = communication.getIn();
            while (true) {
                try {
                    String messageRead = in.readLine();
                    String currentTime = getCurrentTime();
                    communication.appendMessage(messageRead, currentTime);
                    if (currentChatSelected == communication)
                        loadMessages(communication);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, communication.getUsername() + " non risponde, chiusura della connessione.");
                    deleteConnection(communication);
                    return;
                }
            }
        }).start();
    }

    private void deleteConnection(Communication communication) {
        for (int i = 0; i < communications.size(); i++) {
            if (communication == communications.get(i)) {
                communication.closeConnection();
                communications.remove(i);
                break;
            }
        }
        if (communication == currentChatSelected) {
            mainPanel.remove(centerMainPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
            currentChatSelected = null;
        }
        updateChatButtons();
    }

    private void loadSelectedChat(int chatIndex) {
        mainPanel.remove(createChatPanel);

        // Edits centerMainPanel adding the selected chat
        Communication selectedChat = communications.get(chatIndex);
        currentChatSelected = selectedChat;

        chatSelectedLabel.setText("Stai scrivendo a " + selectedChat.getUsername());
        loadMessages(selectedChat);

        mainPanel.add(centerMainPanel, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void loadMessages(Communication chat) {
        String[] messages = chat.getMessages();
        String[] timeMessages = chat.getTimeMessages();
        messagesPanel.removeAll();

        messagesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        for (int i = 0; i < messages.length; i++) {
            JPanelCustom messageAreaPanel = getJPanelCustom(messages, i, timeMessages);

            messagesPanel.add(messageAreaPanel);
            messagesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        AdjustmentListener adjustmentListener = e ->
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());

        scrollCenterMainPanel.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
        new Thread(() -> {
            try {
                // Waits that the frame is fully loaded
                Thread.sleep(600);
                // Makes it possible to go up with the scrollbar
                scrollCenterMainPanel.getVerticalScrollBar().removeAdjustmentListener(adjustmentListener);
            } catch (InterruptedException ignore) {}
        }).start();

        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private static JPanelCustom getJPanelCustom(String[] messages, int i, String[] timeMessages) {
        String text = messages[i];
        // Checks if the message was written by the current host
        // If the first character of the message is 'Y', the message was written by the current host
        boolean isYourself = false;
        if (text.charAt(0) == 'Y') {
            isYourself = true;
            text = text.substring(1);
        }

        MessageArea messageArea = new MessageArea(text, 1, 25, isYourself, timeMessages[i]);

        JPanelCustom messageAreaPanel = new JPanelCustom();
        if (isYourself)
            messageAreaPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        else
            messageAreaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageAreaPanel.add(messageArea);
        return messageAreaPanel;
    }

    /**
     * Checks if you're already chatting with someone
     * @return 1 if the username already exists in your chats; 0 if the username doesn't exist in your chats
    * */
    private int checkUsername(String username) {
        for (Communication communication : communications) {
            if (communication.getUsername().equals(username))
                return 1;
        }
        return 0;
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (message.isEmpty())
            return;

        DataOutputStream out = currentChatSelected.getOut();
        String currentTime = getCurrentTime();
        try {
            out.writeBytes(message + "\n");
            // Adds a 'Y' to remember the message was written by this host
            currentChatSelected.appendMessage("Y" + message, currentTime);
            loadMessages(currentChatSelected);
        } catch (IOException ex) {
            return;
        }
        messageField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addNewChat) {
            addNewChat.setEnabled(false);
            mainPanel.remove(centerMainPanel);
            mainPanel.add(createChatPanel, BorderLayout.CENTER);
            mainPanel.revalidate();
        }
        else if (e.getSource() == sendChatInformationButton) {
            String ipAddress = ipAddressField.getText();
            if (ipAddress.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Indirizzo IP non valido");
                return;
            }

            int port;
            try {
                port = Integer.parseInt(portField.getText());
                // Cannot exceed port range:
                // (< 1024 are well-known ports, the last available port is 65535)
                if (port < 1024 || port > 65535)
                    throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Porta non valida");
                return;
            }

            // Deletes the content of the IP address and port field
            ipAddressField.setText("");
            portField.setText("");

            connectToNewHost(ipAddress, port);
        }
        else if (e.getSource() == sendButton) {
            sendMessage();
        }
        else {
            for (int i = 0; i < allChatButtons.length; i++) {
                if (e.getSource() == allChatButtons[i]) {
                    loadSelectedChat(i);
                    addNewChat.setEnabled(true);
                }
            }
        }
    }
}
