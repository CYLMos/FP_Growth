import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * FP-Growth
 */
public class FPGrowth {

    private FPTree tree;

    private Map<String, Integer> items;

    private Map<String, ConditionalPatternBase> conditionalPatternBases;

    private List<List<String>> transactions;

    private int minsupport;

    public FPGrowth(String dataPath, int minsupport) {
        this.tree = new FPTree();
        this.items = new HashMap<>();
        this.conditionalPatternBases = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.minsupport = minsupport;

        this.readData(dataPath);

        /// 刪去小於minsupport的項目
        for(String item : this.items.keySet()) {
            /// 如果該項目小於minsupport，則從transactions中刪去
            if (this.items.get(item) < this.minsupport) {
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

                cpb.getItemSets().add(itemSet);

                if (node.getNextNode() == null) {
                    break;
                }

                node = node.getNextNode();
            }

            /// 將沒過minsupport的項目移除
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

                if(count < this.minsupport) {
                    for(List<String> itemSet : cpb.getItemSets()) {
                        itemSet.remove(it);
                    }
                }
            }

            this.conditionalPatternBases.put(item, cpb);
        }

        System.out.println("Success");

    }

    private void findFrequentItemSet() {
        for (String item : this.conditionalPatternBases.keySet()) {

        }
    }

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
