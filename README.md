# ディレクトリ構成
- bin: classファイル
- src: javaファイル
- jar: 外部jar
- prime: 乱数シード用のファイル（読み込むシード値が記載）
- properties: propertiesファイル
- shell: シェルスクリプトファイル
- class_figure: クラス図

# プログラム実行方法
- run.shのシェルスクリプトを実行すると実験が回せるようになっている

# 注意点
- .classpathに書かれてる"EXTERNAL_JAR"はjarディレクトリのパスをeclipseで変数として作成してある
- antをインストールすれば```ant```コマンドでビルドができる（はず）
    - ただし、ビルドに必要なxmlファイルを作成する必要あり ←eclipseで作成できる！

# メールやスラックに通知を飛ばしたいとき
1. ```cp properties/prototype/*.properties properties/```を実行
2. propertiesファイルに必要項目を記入

- 暗号化されたファイルは```properties/encryption/```にある