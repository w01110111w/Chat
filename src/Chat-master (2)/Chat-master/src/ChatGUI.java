// ChatGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatGUI {

    private String serverAddress;
    private int serverPort;
    private Scanner in;
    private PrintWriter out;
    private JFrame frame;
    private JTextField textField;
    private JTextArea messageArea;

    public ChatGUI(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        frame = new JFrame("Chatter");
        textField = new JTextField(50);
        messageArea = new JTextArea(16, 50);
    }

    public void launch() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();

                if (line.startsWith("SUBMIT NAME")) {
                    out.println(getName());

                } else if (line.startsWith("NAME ACCEPTED")) {
                    frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);

                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public static void main(String[] args) {
        String serverAddress = JOptionPane.showInputDialog(
                null,
                "Please enter the server IP address:",
                "Server IP Input",
                JOptionPane.QUESTION_MESSAGE);

        if (serverAddress == null || serverAddress.isEmpty()) {
            System.err.println("Server IP is required.");
            return;
        }

        String port = JOptionPane.showInputDialog(
                null,
                "Please enter the port number:",
                "Port Number Input",
                JOptionPane.QUESTION_MESSAGE);

        if (port == null || port.isEmpty()) {
            System.err.println("Port number is required.");
            return;
        }

        int serverPort = Integer.parseInt(port);
        ChatGUI chatGUI = new ChatGUI(serverAddress, serverPort);
        chatGUI.launch();
    }
}
