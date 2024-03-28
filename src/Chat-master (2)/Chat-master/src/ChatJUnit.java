import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatJUnit {

    @Test
    public void testServerSelectNewCoordinator() {
        Server server = new Server();
        server.names.add("Alice");
        server.names.add("Bob");
        server.coordinator = "Alice";
        server.selectNewCoordinator();
        assertNotNull(server.coordinator);
        assertTrue(server.names.contains(server.coordinator));
    }

    @Test
    public void testServerClientJoin() {
        Server server = new Server();
        server.names.add("Alice");
        server.names.add("Bob");
        server.coordinator = "Alice";
        server.selectNewCoordinator();
        assertEquals(2, server.names.size());
    }

    @Test
    public void testClientConnect() {
        Client client = new Client("localhost", 8181);
        // Test: Making sure that the client can connect to the server
        // Act
        server.launch();
        client.launch();

        // Assert
        assertTrue(client.isConnected());
    }

    
        // Test: Making sure that the client can send a message
        @Test
        public void testClientSendMessage() {
        // Arrange
        Server server = new Server();
        Client client1 = new Client("localhost", 8181);
        Client client2 = new Client("localhost", 8181);
        String message = "Hello, Server!";

        // Act
        server.launch();
        client1.launch();
        client2.launch();
        client1.sendMessage(message);

        // Assert
        assertTrue(client2.getReceivedMessages().contains(message));
    }
        
    @Test
    public void testMessageSending_Broadcast() {
        // Arrange
        Server server = new Server();
        Client client1 = new Client("localhost", 8181);
        Client client2 = new Client("localhost", 8181);
        String message = "Hello, everyone!";

        // Act
        server.launch();
        client1.launch();
        client2.launch();
        client1.sendMessage(message);

        // Assert
        assertTrue(client2.getReceivedMessages().contains(message));
    }

    @Test
    public void testMessageSending_Private() {
        // Arrange
        Server server = new Server();
        Client client1 = new Client("localhost", 8181);
        Client client2 = new Client("localhost", 8181);
        String sender = "Client1";
        String recipient = "Client2";
        String message = "Hello, Client2!";

        // Act
        server.launch();
        client1.launch();
        client2.launch();
        client1.sendPrivateMessage(recipient, message);

        // Assert
        assertTrue(client2.getReceivedPrivateMessages().containsKey(sender));
        assertEquals(message, client2.getReceivedPrivateMessages().get(sender));
    }

    
}
// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class ChatJUnit {

//     @BeforeEach
//     void setUp() throws Exception {
//     }

//     @Test
//     void test() {
//         // Write your test cases here
//     }

// }
