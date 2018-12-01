import java.io.*;
import java.net.Socket;

public class SocketThreadWriter implements Runnable{
    private Socket socket = null;
    private PrintStream ps = null;
    private String string = null;
    public boolean exit = false;
    public SocketThreadWriter(Socket socket) throws IOException {
        this.socket = socket;
        this.ps = new PrintStream(socket.getOutputStream());
    }
    @Override
    public void run() {
        String str;
        while (true && !exit){
            if(socket.isClosed()){
                break;
            }
            if(string !=null){
                ps.println(string);
                string = null;
            }
        }
        try{
            ps.close();
            socket.close();
            socket = null;
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void setString(String string){
        if(socket == null)
            throw new NullPointerException();
        this.string = string;
    }
}