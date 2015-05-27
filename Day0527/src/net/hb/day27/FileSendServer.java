package net.hb.day27;
import java.io.*;	//入出力ストリーム
import java.net.*;	//socket


public class FileSendServer {

	/************************定数宣言***********************/
	public static final int INPUT_STREAM_BUFFER = 512;	//入力ストリーム格納バッファサイズ
	public static final int FILE_READ_BUFFER = 512;	//ファイル読み込みバッファサイズ
 
	//引数は使用しない	
	public static void main(String[] args){
 
		ServerSocket servSock = null;
		Socket sock;    //socket通信
 
		OutputStream outStream;	//送信用ストリーム
		InputStream inStream;	//受信用ストリーム
		FileInputStream fileInStream; //ファイルを読むためのストリーム
		byte[] inputBuff = new byte[INPUT_STREAM_BUFFER];	//クライアントからのコマンド入力を受け取る
		byte[] fileBuff = new byte[FILE_READ_BUFFER];	//ファイルから読み込むバッファ
 
		String absolutePath = new File("").getAbsolutePath();
		File file = new File(absolutePath);	//カレントディレクトリでインスタンス作成
		String[] 	fileList = file.list();	//カレントディレクトリのファイル名取得
		Boolean hiddenFileFlag;	//隠しファイルフラグ
		Boolean lsFlag;
		int recvByteLength;
 
		//大きすぎるtryは身を滅ぼす
		try
		{
 
			//6001番ポート
			servSock = new ServerSocket(8888);
 
			//ソケットに対する接続要求を待機	
			sock = servSock.accept();
 
			//入出力ストリーム設定
			outStream = sock.getOutputStream();
			inStream = sock.getInputStream();
 
			//コマンドの入力があった場合に行う処理(1文字以上読みこんだとする)
			while( (recvByteLength = inStream.read(inputBuff))  != -1 )
			{
				//受け取ったbyte列をStringに変換(構文解析のため)
				String buff = new String(inputBuff , 0 , recvByteLength);
 
				//スペースで区切り格納し直す(sscanfのような)
				String[] getArgs = buff.split("\\s");
 
 
				/************************コマンド解析*********************/
				//lsの場合
				if( getArgs[0].equals("ls") )
				{
				    //ファイル(フォルダ)数繰り返す
					for( int i = 0;  i < fileList.length; i++)
					{
					    //String→byteに変換して送信
						outStream.write( fileList[i].getBytes() );
						outStream.write( "\n".getBytes() );
					}
				}
 
				//getの場合
				if( getArgs[0].equals("get") )
				{				
					//受け取ったファイル名のファイルを読み込むストリーム作成	
					fileInStream = new FileInputStream(getArgs[1]);
					int fileLength = 0;
					//System.out.println("Create stream " + getArgs[1] );
 
					//最大data長まで読み込む(終端に達し読み込むものがないとき-1を返す)
					while( (fileLength = fileInStream.read(fileBuff)) != -1 )
					{
						//出力ストリームに書き込み	
						outStream.write( fileBuff , 0 , fileLength );
					}
 
					//ファイルの読み込みを終える
					//System.out.println("Close stream " + getArgs[1] );
					fileInStream.close();
					
				}
				
				//exitの場合
				if( getArgs[0].equals("exit") )
				{
				    //ストリームを閉じる
					outStream.close();
					System.out.println("close server");
					break;
				}
			}
 
			//ソケットやストリームのクローズ
			//outStream.close();
			inStream.close();
			
			sock.close();
			servSock.close();
 
		}
		
		//例外処理
		catch( Exception e )
		{
			System.err.println(e);
			System.exit(1);
		}
	}
}

