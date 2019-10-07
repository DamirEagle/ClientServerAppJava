import java.io.*;
import java.net.Socket;

public class ConnectionThread extends Thread {

    public Socket socket;
    public Clients client;
    public Log LogFile;

    public ConnectionThread(Socket socket, Clients client){
        this.socket=socket;
        this.client=client;
        this.LogFile = new Log();
        this.LogFile=LogFile;
        start();
    }

    public void run(){
        try{
            BufferedReader is = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            os.write("Login: \n");
            os.flush();
//          BufferedReader NEWs = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
//          BufferedWros = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
//          os.write("Login: \n");
//          os.flush();
//          String login = "";
            String login = "";
            boolean correct = false;
            while (correct == false){
                login = is.readLine();
                if (login.matches("[a-zA-Z0-9а-яА-Я]+") && login.length() > 0){
                    correct = true;
                }
                else{
                    os.write("Incorrect Login. Try again! \n");
                    os.flush();
                }
            }
            if (client.connect(socket ,is, os, login)){
                //System.out.println(login + " connected");
//              client.Send_Ls("[" + login + "] -> [You]: " + message, msg[1]);
//              LogFile.write("[" + login + "] -> [" + msg[1] + "]: " + message + "\n")
                System.out.println(login + " connected");
                client.SendAll(login + " connected");
                LogFile.write(login + " connected\n");
                os.write("you are in {FIIT}\n" +
                        "Print \"!HELP\" to get all the commands \n");
                os.flush();
//                boolean connected = true;
//                while (connected) {
//                    try {
//                        String message = is.readLine();
//                        if ("!HELP".equals(msg[0])) {
//                            os.write("Commands:\n" +
//                                    "!HELP - TO GET HELP\n" +
//                                    "!GLDCDOBALY - TO SEND GLOBALY\n" +
//                                    "!ONLINE - THE LIST OF ONLINE USERS\n" +
//                                    "!MSG {user} {mes}- PRIVATE MSG\n" +
//                                    "!ROOMS - THE LIST OF ROOMS\n" +
//                                    "!ROOM - TO CHANGE || TO CREATE ROOM\n");
//                            os.flush();
//                            LogFile.write(login + " use !help\n");
//                        } else if ("!ONLINE".equals(msg[0])) {
//
                boolean connected = true;
                while (connected) {
                    try {
                        String message = is.readLine();
                        String[] msg = message.split(" ");
                        if ("!HELP".equals(msg[0])) {
                            os.write("Commands:\n" +
                                    "!HELP - TO GET HELP\n" +
                                    "!GLOBALY - TO SEND GLOBALY\n" +
                                    "!ONLINE - THE LIST OF ONLINE USERS\n" +
                                    "!MSG {user} {mes}- PRIVATE MSG\n" +
                                    "!ROOMS - THE LIST OF ROOMS\n" +
                                    "!ROOM - TO CHANGE || TO CREATE ROOM\n");
                            os.flush();
                            LogFile.write(login + " use !help\n");
                        } else if ("!ONLINE".equals(msg[0])) {
                            os.write(client.GetOnline() + "\n");
                            os.flush();
                            LogFile.write(login + " use !online\n");
                        } else if ("!MSG".equals(msg[0])) {
                            if (msg.length > 2) {
                                message = "";
                                for (int i = 2; i < msg.length; i++) {
                                    message = message + msg[i] + " ";
                                }
                                //client.Send_Ls("[" + login +"] -> [" + msg[1] +"]: "+message, msg[1]);
                                client.Send_Ls("[" + login + "] -> [You]: " + message, msg[1]);
                                LogFile.write("[" + login + "] -> [" + msg[1] + "]: " + message + "\n");
                            } else {
                                os.write("!msg {user} {message}\n");
                                os.flush();
                            }
                        } else if ("!GLOBALY".equals(msg[0])) {
                            if (msg.length > 1) {
                                message = "";
                                for (int i = 1; i < msg.length; i++) {
                                    message = message + msg[i] + " ";
                                }
                                client.SendAll("[Global] " + login + ": " + message);
                                LogFile.write("[Global] " + login + ": " + message + "\n");
                            } else {
                                os.write("!g {message}\n");
                                os.flush();
                            }
                        } else if ("!ROOMS".equals(msg[0])) {
                            String rooms = client.Get_Rooms();
                            os.write(rooms + "\n");
                            os.flush();
                            LogFile.write(login + " use !rooms\n");
                        } else if ("!ROOM".equals(msg[0])) {
                            if (msg.length == 2) {
                                if (msg[1].matches("[a-zA-Z0-9а-яА-Я]+") && msg[1].length() > 0) {
                                    client.Change_Room(login, msg[1]);
                                    String rooms = client.Get_Rooms();
                                    LogFile.write(login + " use " + message + "\n");
                                } else {
                                    os.write("Bad name room\n");
                                    os.flush();
                                }
                            } else {
                                os.write("!room {room}\n");
                                os.flush();
                            }
                        } else {
                            String room = client.room.get(login);
                            client.Send_Room("[" + room + "] " + login + ": " + message, login);
                            LogFile.write("[" + room + "] " + login + ": " + message + "\n");
                            //os.write("msg\n");
                            //os.flush();
                        }
                    } catch (IOException e) {
                        connected = false;
                    }
                }
                System.out.println(login + " disconnected");
                client.disconnect(login);
                client.SendAll(login + " disconnected");
                LogFile.write(login + " disconnected\n");
            }else{
                socket.close();
            }
        }
        catch (IOException s){
            System.out.println("Dissconnected witch out auth");
            //s.printStackTrace();
        }
    }
}