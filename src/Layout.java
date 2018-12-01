import Interface.SetPort;
import Interface.SetPortAndIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class Layout extends JFrame implements MouseMotionListener, KeyListener, Interface.SetStatus {
    private JPanel jPanel = null;
    private Point xy_Point = null;
    private JTextArea jTextArea = null;
    private JScrollPane jScrollPane = null;
    private final int window_Width = 600;
    private final int window_Height = 600;
    private JTextField textField = null;
    private JLabel text = null;
    private Interface.SendInterFace sendInterFace = null;
    private SetPortAndIP setPortAndIP = null;
    private JButton jButton = null;
    private SetPort setPort = null;
    private JButton setPortButton = null;
    private JTextField setPortTextField = null;
    private JLabel setPortLabel = null;
    private final int default_x = 50;
    private final int default_button_widith = 60;
    private final int default_height = 20;
    private final int default_fontSize = 20;
    private JTextField jTextFieldIp = null;

    public Layout(String title) throws HeadlessException {
        super(title);
        init();

    }
    private void init(){
        jPanel = (JPanel) this.getContentPane();
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);

        // 客户端和服务端分开
        if(getTitle().equals("客户端")){
            JLabel jLabelIp = new JLabel("设置IP:");
            jLabelIp.setBounds(default_x,470,default_fontSize*3,20);

            jTextFieldIp = new JTextField("127.0.0.1");
            jTextFieldIp.setBounds(default_x+default_fontSize*3,470,default_fontSize*4,20);

            jPanel.add(jLabelIp);
            jPanel.add(jTextFieldIp);

            setPortLabel = new JLabel("设置端口号（默认为5999）:");
            setPortLabel.setBounds(default_x+default_fontSize*7+10,470,default_fontSize*10,20);

            setPortTextField = new JTextField();
            setPortTextField.setBounds(default_x+default_fontSize*18,470,default_fontSize*2,20);

            setPortButton = new JButton("确定");
            setPortButton.setBounds(default_x+default_fontSize*20+30,470,60,20);
            setPortButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    try {
                        String str = setPortTextField.getText();
                        String ip = jTextFieldIp.getText();
                        if(str.isEmpty() || ip.isEmpty()){
                            addText("请输入正确的ip和端口号！");
                            return;
                        }
                        int port = Integer.valueOf(str);
                        if (port > 1024) {
                            try {
                                setPortAndIP.setPortAndIP(ip, port);
                            }catch (IOException e1) {
                                addText("服务器无响应！");
                            }

                        } else {
                            addText("端口设置错误!");
                        }
                    } catch (NumberFormatException e1) {
                        addText("请输入正确的ip和端口号！");
                        setPortTextField.setText("");
                        jTextFieldIp.setText("");
                    }
                }
            });
            text = new JLabel();
            String title = getTitle();
            text.setText("Say to Server:");
            text.setBounds(default_x,500,default_x+50,20);
        }
        else {
            setPortLabel = new JLabel("设置端口号（默认为5999）:");
            setPortLabel.setBounds(default_x,470,200,20);

            setPortTextField = new JTextField();
            setPortTextField.setBounds(280,470,140,20);

            setPortButton = new JButton("确定");
            setPortButton.setBounds(default_x+default_fontSize*20+30,470,60,20);
            setPortButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    try {
                        String str = setPortTextField.getText();
                        if (str.isEmpty()) return;
                        int port = Integer.valueOf(str);
                        if (port > 1024) {
                            setPort.setPort(port);

                        } else {
                            addText("端口设置错误!");
                        }
                    } catch (NumberFormatException e1) {
                        addText("请输入正确的端口号!");
                        setPortTextField.setText("");
                    }

                }
            });
            text = new JLabel();
            String title = getTitle();
            text.setText("Say to Client:");
            text.setBounds(default_x,500,default_x+50,20);
        }


        textField = new JTextField();
        textField.setBounds(default_x*2+60,500,300,20);
        textField.addKeyListener(this);



        jButton = new JButton("发送");
        jButton.setBounds(default_x+default_fontSize*20+30,500,60,20);
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!textField.getText().isEmpty()){
                    try {
                        addText("me:"+textField.getText());
                        sendInterFace.send(textField.getText());
                    }catch (NullPointerException n){
                        addText("客户端无响应！");
                    }
                    textField.setText("");
                }
            }
        });




        jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(default_x,50,500,400);



        //定义窗口事件,移动
        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                xy_Point=e.getPoint();
            }
        });
        this.addMouseMotionListener(this);
        jPanel.add(jScrollPane);
        jPanel.add(textField);
        jPanel.add(text);
        jPanel.add(jButton);
        jPanel.add(setPortButton);
        jPanel.add(setPortLabel);
        jPanel.add(setPortTextField);
        jPanel.setBounds(0,0,700,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //获取屏幕大小，将窗口设置在屏幕中间
        Toolkit kit=Toolkit.getDefaultToolkit();
        Dimension screen_Size=kit.getScreenSize();
        this.setLayout(null);
        this.setLocation(screen_Size.width/2-window_Width/2,screen_Size.height/2-window_Height/2);
        this.setResizable(false); //设置为不可改变窗口大小
        this.setSize(this.window_Width, this.window_Height);
        this.setVisible(true);
    }

    public void setSendInterFace(Interface.SendInterFace sendInterFace){
        this.sendInterFace = sendInterFace;
    }

    public void setSetPort(SetPort setPort){
        this.setPort = setPort;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point=e.getPoint();
        Point location = getLocation();
        int x=location.x+point.x-xy_Point.x;
        int y=location.y+point.y-xy_Point.y;
        setLocation(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
    public void addText(String str){
        this.jTextArea.append("\n"+str);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
            if(sendInterFace != null) {
                String str = textField.getText();
                try {
                    this.addText("me ： "+str);
                    this.sendInterFace.send(str);
                }catch (NullPointerException e){
                    this.addText("客户端无响应！");
                }

            }
            textField.setText("");
        }
    }
    public void addString(String str){
        String title = getTitle();
        if(title.equals("服务器")){
            jTextArea.append("\n"+"Client　say:"+str);
        }else {
            jTextArea.append("\n"+"Server say:"+str);
        }

    }

    public void setSetPortAndIP(SetPortAndIP setPortAndIP){
        this.setPortAndIP = setPortAndIP;
    }

    @Override
    public void setStatus(boolean flag) {
        if(flag){
            addText("客户端已登录");
        }else {
            addText("客户端已登出");
        }
    }
}
