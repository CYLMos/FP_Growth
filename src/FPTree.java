import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * FP-Tree
 */
public class FPTree {

    /// 根節點
    private Node root;

    /// 表頭
    private Map<String, Node> headerTable;

    /**
     * Constructor
     *
     * @param root 根節點
     */
    public FPTree(Node root) {
        this.root = root;
        this.headerTable = new HashMap<>();
    }

    /**
     * Constructor
     */
    public FPTree() {
        this.root = new Node();
        this.headerTable = new HashMap<>();
    }

    /**
     * 將一筆交易加入Tree中
     *
     * @param items 交易項目
     * @param root  根節點
     * @return      回傳根節點
     */
    public Node add(LinkedList<String> items, Node root) {
        if (items.size() <= 0) {
            return null;
        }

        String item = items.poll();

        for (Node node : root.getChildNodes()) {
            if (node.getItem().equals(item)) {
                node.setCount(node.getCount() + 1);

                this.add(items, node);

                return root;
            }
        }

        Node node = new Node(item, 1, root);

        root.getChildNodes().add(node);

        Node nextNode = this.headerTable.get(item);

        if (nextNode == null) {
            this.headerTable.put(item, node);
        }
        else {
            while(nextNode != null) {
                if (nextNode.getNextNode() != null) {
                    nextNode = nextNode.getNextNode();
                }
                else {
                    nextNode.setNextNode(node);

                    break;
                }
            }
        }

        this.add(items, node);

        return root;
    }

    /**
     * 設定表頭
     *
     * @param headerTable 表頭
     */
    public void setHeaderTable(Map<String, Node> headerTable) {
        this.headerTable = headerTable;
    }

    /**
     * 設定根節點
     *
     * @param root 根節點
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * 取得根節點
     *
     * @return 根節點
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * 取得表頭
     *
     * @return 表頭
     */
    public Map<String, Node> getHeaderTable() {
        return this.headerTable;
    }
}
