package labyrinth;

/**
 * 最小堆
 */
public class MinHeap<E extends MyCompare> {
	private int size;
	private Object[] element;

	public MinHeap(int maxSize){
		size = 0;
		element = new Object[maxSize];
	}
	public MinHeap(){
		this(10);
	}

	/**
	 * 元素入堆
	 * @param e
	 */
	public void append(E e){
		ensureCapacity(size+1);
		element[size++] = e;///put the element to the end of the heap

		adjustUp(); //adjust the heap to minHeap
	}
	/**
	 * 取出堆顶元素（最小元素）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E poll(){
		if(isEmpty()){
			return null;
		}

		E min = (E) element[0];
		element[0] = element[size-1];///replace the min element with the last element
		element[size-1] = null ;///let gc do its work
		size--;

		adjustDown();///adjust the heap to minHeap

		return min;
	}
	/**
	 * 查看堆顶元素（最小元素）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E  peek(){
		if(isEmpty()){
			return null;
		}
		return (E) element[0];
	}
	/**
	 * 是否为空堆
	 * @return
	 */
	public boolean isEmpty(){
		return size == 0 ;
	}

	/**
	 * 确保容量空间足够
	 * @param minCapacity
	 */
	private void ensureCapacity(int minCapacity){
		int oldCapacity = element.length;
		if(minCapacity > oldCapacity){
			int newCapacity = (oldCapacity*3)/2+1;///每次扩容至1.5倍
			Object[] copy = new Object[newCapacity];
			///调用本地C方法进行数组复制
			System.arraycopy(element, 0, copy, 0, element.length);

			element = copy;
		}
	}

	/**
	 * 向上调整为堆，将小值往上调
	 */
	@SuppressWarnings("unchecked")
	private void adjustUp(){

		E temp = (E) element[size-1]; ///get the last element
		int parent = size - 1;
		while(parent>0&&((E)element[(size - 1)/2]).isLarger(temp)){
			///if smaller than it parent
			element[parent] = element[(parent - 1)/2];
			parent = (parent - 1)/2;
		}
		element[parent] = temp;
	}

	/**
	 * 向下调整为堆
	 */
	@SuppressWarnings("unchecked")
	private void adjustDown(){
		E temp = (E) element[0]; ///get the first element
		int child = 1;
		while(child<size){
			E left = (E) element[child];
			E right = (E) element[child+1];///这里的child+1不会越界（想想为什么）
			if(right!=null&&left.isLarger(right)){
				child++;
			}

			if(temp.isSmaller((E)element[child])){
				break; ////如果比两个孩子中较小者都还小，则结束
			}

			element[(child-1)/2] = element[child]; ///assign the smaller to its parent
			child = child*2 + 1;
		}

		element[(child-1)/2] = temp;
	}
}
