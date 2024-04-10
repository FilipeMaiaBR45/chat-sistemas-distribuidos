import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Chat extends Remote {
    void sendMessage(String message) throws RemoteException;
    void registerClient(Chat client) throws RemoteException;
    void unregisterClient(Chat client) throws RemoteException;

    void broadcast(String message) throws RemoteException;

    String getMessage() throws RemoteException;;
}