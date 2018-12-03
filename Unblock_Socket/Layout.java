package Unblock_Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Layout extends JFrame implements MouseMotionListener{

    private JPanel jPanel = null;
    private Point xy_Point = null;
    private JTextArea jTextArea = null;
    private JScrollPane jScrollPane = null;
    private final int window_Width = 600;
    private final int window_Height = 600;
    private JTextField textField = null;
    private JLabel text = null;
    private JButton jButton = null;
    private JButton setPortButton = null;
    private JTextField setPortTextField = null;
    private JLabel setPortLabel = null;
    private final int default_x = 50;
    private final int default_button_widith = 60;
    private final int default_height = 20;
    private final int default_fontSize = 20;
    private JTextField jTextFieldIp = null;

    public Layout() throws HeadlessException {
        super();
        init();
    }
    private void init(){
        jPanel = (JPanel) this.getContentPane();
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        JLabel jLabelIp = new JLabel("设置IP:");
        jLabelIp.setBounds(default_x,470,default_fontSize*3,default_height);

        jTextFieldIp = new JTextField();
        jTextFieldIp.setBounds(default_x+default_fontSize*3,470,default_fontSize*4,default_height);

        jPanel.add(jLabelIp);
        jPanel.add(jTextFieldIp);

        setPortLabel = new JLabel("设置端口号:");
        setPortLabel.setBounds(default_x+default_fontSize*7+10,470,default_fontSize*6,default_height);

        setPortTextField = new JTextField();
        setPortTextField.setBounds(default_x+default_fontSize*18,470,default_fontSize*2,default_height);

        setPortButton = new JButton("确定");
        setPortButton.setBounds(default_x+default_fontSize*20+30,470,default_button_widith,default_height);

        text = new JLabel();
        String title = getTitle();
        text.setText("Say to Server:");
        text.setBounds(default_x,500,default_x+50,default_height);
        textField = new JTextField();
        textField.setBounds(default_x*2+60,500,300,default_height);


        jButton = new JButton("发送");
        jButton.setBounds(default_x+default_fontSize*20+30,500,60,default_height);


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
    @Override
    public void mouseDragged(MouseEvent e) {
        Point point=e.getPoint();
        Point location = getLocation();
        int x=location.x+point.x-xy_Point.x;
        int y=location.y+point.y-xy_Point.y;
        setLocation(x, y);
    }

    public void clearSendTextField(){
        textField.setText("");
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
    public void addText(String str){
        this.jTextArea.append("\n"+str);
    }


    public void addSendButtonListener(MouseAdapter mouseAdapter){
        this.jButton.addMouseListener(mouseAdapter);
    }

    public String getIP(){
        return jTextFieldIp.getText();
    }

    public int getPort(){
        return Integer.valueOf(setPortTextField.getText());
    }

    public void addSetPortAndIPListener(MouseAdapter mouseAdapter){
        this.setPortButton.addMouseListener(mouseAdapter);
    }

    public void setIp(String s){
        this.jTextFieldIp.setText(s);
    }

    public void setPort(int port){
        this.setPortTextField.setText(String.valueOf(port));
    }

    public String getSendContent(){
        return this.textField.getText();
    }

}
