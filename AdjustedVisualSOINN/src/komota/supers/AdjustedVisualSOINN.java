package komota.supers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import soinn.SOINN;


public class AdjustedVisualSOINN extends JFrame{

	//2015/5/11
	//２次元データ分布表示用のフレームクラス。クラスタは色で表現する
	//2015/5/13
	//バッファ部分を作成。簡単なメソッドのみの描画ができるようになった。
	//2015/5/15
	//描画時のGraphics2Dの取得と廃棄のタイミングを調整し、画面のちらつきをなくした。
	//依然として時々描画されなくなる現象が生じる。
	//2015/06/16
	//MyFrame(別名SOINNFrame)とVisualSOINNを統合し、AdjustedVisualSOINNを作成。
	//「２画面以上同時に表示できない」という問題はGraphics2Dの取得方法に問題があったようなので、煩雑だった２層構造を統括した
	//これ以降はVisualSOINNは使用禁止

	//実行フラグ。データインプットなどの前処理が終わってからrunを実行できるようにするためのフラグ
	public boolean playflag = false;

	Graphics2D g;

	//バッファストラテジー。コンストラクタでMyFrame自身のBufferStrategyを参照させる。
	public BufferStrategy buffer;

	//描画域の更新フラグ。アームにてうまく描画できなかったため作成。使わないのが理想
	private boolean resizable = false;

	//コンストラクタ
	//SOINN		soinn		:描画するSOINNクラス。Nodeの第1引数と第2引数の2次元で描画する。
	//String	framename	:フレーム名
	//int		move_x		:描画位置の補正の際のX方向の変化率
	//int		move_y		:描画位置の補正の際のY方向の変化率
	//int		scale		:描画域（倍率）の初期値。この値から逐次減少させていく
	public AdjustedVisualSOINN(SOINN soinn , String framename , int move_x , int move_y , int scale ,boolean resizable){
		this.setTitle(framename);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		//バッファの作成
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();
		this.g = (Graphics2D)this.buffer.getDrawGraphics();

		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,1);

		this.soinn = soinn;
		this.MOVE_X = move_x;
		this.MOVE_Y = move_y;
		this.SCALE = scale;
		this.resizable = resizable;
		this.playflag = true;
	}

	//簡単版コンストラクタ
	//特別な場合を除き、こちらを使用する
	public AdjustedVisualSOINN(SOINN soinn){
		this(soinn,"VisualSOINN",40,40,2000,true);
	}

	//画面をクリアする
	public void drawClear(){
		g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.dispose();
	}
	//点を描画する
	public void drawDot(double x,double y,double r,Color color){
		g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setColor(color);
		g.fillOval((int)x, (int)y, (int)r, (int)r);
		g.dispose();
	}
	//直線を描画する
	public void drawLine(double px,double py,double qx,double qy,Color color){
		g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setColor(color);
		g.drawLine((int)px, (int)py, (int)qx, (int)qy);
		g.dispose();

	}


