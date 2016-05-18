package komota.CLASS.keisokusingousyori;

public class SpectrumAnalysis extends AutoCorrelationFunction{

	//Δt
	static double delta = 1;

	public static void main(String[] args){
		SpectrumAnalysis s = new SpectrumAnalysis();
		s.input("hry201603MR.txt");
		s.output("うんこ.txt");

		for(int i=0;i<s.numofdata;i++){
			s.pw.println(s.calcPowerSpectrum(i));
		}
		s.pw.close();

		s.show();
	}

	//a_n
	public double calcA(int n){
		double output = 0;

		for(int k=0;k<this.numofdata;k++){
			output += this.data[k] * delta *Math.cos((2*Math.PI*n*k*delta)/this.numofdata);
		}

		return output;
	}
	//b_n
	public double calcB(int n){
		double output = 0;

		for(int k=0;k<this.numofdata;k++){
			output += this.data[k] * delta *Math.sin((2*Math.PI*n*k*delta)/this.numofdata);
		}

		return output;
	}

	//power spectrum
	public double calcPowerSpectrum(int n){
		double output = 0;

		output += calcA(n)*calcA(n);
		output += calcB(n)*calcB(n);

		return output;
	}

	//amplitude spectrum
	public double calcAmplitudeSpectrum(int n){
		return Math.sqrt(this.calcPowerSpectrum(n));
	}

}
