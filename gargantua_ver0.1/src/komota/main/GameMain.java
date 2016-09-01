package komota.main;

import komota.chapters.Chapter_0_0;

public class GameMain {

	public static void main(String[] args){
		MyKeyListener key = new MyKeyListener();
		MyFrame frame = new MyFrame(key);
		//frame.currentmode = new Manzai_Mode(frame);
		frame.currentchapter = new Chapter_0_0(frame,key);
	}

}
