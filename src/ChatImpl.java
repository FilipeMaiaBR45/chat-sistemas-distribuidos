import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatImpl extends UnicastRemoteObject implements Chat{

    private List<Chat> clients = new ArrayList<>();

    String lastMessage;



    protected ChatImpl() throws RemoteException {
        super();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println(message);
    }


    @Override
    public void registerClient(Chat client) throws RemoteException {
        clients.add(client);
        System.out.println("Novo cliente registrado.");
    }


    @Override
    public void unregisterClient(Chat client) throws RemoteException {
        clients.remove(client);
        System.out.println("Cliente desconectado.");
    }

    @Override
    public void broadcast(String message) throws RemoteException {
        lastMessage = message;
    }

    @Override
    public String getMessage() throws RemoteException {
        return lastMessage;
    }
}
