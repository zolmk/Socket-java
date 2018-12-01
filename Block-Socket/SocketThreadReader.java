import Interface.GetInterFace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketThreadReader implements Runnable{
    private Socket socket = null;
    private BufferedReader br = null;
    public boolean exit = false;
    private GetInterFace getInterFace = null;
    public SocketThreadReader(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }
    @Override
    public void run() {
        try {
            String str;
            while ((str = readFromClient()) != null && !exit) {
                getInterFace.get(str);
            }
            br.close();
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readFromClient(){
        try {
            return br.readLine();
        }catch (IOException e){
            return null;
        }
    }

    public void setGetInterFace(GetInterFace getInterFace) {
        this.getInterFace = getInterFace;
    }
}