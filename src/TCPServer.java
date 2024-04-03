import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor TCP iniciado na porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println("Por favor, digite seu nome de usuario:");
                username = in.readLine();
                System.out.println(username + " se conectou.");
                notifyAllClients(username + " entrou no chat.");

                String userInput;
                while ((userInput = in.readLine()) != null) {
                    if ("/exit".equalsIgnoreCase(userInput)) {
                        break;
                    }
                    System.out.println(username + " diz: " + userInput);
                    broadcastMessage(username, userInput);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    clients.remove(this);
                    System.out.println(username + " saiu.");
                    notifyAllClients(username + " saiu do chat.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void notifyAllClients(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }

        private void broadcastMessage(String sender, String message) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.out.println(sender + ": " + message);
                }
            }
        }
    }
}