package labyrinth;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;


public class MainFrame extends JFrame {
	 private static JLabel mainBackJLabel;//放置mianback图片的标签
	 private static JLabel typeShowpath = new JLabel("");
	 private static JLabel typeShowstone1 = new JLabel("");
	 private static JLabel typeShowstone2 = new JLabel("");
	 private static JLabel typeShowscore = new JLabel("");

	 private static MazeSolver agent;

	 private static ImageIcon mainBack;	   //mainback图片;
     private static ImageIcon wall;	   //迷宫边界(墙体)的图片
     private static ImageIcon characterType;//迷宫人物图片
     private static ImageIcon stone;
     private static ImageIcon win;//迷宫终点图片  
     private static int[][] labyrinthData = new int[23][23];//逻辑上的迷宫
	 private static String[][] algorithmData = new String[23][23]; //用于自动寻路算法的迷宫
     private static JLabel[][] labyrinthLable = new JLabel[23][23];	//图像显示上的迷宫
     private static int[][] newData=new int[23][23];  //定义newData2数组用于暂存移动前中的数组（用于数组是否变化）
     private static int steps = 0;		//步数
	 private static int redstone = 0;	//红宝石数
	 private static int bluestone = 0;	//蓝宝石数
	 private static String username = new String();		//保存用户名

	 public static boolean isAuto1 = false; //A*算法
	 public static boolean isAuto2 = false; //强化学习

	 public static final int X = 100;
	 public static final int Y = 150;

	 private static Queue<String> path = new LinkedList<String>();


