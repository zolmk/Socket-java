package Unblock_Socket.Client;

import Unblock_Socket.Layout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client extends Layout {
    private Selector selector = null;
    private Charset charset = Charset.forName("utf8");
    private SocketChannel socketChannel = null;
    private InetSocketAddress isa = null;
    private static boolean EXIT = false;

    public Client(String ip,int port){
        isa = new InetSocketAddress(ip,port);
        // Start UI设置
        setPort(isa.getPort());
        setIp(isa.getHostName());
        addSendButtonListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String content = getSendContent();
                content = "朱非非:" + content;
                try {
                    if(socketChannel!=null)
                        socketChannel.write(charset.encode(content));
                } catch (IOException e1) {
                    addText("发送失败！");
                }
                clearSendTextField();
            }
        });
        //　设置端口和ip
        addSetPortAndIPListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String ip = getIP();
                int port = getPort();
                if(!ip.isEmpty() && port > 1024 && port < 655554) {
                    EXIT = true;
                    isa = new InetSocketAddress(ip,port);
                    try {
                        close();
                        selector = Selector.open();
                        socketChannel = SocketChannel.open(isa);
                        successMessage();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                        EXIT = false;
                        new Thread(new ReadThread()).start();

                    }catch (IOException e1){
                        addText(e1.getMessage());
                    }
                }
            }
        });
        // End
    }
    public void successMessage(){
        addText("连接成功！");
    }
    public void init()throws IOException{
        selector = Selector.open();
        try {
            socketChannel = SocketChannel.open(isa);
            successMessage();
            //　设置以非阻塞方式
            socketChannel.configureBlocking(false);
            //　注册
            socketChannel.register(selector, SelectionKey.OP_READ);

        }catch (SocketException e){
            addText(e.getMessage());
        }
        //　将读取到的数据显示出来
        new Thread(new ReadThread()).start();
    }

    public void close(){
        try {
            selector.close();
            if(socketChannel!=null) socketChannel.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public class ReadThread implements Runnable{

        @Override
        public void run(){
            try {
                while (selector.select() > 0 && !EXIT){
                    for(SelectionKey sk : selector.selectedKeys()){
                        selector.selectedKeys().remove(sk);
                        if(sk.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            String content = "";
                            SocketChannel socketChannel = (SocketChannel)sk.channel();
                            while(socketChannel.read(buffer) > 0){
                                buffer.flip();
                                content += charset.decode(buffer);
                            }
                            System.out.println(content);
                            addText(content);

                            // 为下一次读取做准备
                            sk.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException{
        new Client("47.106.254.19",40000).init();
    }
}
