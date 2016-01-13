package komota.test;

import komota.main.MyPR;
import komota.main.MySerialFrame;

public class CheckFrame extends MySerialFrame{

	public static void main(String[] args){
		CheckFrame frame = new CheckFrame();
	}

	MyPR pr;
	int idx = 0;
	@Override
	public void functionPlugin1(){
		this.pr = new MyPR("ERROR_W/output_W_each50.txt");
	}
	@Override
	public void functionPlugin2(){
		this.idx++;
		System.out.println("index:"+idx);
		this.pr.arrangeField(this, this.pr.logdata[idx]);
	}
	@Override
	public void functionPlugin3(){
		this.idx--;
		System.out.println("index:"+idx);
		this.pr.arrangeField(this, this.pr.logdata[idx]);
	}

}
