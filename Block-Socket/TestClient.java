import Interface.GetInterFace;
import Interface.SetPortAndIP;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;

public class TestClient implements Interface.SendInterFace, SetPortAndIP {
    private static Layout layout = null;
    public static Client client = null;
    private static final GetInterFace getInterface = new GetInterFace() {
        @Override
        public void get(String string) {
            layout.addString(string);
        }
    };
    public TestClient(String url,int port) throws IOException {
        client = new Client("127.0.0.1", 5999);
        layout = new Layout("客户端");
        layout.setSendInterFace(this);
        layout.setSetPortAndIP(this);
        client.setGetInterface(getInterface);
    }
    public static void main(String[] args){
        try {
            TestClient testClient = new TestClient("127.0.0.1",5999);
            testClient.client.startMonitor();
        }catch (IOException e){
            System.out.println("服务器已关闭！");
        }
    }

    @Override
    public void send(String str) {
        client.sendString(str);
    }

    @Override
    public void setPortAndIP(String ip, int port) throws IOException{
        client.close();
        client = null;
        client = new Client(ip, port);
        client.setGetInterface(getInterface);
        client.startMonitor();
    }
}
