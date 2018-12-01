import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Test extends JFrame
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Test()
    {

        Font font1 = new Font("幼圆", Font.BOLD, 16);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE,MMMMdd日，yyyy年 HH:mm:ss");
        String mDateTime = formatter.format(cal.getTime());

        MovingMessagePanel messagePanel = new MovingMessagePanel(mDateTime);
        messagePanel.setFont(font1);
        messagePanel.setBackground(Color.BLACK);
        messagePanel.setForeground(Color.PINK);

        add(messagePanel);

    }

    public static void main(String[] args)
    {

        Test frame = new Test();
        JLabel label = new JLabel("开始调试时间：X月X日     结束调试时间：X月X日");
        label.setBackground(Color.black);
        frame.setTitle("走马灯");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320, 120);
        frame.setVisible(true);
        frame.add(label, BorderLayout.SOUTH);
    }

    static class MovingMessagePanel extends JPanel
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String message = " ";
        private int xCoordinate = 0;
        private int yCoordinate = 40;

        public MovingMessagePanel(String message)
        {
            this.message = message;
            Timer timer = new Timer(100, new TimerListener());
            timer.start();
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            if (xCoordinate > getWidth())
            {
                xCoordinate = -100;
            }

            xCoordinate += 5;
            g.drawString(message, xCoordinate, yCoordinate);
        }

        class TimerListener implements ActionListener
        {

            public void actionPerformed(ActionEvent e)
            {
                repaint();
            }
        }
    }
}
