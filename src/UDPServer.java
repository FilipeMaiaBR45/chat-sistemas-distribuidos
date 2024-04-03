import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
    private static final int PORT = 9876;
    private static List<InetSocketAddress> clients = new ArrayList<>();

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(PORT);
        byte[] receiveData = new byte[1024];

        System.out.println("Servidor UDP iniciado...");

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            InetSocketAddress client = new InetSocketAddress(clientAddress, clientPort);

            if (!clients.contains(client)) {
                clients.add(client);
                System.out.println("Novo cliente conectado: " + clientAddress + ":" + clientPort);
            }

            System.out.println("Mensagem recebida de " + clientAddress + ":" + clientPort + ": " + message);

            // Transmite a mensagem para todos os clientes, exceto o remetente
            for (InetSocketAddress c : clients) {
                if (!c.equals(client)) {
                    DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), c.getAddress(), c.getPort());
                    serverSocket.send(sendPacket);
                }
            }
        }
    }
}