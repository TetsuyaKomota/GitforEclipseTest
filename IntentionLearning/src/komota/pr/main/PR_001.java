package komota.pr.main;

import komota.main.MyFrame;
import komota.main.MyPR;

public class PR_001 extends MyPR{

	//参照点を原点に、画面に平行な座標系で動作をとらえる試行。重心位置を参照点に含めるバージョン
	/*
	 * 実装上の問題で、「重心位置の状態番号はどのように割り当てようか」という話がある
	 * 学習フェーズに状態番号を用いている関係上、うまく決めないといけない
	 * 学習フェーズにはオブジェクトを参照点とするもののベクトルだけを先に更新し、それをもとに重心位置の座標を更新すればいい
	 * つまりコンストラクタ呼び出し時に用意したオブジェクトの順番で考えるというもの
	 * あとは重心位置の順番をどのようにするかだが、
	 */




	//定数

	//フィールド
	//参照点の個数
	int numref = 0;
	//参照点クラス
	ReferencePoint_001[] refs;
	//空間のサイズ
	double height;
	double width;

	//重心位置の参照点クラス
	//左から、中心、トラジェクタ、ランドマーク（２～９）
	ReferencePoint_001[][][][][][][][][] cogs;
	//空間内に、土のオブジェクトが存在するかのリスト。実装を単純にするためだけのものであり、一般的には不要
	int[] objectlist;

