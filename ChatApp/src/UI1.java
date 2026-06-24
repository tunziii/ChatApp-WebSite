import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class UI1 extends JFrame  {
    private CardLayout layout;
    private JPanel mainPanel;
    private JPanel chatPanel;
    private JPanel messagePanel;
    public int y = 30;

    public UI1() {
        TCPClient client = new TCPClient();
        String user = "Test";
        String receiver = "Tuni";

        setTitle("Chat App");
        setSize(1080, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        //Chat Panel
        chatPanel = new JPanel();
        chatPanel.setLayout(null);
        mainPanel.add(chatPanel, "chat");

        //Message panel for chat
        messagePanel = new JPanel();
        messagePanel.setLayout(null);
        messagePanel.setBounds(150, 30, 780, 500);
        messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        chatPanel.add(messagePanel, "message");
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBounds(150, 30, 780, 500);

        chatPanel.add(scrollPane);

        //Typing Field
        JTextField type = new JTextField();
        type.setLayout(null);
        type.setBounds(200, 450, 200, 30);
        messagePanel.add(type);

        //Send Button
        JButton send = new JButton("Send");
        send.addActionListener(e -> {
            String msg = type.getText();
            try {
                client.send(user, receiver, msg);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //get input and send msg + update chat
        });
        send.setBounds(430, 450, 100, 30);
        messagePanel.add(send);

        add(mainPanel);
        setVisible(true);

        new Thread(() -> {
            try {
                client.connect(msg -> {
                    SwingUtilities.invokeLater(() -> {
                        String[] msg2 = msg.split("\\^");
                        if(msg2[0].equals(user)) {createText(msg2[0] + ": " + msg2[1], 150);}
                        else if(msg2[0].equals(receiver)){createText(msg2[0] + ": " + msg2[1], 100);}

                        y += 15;
                    });
                }, user, receiver);

                new Thread(() -> {
                    try {
                        client.listenSingleMsg(msg1 -> {
                            SwingUtilities.invokeLater(() -> {
                                String[] msg2 = msg1.split("\\^");
                                System.out.println(msg1);
                                if(msg2[0].equals(user)) {createText(msg2[0] + ": " + msg2[1], 150);}
                                else if(msg2[0].equals(receiver)){createText(msg2[0] + ": " + msg2[1], 100);}

                                y += 15;
                            });
                        }, user, receiver);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void createText(String text, int x){
        JLabel msg = new JLabel(text);
        msg.setBounds(x, y, 100, 30);
        msg.setVisible(true);

        messagePanel.add(msg);
        messagePanel.revalidate();
        messagePanel.repaint();
    }
}