package labyrinth;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.Vector;
/**
 * A*寻路算法
 * <pre>
 * 思路：	每次取期望值最小的位置作为下一步要走的位置，F = G + H
 *  	G 表示从起点 A 移动到网格上指定方格的移动耗费 (可沿斜方向移动,斜方向的代价为对角线长度).
 *  	H 表示从指定的方格移动到终点 B 的预计耗费 (H 有很多计算方法, 这里我们设定只可以上下左右移动).
 *
 *  	此处用一个最小堆来记录开启列表中的格子，每个格子有一个指向父格子的指针，以此记录路劲 </pre>
 */
public class AStar {

	private static MinHeap<Grid> open ;
	private Grid last; //记录最后一个格子

	private final String obstacle = "1";//障碍物标记值
	private String end = "e";	////目标标记值
	private String start = "s";////开始标记值

	public int[] direction = new int[99999];
	 //目标坐标
	private int end_i = -1;
	private int end_j = -1;
	//开始目标
	private int start_i = -1;
	private int start_j = -1;
        
	int max_step=0;
	/**
	 * 初始化操作
	 * @param boxs
	 */
	public void init(String[][] boxs){
		for(int i=0;i<boxs.length;i++){
			for(int j=0;j<boxs[0].length;j++){
				if(boxs[i][j].equals(start)){
					start_i = i;
					start_j = j;
				}
				if(boxs[i][j].equals(end)){
					end_i = i;
					end_j = j;
				}
			}
		}

		Grid sGrid = new Grid(0, 0, 0, start_i, start_j, null);
		open = new MinHeap<Grid>();
		open.append(sGrid);///、将开始位置加入开集
	}
	/**
	 * 开始搜索
	 */
	public void search(String[][] boxs){
		int height = boxs.length;
		int width = boxs[0].length;
		while(open.peek()!=null){//对开集进行遍历，直到找到目标或者找不到通路
			Grid g = open.poll();
			int i = g.getI();
			int j = g.getJ();
			double pre_G = g.getG();///已耗费
			for(int h=-1;h<=1;h++){
				for(int w=-1;w<=1;w++){
                                        if(Math.abs(h*w) == 1) continue;
					int next_i = i + h;	///下一个将加入open 集的格子的i
					int next_j = j + w;///下一个将加入open 集的格子的j

					if(next_i>=0 && next_i<=height-1 && next_j>=0 && next_j<=width-1){
						////数组不越界，则进行计算
						if(boxs[next_i][next_j].equals(obstacle) || boxs[next_i][next_j].equals("-1") ||(h==0&&w==0)){
							//如果该格子是障碍，或者格子本身，跳过
							continue;
						}
						////计算该点到终点的最短路劲
						double H =  Math.abs(end_i - next_i) + Math.abs(end_j - next_j) ;
						if(H<1){
							///找到目标,记录并结束
							last = new Grid(0, pre_G, 0, next_i, next_j,g); ;
							return ;
						}
						////如果是对角线则加1.4，否则加1
						double G = Math.sqrt((next_i-i)*(next_i-i)+(next_j-j)*(next_j-j))>1 	? 	pre_G+1.4 	: 	pre_G+1;
						//生成新格子
						Grid temp = new Grid(H+G, G, H, next_i, next_j,g);
						////加入open集
						open.append(temp);
						boxs[i][j] = "-1";///表示此处已经计算过了
					}
				}
			}

			last = g;
		}


	}

	/**
	 * 打印路径
	 */
	public void printPath(){
                
                int lastI=-2,lastJ=-2,curI=-2,curJ=-2;
                int[] tmp_path = new int[99999];
		if(end_i!=last.getI()||end_j!=last.getJ()){
			System.out.println("无法到达终点！");
			return ;
		}

		System.out.println("路径逆序为:");
		int count = 0;
		while(true){
                        lastI = curI;
                        lastJ = curJ;
                        curI = last.getI();
                        curJ = last.getJ();
                        if( lastI != -2 ){
                            int tmp = 0;
                            if( curJ-lastJ == -1 ) { tmp = 2; }
                            else if( curJ-lastJ == 1 ) { tmp = 1; }
                            else if( curI-lastI == -1 ) { tmp = 4; }
                            else if ( curI-lastI == 1 ) { tmp = 3; }
                            tmp_path[max_step++] = tmp;
                        }
			System.out.print("("+last.getI()+","+last.getJ()+")");
                        count++;
			last = last.getParent();
			if(last==null){
				break;
			}
			System.out.print(" <———");
			if (count == 4){
				count = 0;
				System.out.println("");
			}

		}
                for( int i=0; i<max_step; i++ ){
                    direction[i] = tmp_path[max_step-i-1];
                }
	}

        public void initMapData(String[][] boxs) {
		//从文件中读取数据保存到labyrinthData数组中
    	String[] line=new String [23];
    	int m = 0;
    	
        try { 
            BufferedReader br = new BufferedReader(
            		new  InputStreamReader ( 
    	                    new FileInputStream("src/javaapplication1/maze.txt")));
            
            String line1 ;
            while ((line1= br.readLine()) != null) {
            	line[m]=line1 ;
            	m++;
        	}//while
        } catch (Exception e) {
            e.printStackTrace();
        }
       //将String转化为整型，放入labyrinthData数组中
        for(int j=0;j<23;j++) {
        for(int i=0;i<23;i++){
          String s2 = line[j].substring(i, i+1);
          boxs[j][i] = s2;  //??
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
	       }while(boxs[x1][y1] == "1" || boxs[x2][y2] == "1" || ((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)) < 150));
	       //确保出生地合理性并且出生地和出口相差足够远
	       boxs[x1][y1]="s";//设置2为人物数据
	       boxs[x2][y2]="e";//设置5为出口数据
               System.out.print(x1);
               System.out.print(y1);
               System.out.print(x2);
               System.out.print(y2);
               
	}

}
