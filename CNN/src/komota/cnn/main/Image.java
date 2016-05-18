package komota.cnn.main;

public class Image {

	//画像をピクセルごとの濃度で表すクラス

	//画像の縦横画素
	int X_axis = 0;
	int Y_axis = 0;

	//画像の各画素の濃度。[X座標][Y座標]
	double[][] dots = null;

	//レンジ。自由に使う
	double range = 0;

	//コンストラクタ

	public Image(int x,int y){
		this.X_axis = x;
		this.Y_axis = y;
		this.dots = new double[X_axis][Y_axis];
		this.initialize();
	}
	public Image(int x,int y,double range){
		this(x,y);
		this.range = range;
	}

	//画素の初期化。
	public void initialize(){
		for(int i=0;i<X_axis;i++){
			for(int j=0;j<Y_axis;j++){
				this.dots[i][j] = (double)0;
			}
		}
	}

	//パディング処理(ゼロパディング)
	public Image padding(int range){
		Image oldimg = new Image(this.X_axis,this.Y_axis);
		oldimg.dots = this.dots;
		this.X_axis += 2*range;
		this.Y_axis += 2*range;
		this.dots = new double[this.X_axis][this.Y_axis];
		this.initialize();
		for(int j=0;j<oldimg.Y_axis;j++){
			for(int i=0;i<oldimg.X_axis;i++){
				this.dots[range+i][range+j] = oldimg.dots[i][j];
			}
		}
		Image output = new Image(this.X_axis,this.Y_axis);
		output.dots = this.dots;
		this.X_axis = oldimg.X_axis;
		this.Y_axis = oldimg.Y_axis;
		this.dots = oldimg.dots;
		return output;
	}


}
