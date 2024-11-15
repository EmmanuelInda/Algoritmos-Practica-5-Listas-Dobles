package lists;

import java.util.Random;

public class DoubleList<T> {
	private DoubleNode<T> head;

	public DoubleList() {
		head = null;
	}

	public void insertFirst(T data) {
		DoubleNode<T> newNode = new DoubleNode<>(data);

		newNode.setNext(head);
		if (head != null) head.setPrev(newNode);
		head = newNode;
	}

	public T deleteFirst() {
		if (head == null) return null;

		DoubleNode<T> temp = head;
		T data = temp.getData();

		head = head.getNext();
		if (head != null) head.setPrev(null);

		return data;
	}

	public void insertLast(T data) {
		DoubleNode<T> newNode = new DoubleNode<>(data);

		if (head == null) {
			head = newNode;
		} else {
			DoubleNode<T> last = nodeAt(size() - 1);
			last.setNext(newNode);
			newNode.setPrev(last);
		}
	}

	public T deleteLast() {
		if (head == null) return null;

		T data;

		if (head.getNext() == null) {
			data = head.getData();
			head = null;
			return data;
		}

		DoubleNode<T> nextToLast = nodeAt(size() - 2);
		DoubleNode<T> last = nodeAt(size() - 1);
		data = last.getData();

		nextToLast.setNext(null);

		return data;
	}

	public void insertAt(int pos, T data) {
		DoubleNode<T> newNode = new DoubleNode<>(data);

		if (pos == 0) {
			newNode.setNext(head);
			if (head != null) head.setPrev(newNode);
			head = newNode;
			return;
		}

		DoubleNode<T> prev = nodeAt(pos - 1);

		newNode.setNext(prev.getNext());
		newNode.setPrev(prev);

		if (prev.getNext() != null) prev.getNext().setPrev(newNode);
		prev.setNext(newNode);
	}

	public T deleteAt(int pos) {
		T data;

		if (pos == 0) {
			data = head.getData();
			head = head.getNext();
			if (head != null) head.setPrev(null);
			return data;
		}

		DoubleNode<T> current = nodeAt(pos);
		DoubleNode<T> prev = nodeAt(pos - 1);
		data = current.getData();

		prev.setNext(current.getNext());
		if (current.getNext() != null) current.getNext().setPrev(prev);

		return data;
	}

	public int size() {
		int i = 0;
		DoubleNode<T> temp = head;

		while (temp != null) {
			++i;
			temp = temp.getNext();
		}

		return i;
	}

	public void shuffle() {
		if (head == null || head.getNext() == null) return;

		Random rand = new Random();
		int size = size();

		for (int i = 0; i < size; ++i) {
			int swapIdx = rand.nextInt(size);

			DoubleNode<T> node1 = nodeAt(i);
			DoubleNode<T> node2 = nodeAt(swapIdx);

			T temp = node1.getData();
			node1.setData(node2.getData());
			node2.setData(temp);
		}
	}

	/*
	 *		Shell sort
	 */
	public void sort() {
		int n = size();

		for (int gap = n / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < n; ++i) {
				T temp = nodeAt(i).getData();
				int j;
				for (j = i; j >= gap && ((Comparable<T>) nodeAt(j - gap).getData()).compareTo(temp) > 0; j -= gap) {
					nodeAt(j).setData(nodeAt(j - gap).getData());
				}
				nodeAt(j).setData(temp);
			}
		}
	}

	public T get(int pos) {
		DoubleNode<T> temp = nodeAt(pos);

		if (temp == null) return null;
		return temp.getData();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		DoubleNode<T> temp = head;

		while (temp != null) {
			sb.append(temp.getData());
			if (temp.getNext() != null) sb.append(" ");

			temp = temp.getNext();
		}

		return sb.toString();
	}

	private DoubleNode<T> nodeAt(int pos) {
		DoubleNode<T> temp = head;

		for (int i = 0; i < pos && temp != null; ++i)
			temp = temp.getNext();

		return temp;
	}
}
