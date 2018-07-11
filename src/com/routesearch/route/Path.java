package com.routesearch.route;


import java.util.ArrayList;

public class Path {
	private ArrayList<DirectedEdge> pathEdge;
	private int weight;
	private ArrayList<Integer> mustPassNotPass;
	private int endNode;
	

	public Path() {
		this.pathEdge = new ArrayList<DirectedEdge>();
		this.weight = 0;
		this.mustPassNotPass = new ArrayList<Integer>();
		this.endNode = 0;
		

	}

	public Path(ArrayList<DirectedEdge> pathEdge, int weight,
			ArrayList<Integer> mustPassNotPass, int endNode) {
		this.pathEdge = new ArrayList<DirectedEdge>();
		for (DirectedEdge e : pathEdge) {
			this.pathEdge.add(e);
		}
		this.weight = weight;
		this.mustPassNotPass = new ArrayList<Integer>();
		for (int i : mustPassNotPass) {
			this.mustPassNotPass.add(i);
		}
		this.endNode = endNode;
		
	}

	public int nextHop(EdgeWeightedDigraph G,ArrayList<Integer> demandPastNode,ArrayList<Path>[] pqDG,int candicateNum) {
		int operation=0;
		int v = this.pathEdge.get(this.pathEdge.size() - 1).to();
		G.reBuilt();
		G.revisePath1(this.pathEdge);
		G.reviseNode(this.endNode);
		DijstraSP aDsp = new DijstraSP(G, v);
		for (int w : mustPassNotPass) {
			if (aDsp.hasPathTo(w)) {
				if (true) {//aDsp.distTo(w) < 200
					int weightNew = this.weight;
					weightNew = weightNew + aDsp.distTo(w);
					
					//���ıع����޸����룭������������������������������������������������������������������������������
					ArrayList<Integer> carelessPass=new ArrayList<Integer>();
					ArrayList<DirectedEdge> addEdge=new ArrayList<DirectedEdge>();
					addEdge=aDsp.pathTo(w);
					for(DirectedEdge ea:addEdge){
						int ean=ea.to();
						if((this.mustPassNotPass.indexOf(ean)!=(-1))&&(ean!=w)){
							carelessPass.add(ean);
						}
					}
					ArrayList<Integer> newMPNP=new ArrayList<Integer>();
					for(int ia:this.mustPassNotPass){
						if((carelessPass.indexOf(ia)<0)&&(ia!=w)){
							newMPNP.add(ia);
						}
					}
					int newMPNPsize=newMPNP.size();
					//���ıع����޸����룭������������������������������������������������������������������������������

										
					
										
					if(pqDG[newMPNPsize].size()==0){
						//����Path����Ӵ��룭������������������������������������������������������������������
						ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
						for (DirectedEdge e : this.pathEdge) {
							a.add(e);
						}
						for (int i = addEdge.size() - 1; i >= 0; i--) {
							a.add(addEdge.get(i));
						}
						Path aPath = new Path(a, weightNew, newMPNP, this.endNode);
						//����Path����Ӵ��룭������������������������������������������������������������������
						pqDG[newMPNPsize].add(aPath);
						operation++;
						
						
						
						
					}else if (weightNew >= pqDG[newMPNPsize].get(pqDG[newMPNPsize].size()-1).weight()) {
						if(pqDG[newMPNPsize].size()<candicateNum){
							//����Path����Ӵ��룭������������������������������������������������������������������
							ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
							for (DirectedEdge e : this.pathEdge) {
								a.add(e);
							}
							for (int i = addEdge.size() - 1; i >= 0; i--) {
								a.add(addEdge.get(i));
							}
							Path aPath = new Path(a, weightNew, newMPNP, this.endNode);
							//����Path����Ӵ��룭������������������������������������������������������������������
							pqDG[newMPNPsize].add(aPath);
							operation++;
							
							
						}

						}else {
							for (int i = 0; i < pqDG[newMPNPsize].size(); i++) {
								if (weightNew < pqDG[newMPNPsize].get(i).weight()) {
									//����Path����Ӵ��룭������������������������������������������������������������������
									ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
									for (DirectedEdge e : this.pathEdge) {
										a.add(e);
									}
									for (int j = addEdge.size() - 1; j >= 0; j--) {
										a.add(addEdge.get(j));
									}
									Path aPath = new Path(a, weightNew, newMPNP, this.endNode);
									//����Path����Ӵ��룭������������������������������������������������������������������
									pqDG[newMPNPsize].add(aPath);
									operation++;
									break;
								}
							}
							//��ɾ�����룭������������������������������������������������������������������������������
							if(pqDG[newMPNPsize].size()>candicateNum){//��������������ȥ��
								ArrayList<Integer> needDel=new ArrayList<Integer>();
								for(int i=pqDG[newMPNPsize].size()-1;i>=candicateNum;i--){
									needDel.add(i);
								}
								for(int i:needDel){
									pqDG[newMPNPsize].remove(i);
									operation++;
								}
							}
							//��ɾ�����룭������������������������������������������������������������������������������
//							//��ɾ�����룭������������������������������������������������������������������������������
//							if(pqDG[nextLoc].size()>candicateNum){
//								pqDG[nextLoc].remove(candicateNum);
//							}
//							//��ɾ�����룭������������������������������������������������������������������������������
						}
					
					
					

				}
			}
		}
		return operation++;
	}

