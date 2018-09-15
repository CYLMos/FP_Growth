import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * FP-Growth
 */
public class FPGrowth {

    /// FP-Tree
    private FPTree tree;

    /// 項目
    private Map<String, Integer> items;

    /// 記錄每個One ItemSet的Conditional Pattern Bases
    private Map<String, ConditionalPatternBase> conditionalPatternBases;

    /// 記錄頻繁項目集
    private Map<String, List<Set<String>>> frequentItemSets;

    /// 交易資料
    private List<List<String>> transactions;

    /// 最小支持度
    private int minSupport;

    /**
     * Constructor
     *
     * @param dataPath   資料庫路徑
     * @param minSupport 最小支持度
     */
    public FPGrowth(String dataPath, int minSupport) {
        this.items = new HashMap<>();
        this.conditionalPatternBases = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.frequentItemSets = new HashMap<>();
        this.minSupport = minSupport;

        this.readData(dataPath);

        this.tree = new FPTree(this.items);

        /// 刪去小於minsupport的項目
        for(String item : this.items.keySet()) {
            /// 如果該項目小於minsupport，則從transactions中刪去
            if (this.items.get(item) < this.minSupport) {
                for (List<String> transaction : this.transactions) {
                    transaction.remove(item);
                }
            }
        }

        /// 交易資料庫進行排序，並加入Tree中
        for(List<String> transaction : this.transactions) {
            Collections.sort(transaction, new Comparator<String>() {
                @Override
                public int compare(String ls, String rs) {
                    int rCount = items.get(rs);
                    int lCount = items.get(ls);

                    if(rCount > lCount) {
                        return 1;
                    }
                    else if(rCount < lCount) {
                        return -1;
                    }
                    else {
                        return rs.compareTo(ls) < 0 ? 1 : -1;
                    }
                }
            });

            LinkedList<String> t = new LinkedList<>(transaction);

            this.tree.add(t, this.tree.getRoot());
        }

        /// 排序HeaderTable
        this.tree.sortHeaderTable();

        /// 找出Conditional Pattern Base
        for(String item : this.tree.getHeaderTable().keySet()) {
            Node node = this.tree.getHeaderTable().get(item);
            ConditionalPatternBase cpb = new ConditionalPatternBase();

            while(true) {
                List<String> itemSet = new LinkedList<>();
                Node parent = node.getParent();

                cpb.getCounts().add(node.getCount());

                while(!parent.isRoot()) {
                    itemSet.add(parent.getItem());

                    parent = parent.getParent();
                }

                /// 倒序
                Collections.reverse(itemSet);
                cpb.getItemSets().add(itemSet);

                if (node.getNextNode() == null) {
                    break;
                }

                node = node.getNextNode();
            }

            /// 將沒過minSupport的項目移除
            Set<String> set = new HashSet<>();
            for(List<String> itemSet : cpb.getItemSets()) {
                set.addAll(itemSet);
            }

            for(String it : set) {
                int count = 0;

                int index = 0;
                for(List<String> itemSet : cpb.getItemSets()) {
                    if(itemSet.contains(it)) {
                        count += cpb.getCounts().get(index);
                    }

                    ++index;
                }

                if(count < this.minSupport) {
                    for(List<String> itemSet : cpb.getItemSets()) {
                        itemSet.remove(it);
                    }
                }
            }

            /// 記錄該項目的Conditional Pattern Base
            this.conditionalPatternBases.put(item, cpb);
        }

        System.out.println("Frequent ItemSet:");
        System.out.println();

        /// 尋找頻繁項目集
        this.findFPConditionalTree();

        System.out.println("End");

    }

