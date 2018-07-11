package com.routesearch.route;

public class DirectedEdge {
	private int edgeNumber;
	private int v;
	private int w;
	private int weight;
	
	public DirectedEdge(int edgeNumber,int v,int w,int weight){
		this.edgeNumber=edgeNumber;
		this.v=v;
		this.w=w;
		this.weight=weight;		
	}
	public int edgeNumber(){
		return this.edgeNumber;
	}
	public int from(){
		return this.v;
	}
	public int to(){
		return this.w;
	}
	public int weight(){
		return this.weight;
	}
	public String toString(){
		return String.format("%d,%d->%d,%d",edgeNumber,v,w,weight);
	}
	
}
