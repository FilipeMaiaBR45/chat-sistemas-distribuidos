import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ChatServer{

    protected ChatServer() throws RemoteException {
        super();
    }


    public static void main(String[] args) {
        try {
            Chat remoteObj = new ChatImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("ChatService", remoteObj);
            System.out.println("Servidor de chat iniciado.");
        } catch (Exception e) {
            System.err.println("Exceção do servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}