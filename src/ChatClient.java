import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ChatClient implements Chat, Serializable {
    private static String name;
    private Chat server;

    public ChatClient(String name, Chat server) {
        this.name = name;
        this.server = server;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        server.sendMessage(message);
    }

    @Override
    public void registerClient(Chat client) throws RemoteException {
        // Não necessário para o cliente
    }

    @Override
    public void unregisterClient(Chat client) throws RemoteException {
        // Não necessário para o cliente
    }

    @Override
    public void broadcast(String message) throws RemoteException {
        // Não necessário para o cliente
    }

    @Override
    public String getMessage() throws RemoteException {
        // Não necessário para o cliente
        return null;
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            Chat remoteObj = (Chat) registry.lookup("ChatService");

            Scanner scanner = new Scanner(System.in);


            ChatClient client = new ChatClient(name, remoteObj);
            remoteObj.registerClient(client);

            System.out.println("Você está conectado ao chat. Digite 'sair' para desconectar.");

            new Thread(() -> {
                System.out.println("Digite seu nome:");
                name = scanner.nextLine();
                try {
                    remoteObj.broadcast(name + " entrou no chat.");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                while (true) {
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("sair")) {
                        try {
                            remoteObj.unregisterClient(client);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Você foi desconectado.");
                        break;
                    } else {
                        try {
                            remoteObj.sendMessage(name + ": " + message);
                            remoteObj.broadcast(name + ": " + message);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }).start();

            new Thread(() -> {
                try {
                    String lastMessage = "";
                    while (true) {
                        String message = remoteObj.getMessage();
                        if (message != null && !message.equals(lastMessage)) {
                            System.out.println(message);
                            lastMessage = message;
                        }
                        Thread.sleep(2000);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();


        } catch (Exception e) {
            System.err.println("Exceção do cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}