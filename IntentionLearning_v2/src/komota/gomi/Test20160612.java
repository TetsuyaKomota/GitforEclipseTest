package komota.gomi;

import edu.ufl.digitalworlds.j4k.J4KSDK;

public class Test20160612 extends J4KSDK{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0)
		{
			System.out.println("WARNING: You are running a 32bit version of Java.");
			System.out.println("This may reduce significantly the performance of this application.");
			System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
		}

		Test20160612 kinect = new Test20160612();
		kinect.start(J4KSDK.COLOR|J4KSDK.INFRARED|J4KSDK.DEPTH|J4KSDK.SKELETON);
	}

	@Override
	public void onColorFrameEvent(byte[] arg0) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Color を受け取ったよ！");
	}

	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Depth を受け取ったよ！");
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] arg0, float[] arg1, float[] arg2, byte[] arg3) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Skeleton を受け取ったよ！");
	}

}
