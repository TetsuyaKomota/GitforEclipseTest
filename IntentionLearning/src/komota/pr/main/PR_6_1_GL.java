package komota.pr.main;

import komota.coordinate.Coordinate_GL;
import komota.coordinate.MyCoordinate;
import komota.main.MyFrame;
import komota.main.MyPR;

public class PR_6_1_GL extends MyPR{

	//定数

	//フィールド

	//観点の個数。ただの、タスク的視野によって観点の数を減らせていることの確認用。IDやLTを併用する場合、別物として数える
	public static int referencecount = 0;
	//オブジェクトの最大種類数
	int numref = 0;
	//参照点クラス
	ReferencePoint[] refs;
	//空間のサイズ
	double height;
	double width;

	//重心位置の参照点クラス
	//左から、中心、トラジェクタ、ランドマーク（２～９）
	//G_ランドマークでは重心の存在参照のみに使用し、学習はrefs_GLを使用する
	ReferencePoint[][][][][][][][][] cogs;
	//G_ランドマークで使用する参照点
	//左から、中心、トラジェクタ、ランドマーク（２～９）、座標系に対応させるランドマーク  であり、最後のインデックスのみ０～８
	ReferencePoint[][][][][][][][][][] refs_GL;

	//空間内に、土のオブジェクトが存在するかのリスト。実装を単純にするためだけのものであり、一般的には不要
	int[] objectlist;

