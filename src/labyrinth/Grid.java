package labyrinth;

/**
 * 比较大小的函数接口
 */

/*
public interface MyCompare {

	public boolean isLarger(MyCompare m2);

	public boolean isSmaller(MyCompare m2);

	public boolean isEqual(MyCompare m2);
}
*/

/**
 * pojo ，格子
 * <pre> F = G + H
 * G 表示从起点 A 移动到网格上指定方格的移动耗费 (可沿斜方向移动,斜方向的代价为对角线长度)
 * H 表示从指定的方格移动到终点 B 的预计耗费 (H 有很多计算方法, 这里我们设定只可以上下左右移动).</pre>
 */
public class Grid implements MyCompare{

	private double F;
	private double H;
	private double G;

	private int i ;
	private int j;

	private Grid parent; ///该格子的父格子

	/**
	 * pojo ，格子
	 * @param F F = G + H
	 * @param G 表示从起点 A 移动到网格上指定方格的移动耗费 (可沿斜方向移动,斜方向的代价为对角线长度)
	 * @param H	表示从指定的方格移动到终点 B 的预计耗费 (H 有很多计算方法, 这里我们设定只可以上下左右移动).
	 * @param i 纵坐标i
	 * @param j 横坐标j
	 * @param parent  父结点
	 */
	public Grid(double F,double G,double H,int i,int j,Grid parent){
		this.F = F;
		this.G = G;
		this.H = H;
		this.i = i;
		this.j = j;
		this.parent = parent;
	}
	public Grid(){}



	public Grid getParent() {
		return parent;
	}
	public void setParent(Grid parent) {
		this.parent = parent;
	}

	public int getI() {
		return i;
	}
	public int getJ() {
		return j;
	}
	public void setI(int i) {
		this.i = i;
	}
	public void setJ(int j) {
		this.j = j;
	}


	/**
	 * 经过当前点到终点B的总耗费  期望值
	 * @return
	 */
	public double getF() {
		return F;
	}

	/**
	 * H 表示从指定的方格移动到终点 B 的预计耗费 (H 有很多计算方法, 这里我们设定只可以上下左右移动)
	 * @return
	 */
	public double getH() {
		return H;
	}
	/**
	 * 表示从起点 A 移动到当前网格上的移动耗费 (可沿斜方向移动,斜方向的代价为对角线长度)
	 * @return
	 */
	public double getG() {
		return G;
	}



	public void setF(double f) {
		F = f;
	}
	public void setH(double h) {
		H = h;
	}
	public void setG(double g) {
		G = g;
	}

	@Override
	public boolean isLarger(MyCompare m2) {
		// TODO Auto-generated method stub
		return this.F>((Grid)m2).getF();
	}
	@Override
	public boolean isSmaller(MyCompare m2) {
		// TODO Auto-generated method stub
		return this.F<((Grid)m2).getF();
	}
	@Override
	public boolean isEqual(MyCompare m2) {
		// TODO Auto-generated method stub
		return this.F==((Grid)m2).getF();
	}

}

