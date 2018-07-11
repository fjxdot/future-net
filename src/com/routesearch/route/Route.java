/**
 * 瀹炵幇浠ｇ爜鏂囦欢
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public final class Route {
	/**
	 * 浣犻渶瑕佸畬鎴愬姛鑳界殑鍏ュ彛
	 * 
	 * @author XXX
	 * @since 2016-3-4
	 * @version V1
	 */
	public static String searchRoute(String graphFilePath,
			String conditionFilePath) {
		String resultPath2 = new String();
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(graphFilePath);// 读图

		File file = new File(conditionFilePath);
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

		String demandString = tempStringList.removeLast();
		String demandStringSplit[] = demandString.split(",");
		int demandOrigin = Integer.parseInt(demandStringSplit[0]);
		int demandDestination = Integer.parseInt(demandStringSplit[1]);
		String demandStringSplit2[] = demandStringSplit[2].split("\\|");
		ArrayList<Integer> demandPastNode = new ArrayList<Integer>();
		for (int i = 0; i < demandStringSplit2.length; i++) {
			demandPastNode.add(Integer.parseInt(demandStringSplit2[i]));
		}// 获取题目要求数据-------------------------------------------------------------------------

		// 保存结果数据变量
		ArrayList<Path> joinPath = new ArrayList<Path>();
		int resultWeight = Integer.MAX_VALUE;
		int resultPathIndex = Integer.MAX_VALUE;
		Path resultPath = new Path();

		// DijstraSP abcd=new DijstraSP(G,19);
		// System.out.println("连通"+abcd.distTo(69));

		// 索引优先方式处理代码－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		// 算法函数输入参数－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		int maxPn = 200;// 最大走通解Integer.MAX_VALUE
		double maxOp = Double.POSITIVE_INFINITY;// 若写为函数则为输入输出参数
		// 算法函数输入参数－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		// 选举机制参数初始化－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		int demandPastNodeSize = demandPastNode.size();
		int candicateNum = 1200;// 制定每轮选举规则Integer.MAX_VALUE

		// 用链表数组创建一个索引优先队列－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		ArrayList<Path>[] pqDG = (ArrayList<Path>[]) new ArrayList[demandPastNodeSize + 1];
		for (int i = 0; i < demandPastNodeSize + 1; i++) {
			pqDG[i] = new ArrayList<Path>();
		}
		// 用链表数组创建一个索引优先队列－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		// 初始化－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		int pathNumber = 0;
		double op = 0.0;
		if (demandPastNodeSize <= 11) {
			// //暴力算法代码段－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
			op = op
					+ new Path().starStep(G, demandOrigin, demandDestination,
							demandPastNode, pqDG);
			op = op + new Path().allStep(G, pqDG, candicateNum, demandPastNode);
			// //暴力算法代码段－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		} else {
			// //弹性算法代码段－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
			op = op + new Path().startHop(G,demandOrigin,demandDestination, demandPastNode,pqDG,candicateNum);
			// 更新代码－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
			for (int locate = demandPastNodeSize - 1; locate > 0; locate--) {
				if (pqDG[locate].size() != 0) {
					for (Path pathDelMin : pqDG[locate]) {
						op = op
								+ pathDelMin.nextHop(G, demandPastNode, pqDG,
										candicateNum);
						if (op >= maxOp) {
							break;
						}// 操作数过多则退出
					}
				} else {
					System.out.println("条件太苛刻导致" + (locate + 1) + "的下一轮无解");
				}
			}
			// //弹性算法代码段－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		}

		// System.out.println(pqDG[0].size());
		if (pqDG[0].size() != 0) {
			for (Path pathDelMin : pqDG[0]) {
				Path aPath = new Path();
				aPath = pathDelMin.lastHop(G);
				if (aPath.weight() != 0) {
					joinPath.add(aPath);
					// System.out.println("一条路径解" + aPath.pathEdge());
				}
				pathNumber++;
				if (pathNumber >= maxPn) {
					break;
				}
			}

		} else {
			System.out.println("条件太苛刻导致无法到达终点");
		}
		// 更新代码－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－

		System.out.println("");
		System.out.println("操作次数" + op);
		// 保存数据结果－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		if (joinPath.size() > 0) {
			for (int i = 0; i < joinPath.size(); i++) {
				// System.out.println(joinPath.get(i).pathEdge());
				if (joinPath.get(i).weight() < resultWeight) {
					resultWeight = joinPath.get(i).weight();
					resultPathIndex = i;
					resultPath = joinPath.get(i);// 指针
				}
			}

			System.out.println("最低权" + resultWeight);
			// System.out.println("最低权路径" + resultPath.pathEdge());

			int ccc = 0;
			for (int i = 0; i < resultPath.pathEdge().size(); i++) {
				resultPath2 = resultPath2
						+ resultPath.pathEdge().get(i).edgeNumber();
				if (ccc < resultPath.pathEdge().size() - 1) {
					resultPath2 = resultPath2 + "|";
				}
				ccc++;
			}
			System.out.println("最低权路径" + resultPath2);

		} else {
			System.out.println("无解");
			resultPath2 = "NA";
		}

		// //检查路段－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
		// ArrayList<Integer> demandPastNodeOrder=new ArrayList<Integer>();
		// for(DirectedEdge e:resultPath.pathEdge()){
		// if(demandPastNode.indexOf(e.to())!=-1){
		// demandPastNodeOrder.add(e.to());
		// }
		// }
		// System.out.println("结果路径上的必过点顺序"+demandPastNodeOrder);
		// for(int i=0;i<demandPastNodeOrder.size()-1;i++){
		// int tempf=0;
		// int tempw=0;
		// for(DirectedEdge e:resultPath.pathEdge()){
		// if(e.from()==demandPastNodeOrder.get(i)){
		// tempf=1;
		// }
		// if(e.from()==demandPastNodeOrder.get(i+1)){
		// tempf=0;
		// }
		// if(tempf==1){
		// tempw=tempw+e.weight();
		// }
		// }
		// System.out.println("结果路径必过点"+demandPastNodeOrder.get(i)+"到必过点"+demandPastNodeOrder.get(i)+"的距离"+tempw);
		//
		// }
		// //检查路段－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－

		return resultPath2;
	}

}