/* ************************************************************************************************************************ */



	//画面端の位置(定数)
	final int FRAMESIDE = 50;
	final int NODESIZE = 10;


	//描画時の平行移動量と倍率
	int MOVE_X;
	int MOVE_Y;
	int SCALE;


	//描画するSOINN
	SOINN soinn = null;

	//SOINNに代入するデータセット。基本的にデータ入力済みのSOINNを引数に与えることを想定しているが、入力ごとの描画が見たいときにのみ使用する。
	ArrayList<double[]> dataset = null;

	//逐次描画を行う場合のフラグ
	boolean realtimeflag = false;

	//逐次描画を行う際、データをランダムで選択する場合のフラグ
	boolean randomdataflag = false;

	Color color = Color.white;



	//soinnのセッター、ゲッター
	public void setSOINN(SOINN soinn){
		this.soinn = soinn;
	}
	public SOINN getSOINN(){
		return this.soinn;
	}

	//逐次入力用データセットのセッターとゲッター
	public void setDataset(ArrayList<double[]> dataset){
		this.dataset = dataset;
	}
	public ArrayList<double[]> getDataset(){
		return this.dataset;
	}

	//逐次描画のフラグのセッターとゲッター
	public void setRealtimeFlag(boolean realtimeflag){
		this.realtimeflag = realtimeflag;
	}
	public boolean getRealtimeFlag(){
		return this.realtimeflag;
	}
	//データ乱択のフラグのセッターとゲッター
	public void setRandomDataFlag(boolean randomdataflag){
		this.randomdataflag = randomdataflag;
	}
	public boolean getRandomDataFlag(){
		return this.randomdataflag;
	}

	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		//逐次描画のフラグが立っている場合、datasetがnullでない場合は一つSOINNに入力して破棄
		if(this.realtimeflag == true && this.dataset != null && this.dataset.size() > 0){
			if(this.randomdataflag == true){
				int rand = (int)(Math.random() * this.dataset.size());
				double[] temp = this.dataset.get(rand);
				this.soinn.inputSignal(temp);
				this.dataset.remove(rand);
			}
			else{
				double[] temp = this.dataset.get(0);
				this.soinn.inputSignal(temp);
				this.dataset.remove(0);
			}
		}
		else{
			this.realtimeflag = false;
			//this.soinn.removeUnnecessaryNode();
		}

		try{
			soinn.classify();
		}catch(Exception e){
			System.out.println("[VisualSOINN]		Something wrong with soinn.classisy in this.run.");
		}

		//パラメータ更新用のフラグ
		boolean MOVE_X_flag = false;
		boolean MOVE_Y_flag = false;
		boolean SCALE_flag = false;


		for (int i = 0; i < soinn.getEdgeNum(); i++) {

			if(soinn.getNode(soinn.getEdge(i).getFrom()).getSignal() != null){
				double[] tempedge = {(soinn.getNode(soinn.getEdge(i).getFrom()).getSignal()[0])*SCALE+MOVE_X + NODESIZE/2,
						(soinn.getNode(soinn.getEdge(i).getFrom()).getSignal()[1])*SCALE+MOVE_Y + NODESIZE/2,
						(soinn.getNode(soinn.getEdge(i).getTo()).getSignal()[0])*SCALE+MOVE_X + NODESIZE/2,
						(soinn.getNode(soinn.getEdge(i).getTo()).getSignal()[1])*SCALE+MOVE_Y + NODESIZE/2};

				drawLine(tempedge[0],tempedge[1],tempedge[2],tempedge[3], Color.gray);
			}
		}

		for (int i = 0; i < soinn.getNodeNum(false); i++) {
			switch (soinn.getNode(i).getClassID()) {
			case 0:
				color = Color.red;
				break;
			case 1:
				color = Color.blue;
				break;
			case 2:
				color = Color.green;
				break;
			case 3:
				color = Color.orange;
				break;
			case 4:
				color = Color.cyan;
				break;
			case 5:
				color = Color.pink;
				break;
			default:
				color = Color.white;
			}

			//現在のパラメータでは画面外にはみ出してしまうデータがある場合、更新フラグを立てる

			if((soinn.getNode(i).getSignal()[0])*SCALE+MOVE_X < FRAMESIDE){
				MOVE_X_flag = true;
			}
			if((soinn.getNode(i).getSignal()[1])*SCALE+MOVE_Y < FRAMESIDE){
				MOVE_Y_flag = true;
			}
			if((soinn.getNode(i).getSignal()[0])*SCALE+MOVE_X > this.getWidth() - FRAMESIDE || (soinn.getNode(i).getSignal()[1])*SCALE+MOVE_Y > this.getHeight() - FRAMESIDE){
				SCALE_flag = true;
			}



			drawDot((soinn.getNode(i).getSignal()[0])*SCALE+MOVE_X,
					(soinn.getNode(i).getSignal()[1])*SCALE+MOVE_Y,
					NODESIZE,
					color);
		}
		//描画位置を調節するパラメータを学習する

			if(MOVE_X_flag == true){
				MOVE_X += FRAMESIDE;
			}
			if(MOVE_Y_flag == true){
				MOVE_Y += FRAMESIDE;
			}
		if(resizable){
			if(SCALE_flag == true){
				SCALE -= FRAMESIDE / 10;
				MOVE_X -= FRAMESIDE;
				MOVE_Y -= FRAMESIDE;
			}
		}
	}
/* ************************************************************************************************************************ */



	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			g = (Graphics2D)AdjustedVisualSOINN.this.buffer.getDrawGraphics();
			drawClear();
			//実行フラグ(playflag)がtrueのとき、各サブフレームで実装したrunが実行される
			if(playflag == true){
				AdjustedVisualSOINN.this.run();
			}
			g.dispose();
			buffer.show();
		}
	}
}
