package komota.lifegame;

public class Life1 extends LifeFrame{

	public static void main(String[] args){
		Life1 life = new Life1();
	}
	@Override
	public void startWORLD(){

		//エージェントをランダムに召喚
		for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
			for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
				this.agents[i][j].status = (int)(Math.random()*2 - 0.5);
			}
		}
		//生命の泉を召喚
		this.agents[(int)(Statics.NUMBEROFAGENTS*Math.random())][(int)(Statics.NUMBEROFAGENTS*Math.random())].status = 6;

	}
	@Override
	public int rule(int[] neighbors,int status){
		int output = status;
		//単純なライフゲーム
		output = Life1.RULE_gameofLIFE(neighbors, output);
		//生命の泉
		//output = Life1.RULE_influence(neighbors, output);

		return output;
	}
	//ライフゲーム部分をメソッド分けしてみる．
	public static int RULE_gameofLIFE(int[] neighbors,int status){
		int output = status;
		if(status == 1 && (neighbors[1] < 2 || neighbors[1] > 3)){
			if(Math.random()>0.02){
				output = 0;
			}
		}
		else if(status == 0 && (neighbors[1] == 3)){
			output = 1;
		}
		return output;
	}
	//生命の泉改め，侵略感染
	public static int RULE_influence(int[] neighbors,int status){
		int output = status;
		if(status == 6 && neighbors[1] > 5){
			output = 8;
		}
		if(status == 1 && neighbors[6] >= 3){
			output = 6;
		}
		if(status == 0 && Math.random()*neighbors[6] > 0.3){
			output = 1;
		}
		if(status == 0 && neighbors[6] == 1 && Math.random() > 0.8){
			output = 6;
		}
		//腐食した泉
		if(status == 6 && neighbors[8] > 0 && Math.random()*neighbors[8] > 0.6){
			output = 8;
		}
		if(status == 8 && neighbors[6] < 2){
			output = 0;
		}
		return output;
	}
}
