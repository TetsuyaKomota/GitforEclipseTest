package komota.lib;

import java.io.IOException;

public class DataSetGenerator_v2 extends DataSetGenerator{

	//grand truth
	MyMatrix grtrh = null;
	//誤差の分散
	double variance;

	public static void main(String[] args){
		DataSetGenerator_v2 frame = new DataSetGenerator_v2("NbO",0.1);
	}

	//コンストラクタ
	public DataSetGenerator_v2(String cmd, double variance){
		this.setGrandTruth(cmd);
		this.variance = variance;
	}

	public void setGrandTruth(String cmd){
		MyIO io = new MyIO();
		io.readFile("generator_grtrh/grtrh_2D_"+cmd+".txt");
		this.grtrh = io.readMatrix(666);
		io.close();
	}

	public MyMatrix teach(){
		//grand truth と誤差によって現環境からの目標状態に動かす
		//適切な（環境範囲内にすべて収まる）結果でない場合は null を返す
		MyMatrix next;
		MyMatrix current = this.getStatusforMatrix();
		next = grtrh.mult(current);
		//誤差を付与する
		int num = this.countNumberofObject();
		for(int i=1;i<2*num+1;i++){
			next.setData(i, 0, next.getData(i,0)+this.nextError(variance));
			if(next.getData(i,0) < 0 || next.getData(i,0) >= Statics.NUMBEROFPANEL){
				System.out.println("戦犯:"+i+","+next.getData(i, 0));
				next = null;
				break;
			}
		}
		return next;
	}

	@Override
	public void functionPlugin1(){
		System.out.println("データジェネレート2！");
		int count = 0;
		while(count < this.numberofdataset){
			this.initialize();
			MyMatrix next = this.teach();
			if(next != null){
				this.setStatusforMatrix(next);
				this.pushSPACE();
				this.pushGoal();
				count++;
			}
			else{
				System.out.println("だめだ！");
			}
		}
		System.out.println("データジェネレート完了！");
		try {
			System.out.println("logdata.txtには"+this.countGoal("logdata.txt")+"個のデータが存在します");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.getMyIO().close();

	}

}