     //构造函数
	public MainFrame(String username) {

		this.username = username;
		//初始化窗体(标题，大小，位置等
		initBasic();
		
		//初始化迷宫(逻辑上)
		initMapData();
		
		//初始化迷宫（图形上）
		initMapFrame();
		
		//初始化迷宫模式
		initProp();
		upDateUIpri(labyrinthData);

			//按键监听
			this.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					int k = event.getKeyCode();
					//暂时先存好移动前的数组
					for (int m = 0; m < 23; m++) {
						for (int n = 0; n < 23; n++) {
							newData[m][n] = labyrinthData[m][n];
						}
					}
					if (!isAuto1 && !isAuto2) {
						switch (k) {//1左2右3上4下
							case 37://左
								move(1);
								break;
							case 38://上
								move(3);
								break;
							case 39://右
								move(2);
								break;
							case 40://下
								move(4);
								break;
							case 65:
								isAuto1 = true;
								autoMove();
								break;
							case 66:
								isAuto2 = true;
								break;
						}//switch
					}

					if (!isAuto1 && !isAuto2) {
						yesornoMove();
						boolean havemove = yesornoMove();
						if (havemove == true) {//有效移动就刷新
							try {
								upDateUIpub(labyrinthData);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							steps++;
							upDateUIpri(labyrinthData);
							gamestate();
						}
						else {
							System.out.println("***isWall***");
						}
					}

					else if (isAuto1){
						new Thread(new Runnable() {
							@Override
							public void run() {
								while (!path.isEmpty()){
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									int direction = Integer.parseInt(path.poll());
									move(direction);

									yesornoMove();
									boolean havemove = yesornoMove();
									if (havemove == true) {//有效移动就刷新
										try {
											upDateUIpub(labyrinthData);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										steps++;
										upDateUIpri(labyrinthData);
										gamestate();
									}
								}
							}
						}).start();
					}
					else if (isAuto2){
						new Thread(new Runnable() {
							@Override
							public void run() {
								int[][] QLlabyrinthData = new int[23][23];
								agent = new MazeSolver(algorithmData);
								while (true) {
									agent.update();
									QLlabyrinthData = agent.getMaze();
									try {
										upDateUIpub(QLlabyrinthData);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}).start();
					}
				}
			});

		//初始化随机背景图 (先放置的图片在最上层，后放置的在下层。)
		initMainFrameBack();
		//可视化
		setVisible(true);
	}

	//初始化窗体方法(标题，大小，位置等)
	private  void initBasic()
	{
		this.setTitle("迷宫游戏");
		this.setSize(875, 725);
		this.setLocation(600, 0);	
		this.setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	//背景图片
	private void initMainFrameBack()
	{
		Random random1 = new Random();
		mainBack = new ImageIcon("pic/new/back3.png");
		mainBackJLabel = new JLabel(mainBack);
		mainBackJLabel.setBounds(0, 0, 875, 715);
		this.add(mainBackJLabel);
	}

	//初始化关卡地图
	private void initMapData() {
		//从文件中读取数据保存到labyrinthData数组中
    	String[] line = new String [23];
    	int m = 0;
        try { 
            BufferedReader br = new BufferedReader(
            		new  InputStreamReader ( 
    	                    new FileInputStream("src/labyrinth/maze.txt")));
            String line1 ;
            while ((line1 = br.readLine()) != null) {
            	line[m] = line1 ;
            	m++;
        	}//while
        } catch (Exception e) {
            e.printStackTrace();
        }
       //将String转化为整型，放入labyrinthData数组中
        for(int j = 0; j < 23; j++) {
			for(int i = 0; i < 23; i++){
			  String s2 = line[j].substring(i, i+1);
			  algorithmData[j][i] = s2;
			  labyrinthData[j][i] = Integer.valueOf(s2).intValue();
			  }
        }   
        Random random = new Random();
	     //随机产生两个坐标（出生地和终点）
	     int x1, y1, x2, y2;
	       do
	       {
	         x1 = random.nextInt(23);
		     y1 = random.nextInt(23);
		     x2 = random.nextInt(23);
		     y2 = random.nextInt(23);
	       }while(labyrinthData[x1][y1] == 1 || labyrinthData[x2][y2] == 1 || ((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)) < 100) ||
                   ((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)) > 200));
	       //确保出生地合理性并且出生地和出口相差足够远
			labyrinthData[x1][y1] = 2;	//设置2为逻辑人物
	        labyrinthData[x2][y2] = 5;	//设置5为逻辑出口
			algorithmData[x1][y1] = "s";//设置s为算法起点
			algorithmData[x2][y2] = "e";//设置e为算法终点
	}

	//初始化迷宫地图（图形上）
	private  void initMapFrame() {
		Random random = new Random();//随机墙体类型
		//（frame中纵横与数组相反）
		for(int i = 0; i < 23; i++) {
			for(int j = 0; j < 23; j++) {
				//迷宫墙体
				if(labyrinthData[i][j] == 0)
				{
					labyrinthLable[j][i] = new JLabel();
					labyrinthLable[j][i].setIcon(null);
					labyrinthLable[j][i].setBounds(X+j*20, Y+i*20, 20, 20);
				    this.add(labyrinthLable[j][i]);
				}
					if(labyrinthData[i][j] == 1) {
						wall= new ImageIcon("pic/new/wall4.png");
				    labyrinthLable[j][i] = new JLabel(wall);
				    labyrinthLable[j][i].setBounds(X+j*20, Y+i*20, 20, 20);
				    this.add(labyrinthLable[j][i]);
					}
					if(labyrinthData[i][j] == 2)
				       {
						    characterType = new ImageIcon("pic/new/char.png");//人物图片初始化;//迷宫人物图片
						    labyrinthLable[j][i] = new JLabel(characterType);
						    labyrinthLable[j][i].setBounds(X+j*20, Y+i*20, 20, 20);
						    this.add(labyrinthLable[j][i]);						    				 
	                   }
				//终点
					if(labyrinthData[i][j] == 5) {
						win = new ImageIcon("pic/new/goal.png");
						labyrinthLable[j][i] = new JLabel(win);
					    labyrinthLable[j][i].setBounds(X+j*20, Y+i*20, 20, 20);
					    this.add(labyrinthLable[j][i]);
					}
			}//内层for	
		}//外层for
	}

	//初始化寻宝游戏
	private void initProp()
	{
		Font font = new Font("Verdana", Font.BOLD , 25);      //创建1个字体实例Font.PLAIN
		//步数显示
		JLabel typeShow23=new JLabel("步数:");
		typeShow23.setFont(font);      //设置JLabel的字体
		typeShow23.setForeground(Color.darkGray);       //设置文字的颜色
		typeShow23.setBounds(X + 75,0,150,150);
		this.add(typeShow23);
	  	typeShowpath.setFont(font);      //设置JLabel的字体
	  	typeShowpath.setForeground(Color.darkGray);       //设置文字的颜色
	  	typeShowpath.setBounds(X + 150,0,150,150);
	  	this.add(typeShowpath);
		//红蘑菇显示
		JLabel typeShow24=new JLabel("红蘑菇:");
		typeShow24.setFont(font);      //设置JLabel的字体
		typeShow24.setForeground(Color.darkGray);       //设置文字的颜色
		typeShow24.setBounds(X + 200,0,150,150);
		this.add(typeShow24);
		typeShowstone1.setFont(font);      //设置JLabel的字体
		typeShowstone1.setForeground(Color.darkGray);       //设置文字的颜色
		typeShowstone1.setBounds(X + 300,0,150,150);
		this.add(typeShowstone1);
		//蓝蘑菇显示
		JLabel typeShow25=new JLabel("蓝蘑菇:");
		typeShow25.setFont(font);      //设置JLabel的字体
		typeShow25.setForeground(Color.darkGray);       //设置文字的颜色
		typeShow25.setBounds(X + 350,0,150,150);
		this.add(typeShow25);
		typeShowstone2.setFont(font);      //设置JLabel的字体
		typeShowstone2.setForeground(Color.darkGray);       //设置文字的颜色
		typeShowstone2.setBounds(X + 450,0,150,150);
		this.add(typeShowstone2);
		//奖励分显示
		JLabel typeShow26=new JLabel("奖励分:");
		typeShow26.setFont(font);      //设置JLabel的字体
		typeShow26.setForeground(Color.darkGray);       //设置文字的颜色
		typeShow26.setBounds(X + 500,0,150,150);
		this.add(typeShow26);
		typeShowscore.setFont(font);      //设置JLabel的字体
		typeShowscore.setForeground(Color.DARK_GRAY);       //设置文字的颜色
		typeShowscore.setBounds(X + 600,0,150,150);
		this.add(typeShowscore);
		//规则显示
		JLabel typeShow27=new JLabel();
		ImageIcon intro = new ImageIcon("pic/new/intro.png");
		typeShow27.setIcon(intro);
		typeShow27.setBounds(X + 430,0,280,800);
		this.add(typeShow27);
	 	//道具数据初始化
	  	Random random = new Random();
	  	int x,y;
	  	//5个随机宝石（红蓝）（数据上）
		for(int i = 0; i < 5; i++) {
	       do
	       {
	         x = random.nextInt(23);
		     y = random.nextInt(23);
	       }while(labyrinthData[x][y] == 1 || labyrinthData[x][y] == 2 || labyrinthData[x][y] == 5);//出生地合理性
	     labyrinthData[x][y] = random.nextInt(2)+3;//设置3,4为道具数据(3代表蓝宝石，4代表红宝石)
	     } 
	     
	   	//5个随机宝石（红蓝）（图形上上）(3代表蓝宝石stone1,4代表红宝石stone2)
	     	for(int i = 0; i < 23; i++) {
				for(int j = 0; j < 23; j++) {
					      if(labyrinthData[i][j] == 3||labyrinthData[i][j] == 4)
					      {
					    	  switch(labyrinthData[i][j]) {
					    	  case 3:
								  stone = new ImageIcon("pic/new/stone2.png");
					    	  	break;
					    	  case 4:
								  stone = new ImageIcon("pic/new/stone1.png");
					    	  	break;
					    	  }//switch					    	  
							    labyrinthLable[j][i] = new JLabel(stone);
							    labyrinthLable[j][i].setBounds(X+j*20, Y+i*20, 20, 20);
							    this.add(labyrinthLable[j][i]);
					      }     					      
					}
				}
	}

	//根据数据数组来刷新游戏界面，使视觉效果和真实数据是一致的
	//公共部分更新（人物移动）
	public static void upDateUIpub(int[][] labyrinthData) throws InterruptedException {
		 Random random = new Random();
		//更新：相同部分（人物移动）      注意:frame中纵横与数组相反
		for(int i = 0; i < 23; i++) {
			for(int j = 0;j < 23; j++) {
				    if(labyrinthData[i][j] == 2)
				       {
				    	labyrinthLable[j][i].setIcon(characterType);
					   }
				    else if(labyrinthData[i][j] == 0)
				    {
				    	labyrinthLable[j][i].setIcon(null);   
	                 }
			}//内for
		}//外for
	}

	//更新函数不同部分
	public static void upDateUIpri(int[][] labyrinthData)
	{
		Random random = new Random();
		typeShowpath.setText(steps + "");
		typeShowstone1.setText(redstone + "");
		typeShowstone2.setText(bluestone + "");
		typeShowscore.setText((redstone * 10 + bluestone * 20) + "");
   }

	//游戏状态函数
	public void gamestate()
	{
		int flag=0;//0代表无输赢 1赢2输    数组数据中7代表胜利
		for(int i=0; i<23; i++)
		{
			for(int j=0; j<23; j++)
			{//移动函数中到达终点将数据赋值为人物数据2
				if(labyrinthData[i][j]==7) {flag=1;}
			}
		}

		if(flag==1) {
		//确认对话框。提出问题，然后由用户自己来确认（按"Yes"或"No"按钮）
			int score = (50 - steps / 2) + redstone * 10 + bluestone * 20;
			String outcome = "你赢啦!\n " + "得分是：" + score + "\n返回主界面？";
			int result = JOptionPane.showConfirmDialog(null, outcome, "游戏结果",JOptionPane.YES_NO_OPTION);
		//选择YES重新开始游戏
		if(result == 0)
		{
			FileWriter fw = null;
			try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
				File f=new File("src/labyrinth/result.txt");
				fw = new FileWriter(f, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			PrintWriter pw = new PrintWriter(fw);
			pw.println(username + "," + steps + "," + redstone + "," + bluestone + "," + score);
			pw.flush();
			try {
				fw.flush();
				pw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.dispose();
			steps = 0;
			redstone = 0;
			bluestone = 0;
			isAuto1 = false;
			isAuto2 = false;
		}
		//选择NO则退出游戏程序
		else   {System.exit(0);}
		}
  }//状态函数
	
	//移动函数（参数1左2右3上4下）
	public static void move(int direction) {
		piao:for(int i = 0; i < 23; i++) {
			for(int j = 0; j < 23; j++) {
				if(labyrinthData[i][j] == 2) {
				switch(direction) {
				case 1:{
					System.out.println("left");
					switch(labyrinthData[i][j-1]){
					case 3:bluestone++;break;//蓝宝石
					case 4:redstone++;break;//红宝石
					case 5:labyrinthData[0][0]=7;break;//胜利标志
					}
                    if(labyrinthData[i][j - 1] != 1 && labyrinthData[i][j - 1] != 6) {
				    	labyrinthData[i][j - 1] = 2;
				    	labyrinthData[i][j] = 0;
				    	algorithmData[i][j - 1] = "s";
				    	algorithmData[i][j] = "0";
				    }
					break piao;
				}
				case 2:{
					System.out.println("right");
					switch(labyrinthData[i][j+1]){
				          case 3:bluestone++;break;//蓝宝石
				          case 4:redstone++;break;//红宝石
				          case 5:labyrinthData[0][0]=7;break;//胜利标志
				         }					
					if(labyrinthData[i][j+1]!=1 && labyrinthData[i][j+1]!=6) {
						labyrinthData[i][j+1]=2;
						labyrinthData[i][j]=0;
						algorithmData[i][j+1]="s";
						algorithmData[i][j]="0";
					}
					break piao;}
				case 3:{
					System.out.println("up");
					switch(labyrinthData[i-1][j]){
			          case 3:bluestone++;break;//蓝宝石
			          case 4:redstone++;break;//红宝石
			          case 5:labyrinthData[0][0]=7;break;//胜利标志
			         }		
					 if(labyrinthData[i-1][j]!=1&&labyrinthData[i-1][j]!=6) {
			        	labyrinthData[i-1][j]=2;
			        	labyrinthData[i][j]=0;
			        	algorithmData[i-1][j]="s";
			        	algorithmData[i][j]="0";
			        }
				    break piao;}
				case 4:{
					System.out.println("down");
					switch(labyrinthData[i+1][j]){
			          case 3:bluestone++;break;//蓝宝石
			          case 4:redstone++;break;//红宝石
			          case 5:labyrinthData[0][0]=7;break;//胜利标志
			         }		
					if(labyrinthData[i+1][j]!=1&&labyrinthData[i+1][j]!=6) {
						labyrinthData[i+1][j]=2;
						labyrinthData[i][j]=0;
						algorithmData[i+1][j]="s";
						algorithmData[i][j]="0";
					}
					break piao;}
				}//switch(direction)
				}//if(labyrinthData[i][j]==2) 
			}//内for
		}//外for
	}

	//自动寻路的函数
	private void autoMove() {
		AStar star = new AStar();
		star.init(algorithmData);
		star.search(algorithmData);
		star.printPath();
		for( int i = 0; i < star.max_step; i++ ){
			path.offer(star.direction[i] + "");
		}
	}



	//判断是否移动的函数
	private static boolean yesornoMove()
	{
		boolean flag = false;//0代表未移动
		for(int m=0; m<23; m++)
	      {  for(int n=0; n<23; n++)
		     {
	    	  if(newData[m][n]!=labyrinthData[m][n])
	    		  flag = true;
	    	  }
	      }
		return flag;
	}
}