    /**
     * 從Conditional FP-Tree尋找頻繁項目集
     */
    private void findFPConditionalTree() {
        for(Map.Entry<String, Node> item : this.tree.getHeaderTable().entrySet()) {
            List<List<String>> itemSets = this.conditionalPatternBases.get(item.getKey()).getItemSets();

            /// 建立Conditional FP-Tree
            FPTree subTree = new FPTree(this.items);

            for(List<String> itemSet : itemSets) {
                LinkedList<String> t = new LinkedList<>(itemSet);

                subTree.add(t, subTree.getRoot());
            }

            /// 如果還沒有實例，建立一個
            if(this.frequentItemSets.get(item.getKey()) == null) {
                List<Set<String>> data = new LinkedList<>();
                this.frequentItemSets.put(item.getKey(), data);
            }

            Set<String> frequentItemSet = new HashSet<>();

            /// 開始從Conditional FP-Tree尋找頻繁項目集
            this.findFrequentItemSet(subTree, frequentItemSet, item.getKey());

            /// 印出
            System.out.println(item.getKey() + ":");
            System.out.printf("[%s]\n", item.getKey());

            for(Set<String> set : this.frequentItemSets.get(item.getKey())) {
                set.add(item.getKey());

                System.out.println(set);
            }

            System.out.println();
        }
    }

    /**
     * 從一個oneItemSet的Conditional FP-Tree尋找頻繁項目集
     *
     * @param tree            子樹
     * @param frequentItemSet 頻繁項目集
     * @param item            目前正在執行的oneItemSet
     */
    private void findFrequentItemSet(FPTree tree, Set<String> frequentItemSet, String item) {
        LinkedHashMap<String, Node> headerTable = (LinkedHashMap<String, Node>) tree.getHeaderTable();

        for(Map.Entry<String, Node> itemLink : headerTable.entrySet()) {
            frequentItemSet.add(itemLink.getKey());

            /// 將得到的頻繁項目集加入
            this.frequentItemSets.get(item).add(new HashSet<>(frequentItemSet));

            /// 如果parent還不是root，繼續迭代
            if(itemLink.getValue().getParent() != null) {
                List<String> t = new LinkedList<>();
                Node parent = itemLink.getValue().getParent();

                while(!parent.isRoot()) {
                    t.add(parent.getItem());

                    parent = parent.getParent();
                }

                /// 倒序
                Collections.reverse(t);

                /// 建立新的FP-Tree
                FPTree subTree = new FPTree(this.items);
                subTree.add((LinkedList<String>) t, subTree.getRoot());

                /// 繼續迭代
                this.findFrequentItemSet(subTree, new HashSet<>(frequentItemSet), item);
            }
            else {
                return;
            }

            frequentItemSet.remove(itemLink.getKey());
        }
    }

    /**
     * 讀取資料
     *
     * @param dataPath 資料庫路徑
     */
    private void readData(String dataPath) {
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(dataPath));

            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(" ");

                for(String item : data) {
                    if (!this.items.containsKey(item)) {
                        this.items.put(item, 1);
                    }
                    else {
                        this.items.put(item, this.items.get(item) + 1);
                    }
                }

                List<String> transaction = new LinkedList<>(Arrays.asList(data));
                this.transactions.add(transaction);
            }
        }
        catch(Exception e) {
            System.err.println(e.toString());

            System.exit(0);
        }
    }

    /**
     * ConditionalPatternBase
     */
    private class ConditionalPatternBase {

        /// 項目集
        private List<List<String>> itemSets;

        /// 出現次數
        private List<Integer> counts;

        public ConditionalPatternBase() {
            this.itemSets = new LinkedList<>();
            this.counts = new LinkedList<>();
        }

        /**
         * 設定項目集
         *
         * @param itemSets 項目集
         */
        public void setItemSets(List<List<String>> itemSets) {
            this.itemSets = itemSets;
        }

        /**
         * 設定出現次數
         *
         * @param counts 出現次數
         */
        public void setCounts(List<Integer> counts) {
            this.counts = counts;
        }

        /**
         * 取得項目集
         *
         * @return 項目集
         */
        public List<List<String>> getItemSets() {
            return this.itemSets;
        }

        /**
         * 取得出現次數
         *
         * @return 出現次數
         */
        public List<Integer> getCounts() {
            return this.counts;
        }
    }
}
