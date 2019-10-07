import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Clients {
    public Map<String, String> users = new HashMap<String, String>();
    public Map<String, Socket> connections = new HashMap<String, Socket>();
    public ArrayList<String> online = new ArrayList<String>();
    public Map<String, String> room = new HashMap<String, String>();


    public Clients(){}

    public boolean connect (Socket socket, BufferedReader is, BufferedWriter os, String login) {
        String pass = "";
        boolean correct = false;
        if (users.get(login) != null) {
            boolean yes = false;
            while (yes == false) {
                try {
                    os.write("Password: \n");
                    os.flush();
                    pass = is.readLine();
                    if (users.get(login).equals(pass)) {
                        yes = true;;
                    } else {
                        os.write("Bad password. Try again\n\n");
                        os.flush();
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        } else {
            try {
                os.write("New password: \n");
                os.flush();
                pass = is.readLine();
                //System.out.println(users);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        boolean IsOnline = false;
        for (int i = 0; i < online.size(); i++) {
            if (online.get(i).equals(login)) {
                IsOnline = true;
            }
        }
        if (IsOnline) {
            try {
                os.write("User already oline!\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

//        if (IsOnline) {
//            try {
//                os.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return false;

        else {
            users.put(login, pass);
            online.add(login);
            connections.put(login, socket);
            room.put(login, "FIIT");
            return true;
        }
    }

    public void disconnect(String login){
        online.remove(login);
        connections.remove(login);
    }


//    public String GetOnline(){
//        for (int i = 0; i<(online.size()-1) ; i++){
//            res=res+online.get(i)+" in room [" + room.get(online.get(i)) +"], ";
//        }
//        res=res+online.get(online.size()-1)+" in room [" + room.get(online.get(online.size()-1)) +"]";
//        return res;
//    }


    public String GetOnline(){
        String res = "Online: ";
        for (int i = 0; i<(online.size()-1) ; i++){
            res=res+online.get(i)+" in room [" + room.get(online.get(i)) +"], ";
        }
        res=res+online.get(online.size()-1)+" in room [" + room.get(online.get(online.size()-1)) +"]";
        return res;
    }

    public void SendAll(String msg){
        for (int i=0; i<online.size(); i++){
            Socket SendSocket = connections.get(online.get(i));
            try {
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(SendSocket.getOutputStream()));
                os.write(msg+"\n");
                os.flush();
            } catch (IOException e) {
                System.out.println("Can't send to " + online.get(i));
            }
        }
    }

    public void Send_Ls(String msg, String user){
        for (int i = 0; i<online.size(); i++){
            if (user.equals(online.get(i))){
                Socket SendSocket = connections.get(user);
                try {
                    BufferedWriter os = new BufferedWriter(new OutputStreamWriter(SendSocket.getOutputStream()));
                    os.write(msg+"\n");
                    os.flush();
                } catch (IOException e) {
                    System.out.println("Can't send to " + user);
                }
                break;
            }
        }
    }

    public String Get_Rooms(){
        ArrayList<String> rooms = new ArrayList<String>();
//      String res = "Online: ";
//      for (int i = 0; i<(online.size()-1) ; i++){
//          res=res+online.get(i)+" in room [" + room.get(online.get(i)) +"], ";
//      }
//      res=res+online.get(online.size()-1)+" in room [" + room.get(online.get(online.size()-1)) +"]";
//      return res;
        for (Map.Entry<String, String> entry : room.entrySet()){
            boolean in = false;
            for (int i = 0; i<rooms.size(); i++){
                if (rooms.get(i).equals(entry.getValue())){
                    in = true;
                }
            }
            if (in == false){
                rooms.add(entry.getValue());
            }
        }
        String AllRooms = "";
        for (int i = 0; i<(rooms.size()-1); i++){
            AllRooms = AllRooms + rooms.get(i) + ", ";
        }
        AllRooms = AllRooms + rooms.get(rooms.size()-1);
        return AllRooms;
    }

    public void Send_Room(String msg, String login){
        String sendroom = room.get(login);
        for (Map.Entry<String, String> entry : room.entrySet()){
            if (entry.getValue() == sendroom){
                Socket SendSocket =  connections.get(entry.getKey());
                try {
                    BufferedWriter os = new BufferedWriter(new OutputStreamWriter(SendSocket.getOutputStream()));
                    os.write(msg+"\n");
                    os.flush();
                } catch (IOException e) {
                    System.out.println("Can't send to " + entry.getKey());
                }
            }
        }
    }

    public void Change_Room(String login, String newroom){
        //room.remove(login);
        room.put(login, newroom);
        Send_Room(login + " login in room [" + newroom + "]", login);
        }
}