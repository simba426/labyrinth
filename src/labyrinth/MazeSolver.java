package labyrinth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import qlearning.Agent;
import qlearning.State;

public class MazeSolver extends Agent {
	private Location location = new Location(0, 0);  //record the position of the agent
	private Location startLocation = new Location(0, 0);
	private Location endLocation = new Location(0, 0);
	private String end = "e";	////目标标记值
	private String start = "s";    //开始标记值
	private String[][] maze = new String[23][23];
	private int stepsToGoal;
	private double lastReward;

	public MazeSolver(String[][] maze) { //record the maze and the position of the agent
		int start_i = -1,start_j = -1;
                int end_i = -1,end_j = -1;
              
                for(int i=0;i<maze.length;i++){
			for(int j=0;j<maze[0].length;j++){
				if(maze[i][j].equals(start)){
					start_i = i;
					start_j = j;
				}
				if(maze[i][j].equals(end)){
					end_i = i;
					end_j = j;
				}
			}
                }
                this.startLocation.setRow(start_i);
                this.startLocation.setCol(start_j);
                this.endLocation.setRow(end_i);
                this.endLocation.setCol(end_j);
                this.location.setRow(start_i);
                this.location.setCol(start_j);
                this.maze = maze;
                
	}

        public void setAgentLocation(Location lastlocation, Location location) {
            int lastx, lasty;
            int curx, cury;
            lastx = lastlocation.getRow();
            lasty = lastlocation.getCol();
            curx = location.getRow();
            cury = location.getCol();
            this.maze[lastx][lasty] = "0";
            this.maze[curx][cury] = "s";
        }
        
        public boolean meet_the_block() {
            if( maze[location.getRow()][location.getCol()].equals("1") ) {
                return true;
            }
            return false;
        }
        
        public boolean win() {
            if( maze[location.getRow()][location.getCol()].equals("e")) {
                return true;
            }
            return false;
        }
        
	public void update() {
		double score = 0;
		stepsToGoal++;
		Location lastLocation = location;
		switch (nextAction(new State(location.getRow(), location.getCol()), lastReward)) {
		case UP:
			location = new Location(location.getRow() - 1, location.getCol());
			break;
		case DOWN:
			location = new Location(location.getRow() + 1, location.getCol());
			break;
		case LEFT:
			location = new Location(location.getRow(), location.getCol() - 1);
			break;
		case RIGHT:
			location = new Location(location.getRow(), location.getCol() + 1);
			break;
		}

		FileWriter fw = null;
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f=new File("src/labyrinth/step.txt");
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
                
		if (meet_the_block()/* when q_learning, the agent can walk to the block*/  == true) {// when the agent hits the block, give negative reward
			location = lastLocation; 
			lastReward = -2 * 0;
		} else if ( win() == true ) { //give reward and restart, when get to the end
			location = startLocation;  //this is important!!!
			System.out.println("the steps to goal: "+stepsToGoal);
			pw.println(stepsToGoal);
			pw.flush();
			try {
				fw.flush();
				pw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			lastReward = 1 * 1;
			stepsToGoal = 0;
                        System.out.println("the last reward is: "+lastReward+'\n');
		} else {
			this.setAgentLocation(lastLocation,location); /* set the agent to the location */
			lastReward = -1 * 0;
		}
	}

	public Location getLocation() {
		return location;
	}
        
        public int[][] getMaze(){
            int[][] tmpMaze = new int[23][23];
            for(int i=0;i<maze.length;i++){
			for(int j=0;j<maze[0].length;j++){
                                //String tmps = maze[i][j];
                                
				if(maze[i][j].equals(start)){
					tmpMaze[i][j] = 2;
				}
                                else if(maze[i][j].equals(end)){
                                        tmpMaze[i][j] = 5;
				}
                                else{
                                    tmpMaze[i][j] = Integer.valueOf(maze[i][j]).intValue();
                                }
			}
                }

			return tmpMaze;
        }
}
