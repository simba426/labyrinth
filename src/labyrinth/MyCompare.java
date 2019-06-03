package labyrinth;

/**
 * 比较大小的函数接口
 */
public interface MyCompare {

	public boolean isLarger(MyCompare m2);

	public boolean isSmaller(MyCompare m2);

	public boolean isEqual(MyCompare m2);
}
