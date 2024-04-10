import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPCliente {
    private static final String SERVER_IP = "localhost"; // IP do servidor
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             OutputStream outputStream = socket.getOutputStream()) {

            // Modifique a quantidade de dados a serem enviados conforme necessário
            byte[] data = new byte[60 * 1024]; // 1 MB de dados

            long startTime = System.nanoTime();

            outputStream.write(data);

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;

            System.out.println("Dados enviados em " + elapsedTime + " milissegundos.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}