	public int startHop(EdgeWeightedDigraph G, int demandOrigin,
			int demandDestination, ArrayList<Integer> demandPastNode,ArrayList<Path>[] pqDG,int candicateNum) {
		G.reBuilt();// Ҫ��ͼ��ԭ
		G.reviseNode(demandDestination);// ɾ���յ�ĵ����
		G.reviseNode(demandOrigin);
		DijstraSP dSP1 = new DijstraSP(G, demandOrigin);

		// //��ʾ�ع��㵽����·����������������������������������������������������������������������������������������������
		// int[] gotoOrigin=new int[demandPastNode.size()];
		// int[] gotoNumOrigin=new int[demandPastNode.size()];
		// for(int i=0;i<demandPastNode.size();i++){
		// gotoOrigin[i]=dSP1.distTo(demandPastNode.get(i));
		// gotoNumOrigin[i]=dSP1.pathTo(demandPastNode.get(i)).size();
		// }
		// for(int i=0;i<demandPastNode.size();i++){
		// System.out.println("�ع���"+demandPastNode.get(i)+"�������룺"+gotoOrigin[i]+"�����ڵ���"+gotoNumOrigin[i]);
		// }
		// //��ʾ�ع��㵽����·����������������������������������������������������������������������������������������������

		int operation=0;
		for (int w : demandPastNode) {
			if (dSP1.hasPathTo(w)) {
//				System.out.println();
//				System.out.println("Դ�������ɴ�ع���"+w);
				int weightNew = dSP1.distTo(w);
				//���ıع����޸����룭������������������������������������������������������������������������������
				ArrayList<Integer> carelessPass=new ArrayList<Integer>();
				ArrayList<DirectedEdge> addEdge=new ArrayList<DirectedEdge>();
				addEdge=dSP1.pathTo(w);
				for(DirectedEdge ea:addEdge){
					int ean=ea.to();
					if((demandPastNode.indexOf(ean)!=(-1))&&(ean!=w)){
						carelessPass.add(ean);
					}
				}
//				System.out.println("����ͨ���ع�����"+carelessPass.size());
				ArrayList<Integer> newMPNP=new ArrayList<Integer>();
				for(int ia:demandPastNode){
					if((carelessPass.indexOf(ia)==(-1))&&(ia!=w)){//(carelessPass.indexOf(ia)<0)&&
						newMPNP.add(ia);
					}
				}
//				System.out.println("������ʣ�ع�����"+newMPNP.size());
				int newMPNPsize=newMPNP.size();
				//���ıع����޸����룭������������������������������������������������������������������������������
				
				
				if(pqDG[newMPNPsize].size()==0){
					//����Path����Ӵ��룭������������������������������������������������������������������
					ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
					for (int i = addEdge.size() - 1; i >= 0; i--) {
						a.add(addEdge.get(i));
					}
					Path aPath = new Path(a, weightNew, newMPNP, demandDestination);
					//����Path����Ӵ��룭������������������������������������������������������������������
					pqDG[newMPNPsize].add(aPath);
					operation++;
					
					
					
					
				}else if (weightNew >= pqDG[newMPNPsize].get(pqDG[newMPNPsize].size()-1).weight()) {
					if(pqDG[newMPNPsize].size()<candicateNum){
						//����Path����Ӵ��룭������������������������������������������������������������������
						ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
						for (int i = addEdge.size() - 1; i >= 0; i--) {
							a.add(addEdge.get(i));
						}
						Path aPath = new Path(a, weightNew, newMPNP, demandDestination);
						//����Path����Ӵ��룭������������������������������������������������������������������
						pqDG[newMPNPsize].add(aPath);
						operation++;
						
						
					}

					}else {
						for (int i = 0; i < pqDG[newMPNPsize].size(); i++) {
							if (weightNew < pqDG[newMPNPsize].get(i).weight()) {
								//����Path����Ӵ��룭������������������������������������������������������������������

								ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
								for (int j = addEdge.size() - 1; j >= 0; j--) {
									a.add(addEdge.get(j));
								}
								Path aPath = new Path(a, weightNew, newMPNP, demandDestination);
								//����Path����Ӵ��룭������������������������������������������������������������������
								pqDG[newMPNPsize].add(aPath);
								operation++;
								break;
							}
						}
						//��ɾ�����룭������������������������������������������������������������������������������
						if(pqDG[newMPNPsize].size()>candicateNum){//��������������ȥ��
							ArrayList<Integer> needDel=new ArrayList<Integer>();
							for(int i=pqDG[newMPNPsize].size()-1;i>=candicateNum;i--){
								needDel.add(i);
							}
							for(int i:needDel){
								pqDG[newMPNPsize].remove(i);
								operation++;
							}
						}
						//��ɾ�����룭������������������������������������������������������������������������������
//						//��ɾ�����룭������������������������������������������������������������������������������
//						if(pqDG[nextLoc].size()>candicateNum){
//							pqDG[nextLoc].remove(candicateNum);
//						}
//						//��ɾ�����룭������������������������������������������������������������������������������
					}
			}
		}
		return operation;
	}

