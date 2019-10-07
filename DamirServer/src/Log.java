import java.io.FileWriter;
import java.io.IOException;

public class Log extends Thread{

    public FileWriter LogFile;

    public Log(){
        try {
            this.LogFile = new FileWriter("log.txt", true);
        } catch (IOException e) {
            System.out.println("Can't append to log file");
        }
    }

    public boolean write(String msg){
        try {
            this.LogFile.write(msg);
            this.LogFile.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
