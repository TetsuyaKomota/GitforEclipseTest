package komota.cnn.main;

public class Image {

	//画像をピクセルごとの濃度で表すクラス

	//画像の縦横画素数
	int X_axis = 0;
	int Y_axis = 0;

	//画像の各画素の濃度。[行][列]
	double[][] dots = null;

	//レンジ。自由に使う
	double range = 0;

	//コンストラクタ

	public Image(int y,int x){
		this.X_axis = x;
		this.Y_axis = y;
		this.dots = new double[Y_axis][X_axis];
		this.initialize();
	}
	public Image(int y,int x,double range){
		this(y,x);
		this.range = range;
	}

	//画素の初期化。
	public void initialize(){
		for(int i=0;i<X_axis;i++){
			for(int j=0;j<Y_axis;j++){
				this.dots[j][i] = (double)0;
			}
		}
	}

	//パディング処理(ゼロパディング)
	public Image padding(int range){
		Image oldimg = new Image(this.Y_axis,this.X_axis);
		oldimg.dots = this.dots;
		this.X_axis += 2*range;
		this.Y_axis += 2*range;
		this.dots = new double[this.Y_axis][this.X_axis];
		this.initialize();
		for(int j=0;j<oldimg.Y_axis;j++){
			for(int i=0;i<oldimg.X_axis;i++){
				this.dots[range+j][range+i] = oldimg.dots[j][i];
			}
		}
		Image output = new Image(this.Y_axis,this.X_axis);
		output.dots = this.dots;
		this.X_axis = oldimg.X_axis;
		this.Y_axis = oldimg.Y_axis;
		this.dots = oldimg.dots;
		return output;
	}


}
