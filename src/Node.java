import java.util.LinkedList;
import java.util.List;

/**
 * FP-Tree node
 */
public class Node {

    /// 項目名稱
    private String item;

    /// 出現次數
    private int count;

    /// 是否為根節點
    private boolean root;

    /// 母節點
    private Node parent;

    /// 子節點
    private List<Node> childNodes;

    /// 下個相同節點
    private Node nextNode = null;

    /**
     * Constructor
     *
     * @param item   項目名稱
     * @param count  出現次數
     * @param parent 母節點
     */
    public Node(String item, int count, Node parent) {
        this.item = item;
        this.count = count;
        this.parent = parent;
        this.childNodes = new LinkedList<>();
        this.root = false;
    }

    /**
     * Constructor
     */
    public Node() {
        this.parent = null;
        this.childNodes = new LinkedList<>();
        this.root = true;
    }

    /**
     * 設定項目名稱
     *
     * @param item 項目名稱
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * 設定出現次數
     *
     * @param count 出現次數
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 設定母節點
     *
     * @param parent 母節點
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 設定子節點
     *
     * @param nodes 子節點
     */
    public void setChildNodes(List<Node> nodes) {
        this.childNodes = nodes;
    }

    /**
     * 設定下個相同節點
     *
     * @param node 下個相同節點
     */
    public void setNextNode(Node node) {
        this.nextNode = node;
    }

    /**
     * 取得項目名稱
     *
     * @return 項目名稱
     */
    public String getItem() {
        return this.item;
    }

    /**
     * 取得出現次數
     *
     * @return 出現次數
     */
    public int getCount() {
        return this.count;
    }

    /**
     * 取得母節點
     *
     * @return 母節點
     */
    public Node getParent() {
        return this.parent;
    }

    /**
     * 取得子節點
     *
     * @return 子節點
     */
    public List<Node> getChildNodes() {
        return this.childNodes;
    }

    /**
     * 取得下個相同節點
     *
     * @return 下個相同節點
     */
    public Node getNextNode() {
        return this.nextNode;
    }

    /**
     * 確認是否為根節點
     *
     * @return 是否為根節點
     */
    public boolean isRoot() {
        return this.root;
    }
}
