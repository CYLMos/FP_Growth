FP_Growth簡單範例
=

#注意事項

撰寫時並未考慮效能問題，有需要請依自己想法進行最佳化

程式語言是Java，使用的是OpenJDK 11，但是與Oracle版的的使用上其實沒差
差別就只是與Oracle的部份內部實作與是否完全開源

IDE使用的是IntelliJ(JetBrains的產品能不用嗎？)

#操作說明

參數 -d : 後面接資料庫路徑，ex : -d /home/user/data
參數 -s : 後面接最小支持度，ex : -s 3

執行範例(最小支持度為3)：
java Main -d /home/user/data -s 3

#備註

如果覺得有Bug，請回報給我，謝謝！
