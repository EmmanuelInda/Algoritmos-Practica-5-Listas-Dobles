package lists;

public class DoubleNode<T> {
	private T data;
	private DoubleNode<T> next;
	private DoubleNode<T> prev;

	public DoubleNode() {
		this(null);
	}

	public DoubleNode(T data) {
		this.data = data;
		this.next = null;
		this.prev = null;
	}

	public T getData() {
		return data;
	}

	public DoubleNode<T> getNext() {
		return next;
	}

	public DoubleNode<T> getPrev() {
		return prev;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setNext(DoubleNode<T> next) {
		this.next = next;
	}

	public void setPrev(DoubleNode<T> prev) {
		this.prev = prev;
	}
}
