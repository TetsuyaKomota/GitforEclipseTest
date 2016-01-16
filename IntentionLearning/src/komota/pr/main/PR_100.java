package komota.pr.main;

import komota.coordinate.Coordinate_ID;
import komota.coordinate.MyCoordinate;
import komota.main.MyFrame;
import komota.main.MyPR;

public class PR_100 extends MyPR{

	//座標変換を行うPR。
	/* ******************************************************************************************************** */
	/* ******************************************************************************************************** */
	/* ****このクラスは直接使わず、サブクラスであるPR_100_**クラスを使用すること******************************* */
	/* ******************************************************************************************************** */
	/* ******************************************************************************************************** */

	//スタティックフィールド
	//タスク的視野
	private static double view_range = 400;

	//フィールド
	//参照点クラス
	ReferencePoint_100[] refs;
	//空間のサイズ
	double height;
	double width;

	//重心位置の参照点クラス
	//左から、中心、トラジェクタ、ランドマーク（２～９）
	ReferencePoint_100[][][][][][][][][] cogs;
	//空間内に、どのオブジェクトが存在するかのリスト。実装を単純にするためだけのものであり、一般的には不要
	int[] objectlist;


	//コンストラクタ
	public PR_100(int numref,String filename){
		super(filename);
		this.numref = numref;
		this.refs = new ReferencePoint_100[numref];

		//オブジェクト数は既知（9種類）として、重心位置とリストの配列を作成する
		this.cogs = new ReferencePoint_100[2][2][2][2][2][2][2][2][2];
		this.objectlist = new int[9];
		for(int i=0;i<this.objectlist.length;i++){
			this.objectlist[i] = 0;
		}

		//参照点一つ目は画面中央にする。
		this.height = this.logdata[0].getStepStatusField().length;
		this.width = this.logdata[0].getStepStatusField()[0].length;

		refs[0] = new ReferencePoint_100(this,0,height/2 ,width/2);

		//まず、視野を確認するため、最初に来たgoalログのstartpointの位置を取得する
		int[] tempgoal = new int[2];
		int k=1;
		for(int t=0;t<this.logdata.length;t++){
			if(this.logdata[t].getType() == GOAL){
				//goalpointを探す
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						if(this.logdata[t].getStepStatus(i, j) == 1){
							tempgoal[0] = i;
							tempgoal[1] = j;
						}
					}
				}
				//オブジェクトを認識する
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						//0と1以外がlogdata[0].getStepStatusField()[i][j]にあったらrefs[k].reference[0] = i,[i] = jとして、状態もセット
						//タスク的視野の中にいることも条件
						if((i-tempgoal[0])*(i-tempgoal[0])+(j-tempgoal[1])*(j-tempgoal[1]) < PR_100.getViewRange()*PR_100.getViewRange() && this.logdata[0].getStepStatus(i,j) > 1){
							this.refs[k] = new ReferencePoint_100(this,this.logdata[0].getStepStatus(i,j),i,j);
							//観点の個数をインクリメント
							PR_6_1.referencecount++;
							//存在した状態番号をobjectlistに保存
							this.objectlist[this.logdata[0].getStepStatus(i, j)] = 1;
							k++;
						}
					}
				}
				break;
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
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]] = new ReferencePoint_100(this,10,temppoint[0],temppoint[1]);
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
		//座標系を設定する
		this.coordinate = new Coordinate_ID();
	}
	public PR_100(int numref){
		this(numref,"logdata.txt");
	}

	//座標系のセッター
	public void setCoordinate(MyCoordinate coordinate){
		this.coordinate = coordinate;
	}
	//視野のセッター、ゲッター
	public static void setViewRange(double range){
		PR_100.view_range = range;
	}
	public static double getViewRange(){
		return PR_100.view_range;
	}

	//ログデータに基づいて学習
	@Override
	public void learnfromLog(){
		//learnメソッドには移動前のトラジェクタの位置を渡すので、その変数
		double[] startpoint = new double[2];
		//goal参照回数
		int count = 0;
		for(int t=0;t<this.logdata.length;t++){
			if(logdata[t] == null || count > MyPR.getNumberofEvaluation()){
				break;
			}
			//"start "ログの場合、参照点の座標を更新する
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
						//トラジェクタの開始位置を更新する
						else if(this.logdata[t].getStepStatus(i, j) == 1){
							startpoint[0] = i;
							startpoint[1] = j;
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
				//goal参照回数をインクリメント
				count++;
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
						this.refs[i].learn(trajector,startpoint);
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
														this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].learn(trajector,startpoint);
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
		/* *********************************************************************************************************** */
		/* 以上でgoalpointの計算が終了してるので、改めて分散を計算する                                                 */
		/* *********************************************************************************************************** */

		for(int t=0;t<this.logdata.length;t++){
			if(logdata[t] == null || count > this.getNumberofEvaluation()){
				break;
			}
			//"start "ログの場合、参照点の座標を更新する
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
						//トラジェクタの開始位置を更新する
						else if(this.logdata[t].getStepStatus(i, j) == 1){
							startpoint[0] = i;
							startpoint[1] = j;
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
				//goal参照回数をインクリメント
				count++;
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
						this.refs[i].learnVariance(trajector,startpoint);
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
														this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].learnVariance(trajector,startpoint);
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

		/* *********************************************************************************************************** */
		/* 以上で分散の計算が終了しているので、likelihoodの計算を行う                                                  */
		/* *********************************************************************************************************** */

		for(int t=0;t<this.logdata.length;t++){
			if(logdata[t] == null){
				break;
			}
			//"start "ログの場合、参照点の座標を更新する
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
						//トラジェクタの開始位置を更新する
						else if(this.logdata[t].getStepStatus(i, j) == 1){
							startpoint[0] = i;
							startpoint[1] = j;
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
						this.refs[i].learnLikelihood(trajector,startpoint);
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
														this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].learnLikelihood(trajector,startpoint);
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
		ReferencePoint_100 temprefpoint = null;
		double[] startpoint = new double[2];

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
				else if(frame.getPanels()[i][j].getStatus() == 1){
					startpoint[0] = i;
					startpoint[1] = j;
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
		System.out.println("[PR_100]reproduction:tempref:"+tempref);
		System.out.println("[PR_100]reproduction:ref.reference:"+temprefpoint.reference[0]+" , "+temprefpoint.reference[1]);
		System.out.println("[PR_100]reproduction:ref.goalpoint:"+temprefpoint.goalpoint[0]+" , "+temprefpoint.goalpoint[1]);
		//参照点の絶対ベクトル＋座標逆変換K(参照点からの相対ベクトル)＝トラジェクタの推定移動先
		double[] tempoutput = new double[2];
		double[][] inputs = new double[3][2];
		inputs[0][0] = temprefpoint.goalpoint[0];
		inputs[0][1] = temprefpoint.goalpoint[1];
		inputs[1][0] = temprefpoint.reference[0];
		inputs[1][1] = temprefpoint.reference[1];
		inputs[2][0] = startpoint[0];
		inputs[2][1] = startpoint[1];
		tempoutput = this.coordinate.inverseConvert(inputs);
		tempoutput[0] += temprefpoint.reference[0];
		tempoutput[1] += temprefpoint.reference[1];
		//doubleになっているので、パネルに変換する(まあただの四捨五入)
		System.out.println("[PR_100]reproduction:tempoutput:"+tempoutput[0]+" , "+tempoutput[1]);
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
				refs[i].variance = 1;
				refs[i].likelihood = 1;
				refs[i].numlearning = 0;
				refs[i].numlearningvariance = 0;
				refs[i].numlearninglikelihood = 0;

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
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].variance = 1;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].likelihood = 1;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].numlearning = 0;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].numlearningvariance = 0;
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].numlearninglikelihood = 0;
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
	//最大尤度を出力する
	//複数のPRを使用する時にこれで比較する
	public double getMaxLikelihood(){
		if(this.refs[0] == null){
			return -1000000;
		}
		else{
			//単体オブジェクトの参照点の最大尤度を検索する
			double output = -1;
			for(int i=0;i<this.refs.
					length;i++){
				if(this.refs[i] == null){
					break;
				}
				else if(this.refs[i].likelihood > output){
					output = this.refs[i].likelihood;
				}
			}
			//重心位置の最大尤度を検索する
			for(int i0 = 0;i0<2;i0++){
				for(int i1 = 0;i1<2;i1++){
					for(int i2 = 0;i2<2;i2++){
						for(int i3 = 0;i3<2;i3++){
							for(int i4 = 0;i4<2;i4++){
								for(int i5 = 0;i5<2;i5++){
									for(int i6 = 0;i6<2;i6++){
										for(int i7 = 0;i7<2;i7++){
											for(int i8 = 0;i8<2;i8++){
												if(this.cogs[i0][i1][i2][i3][i4][i5][i6][i7][i8] != null && this.cogs[i0][i1][i2][i3][i4][i5][i6][i7][i8].likelihood > output){
													output = this.cogs[i0][i1][i2][i3][i4][i5][i6][i7][i8].likelihood;
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

			return output;
		}
	}
}
