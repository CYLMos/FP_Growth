/**
 * Main
 *
 * 參數 -d : 後面接資料庫路徑，ex : -d /home/user/data
 * 參數 -s : 後面接最小支持度，ex : -s 3
 */
public class Main {

    public static void main (String[] argv) {

        String dataPath = "";
        int minSupport = 3;

        for(int i = 0; i < argv.length;) {
            String input = argv[i];

            if(argv.length - 1 >= i + 1) {
                if(input.equals("-d")) {
                    dataPath = argv[i + 1];
                }
                else if(input.equals("-s")) {
                    minSupport = Integer.parseInt(argv[i + 1]);
                }

                i += 2;
            }
            else {
                System.err.print("Arguments Error !");
                return;
            }
        }

        FPGrowth fpGrowth = new FPGrowth(dataPath, minSupport);

        return;
    }
}