	//コンストラクタ
	public PR_6_1_GL(int numref,String filename){
		super(filename);
		this.numref = numref;
		this.refs = new ReferencePoint[numref];

		//オブジェクト数は既知（9種類）として、重心位置とリストの配列を作成する
		this.cogs = new ReferencePoint[2][2][2][2][2][2][2][2][2];
		//G_ランドマークで使用する参照点クラスを作成する
		this.refs_GL = new ReferencePoint[2][2][2][2][2][2][2][2][2][9];
		this.objectlist = new int[numref];
		for(int i=0;i<this.objectlist.length;i++){
			this.objectlist[i] = 0;
		}

		//参照点一つ目は画面中央にする。
		this.height = this.logdata[0].getStepStatusField().length;
		this.width = this.logdata[0].getStepStatusField()[0].length;

		refs[0] = new ReferencePoint(this,0,height/2 ,width/2);

		//logdataの0行目（logdata[0]というStepDataインスタンス）から状態0と1以外のオブジェクトがくるまで回す
		int k=1;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				//0と1以外がlogdata[0].getStepStatusField()[i][j]にあったらrefs[k].reference[0] = i,[i] = jとして、状態もセット
				if(this.logdata[0].getStepStatus(i,j) > 1){
					this.refs[k] = new ReferencePoint(this,this.logdata[0].getStepStatus(i,j),i,j);
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
												this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]] = new ReferencePoint(this,10,temppoint[0],temppoint[1]);
												//G_ランドマークの参照点を作成する
												for(int a=0;a<tempidx.length;a++){
													if(a != 1 && tempidx[1] == 0 && tempidx[a] == 1){
														this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] = new ReferencePoint(this,11,temppoint[0],temppoint[1]);
														//観点の数をインクリメント
														PR_6_1_GL.referencecount++;
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
		}
		//座標系を設定する
		this.coordinate = new Coordinate_GL();
	}
	public PR_6_1_GL(int numref){
		this(numref,"test4.txt");
	}

	//座標系のセッター
	public void setCoordinate(MyCoordinate coordinate){
		this.coordinate = coordinate;
	}

	//ログデータに基づいて学習
	@Override
	public void learnfromLog(){
		//learnメソッドには移動前のトラジェクタの位置を渡すので、その変数
		double[] startpoint = new double[2];
		//goalの参照回数
		int count = 0;
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
														for(int a=0;a<tempidx.length;a++){
															if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
																this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].reference[0] = temppoint[0];
																this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].reference[1] = temppoint[1];
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
													for(int a=0;a<tempidx.length;a++){
														if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
															for(int k=0;k<refs.length;k++){
																if(refs[k] != null && refs[k].status == a){
																	startpoint[0] = this.refs[k].reference[0];
																	startpoint[1] = this.refs[k].reference[1];
																	break;
																}
															}
															this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].learn(trajector,startpoint);
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
			}
			//"change"ログの場合、何もしない
			else if(logdata[t].getType() == CHANGE){

			}
			//"status"ログの場合、何もしない
			else if(logdata[t].getType() == STATUS){

			}
		}
		/* *********************************************************************************************************** */
		/* 以上でgoalpointの計算が終了してるので、改めてlikelihoodを計算する                                           */
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
														for(int a=0;a<tempidx.length;a++){
															if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
																this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].reference[0] = temppoint[0];
																this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].reference[1] = temppoint[1];
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
													for(int a=0;a<tempidx.length;a++){
														if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
															for(int k=0;k<refs.length;k++){
																if(refs[k] != null && refs[k].status == a){
																	startpoint[0] = this.refs[k].reference[0];
																	startpoint[1] = this.refs[k].reference[1];
																	break;
																}
															}
															this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].learnLikelihood(trajector,startpoint);
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
		double templikelihood = -100000;
		ReferencePoint temprefpoint = null;
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
												//this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].reference[0] = temppoint[0];
												//this.cogs[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]].reference[1] = temppoint[1];
												for(int a=0;a<tempidx.length;a++){
													if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
														this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].reference[0] = temppoint[0];
														this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].reference[1] = temppoint[1];
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
		}
		//尤度最大の参照点を検索する
		//G_ランドマークでは単体オブジェクトを候補としない
		for(int i=0;i<this.refs.length;i++){
			if(this.refs[i] != null && this.refs[i].likelihood > templikelihood){
				templikelihood = this.refs[i].likelihood;
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
											for(int a=0;a<tempidx.length;a++){
												if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
													if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].likelihood > templikelihood){
														templikelihood = this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].likelihood;
														temprefpoint = this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a];
														for(int k=0;k<refs.length;k++){
															if(refs[k] != null && refs[k].status == a){
																System.out.println("[PR_004_GL]reproduction:tempa:"+a);
																System.out.println("[PR_004_GL]reproduction:tempref.goalpoint:"+temprefpoint.goalpoint[0]+" , "+temprefpoint.goalpoint[1]);
																startpoint[0] = refs[k].reference[0];
																startpoint[1] = refs[k].reference[1];
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
				}
			}
		}
		System.out.println("[TestPR1]reproduction:tempref:"+/*tempref*/temprefpoint.status);
		System.out.println("[TestPR1]reproduction:ref.reference:"+temprefpoint.reference[0]+" , "+temprefpoint.reference[1]);
		System.out.println("[TestPR1]reproduction:ref.goalpoint:"+temprefpoint.goalpoint[0]+" , "+temprefpoint.goalpoint[1]);
		//参照点の絶対ベクトル＋参照点からの相対ベクトル＝トラジェクタの推定移動先
		double[] tempoutput = new double[2];
		double[][] inputs = new double[3][2];
		inputs[0][0] = temprefpoint.goalpoint[0];
		inputs[0][1] = temprefpoint.goalpoint[1];
		inputs[1][0] = temprefpoint.reference[0];
		inputs[1][1] = temprefpoint.reference[1];
		//G_ランドマークの場合、input[2]はその参照点ごとの参照ランドマークの座標が入る
		inputs[2][0] = startpoint[0];
		inputs[2][1] = startpoint[1];
		tempoutput = this.coordinate.inverseConvert(inputs);
		tempoutput[0] += temprefpoint.reference[0];
		tempoutput[1] += temprefpoint.reference[1];
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
											for(int a=0;a<tempidx.length;a++){
												if(this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a] != null){
													this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].goalpoint[0] = 0;
													this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].goalpoint[1] = 0;
													this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].likelihood = 1;
													this.refs_GL[tempidx[0]][tempidx[1]][tempidx[2]][tempidx[3]][tempidx[4]][tempidx[5]][tempidx[6]][tempidx[7]][tempidx[8]][a].numlearning = 0;
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
											for(int a=0;a<9;a++){
												if(this.refs_GL[i0][i1][i2][i3][i4][i5][i6][i7][i8][a] == null){
												}
												else{
													System.out.print("Center of Gravity:"+i0+" "+i1+" "+i2+" "+i3+" "+i4+" "+i5+" "+i6+" "+i7+" "+i8+" "+a+" ");
													this.refs_GL[i0][i1][i2][i3][i4][i5][i6][i7][i8][a].show();
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
	}
	//最大尤度を出力する
	//複数のPRを使用する時にこれで比較する
	public double getMaxLikelihood(){
		if(this.refs[0] == null){
			return -1000000;
		}
		else{
			double output = this.refs[0].likelihood;
			for(int i=1;i<this.refs.
					length;i++){
				if(this.refs[i] == null){
					break;
				}
				else if(this.refs[i].likelihood > output){
					output = this.refs[i].likelihood;
				}
			}
			return output;
		}
	}
}
