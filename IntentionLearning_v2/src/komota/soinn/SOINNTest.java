package komota.soinn;

import java.util.Random;

public class SOINNTest {

	public static void main(String[] args){

		KomotaSOINN_Euclidean eu = new KomotaSOINN_Euclidean(121,100000,100000);
		KomotaSOINN_Chebyshev ch = new KomotaSOINN_Chebyshev(121,100000,100000);
		KomotaSOINN_CosSimilarity cs = new KomotaSOINN_CosSimilarity(121,100000,100000);

		SOINNTest test1 = new SOINNTest();
		SOINNTest test2 = new SOINNTest();

		test2.mean = 100;

		for(int i=0;i<10000;i++){
			eu.inputSignal(test1.generate1());
			ch.inputSignal(test1.generate1());
			cs.inputSignal(test1.generate1());
			eu.inputSignal(test2.generate1());
			ch.inputSignal(test2.generate1());
			cs.inputSignal(test2.generate1());
		}

		eu.removeUnnecessaryNode();
		ch.removeUnnecessaryNode();
		cs.removeUnnecessaryNode();

		eu.classify();
		ch.classify();
		cs.classify();

		System.out.println("ClassNUM:eu "+eu.getClassNum()+" ch "+ch.getClassNum()+" cs "+cs.getClassNum());

		eu = new KomotaSOINN_Euclidean(121,100000,100000);
		ch = new KomotaSOINN_Chebyshev(121,100000,100000);
		cs = new KomotaSOINN_CosSimilarity(121,100000,100000);

		for(int i=0;i<10000;i++){
			eu.inputSignal(test1.generate2(1));
			ch.inputSignal(test1.generate2(1));
			cs.inputSignal(test1.generate2(1));
			eu.inputSignal(test2.generate2(2));
			ch.inputSignal(test2.generate2(2));
			cs.inputSignal(test2.generate2(2));
		}

		eu.removeUnnecessaryNode();
		ch.removeUnnecessaryNode();
		cs.removeUnnecessaryNode();

		eu.classify();
		ch.classify();
		cs.classify();

		System.out.println("ClassNUM:eu "+eu.getClassNum()+" ch "+ch.getClassNum()+" cs "+cs.getClassNum());

	}

	int dimension = 121;
	int mean = 0;
	int variance = 100;
	Random rand = new Random();


	double[] generate1(){


		double[] output = new double[this.dimension];
		for(int i=0;i<this.dimension;i++){
			output[i] = Math.sqrt(this.variance) *rand.nextGaussian()+this.mean;
		}

		return output;
	}
	double[] generate2(int i){


		double[] output = new double[this.dimension];

		output[i] = Math.sqrt(this.variance) *rand.nextGaussian()+this.mean;

		return output;
	}

}
