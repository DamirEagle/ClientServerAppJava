import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread{
    String host;
    int port;
    BufferedReader is;
    BufferedWriter os;

    public Client (String host, int port){
        this.host = host;
        this.port = port;
        start();
    }

    public void run(){
        try {
            Socket socket = new Socket(this.host, this.port);
            System.out.println("Подключение установлено");
            Reciver reciver = new Reciver(socket);
            this.is = new BufferedReader(new InputStreamReader(System.in));
            this.os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            this.is = new Buffered(new InputStreamReader(System.in));
//            this.os = new BufferedWriter(new ));
            while (true){
                String message = is.readLine();
                os.write(message + "\n");
                os.flush();
                //System.out.println(message);
            }
        } catch (SocketException e) {
            System.out.println("Нет доступа к серверу");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Нет доступа к серверу");
            System.exit(0);
        }

    }
}