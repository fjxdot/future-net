package com.routesearch.route;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

//将Iterable使用链表代替
public class EdgeWeightedDigraph {
	private int V;
	private int E;
	private ArrayList<DirectedEdge>[] adj;

	private ArrayList<DirectedEdge>[] inboxEdge;
	private ArrayList<DirectedEdge>[] trashEdge;// 修边的数据结构

	public EdgeWeightedDigraph(int V) {
		this.V = V;
		this.E = 0;
		this.adj = (ArrayList<DirectedEdge>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<DirectedEdge>();
		}

		inboxEdge = (ArrayList<DirectedEdge>[]) new ArrayList[this.V()];
		for (int v = 0; v < this.V(); v++) {
			inboxEdge[v] = new ArrayList<DirectedEdge>();
		}
		for (int v = 0; v < this.V(); v++) {
			for (DirectedEdge e : this.adj(v)) {
				inboxEdge[e.to()].add(e);
			}
		}
		trashEdge = (ArrayList<DirectedEdge>[]) new ArrayList[this.V()];
		for (int v = 0; v < this.V(); v++) {
			trashEdge[v] = new ArrayList<DirectedEdge>();
		}// 修边数据结构的初始化

	}

	public EdgeWeightedDigraph(String filename) {
		File file = new File(filename);
		FileReader fr = null;
		try {
			fr = new FileReader(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);

		String tempString = null;
		LinkedList<String> tempStringList = new LinkedList<String>();
		try {
			while ((tempString = br.readLine()) != null) {
				tempStringList.add(tempString);
			}
			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<DirectedEdge> allDirectedEdge = new ArrayList<DirectedEdge>();// 保存了所有有向图的正向边
		int maxNodeNumber = 0;
		for (String tempString2 : tempStringList) {

			String edgeString[] = tempString2.split(",");
			if (maxNodeNumber < Integer.parseInt(edgeString[1])) {
				maxNodeNumber = Integer.parseInt(edgeString[1]);
			}
			if (maxNodeNumber < Integer.parseInt(edgeString[2])) {
				maxNodeNumber = Integer.parseInt(edgeString[2]);
			}
			DirectedEdge tempEdge = new DirectedEdge(
					Integer.parseInt(edgeString[0]),
					Integer.parseInt(edgeString[1]),
					Integer.parseInt(edgeString[2]),
					Integer.parseInt(edgeString[3]));
			allDirectedEdge.add(tempEdge);
		}
		maxNodeNumber++;
		this.V = maxNodeNumber;
		this.E = 0;
		this.adj = (ArrayList<DirectedEdge>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<DirectedEdge>();
		}
		for (DirectedEdge e : allDirectedEdge) {
			this.addEdge(e);
		}

		inboxEdge = (ArrayList<DirectedEdge>[]) new ArrayList[this.V()];
		for (int v = 0; v < this.V(); v++) {
			inboxEdge[v] = new ArrayList<DirectedEdge>();
		}
		for (int v = 0; v < this.V(); v++) {
			for (DirectedEdge e : this.adj(v)) {
				inboxEdge[e.to()].add(e);
			}
		}
		trashEdge = (ArrayList<DirectedEdge>[]) new ArrayList[this.V()];
		for (int v = 0; v < this.V(); v++) {
			trashEdge[v] = new ArrayList<DirectedEdge>();
		}// 修边数据结构的初始化

	}

	public EdgeWeightedDigraph reverse() {// 对图求反向图函数
		EdgeWeightedDigraph R = new EdgeWeightedDigraph(this.V);

		for (int v = 0; v < this.V; v++) {// 反向图中原图的序号和权值保持不变
			for (DirectedEdge e : adj[v]) {
				DirectedEdge f = new DirectedEdge(e.edgeNumber(), e.to(),
						e.from(), e.weight());
				R.addEdge(f);
			}
		}
		return R;
	}

	public int V() {
		return this.V;
	}

	public int E() {
		return this.E;
	}

	public void addEdge(DirectedEdge e) {
		adj[e.from()].add(e);
		E++;
	}

	public void removeEdge(DirectedEdge e) {
		for (int w = 0; w < this.adj[e.from()].size(); w++) {
			if ((this.adj[e.from()].get(w).from() == e.from())
					&& (this.adj[e.from()].get(w).to() == e.to())) {
				this.adj[e.from()].remove(w);
				E--;
				return;
			}
		}
	}

	public ArrayList<DirectedEdge> adj(int v) {// 指向点v
		return adj[v];
	}

	public ArrayList<DirectedEdge> edges() {
		ArrayList<DirectedEdge> bag = new ArrayList<DirectedEdge>();
		for (int v = 0; v < V; v++) {
			for (DirectedEdge e : adj[v])
				bag.add(e);
		}
		return bag;
	}

	// 修边的类方法－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
	public void reviseNode(int v) {
		// 删除点到达边****************************************
		for (DirectedEdge e : this.inboxEdge[v]) {
			this.trashEdge[v].add(e);
			this.removeEdge(e);
		}
		this.inboxEdge[v] = new ArrayList<DirectedEdge>();
		// 删除点到达边****************************************
	}
	public void revisePath1(ArrayList<DirectedEdge> hasPath) {
		// 删除allPassNodePath路段上全部点到达边，路段应该是连通的**********
		this.reviseNode(hasPath.get(0).from());
		for (DirectedEdge e : hasPath) {
			for (DirectedEdge f : this.inboxEdge[e.to()]) {
				this.trashEdge[f.to()].add(f);
				this.removeEdge(f);
			}
		}
		for (DirectedEdge e : hasPath) {
			this.inboxEdge[e.to()] = new ArrayList<DirectedEdge>();
		}
		// 删除allPassNodePath路段上全部点到达边，路段应该是连通的**********
	}
	public void revisePath2(ArrayList<DirectedEdge> hasPath) {
		// 删除allPassNodePath路段上指向点到达边，路段应该是连通的**********
		for (DirectedEdge e : hasPath) {
			for (DirectedEdge f : this.inboxEdge[e.to()]) {
				this.trashEdge[f.to()].add(f);
				this.removeEdge(f);
			}
		}
		for (DirectedEdge e : hasPath) {
			this.inboxEdge[e.to()] = new ArrayList<DirectedEdge>();
		}
		// 删除allPassNodePath路段上指向点到达边，路段应该是连通的**********
	}

	public void revisePath3(ArrayList<DirectedEdge> hasPath) {
		// 删除allPassNodePath路段上出发点到达边，路段应该是连通的**********
		for (DirectedEdge e : hasPath) {
			for (DirectedEdge f : this.inboxEdge[e.from()]) {
				this.trashEdge[f.to()].add(f);
				this.removeEdge(f);
			}
		}
		for (DirectedEdge e : hasPath) {
			this.inboxEdge[e.from()] = new ArrayList<DirectedEdge>();
		}
		// 删除allPassNodePath路段上出发点到达边，路段应该是连通的**********
	}
	public void revisePath4(ArrayList<DirectedEdge> hasPath) {
		// 删除allPassNodePath路段上除始终点之外点到达边，路段应该是连通的**********
		for (int i=1;i<hasPath.size();i++) {
			for (DirectedEdge f : this.inboxEdge[hasPath.get(i).from()]) {
				this.trashEdge[f.to()].add(f);
				this.removeEdge(f);
			}
		}
		for (int i=1;i<hasPath.size();i++) {
			this.inboxEdge[hasPath.get(i).from()] = new ArrayList<DirectedEdge>();
		}
		// 删除allPassNodePath路段上除始终点之外点到达边，路段应该是连通的**********
	}

	public void reBuilt() {// 还原图
		// 还原边************************************************
		for (int v = 0; v < this.V(); v++) {
			for (DirectedEdge e : this.trashEdge[v]) {
				this.inboxEdge[v].add(e);
				this.addEdge(e);
			}
		}
		for (int v = 0; v < this.V(); v++) {
			this.trashEdge[v] = new ArrayList<DirectedEdge>();
		}
		// 还原边************************************************
	}
	// 修边的类方法－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
	
	// 生成建立路段后的可达点列表的类方法－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
	public ArrayList<Integer>[] arriveNodeTable(int demandOrigin,int demandDestination,ArrayList<Integer> demandPastNode,ArrayList<DirectedEdge> hasPath){
		//不含起点,无需手动修图和还原图
		this.revisePath2(hasPath);
		
		ArrayList<Integer> mustPassNotPass=new ArrayList<Integer>();
		for(int i:demandPastNode){
			mustPassNotPass.add(i);
		}
		int index0=mustPassNotPass.indexOf(hasPath.get(0).from());
		if(index0!=-1){
			mustPassNotPass.remove(index0);
		}
		for(int i=0;i<hasPath.size()-1;i++){
			int index=mustPassNotPass.indexOf(hasPath.get(i).to());
			if(index!=-1){
				mustPassNotPass.remove(index);
			}
		}//路段终点仍判断其可达性
		
		ArrayList<Integer>[] table=(ArrayList<Integer>[])new ArrayList[mustPassNotPass.size()+1];
		for(int i=0;i<mustPassNotPass.size()+1;i++){
			table[i]=new ArrayList<Integer>();
		}
		for(int i=0;i<mustPassNotPass.size();i++){
			table[i].add(mustPassNotPass.get(i));
		}
		table[mustPassNotPass.size()].add(mustPassNotPass.get(mustPassNotPass.size()-1));
		
		
		for (int i = 0; i < mustPassNotPass.size() + 1; i++) {
			DijstraSP aDijstra = new DijstraSP(this, table[i].get(0));
			for (int j = 0; j < mustPassNotPass.size() + 1; j++) {
				if (j != i) {
					if (aDijstra.hasPathTo(table[j].get(0))) {
						table[i].add(table[j].get(0));
					}
				}
			}
		}
		for (int i = 0; i < mustPassNotPass.size() + 1; i++) {
			System.out.println( "首位必过点/终点，后面可达点："
					+ table[i]);
		}// 输出可达点列表
		
		this.reBuilt();//还原图
		return table;
	}
	
	
	// 生成建立路段后的可达点列表的类方法－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－


}
