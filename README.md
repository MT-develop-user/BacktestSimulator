# 投資バックテストシミュレーター README

## 1. 本アプリケーションの仕様

以下のファイルを参照。
- [`README_reference/specification.md`](README_reference/specification.md)

## 2. 本アプリケーションのイメージ

以下の画像サンプルを参照。
- [`README_reference/image_01.png`](README_reference/image_01.png)　⇒起動直後のイメージ。
- [`README_reference/image_02.png`](README_reference/image_02.png)　⇒ユーザー入力後のイメージ。（最大5銘柄まで入力可能）
- [`README_reference/image_03.png`](README_reference/image_03.png)　⇒バックテスト実行後のイメージ。（各種算出結果、円グラフ、折れ線グラフ）

## 3. 本アプリケーションの起動方法

### 3.1. GitHubからcloneする。

ターミナルを開き、GitHub上のプロジェクトをcloneしたいフォルダに移動してから、以下のコマンドを実行する。
```sh
git clone https://github.com/MT-develop-user/BacktestSimulator.git .
```

### 3.2. アプリケーションを起動する。

`Project_BacktestSimulator`フォルダ直下で以下のコマンドを実行する。
```sh
mvn spring-boot:run
```

### 3.3. ブラウザからローカルホストにアクセスする。

ブラウザのアドレスバーに以下を入力する。

<http://localhost:8080/>

## 4. 本アプリケーションの停止方法

### 4.1. アプリケーション起動中のターミナルから停止させる。

アプリケーション起動中のターミナルで、`Ctrl` + `C` を押下する。その後、「バッチ ジョブを終了しますか (Y/N)?」というメッセージが表示されるので `Y` を入力してEnterキーを押下する。

### 4.2. （予備手順）別のターミナルから停止させる。

> ※ 手順4.1でアプリケーションが正常に停止できた場合、手順4.2は実施不要となる。

新規ターミナルを開き、以下のWindowsコマンドを実行することで、実行中のSpring Bootアプリケーションを停止させる。
```sh
taskkill /F /IM java.exe
```