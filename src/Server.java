import Interface.GetInterFace;
import Interface.SetStatus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Socket> sockets = null;
    private ServerSocket serverSocket = null;
    private ArrayList<String> arrayStr = null;
    private SocketThreadWriter socketThreadWriter = null;
    private SocketThreadReader socketThreadReader = null;
    private SetStatus setStatus = null;

    public Server(int port,SetStatus setStatus) throws IOException {
        this.sockets = new ArrayList<Socket>();
        serverSocket = new ServerSocket(port);
        arrayStr = new ArrayList<>();
        this.setSetStatus(setStatus);
        Socket socket = serverSocket.accept();
        setStatus.setStatus(true);
        socketThreadWriter = new  SocketThreadWriter(socket);
        socketThreadReader = new SocketThreadReader(socket);

    }
    public void startMonitor() throws IOException {
        System.out.println("Server starting monitor......");
        new Thread(socketThreadReader).start();
        new Thread(socketThreadWriter).start();
    }
    public void sendString(String str){
        socketThreadWriter.setString(str);
    }
    public void close(){
        try {
            setStatus.setStatus(false);
            socketThreadReader.exit = true;
            socketThreadWriter.exit = true;
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGetInterface(GetInterFace getInterface){
        socketThreadReader.setGetInterFace(getInterface);
    }

    public void setSetStatus(SetStatus setStatus) {
        this.setStatus = setStatus;
    }
}