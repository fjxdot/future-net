/**
 * 实现代码文件
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
	 * 你需要完成功能的入口
	 * 
	 * @author XXX
	 * @since 2016-3-4
	 * @version V1
	 */
	public static String searchRoute(String graphFilePath,
			String conditionFilePath) {
		String resultPath2 = new String();
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(graphFilePath);// ��ͼ

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
		}// ��ȡ��ĿҪ������-------------------------------------------------------------------------

		// ���������ݱ���
		ArrayList<Path> joinPath = new ArrayList<Path>();
		int resultWeight = Integer.MAX_VALUE;
		int resultPathIndex = Integer.MAX_VALUE;
		Path resultPath = new Path();

		// DijstraSP abcd=new DijstraSP(G,19);
		// System.out.println("��ͨ"+abcd.distTo(69));

		// �������ȷ�ʽ������룭��������������������������������������������������������������������������������������
		// �㷨���������������������������������������������������������������������������������������������������������
		int maxPn = 200;// �����ͨ��Integer.MAX_VALUE
		double maxOp = Double.POSITIVE_INFINITY;// ��дΪ������Ϊ�����������
		// �㷨���������������������������������������������������������������������������������������������������������
		// ѡ�ٻ��Ʋ�����ʼ��������������������������������������������������������������������������������������������������������������������
		int demandPastNodeSize = demandPastNode.size();
		int candicateNum = 1200;// �ƶ�ÿ��ѡ�ٹ���Integer.MAX_VALUE

		// ���������鴴��һ���������ȶ��У�����������������������������������������������������������������������������������������
		ArrayList<Path>[] pqDG = (ArrayList<Path>[]) new ArrayList[demandPastNodeSize + 1];
		for (int i = 0; i < demandPastNodeSize + 1; i++) {
			pqDG[i] = new ArrayList<Path>();
		}
		// ���������鴴��һ���������ȶ��У�����������������������������������������������������������������������������������������
		// ��ʼ������������������������������������������������������������������������������������������������������������������������������
		int pathNumber = 0;
		double op = 0.0;
		if (demandPastNodeSize <= 11) {
			// //�����㷨����Σ�������������������������������������������������������������������������������������������������������������������������
			op = op
					+ new Path().starStep(G, demandOrigin, demandDestination,
							demandPastNode, pqDG);
			op = op + new Path().allStep(G, pqDG, candicateNum, demandPastNode);
			// //�����㷨����Σ�������������������������������������������������������������������������������������������������������������������������
		} else {
			// //�����㷨����Σ�������������������������������������������������������������������������������������������������������������������������
			op = op + new Path().startHop(G,demandOrigin,demandDestination, demandPastNode,pqDG,candicateNum);
			// ���´��룭��������������������������������������������������������������������������������������
			for (int locate = demandPastNodeSize - 1; locate > 0; locate--) {
				if (pqDG[locate].size() != 0) {
					for (Path pathDelMin : pqDG[locate]) {
						op = op
								+ pathDelMin.nextHop(G, demandPastNode, pqDG,
										candicateNum);
						if (op >= maxOp) {
							break;
						}// �������������˳�
					}
				} else {
					System.out.println("����̫���̵���" + (locate + 1) + "����һ���޽�");
				}
			}
			// //�����㷨����Σ�������������������������������������������������������������������������������������������������������������������������
		}

		// System.out.println(pqDG[0].size());
		if (pqDG[0].size() != 0) {
			for (Path pathDelMin : pqDG[0]) {
				Path aPath = new Path();
				aPath = pathDelMin.lastHop(G);
				if (aPath.weight() != 0) {
					joinPath.add(aPath);
					// System.out.println("һ��·����" + aPath.pathEdge());
				}
				pathNumber++;
				if (pathNumber >= maxPn) {
					break;
				}
			}

		} else {
			System.out.println("����̫���̵����޷������յ�");
		}
		// ���´��룭��������������������������������������������������������������������������������������

		System.out.println("");
		System.out.println("��������" + op);
		// �������ݽ����������������������������������������������������������������������������������������������������
		if (joinPath.size() > 0) {
			for (int i = 0; i < joinPath.size(); i++) {
				// System.out.println(joinPath.get(i).pathEdge());
				if (joinPath.get(i).weight() < resultWeight) {
					resultWeight = joinPath.get(i).weight();
					resultPathIndex = i;
					resultPath = joinPath.get(i);// ָ��
				}
			}

			System.out.println("���Ȩ" + resultWeight);
			// System.out.println("���Ȩ·��" + resultPath.pathEdge());

			int ccc = 0;
			for (int i = 0; i < resultPath.pathEdge().size(); i++) {
				resultPath2 = resultPath2
						+ resultPath.pathEdge().get(i).edgeNumber();
				if (ccc < resultPath.pathEdge().size() - 1) {
					resultPath2 = resultPath2 + "|";
				}
				ccc++;
			}
			System.out.println("���Ȩ·��" + resultPath2);

		} else {
			System.out.println("�޽�");
			resultPath2 = "NA";
		}

		// //���·�Σ�����������������������������������������������������������������������������������������������������������������������
		// ArrayList<Integer> demandPastNodeOrder=new ArrayList<Integer>();
		// for(DirectedEdge e:resultPath.pathEdge()){
		// if(demandPastNode.indexOf(e.to())!=-1){
		// demandPastNodeOrder.add(e.to());
		// }
		// }
		// System.out.println("���·���ϵıع���˳��"+demandPastNodeOrder);
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
		// System.out.println("���·���ع���"+demandPastNodeOrder.get(i)+"���ع���"+demandPastNodeOrder.get(i)+"�ľ���"+tempw);
		//
		// }
		// //���·�Σ�����������������������������������������������������������������������������������������������������������������������

		return resultPath2;
	}

}