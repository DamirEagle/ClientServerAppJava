import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    String host;
    int port;

    public Server (String host, int port){
        this.host = host;
        this.port = port;
        try {
            Clients clients = new Clients();
            ServerSocket ServerSocket = new ServerSocket(port);
            FileWriter LogFile = new FileWriter("log.txt", false);
            LogFile.write("Server started on " + host + ":" + port+"\n");
            LogFile.flush();
            System.out.println("Server started on " + host + ":" + port);
            try{
                while (true){
                    Socket ClientSocket = ServerSocket.accept();
                    ConnectionThread connection = new ConnectionThread(ClientSocket, clients);
                }
            }
            catch (IOException ex) {
                System.out.println("Can't start server");
            }
        }
        catch (IOException ex){
            System.out.println("Can't start server");
        }
    }
}