
package komota.main;

import komota.lib.DataSetGenerator_v2;
import komota.lib.MatFactory;
import komota.lib.MyIO;
import komota.lib.MyMatrix;
import komota.lib.Statics;
import komota.supers.MyFrame;

public class ExperimentalFrame_001 extends MyFrame{

	//結果書き出し先ファイル名
	String resultfile = "20160616/result_Mat_SOINN.txt";

	//PRv2_EM em;
	PRv2_Mat_SOINN em;

	public static void main(String[] args){
		ExperimentalFrame_001 frame = new ExperimentalFrame_001();
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


		this.panels[(int)(Math.random() * Statics.NUMBEROFPANEL)][(int)(Math.random() * Statics.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(2);
				while(true){
					double temp3 = Math.random();
					if(temp3<0.25&&temp1>50&&this.panels[temp1-40][temp2].getStatus() == 0){
						this.panels[temp1-40][temp2].setStatus(3);
						break;
					}else if(temp3<0.5&&temp2>50&&this.panels[temp1][temp2-40].getStatus() == 0){
						this.panels[temp1][temp2-40].setStatus(3);
						break;
					}else if(temp3<0.75&&temp1<Statics.NUMBEROFPANEL-50&&this.panels[temp1+40][temp2].getStatus() == 0){
						this.panels[temp1+40][temp2].setStatus(3);
						break;
					}else if(temp3<1.1&&temp2<Statics.NUMBEROFPANEL-50&&this.panels[temp1][temp2+40].getStatus() == 0){
						this.panels[temp1][temp2+40].setStatus(3);
						break;
					}
				}
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(4);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(5);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(6);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(7);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(8);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(9);
				break;
			}
		}

		this.outputStart();
	}
/* *********************************************************************************************************************** */
	//ベクトル読み込み書き出しのテスト．すぐ消していい
	MyMatrix temp = null;
	@Override
	public void functionPlugin1(){
		temp = this.getStatusforMatrix();
		temp.show();
	}
	@Override
	public void functionPlugin2(){
		this.setStatusforMatrix(temp, this.countNumberofObject());
	}
	@Override
	public void functionPlugin3(){
		this.em = new PRv2_Mat_SOINN(5,"logdata.txt");
		//this.em = new PRv2_EM(5,"logdata.txt");

		em.learnfromLog();
		System.out.println("e_min:"+em.calcE(this.em.getX()));

		//再現
		MyMatrix temp = new MyMatrix(19);
		for(int i=0;i<em.getX().getDimension();i++){
			for(int j=0;j<em.getX().getDimension();j++){
				temp.setData(i, j, em.getX().getData(i, j));
			}
		}
		MyMatrix temp2 = this.getStatusforMatrix();
		temp2.show();
		temp2 = temp.mult(this.getStatusforMatrix());
		temp2.show();
		this.setStatusforMatrix(temp2);
	}
/* *********************************************************************************************************************** */

	@Override
	public void functionPlugin4(){
		//this.em = new PRv2_EM(5,"logdata.txt");
		this.em = new PRv2_Mat_SOINN(5,"logdata.txt");
		this.em.show();
	}

	@Override
	public void functionPluginQ(){
		//データ量によって収束結果がどう変わるのかを検証する
		//this.em = new PRv2_EM(5,"logdata.txt");
		//this.em = new PRv2_Mat_SOINN(5,"logdata.txt");
		//描画を止める
		this.setRenderFlag(false);
		//DataSetGenerator generator = new DataSetGenerator();
		DataSetGenerator_v2 generator = new DataSetGenerator_v2("NbO",0.1);
		generator.setRenderFlag(false);
		MyIO io = new MyIO();
		io.writeFile(resultfile);
		io.println("以下実験結果です．[データ量],[再代入誤差],[汎化誤差]");
		io.execute();
		generator.setNumberofData(10);
		int count = 0;
		while(count < 10){
			count++;
			generator.functionPlugin1();
			//this.em = new PRv2_EM(5,"logdata.txt");
			this.em = new PRv2_Mat_SOINN(5,"logdata.txt");

			this.em.learnfromLog();

			//resultは「[データ量],[再代入誤り率],[汎化誤差]」という並び
			io.print(count*10+",");
			System.out.println("numberofdata:"+count*10);
			io.print(em.calcE(this.em.getX())+",");
			System.out.println("e_min:"+em.calcE(this.em.getX()));
		/* ************************************************************************************** */
			//汎化誤差を求める
			MyIO grtrh_i = new MyIO();
			grtrh_i.readFile("generator_grtrh/grtrh_2D_NbO.txt");
			MyMatrix grtrh = grtrh_i.readMatrix(666);
			io.println(em.getX().sub(grtrh).getMaxNorm());
			System.out.println("error from grtrh:"+em.getX().sub(grtrh).getMaxNorm());
		/* ************************************************************************************** */
			io.execute();
		}
		System.out.println("実験しゅうりょう！");
		this.expranation = "実験しゅうりょう！！";
		this.setRenderFlag(true);
	}

	@Override
	public void functionPlugin9(){
		MyIO io = new MyIO();
		io.writeFile("logdata.txt");
		io.printMatrix(MatFactory.random(11, 200, -200), 999);
		io.close();
	}
	@Override
	public void functionPluginW(){
		this.em.reproduction(this);
	}
}
