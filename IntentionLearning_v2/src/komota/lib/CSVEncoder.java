package komota.lib;


public class CSVEncoder {
/*
 * 	//1行に羅列されたlogデータをcsv形式に変更する
	public void encodeToCSV(String in,String out){

		this.data = new Log[MAXSIZE];

		this.file_in = new File("log/"+in);
		this.file_out = new File("log/"+out);
		try {
		      FileOutputStream fos = new FileOutputStream("log/"+out,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			e.printStackTrace();
		}
		this.file_in = new File("log/"+in);
		try {
			this.br = new BufferedReader(new FileReader(file_in));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		String[][] encodeddata = new String[MAXSIZE][MAXROW];
		int i = 0;
		int j = 0;
		boolean tempflag = false;
		while(true){
			//最大量超過時に終了
			if(i>=MAXSIZE || j>MAXROW){
				break;
			}
			//データ読み込み完了時に終了
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(line == null){
				break;
			}
			//resultログの場合、格納してi++
			if(line.split(",")[0].equals("result")){
				encodeddata[i][j] = line.split(",")[1];
				i++;
				tempflag = true;
			}
			//resultログ以外で、次データフラグが立っている場合は、j++
			else if(tempflag == true){
				i=0;
				j++;
				tempflag = false;
			}
			//フラグが立っていない場合、フラグを立てる
			else{
				//tempflag = true;
			}
		}
		//取れたデータを出力
		for(i=0;i<MAXSIZE;i++){
			String outputline = "result";
			for(j=0;j<MAXROW;j++){
				if(encodeddata[i][j] != null){
					outputline = outputline + "," + encodeddata[i][j];
				}
			}
			//データが存在した場合、出力
			if(outputline.equals("result") == false){
				pw.println(outputline);
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		pw.close();
	}*/
}
