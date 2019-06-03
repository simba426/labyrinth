package labyrinth;
import javax.swing.*;
import java.awt.event.*;
import labyrinth.MainFrame;

public class HelloFrame extends JFrame{
	 private static JLabel helloLogo = new JLabel();//放置logo图片的标签
	 private static JLabel backJLabel = new JLabel();//放置back图片的标签 
     private static ImageIcon BackIcon = new ImageIcon("pic/new/helloback.png");	   //背景图片
     private static ImageIcon type1Icon = new ImageIcon("pic/new/gamestart.png");	   //游戏记录
     private static ImageIcon type2Icon= new ImageIcon("pic/new/result.png");	   //开始游戏
     
     private static JButton chose1 = new JButton();	   //游戏记录按钮
     private static JButton chose2 = new JButton();      //开始游戏按钮

	 public HelloFrame(){
		    //初始化窗体(标题，大小，位置等)
			initBasic();
			
			//初始化logo，两个模式选择的按钮
			initHelloJLabel();
			
			//背景图 (先放置的图片在最上层，后放置的在下层。)
			initFrameBackGround();

			//可视化
			setVisible(true);
	 }

	 //只在本类使用，设置为私有方法
	 //初始化窗体方法(标题，大小，位置等)
	 private  void initBasic()
		{
			this.setTitle("走迷宫游戏");
			this.setSize(800, 800);
			//this.setLocation(700, 30);
			this.setLayout(null);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);	//
		}

	 //初始化logo，按钮
	 private void initHelloJLabel() {
		//helloLogo.setIcon(logoIcon);
		//helloLogo.setBounds(120, 150, 540, 200);
		//this.add(helloLogo);
		//设定透明效果
		chose1.setOpaque(false);
		chose2.setOpaque(false);
		//去掉背景点击效果
		chose1.setContentAreaFilled(false);
		chose2.setContentAreaFilled(false);
		//去掉聚焦线
		chose1.setFocusPainted(false);
		chose2.setFocusPainted(false);
		//去掉边框
		chose1.setBorder(null);
		chose2.setBorder(null);
		//设置显示的图片
		chose1.setIcon(type1Icon);
		chose2.setIcon(type2Icon);
		chose1.setBounds(280, 340, 250, 75);
		chose2.setBounds(280, 460, 250, 75);
		//给按钮添加事件
		chose1.addMouseListener(new MyMouseListenner1());
		chose2.addMouseListener(new MyMouseListenner2());
		this.add(chose1);
		this.add(chose2);
		}

		 private void initFrameBackGround()
		{
			backJLabel = new JLabel(BackIcon);
			backJLabel.setBounds(0, 0, 800, 800);
			this.add(backJLabel);
		}

	 //鼠标事件监听
	private class MyMouseListenner1  extends MouseAdapter{
		public void mouseClicked(MouseEvent e)//单击鼠标按钮时发生
		{
			nameDialog name = new nameDialog();	//显示输入名字的对话框
			name.setVisible(true);
		}
	}

	 private class MyMouseListenner2 extends MouseAdapter{
			public void mouseClicked(MouseEvent e)//单击鼠标按钮时发生
		    {
		    	ResultDialog result = new ResultDialog();		//显示游戏记录的对话框
		    	result.setVisible(true);
		    }
		}

}