	//コンストラクタ
	public PR_001(int numref,String filename){
		super(filename);
		this.numref = numref;
		this.refs = new ReferencePoint_001[numref];

		//オブジェクト数は既知（9種類）として、重心位置とリストの配列を作成する
		this.cogs = new ReferencePoint_001[2][2][2][2][2][2][2][2][2];
		this.objectlist = new int[9];
		for(int i=0;i<this.objectlist.length;i++){
			this.objectlist[i] = 0;
		}

		//参照点一つ目は画面中央にする。
		this.height = this.logdata[0].getStepStatusField().length;
		this.width = this.logdata[0].getStepStatusField()[0].length;

		refs[0] = new ReferencePoint_001(0,height/2 ,width/2);

		//logdataの0行目（logdata[0]というStepDataインスタンス）から状態0と1以外のオブジェクトがくるまで回す
		int k=1;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				//0と1以外がlogdata[0].getStepStatusField()[i][j]にあったらrefs[k].reference[0] = i,[i] = jとして、状態もセット
				if(this.logdata[0].getStepStatus(i,j) > 1){
					this.refs[k] = new ReferencePoint_001(this.logdata[0].getStepStatus(i,j),i,j);
					//存在した状態番号をobjectlistに保存
					this.objectlist[this.logdata[0].getStepStatus(i, j)] = 1;
					k++;
				}
			}
		}
		//重心位置のインスタンスの作成。objectlistをもとに、可能な組み合わせのインスタンスのみ作成する
		//for文用のインデックス
		int[] tempidx = new int[9];
		for(tempidx[0] = 0;tempidx[0]<2;tempidx[0]++){
			for(tempidx[1] = 0;tempidx[1]<2;tempidx[1]++){
				for(tempidx[2] = 0;tempidx[2]<2;tempidx[2]++){
					for(tempidx[3] = 0;tempidx[3]<2;tempidx[3]++){
						for(tempidx[4] = 0;tempidx[4]<2;tempidx[4]++){
							for(tempidx[5] = 0;tempidx[5]<2;tempidx[5]++){
								for(tempidx[6] = 0;tempidx[6]<2;tempidx[6]++){
									for(tempidx[7] = 0;tempidx[7]<2;tempidx[7]++){
										for(tempidx[8] = 0;tempidx[8]<2;tempidx[8]++){
											//位置ベクトル
											double[] temppoint = new double[2];
											//構成参照点数。最終的にこれでtemppointを割る
											int tempnum = 0;
											//その重心は存在するか
											boolean isexist = true;
											for(int a=0;a<tempidx.length;a++){
												//「状態番号aのオブジェクトを使う」重心であり、かつ状態番号aのオブジェクトが存在するなら
												if(tempidx[a] == 1 && this.objectlist[a] == 1){
													for(int b=0;b<this.refs.length;b++){
														if(this.refs[b].status == a){
															//状態番号aのオブジェクトを検索し、その位置ベクトルを加える
															temppoint[0] += this.refs[b].reference[0];
															temppoint[1] += this.refs[b].reference[1];
															break;
														}
													}
													tempnum++;
												}
												//「状態番号aのオブジェクトを使う」重心であるにもかかわらず、状態番号aのオブジェクトが存在しないなら
												else if(tempidx[a] == 1 && this.objectlist[a] != 1){
													isexist = false;
													break;
												}
											}
											//重心が存在しない（isexist==false）または構成参照点が一つ以下の時、参照点は作成しない
											if(isexist == true && tempnum >= 2){
												temppoint[0] /= tempnum;
												temppoint[1] /= tempnum;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]] = new ReferencePoint_001(10,temppoint[0],temppoint[1]);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public PR_001(int numref){
		this(numref,"test4.txt");
	}

	//ログデータに基づいて学習
	@Override
	public void learnfromLog(){
		for(int t=0;t<this.logdata.length;t++){
			//"start "ログの場合、参照点の座標を更新する
			if(logdata[t] == null){
				break;
			}
			if(logdata[t].getType() == START){
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						if(this.logdata[t].getStepStatus(i, j) > 1){
							int tempstatus = this.logdata[t].getStepStatus(i, j);
							for(int k=0;k<this.refs.length;k++){
								if(this.refs[k] != null && this.refs[k].status == tempstatus){
									this.refs[k].reference[0] = i;
									this.refs[k].reference[1] = j;
								}
							}
						}
					}
				}
				//重心位置の更新
				//for文用のインデックス
				int[] tempidx = new int[9];
				for(tempidx[0] = 0;tempidx[0]<2;tempidx[0]++){
					for(tempidx[1] = 0;tempidx[1]<2;tempidx[1]++){
						for(tempidx[2] = 0;tempidx[2]<2;tempidx[2]++){
							for(tempidx[3] = 0;tempidx[3]<2;tempidx[3]++){
								for(tempidx[4] = 0;tempidx[4]<2;tempidx[4]++){
									for(tempidx[5] = 0;tempidx[5]<2;tempidx[5]++){
										for(tempidx[6] = 0;tempidx[6]<2;tempidx[6]++){
											for(tempidx[7] = 0;tempidx[7]<2;tempidx[7]++){
												for(tempidx[8] = 0;tempidx[8]<2;tempidx[8]++){
													//位置ベクトル
													double[] temppoint = new double[2];
													//構成参照点数。最終的にこれでtemppointを割る
													int tempnum = 0;
													//その重心は存在するか
													boolean isexist = true;
													for(int a=0;a<tempidx.length;a++){
														//「状態番号aのオブジェクトを使う」重心であり、かつ状態番号aのオブジェクトが存在するなら
														if(tempidx[a] == 1 && this.objectlist[a] == 1){
															for(int b=0;b<this.refs.length;b++){
																if(this.refs[b].status == a){
																	//状態番号aのオブジェクトを検索し、その位置ベクトルを加える
																	temppoint[0] += this.refs[b].reference[0];
																	temppoint[1] += this.refs[b].reference[1];
																	break;
																}
															}
															tempnum++;
														}
														//「状態番号aのオブジェクトを使う」重心であるにもかかわらず、状態番号aのオブジェクトが存在しないなら
														else if(tempidx[a] == 1 && this.objectlist[a] != 1){
															isexist = false;
															break;
														}
													}
													//重心が存在しない（isexist==false）または構成参照点が一つ以下の時、参照点は作成しない
													if(isexist == true && tempnum >= 2){
														temppoint[0] /= tempnum;
														temppoint[1] /= tempnum;
														this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].reference[0] = temppoint[0];
														this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].reference[1] = temppoint[1];
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			//"goal  "ログの場合、トラジェクタの相対座標とgoalpointを比較し、goalpointとlikelihoodの更新をする
			else if(logdata[t].getType() == GOAL){
				double[] trajector = new double[2];
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						if(this.logdata[t].getStepStatus(i, j) == 1){
							trajector[0] = i;
							trajector[1] = j;
						}
					}
				}
				//各参照点クラスのlearnメソッドで学習する
				for(int i=0;i<this.refs.length;i++){
					if(this.refs[i] != null){
						this.refs[i].learn(trajector);
					}
				}
				//重心位置のlearn
				//for文用のインデックス
				int[] tempidx = new int[9];
				for(tempidx[0] = 0;tempidx[0]<2;tempidx[0]++){
					for(tempidx[1] = 0;tempidx[1]<2;tempidx[1]++){
						for(tempidx[2] = 0;tempidx[2]<2;tempidx[2]++){
							for(tempidx[3] = 0;tempidx[3]<2;tempidx[3]++){
								for(tempidx[4] = 0;tempidx[4]<2;tempidx[4]++){
									for(tempidx[5] = 0;tempidx[5]<2;tempidx[5]++){
										for(tempidx[6] = 0;tempidx[6]<2;tempidx[6]++){
											for(tempidx[7] = 0;tempidx[7]<2;tempidx[7]++){
												for(tempidx[8] = 0;tempidx[8]<2;tempidx[8]++){
													if(this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]] != null){
														this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].learn(trajector);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			//"change"ログの場合、何もしない
			else if(logdata[t].getType() == CHANGE){

			}
			//"status"ログの場合、何もしない
			else if(logdata[t].getType() == STATUS){

			}
		}
	}

	//学習結果に基づいてタスクを推定する
	@Override
	public void reproduction(MyFrame frame){
		double templikelihood = -10000000;
		int tempref = -1;
		ReferencePoint_001 temprefpoint = null;

		//まず、参照点の位置を現在のものに更新する
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				if(frame.getPanels()[i][j].getStatus() > 1){
					int tempstatus = frame.getPanels()[i][j].getStatus();
					for(int k=0;k<this.refs.length;k++){
						if(this.refs[k] != null && this.refs[k].status == tempstatus){
							this.refs[k].reference[0] = i;
							this.refs[k].reference[1] = j;
						}
					}
				}
			}
		}
		//更新された単体オブジェクトの位置を用いて重心位置を更新
		int[] tempidx = new int[9];
		for(tempidx[0] = 0;tempidx[0]<2;tempidx[0]++){
			for(tempidx[1] = 0;tempidx[1]<2;tempidx[1]++){
				for(tempidx[2] = 0;tempidx[2]<2;tempidx[2]++){
					for(tempidx[3] = 0;tempidx[3]<2;tempidx[3]++){
						for(tempidx[4] = 0;tempidx[4]<2;tempidx[4]++){
							for(tempidx[5] = 0;tempidx[5]<2;tempidx[5]++){
								for(tempidx[6] = 0;tempidx[6]<2;tempidx[6]++){
									for(tempidx[7] = 0;tempidx[7]<2;tempidx[7]++){
										for(tempidx[8] = 0;tempidx[8]<2;tempidx[8]++){
											//位置ベクトル
											double[] temppoint = new double[2];
											//構成参照点数。最終的にこれでtemppointを割る
											int tempnum = 0;
											//その重心は存在するか
											boolean isexist = true;
											for(int a=0;a<tempidx.length;a++){
												//「状態番号aのオブジェクトを使う」重心であり、かつ状態番号aのオブジェクトが存在するなら
												if(tempidx[a] == 1 && this.objectlist[a] == 1){
													for(int b=0;b<this.refs.length;b++){
														if(this.refs[b].status == a){
															//状態番号aのオブジェクトを検索し、その位置ベクトルを加える
															temppoint[0] += this.refs[b].reference[0];
															temppoint[1] += this.refs[b].reference[1];
															break;
														}
													}
													tempnum++;
												}
												//「状態番号aのオブジェクトを使う」重心であるにもかかわらず、状態番号aのオブジェクトが存在しないなら
												else if(tempidx[a] == 1 && this.objectlist[a] != 1){
													isexist = false;
													break;
												}
											}
											//重心が存在しない（isexist==false）または構成参照点が一つ以下の時、参照点は作成しない
											if(isexist == true && tempnum >= 2){
												temppoint[0] /= tempnum;
												temppoint[1] /= tempnum;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].reference[0] = temppoint[0];
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].reference[1] = temppoint[1];
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//尤度最大の参照点を検索する
		for(int i=0;i<this.refs.length;i++){
			if(this.refs[i] != null && this.refs[i].likelihood > templikelihood){
				templikelihood = this.refs[i].likelihood;
				tempref = i;
				temprefpoint = refs[i];
			}
		}
		//重心位置も検索
		//for文用のインデックス
		for(tempidx[0] = 0;tempidx[0]<2;tempidx[0]++){
			for(tempidx[1] = 0;tempidx[1]<2;tempidx[1]++){
				for(tempidx[2] = 0;tempidx[2]<2;tempidx[2]++){
					for(tempidx[3] = 0;tempidx[3]<2;tempidx[3]++){
						for(tempidx[4] = 0;tempidx[4]<2;tempidx[4]++){
							for(tempidx[5] = 0;tempidx[5]<2;tempidx[5]++){
								for(tempidx[6] = 0;tempidx[6]<2;tempidx[6]++){
									for(tempidx[7] = 0;tempidx[7]<2;tempidx[7]++){
										for(tempidx[8] = 0;tempidx[8]<2;tempidx[8]++){
											if(this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]] != null){
												if(this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].likelihood > templikelihood){
													templikelihood = this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].likelihood;
													temprefpoint = this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]];
													tempref = 10;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("[TestPR1]reproduction:tempref:"+tempref);
		//選択された参照点を現在の座標に更新する
		//状態が0（画面中心が参照点）の場合は例外
/*
		if(temprefpoint.status != 0){
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					if(frame.getPanels()[i][j].getStatus() == temprefpoint.status){
						temprefpoint.reference[0] = i;
						temprefpoint.reference[1] = j;
					}
				}
			}
		}
*/
		System.out.println("[TestPR1]reproduction:ref.reference:"+temprefpoint.reference[0]+" , "+temprefpoint.reference[1]);
		System.out.println("[TestPR1]reproduction:ref.goalpoint:"+temprefpoint.goalpoint[0]+" , "+temprefpoint.goalpoint[1]);
		//参照点の絶対ベクトル＋参照点からの相対ベクトル＝トラジェクタの推定移動先
		double[] tempoutput = new double[2];
		tempoutput[0] = temprefpoint.reference[0] + temprefpoint.goalpoint[0];
		tempoutput[1] = temprefpoint.reference[1] + temprefpoint.goalpoint[1];
		//doubleになっているので、パネルに変換する(まあただの四捨五入)
		System.out.println("[TestPR1]reproduction:tempoutput:"+tempoutput[0]+" , "+tempoutput[1]);
		int[] output = new int[2];
		output[0] = (int)(tempoutput[0] + 0.5);
		output[1] = (int)(tempoutput[1] + 0.5);
		frame.setSecondSelected(output);
	}
	//学習結果リセット。評価時に使用する
	@Override
	public void initialize(){
		for(int i=0;i<this.refs.length;i++){
			if(refs[i] != null){
				refs[i].goalpoint[0] = 0;
				refs[i].goalpoint[1] = 0;
				refs[i].likelihood = 1;
				refs[i].numlearning = 0;
			}
		}
		//重心を初期化
		int[] tempidx = new int[9];
		for(tempidx[0] = 0;tempidx[0]<2;tempidx[0]++){
			for(tempidx[1] = 0;tempidx[1]<2;tempidx[1]++){
				for(tempidx[2] = 0;tempidx[2]<2;tempidx[2]++){
					for(tempidx[3] = 0;tempidx[3]<2;tempidx[3]++){
						for(tempidx[4] = 0;tempidx[4]<2;tempidx[4]++){
							for(tempidx[5] = 0;tempidx[5]<2;tempidx[5]++){
								for(tempidx[6] = 0;tempidx[6]<2;tempidx[6]++){
									for(tempidx[7] = 0;tempidx[7]<2;tempidx[7]++){
										for(tempidx[8] = 0;tempidx[8]<2;tempidx[8]++){
											if(this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]] != null){
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].goalpoint[0] = 0;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].goalpoint[1] = 0;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].likelihood = 1;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].numlearning = 0;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	//表示
	public void showReference(){
		for(int i=0;i<this.refs.length;i++){
			if(this.refs[i] == null){
				break;
			}
			this.refs[i].show();
		}
		System.out.println("超えてるよ");
		for(int i0 = 0;i0<2;i0++){
			for(int i1 = 0;i1<2;i1++){
				for(int i2 = 0;i2<2;i2++){
					for(int i3 = 0;i3<2;i3++){
						for(int i4 = 0;i4<2;i4++){
							for(int i5 = 0;i5<2;i5++){
								for(int i6 = 0;i6<2;i6++){
									for(int i7 = 0;i7<2;i7++){
										for(int i8 = 0;i8<2;i8++){
											System.out.print("Center of Gravity:"+i0+" "+i1+" "+i2+" "+i3+" "+i4+" "+i5+" "+i6+" "+i7+" "+i8+" ");
											if(this.cogs[i0][i1][i2][i3][i4][i5][i6][i7][i8] == null){
												System.out.println("status:"+10+"  reference:NULL  goalpoint:NULL  likelihood:NULL");
											}
											else{
												this.cogs[i0][i1][i2][i3][i4][i5][i6][i7][i8].show();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/* ************************************************************************************************************* */
	//参照点ごとに学習された位置ベクトルと尤度を持つ内部クラス
	class ReferencePoint_001{
		//定数
		//ベクトルの近さ閾値。learnで使う
		static final double E = 10;
		//フィールド
		//参照点の状態。0は画面中央。10は重心位置
		int status;
		//参照点の位置ベクトル[0]=行 [1]=列
		double[] reference;
		//参照点からの位置ベクトル
		double[] goalpoint;
		//尤度
		double likelihood;
		//学習回数。goalpoint更新の学習率に使用する
		int numlearning;

		//コンストラクタ
		ReferencePoint_001(int status, double referenceg,double referencer){
			this.status = status;
			this.reference = new double[2];
			this.reference[0] = referenceg;
			this.reference[1] = referencer;

			this.goalpoint = new double[2];
			goalpoint[0] = 0;
			goalpoint[1] = 0;

			this.likelihood = 1;
			this.numlearning = 0;
		}

		//トラジェクタの位置ベクトル(絶対ベクトル)が引数として与えられたとき、goalpointとlikelihoodの更新を行う
		void learn(double[] trajector){
			//相対ベクトルに変換する
			double[] tempgoal = new double[2];
			tempgoal[0] = trajector[0] - this.reference[0];
			tempgoal[1] = trajector[1] - this.reference[1];
			System.out.println("[TestPR1.ReferencePoint_001]learn:status:"+this.status+"tempgoal:"+tempgoal[0]+" , "+tempgoal[1]);
			//学習回数を学習率としてgoalpointベクトルを更新する
			this.goalpoint[0] = this.goalpoint[0] * ((double)(this.numlearning)/(this.numlearning + 1)) + tempgoal[0] * ((double)1/(this.numlearning + 1));
			this.goalpoint[1] = this.goalpoint[1] * ((double)(this.numlearning)/(this.numlearning + 1)) + tempgoal[1] * ((double)1/(this.numlearning + 1));
			System.out.println("[TestPR1.ReferencePoint_001]learn:status:"+this.status+" goalpoint:"+goalpoint[0]+" , "+goalpoint[1]);
			//学習回数をインクリメント
			this.numlearning++;
			//近さを求める
			double[] tempcloseness = new double[2];
			tempcloseness[0] = tempgoal[0] - this.goalpoint[0];
			tempcloseness[1] = tempgoal[1] - this.goalpoint[1];
			double closeness = Math.sqrt(tempcloseness[0]*tempcloseness[0]+tempcloseness[1]*tempcloseness[1]);
			//likelihood += （近さ値-近さ閾値）
			System.out.println("[TestPR1.ReferencePoint_001]learn:closeness:"+closeness);
			likelihood += (E - closeness);
		}

		//表示
		void show(){
			System.out.println("status:"+status+"  reference:"+this.reference[0]+" , "+this.reference[1] + "  goalpoint;"+this.goalpoint[0]+" , "+this.goalpoint[1] + "  likelihood:"+this.likelihood);
		}
	}
	/* ************************************************************************************************************* */
}
