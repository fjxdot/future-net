package com.routesearch.route;

import java.util.ArrayList;


public class DijstraSP{
	private int[] distTo;
	private DirectedEdge[] edgeTo;
	private IndexMinPQ<Integer> pq;
	
	public DijstraSP(EdgeWeightedDigraph G,int s){
		this.distTo=new int[G.V()];
		this.edgeTo=new DirectedEdge[G.V()];
		pq=new IndexMinPQ<Integer>(G.V());
		for(int i=0;i<distTo.length;i++){
			distTo[i]=Integer.MAX_VALUE;
		}
		distTo[s]=0;
		
		pq.insert(s,0);
		while(!pq.isEmpty()){
			relax(G,pq.delMin());
		}
	}
	public void relax(EdgeWeightedDigraph G,int v){
		for(DirectedEdge e:G.adj(v)){
			int w=e.to();
			if(distTo[w]>distTo[v]+e.weight()){
				distTo[w]=distTo[v]+e.weight();
				edgeTo[w]=e;
				if(pq.contains(w)){
					pq.change(w,distTo[w]);
				}else{
					pq.insert(w,distTo[w]);
				}
			}
		}		
	}
	public int distTo(int v){return this.distTo[v];}
	public boolean hasPathTo(int v){return this.distTo[v]<Integer.MAX_VALUE;}
	public ArrayList<DirectedEdge> pathTo(int v){
		ArrayList<DirectedEdge> bag=new ArrayList<DirectedEdge>();
		for(DirectedEdge e=edgeTo[v];e!=null;e=edgeTo[e.from()]){
			bag.add(e);
		}
		return bag;
	}
}