	public Path lastHop(EdgeWeightedDigraph G) {
		int v = this.pathEdge.get(this.pathEdge.size() - 1).to();
		G.reBuilt();
		G.revisePath1(this.pathEdge);
		DijstraSP aDsp = new DijstraSP(G, v);
		Path aPath = new Path();
		if (aDsp.hasPathTo(this.endNode)) {
			ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
			for (DirectedEdge e : this.pathEdge) {
				a.add(e);
			}
			int weightNew = this.weight;
			ArrayList<DirectedEdge> b = aDsp.pathTo(this.endNode);
			for (int i = b.size() - 1; i >= 0; i--) {
				a.add(b.get(i));
				weightNew = weightNew + b.get(i).weight();
			}
			// weightNew=weightNew+aDsp.distTo(this.endNode);
			ArrayList<Integer> c = new ArrayList<Integer>();
			aPath = new Path(a, weightNew, c, this.endNode);
		}
		return aPath;
	}

	public ArrayList<DirectedEdge> pathEdge() {
		return this.pathEdge;
	}

	public int weight() {
		return this.weight;
	}

	public ArrayList<Integer> mustPassNotPass() {
		return this.mustPassNotPass;
	}

	public int endNode() {
		return this.endNode;
	}

	
	//������������������������������������������������������������������������������������������������������������
	public int starStep(EdgeWeightedDigraph G, int demandOrigin,
			int demandDestination, ArrayList<Integer> demandPastNode,ArrayList<Path>[] pqDG) {
		int operation=0;
		G.reBuilt();// Ҫ��ͼ��ԭ
		G.reviseNode(demandDestination);// ɾ���յ�ĵ����
		G.reviseNode(demandOrigin);
		for (DirectedEdge e: G.adj(demandOrigin)) {
			if (demandPastNode.indexOf(e.to())!=-1) {
				ArrayList<DirectedEdge> c=new ArrayList<DirectedEdge>();
				c.add(e);
				
				ArrayList<Integer> a = new ArrayList<Integer>();
				for (int num : demandPastNode) {
					a.add(num);
				}
				a.remove(a.indexOf(e.to()));
				Path aPath = new Path(c, e.weight(), a, demandDestination);
				pqDG[demandPastNode.size()-1].add(aPath);
				operation++;				
			}else{
				ArrayList<DirectedEdge> c=new ArrayList<DirectedEdge>();
				c.add(e);
				
				ArrayList<Integer> a = new ArrayList<Integer>();
				for (int num : demandPastNode) {
					a.add(num);
				}
				Path aPath = new Path(c, e.weight(), a, demandDestination);
				pqDG[demandPastNode.size()].add(aPath);
				operation++;	
			}
		}
		return operation;		
	}
	public int allStep(EdgeWeightedDigraph G,ArrayList<Path>[] pqDG,int candicateNum,ArrayList<Integer> demandPastNode) {
		int operation=0;
		while(true){
			int locate=demandPastNode.size();
			for(;locate>0;locate--){
				if(pqDG[locate].size()!=0){
					break;
				}
			}
			if(locate==0){//ֻ�����������бع��㣬���յ㽻��lastHop����
				break;
			}
			
			Path pathDelMin=pqDG[locate].remove(0);
			G.reBuilt();
			G.revisePath1(pathDelMin.pathEdge());
			G.reviseNode(pathDelMin.endNode());
			int v=pathDelMin.pathEdge().get(pathDelMin.pathEdge().size()-1).to();
			for(DirectedEdge e:G.adj(v)){
				int w=e.to();
				if(demandPastNode.indexOf(w)!=-1){
					ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
					for (DirectedEdge f : pathDelMin.pathEdge()) {
						a.add(f);
					}
					a.add(e);
					ArrayList<Integer> c = new ArrayList<Integer>();
					for (int i : pathDelMin.mustPassNotPass()) {
						c.add(i);
					}
					c.remove(c.indexOf(w));
					int weightNew = pathDelMin.weight();
					weightNew = weightNew + e.weight();
					Path aPath = new Path(a, weightNew, c, pathDelMin.endNode());
					pqDG[locate-1].add(aPath);
					operation++;
				}else{
					ArrayList<DirectedEdge> a = new ArrayList<DirectedEdge>();
					for (DirectedEdge f : pathDelMin.pathEdge()) {
						a.add(f);
					}
					a.add(e);
					ArrayList<Integer> c = new ArrayList<Integer>();
					for (int i : pathDelMin.mustPassNotPass()) {
						c.add(i);
					}
					int weightNew = pathDelMin.weight();
					weightNew = weightNew + e.weight();
					Path aPath = new Path(a, weightNew, c, pathDelMin.endNode());
					pqDG[locate].add(aPath);
					operation++;					
				}
//				if(pqDG[0].size()>=1){
//					return operation;
//				}
			}
		}
		return operation;
	}
	//������������������������������������������������������������������������������������������������������������

}
