Akka実践バイブルのサンプルコードのJava版
==============

Akka実践バイブル「第2章　最小のAkkaアプリケーション」のサンプルコードをJavaに置き換えてみた。

### 必要な環境
* JDK8以上
* Maven

### ソースコードの取得
```
git clone <xxx.git>
```

### コンパイル・サーバー起動
- `chapter-up-and-running` ディレクトリで実行
```
mvn compile exec:exec
```

### テスト
- `chapter-up-and-running` ディレクトリで実行
```
mvn test
```

### APIエンドポイント

| 機能 | HTTPメソッド | パス | JSON |
| --- | ----- | ---- | --- |
| イベント作成 | POST | /events/<イベント名>/ | {"tickets":<枚数>} |
| チケット購入 | POST | /events/<イベント名>/tickets/ | {"tickets":<枚数>} |
| イベント一覧 | GET | /events/ | |
| イベント取得 | GET | /events/<イベント名>/ |
| イベントキャンセル | DELETE | /events/<イベント名>/ |
