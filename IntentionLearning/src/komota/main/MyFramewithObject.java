package komota.main;

import java.awt.Color;
import java.awt.Graphics2D;


public class MyFramewithObject extends MyFrame{

	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* *******11/16現在廃案。                                          ****************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ***********今のところ問題はないが、面倒くさそうなので気が向いたら更新するとして、************************************************************* */
	/* ***************************************現行はMyFrameを用いることにする。（ちっちゃくなるのは仕方ない）**************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */
	/* ********************************************************************************************************************************************** */





	//MyFrameの離散空間を、格子数を多くすることで擬似連続空間にする。
	//その場合、各オブジェクトの位置を格子に限定してしまうと非常に小さくなってしまうので、
	//中心の格子を位置ベクトルとして持ち、大きさのあるオブジェクトを扱えるようにMyFrameを拡張する
	//
	//オブジェクト
	MyObject[] objects = null;

	public static void main(String[] args){
		MyFramewithObject task = new MyFramewithObject();
	}


	//コンストラクタ
	public MyFramewithObject(){
		super();
		this.objects = new MyObject[3];
		objects[0] = new MyObject(10,20,5,1);
		objects[1] = new MyObject(50,25,20,2);
	}

	@Override
	public void draw(){
		for(int i=0;i<this.objects.length;i++){
			if(objects[i] != null){
				objects[i].draw();
			}
			else{
				break;
			}
		}
	}


	/* ***************************************************************************************************************** */
	//オブジェクトを表す内部クラス
	class MyObject{
		//フィールド
		//座標
		int[] position = null;
		//大きさ
		int size = -1;
		//状態
		int status = -1;

		//コンストラクタ
		MyObject(int[] position,int size,int status){
			this.position = position;
			this.size = size;
			this.status = status;
		}
		MyObject(int x,int y,int size,int status){
			this.position = new int[2];
			this.position[0] = y;
			this.position[1] = x;
			this.size = size;
			this.status = status;
		}

		//セッター、ゲッター
		MyObject setPosition(int[] position){
			this.position = position;
			return this;
		}
		int[] getPosition(){
			return this.position;
		}

		MyObject setStatus(int status){
			this.status = status;
			return this;
		}
		int getStatus(){
			return this.status;
		}
		MyObject setSize(int size){
			this.size = size;
			return this;
		}
		int getSize(){
			return this.size;
		}

		//描画
		void draw(){
			Graphics2D  g = (Graphics2D)MyFramewithObject.this.buffer.getDrawGraphics();
			switch(this.status){
			case 0:
				g.setColor(Color.white);
				break;
			case 1:
				g.setColor(Color.red);
				break;
			case 2:
				g.setColor(Color.blue);
				break;
			case 3:
				g.setColor(Color.yellow);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.orange);
				break;
			case 6:
				g.setColor(Color.pink);
				break;
			case 7:
				g.setColor(Color.lightGray);
				break;
			case 8:
				g.setColor(Color.gray);
				break;
			case 9:
				g.setColor(Color.black);
				break;
			}
			g.fillRect(MyPanel.SIZE_FRAME + (MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR) * this.position[1] + MyPanel.SIZE_PANEL/2 - this.size * MyPanel.SIZE_PANEL/2,MyPanel.SIZE_FRAME + (MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR) * this.position[0] + MyPanel.SIZE_PANEL/2 - this.size * MyPanel.SIZE_PANEL/2,this.size * MyPanel.SIZE_PANEL,this.size * MyPanel.SIZE_PANEL);
//			g.setColor(Color.black);
//			g.drawString(""+this.status, SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0],SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1]);
			g.dispose();

		}
	}
	/* ***************************************************************************************************************** */

}
