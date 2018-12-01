
import Interface.GetInterFace;
import Interface.SetPort;

import java.io.IOException;

public class TestServer implements Interface.SendInterFace, SetPort {
    private static Server server = null;
    private static Layout layout = null;
    private static final GetInterFace getInterface = new GetInterFace() {
        @Override
        public void get(String string) {
            layout.addString(string);
        }
    };
    public TestServer(int port) throws IOException {
        layout = new Layout("服务器");
        layout.setSendInterFace(this);
        layout.setSetPort(this);
        layout.setVisible(true);
        server = new Server(port,layout);
        server.setGetInterface(getInterface);
    }
    public static void main(String[] args) throws IOException {
        TestServer testServer = new TestServer(5999);
        server.startMonitor();
    }

    @Override
    public void send(String str) throws NullPointerException{

        server.sendString(str);
    }



    @Override
    public void setPort(int port) {
        try {
            server.close();
        } catch (NullPointerException e) {
            server = null;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new Server(port,layout);
                    server.setGetInterface(getInterface);
                    server.setSetStatus(layout);
                    server.startMonitor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
