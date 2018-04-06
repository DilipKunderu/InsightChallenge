/**
 * Author: @DilipKunderu
 */
import java.util.ArrayList;
import java.util.List;

public class MinHeap {

    List<Node> list = new ArrayList<>();
    MinHeap() {
        list.add(new Node("", 1, 1, 1, 1));
        size = 0;
    }
    int size;
    public void add(Node nd) {
        if (list.isEmpty()) {
            size++;
            list.add(nd);
            nd.index = 1;
            return;
        }
        size++;
        list.add(nd);
        nd.index = size;
        int i = list.size()-1, pi = parent(i);
        while(pi > 0 && list.get(pi).val > list.get(i).val) {
            swap(i, pi);
            i = pi;
            pi = parent(i);
        }
    }

    private void swap(int i, int j) {
        list.get(i).index = j;
        list.get(j).index = i;
        Node nd = list.get(i);
        list.set(i, list.get(j));
        list.set(j, nd);
    }

    public Node peek() {
        return list.get(1);
    }

    public int parent(int i) {
        return i/2;
    }

    public int left(int i) {
        return 2*i;
    }

    public int right(int i ) {
        return 2*i + 1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node remove() {
        if (size == 0) return null;
        Node root = list.get(1);
        size--;
        if (size == 0) {
            list.remove(1);
            return root;
        }
        list.set(1, list.get(list.size()-1));
        list.remove(list.size()-1);
        list.get(1).index = 1;
        heapify(1);
        return root;
    }

    public void increaseKey(Node nd, long val) {
        nd.val = val;
        heapify(nd.index);
    }

    public void heapify(int i) {
        int li = left(i);
        int ri = right(i);
        if (li > size) {
            return;
        }
        int ti = li;
        if (ri <= size) {
            if (list.get(li).val > list.get(ri).val) {
                ti = ri;
            }
        }
        if (list.get(ti).val < list.get(i).val) {
            swap(ti, i);
            heapify(ti);
        }
    }
}
