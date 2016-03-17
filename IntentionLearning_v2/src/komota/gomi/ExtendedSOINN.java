package komota.gomi;

import soinn.SOINN;

public class ExtendedSOINN extends SOINN {

	public ExtendedSOINN(int dimension, int removeNodeTime, int deadAge) {
		super(dimension, removeNodeTime, deadAge);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	//指定したクラスのノード平均値を返す
	public double[] getNodeMean(int classID){
		double[] output = new double[this.getDimension()];

		int count = 0;
		for(int t=0;t<this.getNodeNum(false);t++){
			if(this.getNode(t).getClassID() == classID){
				for(int idx = 0;idx<output.length;idx++){
					output[idx] *= count;
					output[idx] += this.getNode(t).getSignal()[idx];
					output[idx] /= count+1;
				}
				count++;
			}
		}
		System.out.println("[ExtendedSOINN]getNodeMean:the number of Node in Class:"+classID+" is "+count);
		return output;
	}

}
