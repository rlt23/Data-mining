package Algo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class testApriori {

	static String arrw[];

	public static void main(String[] args) {
		HashMap<HashSet<String>, Integer> Transaction = new HashMap<>();
		HashMap<HashSet<String>, Integer> fis = new HashMap<>();
		HashSet<String> Item = new HashSet<>();
		HashSet<String> ItemAfterStage1 = new HashSet<>();
		HashSet<HashSet<String>> eleminatinSet = new HashSet<>();
		int support = 2;
		int confidence = 2;
		int totaltrans = 0;
		BufferedReader br;
		int noy = 1;

		File file = new File("C:\\NJIT\\CS 634\\Mid term Project\\D1.txt");
		try {
			// Enter Support and confidence
			Scanner myObj = new Scanner(System.in);
			System.out.println("Enter Support Value in percentage Eg:70 for 70%");
			support = myObj.nextInt();
			System.out.println("Enter Confidence Value in percentage Eg:70 for 70%");
			confidence = myObj.nextInt();
			myObj.close();
			// END Enter Support and confidence
			// Loading the data from database
			br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				String strArray[] = st.split(",");
				HashSet<String> setOfTrans = new HashSet<>(Arrays.asList(strArray));
				if (Transaction.containsKey(setOfTrans)) {
					Transaction.put(setOfTrans, Transaction.get(setOfTrans) + 1);
				} else {
					Transaction.put(setOfTrans, 1);
				}
				totaltrans++;
				Item.addAll(setOfTrans);
			}
			support = (int) (((float) support / 100) * totaltrans);
			// END Loading the data from database
			// Calculating the support and confidence
			Iterator<String> value = Item.iterator();
			while (value.hasNext()) {
				String itemTemp = value.next();
				int val = 0;
				for (Map.Entry<HashSet<String>, Integer> entry : Transaction.entrySet()) {
					if (entry.getKey().contains(itemTemp)) {
						val += entry.getValue();
					}
				}
				if (val >= support) {
					ItemAfterStage1.add(itemTemp);
					HashSet<String> ts3 = new HashSet<>();
					ts3.add(itemTemp);
					fis.put(ts3, val);
				} else {
					HashSet<String> ts3 = new HashSet<>();
					ts3.add(itemTemp);
					eleminatinSet.add(ts3);
				}
			}
			arrw = ItemAfterStage1.toArray(new String[0]);
			int exit = 0;
			while (exit == 0) {
				exit = 1;
				setIteration();
				obj = new HashSet<>();
				combinationGenerator(new String[iteration], 0, 0);
				for (HashSet<String> comb : obj) {
					int k = 0;
					for (HashSet<String> elim : eleminatinSet) {
						if (comb.containsAll(elim)) {
							k = 1;
							break;
						}
					}
					if (k == 0) {
						int val = 0;
						for (Map.Entry<HashSet<String>, Integer> entry : Transaction.entrySet()) {
							if (entry.getKey().containsAll(comb)) {
								val += entry.getValue();
							}
						}
						if (val >= support) {
							fis.put(comb, val);
							exit = 0;
						} else {
							eleminatinSet.add(comb);
						}
					}
				}
			}
			for (Map.Entry<HashSet<String>, Integer> entry : fis.entrySet()) {
				atest = new String[entry.getKey().size()];
				entry.getKey().toArray(atest);
				ax = atest;
				for (int u = 1; u < atest.length; u++) {
					candidateRule(new String[u], 0, 0, u);
				}
				HashSet<String> tempx = new HashSet<>();
				tempx.addAll(entry.getKey());
				for (HashSet<String> ls : orr) {
					tempx.removeAll(ls);
					NumberFormat formatter = new DecimalFormat("#0.00");
					Integer t2 = entry.getValue();
					Integer t3 = 0;
					if (fis.containsKey(ls)) {
						t3 = fis.get(ls);
					} else {
						System.out.println("error");
					}
					float conf = ((float) t2 / t3) * 100;
					float supp = ((float) t2 / totaltrans) * 100;
					if (conf >= confidence) {
						noy = 0;
						System.out.println(ls.toString().replace("[", "").replace("]", "") + "-->"
								+ tempx.toString().replace("[", "").replace("]", "") + ":[" + formatter.format(supp)
								+ "%," + formatter.format(conf) + "%]");
					}
					tempx.addAll(entry.getKey());
				}
				orr = new HashSet<HashSet<String>>();
			}
			// END Calculating the support and confidence
			if (noy == 1) {
				System.out.println("Nothing to display with the given confidece and support");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String[] ax = {};
	static HashSet<HashSet<String>> obj = new HashSet<>();
	static int iteration = 1;
	static String[] atest;
	static HashSet<HashSet<String>> orr = new HashSet<HashSet<String>>();

	public static void candidateRule(String data[], int start, int index, int iterator) {
		if (index == iterator) {
			HashSet<String> temp = new HashSet<>();
			for (int j = 0; j < iterator; j++)
				temp.add(data[j]);
			orr.add(temp);
			return;
		}
		for (int i = start; i <= ax.length - 1 && ax.length - i >= iterator - index; i++) {
			data[index] = ax[i];
			candidateRule(data, i + 1, index + 1, iterator);
		}
	}

	public static void combinationGenerator(String data[], int start, int index) {
		if (index == iteration) {
			HashSet<String> temp = new HashSet<>();
			for (int j = 0; j < iteration; j++)
				temp.add(data[j]);
			obj.add(temp);
			return;
		}
		for (int i = start; i <= arrw.length - 1 && arrw.length - i >= iteration - index; i++) {
			data[index] = arrw[i];
			combinationGenerator(data, i + 1, index + 1);
		}
	}

	public static int getIteration() {
		return iteration;
	}

	public static void setIteration() {
		Apriori.iteration += 1;
	}
}