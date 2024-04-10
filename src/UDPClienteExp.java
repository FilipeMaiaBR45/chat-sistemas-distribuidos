import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClienteExp { private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9876;

    public static void main(String args[]) throws Exception {

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName(SERVER_ADDRESS);
        final byte[][] sendData = {new byte[60 * 1024]};
        byte[] receiveData = new byte[1024];


        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Digite seu username: ");
        String username = inFromUser.readLine();


        Thread sendThread = new Thread(() -> {
            try {
                while (true) {
                    String message = inFromUser.readLine();


                    if (message.equals("/exit")) {
                        System.out.println("Desconectado do chat.");
                        break;
                    }


                    String fullMessage = "[" + username + "]: " + message;
                    sendData[0] = fullMessage.getBytes();



                    DatagramPacket sendPacket = new DatagramPacket(sendData[0], sendData[0].length, serverIP, SERVER_PORT);

                    long startTime = System.nanoTime();
                    clientSocket.send(sendPacket);
                    long endTime = System.nanoTime();
                    long elapsedTime = endTime - startTime;

                    System.out.println("Dados enviados em " + elapsedTime + " milissegundos.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendThread.start();


        Thread receiveThread = new Thread(() -> {
            try {
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);

                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println(receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();


        sendThread.join();
        receiveThread.join();

        // Fecha o socket
        clientSocket.close();
    }

}