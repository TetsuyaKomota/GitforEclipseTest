package komota.lib;

public class DataSetGenerator_v2 extends DataSetGenerator{

	//grand truth
	MyMatrix grtrh = null;
	//誤差の分散
	double variance;

	public static void main(String[] args){
		DataSetGenerator_v2 frame = new DataSetGenerator_v2("NbO",1);
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
	}

	public void teach(){
		//grand truth と誤差によって現環境からの目標状態に動かす
		MyMatrix current = this.getStatusforMatrix();
		MyMatrix next = grtrh.mult(current);
		//誤差を付与する
		next.show();
		for(int i=0;i<next.getDimension();i++){
			next.setData(i, 0, next.getData(i,0)+this.nextError(variance));
		}
		next.show();

		this.setStatusforMatrix(next);

	}

	@Override
	public void functionPlugin1(){
		teach();
	}

}
