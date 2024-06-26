import javax.swing.*;                                                                // import everything needed
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {                                                                // public to use outside of the class

    String serverAddress;
    int serverPort;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);


    public Client2(String serverAddress, int serverPort, JFrame frame) {             // public to use outside of the class

        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.frame = frame;
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
    }

    private String getName() {

        return JOptionPane.showInputDialog(

                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }


    private void run() throws IOException {

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();

                if (line.startsWith("SUBMIT NAME")) {
                    out.println(getName());

                } else if (line.startsWith("NAME ACCEPTED")) {
                    this.frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);

                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } finally {

            frame.setVisible(false);
            frame.dispose();
        }
    }

    public static void main(String[] args) throws Exception {                               // public to use outside of the class

        String serverAddress = JOptionPane.showInputDialog(                                 // enter the server IP address
                null,
                "Please enter the server IP address:",
                "Server IP Input",
                JOptionPane.QUESTION_MESSAGE);

        if (serverAddress == null || serverAddress.isEmpty()) {                              // if the user pressed cancel or entered an empty string, exit the program

            System.err.println("Server IP is required.");
            return;
        }

        String port = JOptionPane.showInputDialog(                                           // enter the port number
                null,
                "Please enter the port number:",
                "Port Number Input",
                JOptionPane.QUESTION_MESSAGE);

        if (port == null || port.isEmpty()) {                                                 // if the user pressed cancel or entered an empty string, exit the program

            System.err.println("Port number is required.");
            return;
        }

        int serverPort = Integer.parseInt(port);

        JFrame frame = new JFrame("Chatter");                                             // create a new JFrame

        Client2 client = new Client2(serverAddress, serverPort, frame);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}