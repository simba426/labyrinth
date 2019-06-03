package labyrinth;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class nameDialog extends JDialog{
    private static JPanel panel = new JPanel();      //创建内容面板

    public nameDialog(){
        //初始化窗口
        initBasic();

        //初始化窗口内容
        initContent();
    }

    private void initBasic(){
        this.setTitle("输入角色名");
        this.setSize(350, 200);
        this.setResizable(false);
        this.setLayout(null);
        setDefaultCloseOperation(2); //设置点击关闭时隐藏
    }

    private void initContent(){
        panel.removeAll();
        // 创建 JLabel
        JLabel userLabel = new JLabel("用户名:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        // 创建登录按钮
        JButton loginButton = new JButton("确认");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainFrame newmain = new MainFrame(userText.getText());
            }
        });
        panel.add(loginButton);

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

}
