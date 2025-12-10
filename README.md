# 🎉 thruster – 問い合わせ管理アプリ　
 このアプリは、  

- ユーザーが「氏名／社員番号／ジャンル／内容」を入力して問い合わせを送信  
- 管理者が問い合わせの履歴を一覧で確認  
というフローで動作する、**シンプルな問い合わせ管理システム**です。
<img src="https://github.com/user-attachments/assets/794785b0-676a-4d47-9fad-6bf5eb62ab7f" width="100%" alt="トップページ" />
🌐 Service URL（サービス URL）

## 🚧 現在はローカル環境のみで動作しています。
### ☕ ローカルでの起動方法（Tomcat で動作）

このアプリは Servlet（Tomcat）ベース で動作します。
以下の手順でローカル環境から起動できます。

### 📌 前提環境

Java 17

Apache Tomcat 9.x

Maven 3.x

### ① WAR ファイルをビルド
mvn clean package


target/ フォルダに inquiry.war が生成されます。

### ② Tomcat の webapps に配置
/apache-tomcat-9.x/webapps/


へ WAR をコピーすると、自動的に展開されます。

### ③ Tomcat を起動

Windows↓

catalina.bat run


Mac / Linux↓

catalina.sh run

### ④ ブラウザからアクセス
http://localhost:8080/inquiry

## 💡 アプリを作ったキッカケ

アルバイトとして働く中で、
社員と話す時間がない・言いづらい・勇気が出ない
そんな理由で、自分の意見や問題点を「心の中にしまい込んでしまう」人をたくさん見てきました。

本当は困っているのに誰にも言えない。
改善できるのに声が届かない。
そんな状況が続くと、働く環境は少しずつ悪くなり、
本人も職場も、どちらも損をしてしまう ――。

「もっと気軽に意見を伝えられる仕組みがあったらいいのに」
そんな思いが、このアプリを作ったキッカケです。

## 🎯 アプリに込めた思い・実現したい未来

このアプリで実現したいのは、
“誰でも、安心して意見を伝えられる職場” です。

会話するのが苦手でも

上司に言いづらくても

同僚同士だけで抱え込まずに

適切な人にちゃんと意見や問題が届く。
そんな環境があれば、働く人のストレスは確実に減り、
組織全体の雰囲気も良くなります。

このアプリを通して、
声を届けられずに悩む人が少しでも減ってほしい。
そして
「意見してよかった」「相談してよかった」
と思える職場が増えてほしい。

そんな願いを込めて、このサービスを作りました。
## 🚀 Features（機能一覧）
 ✏️ 1. 問い合わせ登録
<table> <tr> <td width="55%"> <ul> <li>名前・従業員番号・種別・内容を入力して問い合わせを送信</li> <li>送信前に<strong>確認画面</strong>を表示する 2 ステップ構造</li> <li>必須項目バリデーション対応</li> </ul> </td> <td width="45%"> <img src="https://github.com/user-attachments/assets/ad1c24e6-81d2-4040-b86b-1275ffc57c66" width="100%" /> </td> </tr> </table>
  📜 2. 問い合わせ履歴
  
<table> <tr> <td width="45%"> <img src="https://github.com/user-attachments/assets/be8cf16b-86da-4687-ad6a-3e256d84f1d0" width="100%" /> </td> <td width="55%"> <ul> <li>問い合わせを一覧で確認</li> <li>ジャンルでフィルタリング可能</li> <li>新しい順に表示</li> </ul> </td> </tr> </table>

## 🛠️ 主な使用技術（Tech Stack）
 🔧 フロントエンド
<p align="left"> <img src="https://img.shields.io/badge/HTML5-E34F26?logo=html5&logoColor=white" /> <img src="https://img.shields.io/badge/CSS3-1572B6?logo=css3&logoColor=white" /> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black" /> <img src="https://img.shields.io/badge/JSP-007396?logo=java&logoColor=white" /> </p>
 ⚙️ バックエンド
<p align="left"> <img src="https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white" /> <img src="https://img.shields.io/badge/Jakarta%20Servlet-orange?logo=jakartaee&logoColor=white" /> <img src="https://img.shields.io/badge/Tomcat-9-F8DC75?logo=apachetomcat&logoColor=black" /> </p>
 🗄️ データベース
<p align="left"> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white" /> </p>
 📦 ビルド / 管理
<p align="left"> <img src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white" /> </p>

## 📘 ER Diagram（ER 図）
<img width="317" height="376" alt="Untitled" src="https://github.com/user-attachments/assets/222d8fbd-3c9f-43cd-9253-b7aac7a6942e" />

このアプリでは、問い合わせデータを管理するために
シンプルな 1 テーブル構成（inquiries） を採用しています。

ユーザーは「問い合わせ」データのみを登録

管理者は同じテーブルから履歴・状態を参照

ジャンル・ステータス・登録日時などの項目で柔軟に管理可能

複雑なユーザー認証や他テーブルとのリレーションを持たず、
最小限の構造で 問い合わせの登録・管理に特化した設計 になっています。
