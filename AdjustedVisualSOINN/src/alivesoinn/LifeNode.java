package alivesoinn;

import soinn.Node;

public class LifeNode extends Node{
	//ライフSOINNのノード。速度、加速度の概念を追加する

	//フィールド
	//速度
	private double[] velocity = null;
	//加速度
	private double[] acceleration = null;

	//コンストラクタ
	public LifeNode(){
		super();
		this.velocity = new double[Node.dimension];
		this.acceleration = new double[Node.dimension];
	}

	//getta-
	public double[] getVelocity(){
		return this.velocity;
	}


	//速度を結合重みに加算
	public void move(){
		double[] temp = new double[Node.dimension];
		for(int i=0;i<Node.dimension;i++){
			temp[i] = this.getSignal()[i] + this.getVelocity()[i];
		}

	}



}
