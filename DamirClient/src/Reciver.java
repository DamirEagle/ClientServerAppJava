import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reciver extends Thread{
    private BufferedReader is;

    public Reciver (Socket socket){
        try {
            this.is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            start();
        } catch (IOException e) {
            System.out.println("Не получилось создать поток считывания данных с консоли");
            System.exit(0);
        }
    }

    public void run(){
        while (true){
            try {
                String message = is.readLine();
                if (message != null) {
                    System.out.print(message + "\n");
                }else{
                    System.out.println("Сервер разорвал соедение");
                    System.exit(0);
//              if (message != null) {
//                  System.out.print(message + "\n");
//              }else{
//                  System.out.println("Сервер разорвал соедение");
//                  System.exit(0);
                }
            } catch (IOException e) {
                System.out.println("Нет доступа к серверу");
                System.exit(0);
                break;
            }
        }
    }
}