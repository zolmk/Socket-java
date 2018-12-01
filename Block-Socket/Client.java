import Interface.GetInterFace;

import java.io.*;
import java.net.Socket;

public class Client{

    private Socket socket = null;
    private static SocketThreadWriter socketThreadWriter = null;
    private static SocketThreadReader socketThreadReader = null;

    public Client(String url,int port) throws IOException {
        socket = new Socket(url,port);
        socketThreadWriter = new SocketThreadWriter(socket);
        socketThreadReader = new SocketThreadReader(socket);
    }

    public void startMonitor() throws IOException {
        System.out.println("Client start working......");

        new Thread(socketThreadReader).start();
        new Thread(socketThreadWriter).start();

    }
    public void sendString(String str) {
        socketThreadWriter.setString(str);
    }
    public void setGetInterface(GetInterFace getInterface){
        socketThreadReader.setGetInterFace(getInterface);
    }
    public void close(){
        try {
            socketThreadReader.exit = true;
            socketThreadWriter.exit = true;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}