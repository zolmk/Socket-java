package Unblock_Socket.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

public class Server {
    private Selector selector = null;
    // 定义实现编码解码字符集对象
    private Charset charset = Charset.forName("UTF-8");

    public void init()throws IOException
    {
        selector = Selector.open();
        // 打开一个未绑定的ServerSocketChannel实例
        ServerSocketChannel server = ServerSocketChannel.open();
        // 绑定端口
        InetSocketAddress socketAddress = new InetSocketAddress(40000);
        server.socket().bind(socketAddress);
        // 设置非阻塞方式
        server.configureBlocking(false);
        // 将ServerSocketChannel注册到Selector
        server.register(selector, SelectionKey.OP_ACCEPT);
        while(selector.select() > 0){
            // 依次处理selector上的每个选择的SelectionKey
            for(SelectionKey sk : selector.selectedKeys()){
                // 从被选择的集合中去掉当前sk
                selector.selectedKeys().remove(sk);
                // 如果sk对应的通道包含客户端的连接请求
                if(sk.isAcceptable()){
                    // 服务器接受请求
                    SocketChannel sc = server.accept();
                    // 设置采用非阻塞模式
                    sc.configureBlocking(false);
                    // 将该SocketChannel也注册到Selector
                    sc.register(selector,SelectionKey.OP_READ);
                    // 将对应的Channel设置成准备接受其他请求
                    sk.interestOps(SelectionKey.OP_ACCEPT);
                }
                if(sk.isReadable()){
                    //　将Channel实例化为SocketChannel
                    SocketChannel socketChannel = (SocketChannel)sk.channel();
                    // 定义读取数据时的buffer
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    String content = "";
                    // 开始读取数据
                    try{
                        while(socketChannel.read(buffer) > 0){
                            buffer.flip();
                            content += charset.decode(buffer);
                        }
                        // 将sk设置为准备下一次读取
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                    // 如果在对Channel读取的时候发生了异常，则将该Channel移除
                    catch (IOException e){
                        closeSelectionKey(sk);
                    }
                    if(!content.isEmpty()){
                        System.out.println(content);
                        // 将接收到的消息轮发个每个客户端
                        for(SelectionKey selectionKey : selector.keys()){
                            Channel targetChannel = selectionKey.channel();
                            if(targetChannel instanceof SocketChannel){
                                SocketChannel s = (SocketChannel)targetChannel;
                                s.write(charset.encode(content));
                            }
                        }
                    }
                    // 这里认为如果通道被打开，但是没有数据传送过来，那么该通道就是一个无效通道，必须强制关闭
                    else {
                        closeSelectionKey(sk);
                    }
                }
            }
        }
    }
    // 如果发生异常则关闭该SelectionKey
    private void closeSelectionKey(SelectionKey sk){
        sk.cancel();
        if(sk.channel()!=null){
            try {
                sk.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException
    {
        Server server = new Server();
        server.init();
    }
}
