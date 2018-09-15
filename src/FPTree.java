import java.util.*;

/**
 * FP-Tree
 */
public class FPTree {

    /// 根節點
    private Node root;

    /// 表頭
    private Map<String, Node> headerTable;

    /// 項目
    private Map<String, Integer> items;

    /**
     * Constructor
     *
     * @param root 根節點
     */
    public FPTree(Node root, Map<String, Integer> items) {
        this.root = root;
        this.headerTable = new LinkedHashMap<>();
        this.items = items;
    }

    /**
     * Constructor
     */
    public FPTree(Map<String, Integer> items) {
        this.root = new Node();
        this.headerTable = new LinkedHashMap<>();
        this.items = items;
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
            /// 如果已經包含在根節點的子結點中了，則直接加入
            if (node.getItem().equals(item)) {
                node.setCount(node.getCount() + 1);

                /// 繼續迭代
                this.add(items, node);

                return root;
            }
        }

        /// 如果根節點沒有包含，則新建一個
        Node node = new Node(item, 1, root);

        root.getChildNodes().add(node);

        Node nextNode = this.headerTable.get(item);

        /// 記錄相同項目的節點與表頭的連結
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

        /// 繼續迭代
        this.add(items, node);

        return root;
    }

    /**
     * 排序表頭
     */
    public void sortHeaderTable() {
        List<Map.Entry<String, Node>> entries = new ArrayList<>(this.headerTable.entrySet());
        Collections.sort(entries, new HeaderTableSorter(this.items));

        this.headerTable.clear();

        for (Map.Entry<String, Node> entry : entries) {
            this.headerTable.put(entry.getKey(), entry.getValue());
        }
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

    /**
     * 排序表頭的Sorter
     */
    private class HeaderTableSorter implements Comparator<Map.Entry<String, Node>> {

        private Map<String, Integer> items;

        public HeaderTableSorter(Map<String, Integer> items) {
            this.items = items;
        }

        @Override
        public int compare(Map.Entry<String, Node> le, Map.Entry<String, Node> re) {

            int rCount = this.items.get(re.getKey());
            int lCount = this.items.get(le.getKey());

            if(rCount > lCount) {
                return -1;
            }
            else if(rCount < lCount) {
                return 1;
            }
            else {
                return re.getKey().compareTo(le.getKey()) < 0 ? -1 : 1;
            }
        }
    }
}
