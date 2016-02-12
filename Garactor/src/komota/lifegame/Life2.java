package komota.lifegame;

public class Life2 extends LifeFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Life2 life = new Life2();
	}

	@Override
	public void startWORLD() {
		// TODO 自動生成されたメソッド・スタブ
		//エージェントをランダムに召喚
		for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
			for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
				this.agents[i][j].status = (int)(Math.random()*2 - 0.5);
				if(Math.random()>0.991){
//					this.agents[i][j].status += (int)(Math.random()*2 - 0.5);
				}
			}
		}
		//生命の泉を召喚
		int gyou = (int)(Statics.NUMBEROFAGENTS*Math.random());
		int retsu = (int)(Statics.NUMBEROFAGENTS*Math.random());
		this.agents[gyou][retsu].status = 6;
		this.agents[gyou-1][retsu].status = 6;
		this.agents[gyou+1][retsu].status = 6;
		this.agents[gyou][retsu-1].status = 6;
		this.agents[gyou][retsu+1].status = 6;

	}

	@Override
	public int rule(int[] neighbors, int status) {
		// TODO 自動生成されたメソッド・スタブ
		int output = status;
		//ライフゲーム
		output = Life1.RULE_gameofLIFE(neighbors, output);
		//対抗勢力
		output = Life2.RULE_gameofLIFE_ENEMY(neighbors, output);
		//生命の泉
		output = Life2.RULE_rakeofLIFE(neighbors, output);
		return output;
	}
	//ライフゲームの対抗勢力（同じルール，生存確率が少し高い）．
	public static int RULE_gameofLIFE_ENEMY(int[] neighbors,int status){
		int output = status;
		if(status == 2/* && (neighbors[2] < 2 || neighbors[2] > 3)*/){
			if(Math.random()>0.35 || neighbors[2] > 3){
				output = 0;
			}
		}
		else if(status == 0 && (neighbors[2] == 2)){
			if(Math.random()>0.4){
				output = 2;
			}
		}
		//赤を捕食することで増殖できる
		else if(status == 1 && neighbors[2] > 0){
			if(Math.random()>0.0){
				output = 2;
			}
		}
		return output;
	}
	//生命の泉リベンジ
	public static int RULE_rakeofLIFE(int[] neighbors,int status){
		int output = status;
		if(status == 0 && neighbors[6] > 2){
			output = 2;
		}
		return output;
	}


}
