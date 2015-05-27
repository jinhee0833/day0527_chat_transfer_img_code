package net.hb.day27;

import java.io.*;
import java.net.*;

public class FileRecvCliant {

	/*************************** 定数宣言 ***************************/
	public static final int INPUT_STREAM_BUFFER = 512;
	public static final int FILE_WRITE_BUFFER = 512;

	// 引数として、サーバのIPアドレスとポート番号を要求する
	public static void main(String[] args) {

		// 引数エラー
//		if (args.length != 2) {
//			System.err.println("argument error");
//			System.exit(1);
//		}

		OutputStream outStream; // 送信用ストリーム
		InputStream inStream; // 受信用ストリーム
		FileOutputStream fileOutStream; // ファイルの書き込むためのストリーム

		byte[] inputBuff = new byte[INPUT_STREAM_BUFFER]; // サーバからのls出力を受け取る
		byte[] fileBuff = new byte[FILE_WRITE_BUFFER]; // サーバからのファイル出力を受け取る

		String command; // キーボードからの入力を格納
		int recvFileSize; // InputStreamから受け取ったファイルのサイズ
		int recvByteLength = 0; // 受信したファイルのバイト数格納
		int waitCount = 0; // タイムアウト用

		// キーボードからの入力受付
		BufferedReader keyInputReader = new BufferedReader(new InputStreamReader(
				System.in));

		// tryが大きすぎる。訴訟も辞さない。
		try {
			// ソケットのコンストラクト
			Socket sock = new Socket("203.236.209.122",8888);

			// ソケットが生成できたらストリームを開く
			outStream = sock.getOutputStream();
			inStream = sock.getInputStream();

			// ここからループ
			while (true) {
				// コマンド入力を促す
				System.out.print("cmd:");

				// キーボードからのコマンド入力
				command = keyInputReader.readLine();

				// スペースごとにコマンドを区切る
				String[] getArgs = command.split(" ");

				// キーボードからの入力をそのまま送信
				outStream.write(command.getBytes(), 0, command.length());

				/************************ コマンド解析 ***********************/
				// lsの場合
				if (getArgs[0].equals("ls")) {
					waitCount = 0;
					// 入力を文字列として表示
					while (true) {
						// ストリームから読み込める時
						if (inStream.available() > 0) {
							// byte→Stringに変換して標準出力
							recvByteLength = inStream.read(inputBuff);
							String buff = new String(inputBuff);
							System.out.print(buff);
						}
						// タイムアウト処理
						else {
							waitCount++;
							Thread.sleep(100);
							if (waitCount > 10)
								break;
						}
					}
				}

				// getの場合
				if (getArgs[0].equals("get")) {
					// 引数で指定されたファイルを保存するためのストリーム
					fileOutStream = new FileOutputStream(getArgs[1]);
					waitCount = 0;

					// ストリームからの入力をファイルとして書き込む
					while (true) {
						// ストリームから読み込める時
						if (inStream.available() > 0) {
							// 受け取ったbyteをファイルに書き込み
							recvFileSize = inStream.read(fileBuff);
							fileOutStream.write(fileBuff, 0, recvFileSize);
						}

						// タイムアウト処理
						else {
							waitCount++;
							Thread.sleep(100);
							if (waitCount > 10)
								break;
						}
					}

					// ファイルの書き込みを閉じる
					fileOutStream.close();

					// 書き込み完了表示
					System.out.println("Download " + getArgs[1] + " done");
				}

				// exitコマンド入力でwhileループを抜ける
				if (getArgs[0].equals("exit"))
					break;
			}

			// ストリームのクローズ
			outStream.close();
			inStream.close();
		} catch (Exception e) {
			// 例外表示
			System.err.println(e);
			System.exit(1);
		}
	}
}