package komota.gomi;

import komota.lib.Statics;
import komota.main.PRv2_Mat_HyperMean;
import komota.supers.MyFrame;
import komota.supers.MyPR_v2;

public class Test20160726 extends MyFrame{

	MyPR_v2 pr;

	public static void main(String[] args){
		Test20160726 frame = new Test20160726();
	}


	@Override
	public void initialize(){
		super.initialize();
		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j].setStatus(0);
			}
		}

		/*
		 * 環境中の物体配置は、以下のように定める
		 * 1. トラジェクタの位置をランダムに決定する
		 * 2. 物体2(青色のオブジェクト)の位置を、トラジェクタの位置以外でランダムに決定する
		 * 3. 物体3(黄色のオブジェクト)の位置を、物体2の上下左右にそれぞれ40マス離れた位置のうち、可能な位置からランダムに決定する
		 * 4. 物体4(緑色のオブジェクト)の位置を、現在物体が配置されていない位置からランダムに決定する
		 * 5. 物体5(橙色のオブジェクト)の位置を、現在物体が配置されていない位置からランダムに決定する
		 *
		 * つまり環境中には1つのトラジェクタと4つのオブジェクトが配置され、そのうち必ず上下左右40マスに隣り合う青色と黄色以外は独立にランダムで場所が決定する
		 */


		//this.panels[(int)(Math.random() * Statics.NUMBEROFPANEL)][(int)(Math.random() * Statics.NUMBEROFPANEL)].setStatus(1);

		for(int i=1;i<=Statics.NUMBEROFKIND;i++){
			while(true){
				int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
				int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
				if(this.panels[temp1][temp2].getStatus() == 0){
					this.panels[temp1][temp2].setStatus(i);
					break;
				}
			}

		}

		this.outputStart();
	}

	@Override
	public void functionPlugin1(){
		this.pr = new PRv2_Mat_HyperMean(Statics.NUMBEROFKIND, "logdata.txt");

		pr.learnfromLog();

		pr.reproduction(this);

	}

}
