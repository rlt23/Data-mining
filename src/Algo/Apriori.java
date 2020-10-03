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

public class Apriori {

	static String temp_var_arr[];
	public static long start;
	public static long end;

	public static void main(String[] args) {
		HashMap<HashSet<String>, Integer> Transaction = new HashMap<>();
		HashMap<HashSet<String>, Integer> storage = new HashMap<>();
		HashSet<String> Item = new HashSet<>();
		HashSet<String> ItemAfterStage1 = new HashSet<>();
		HashSet<HashSet<String>> discardSet = new HashSet<>();
		int support = 2;
		int confidence = 2;
		int totaltrans = 0;
		BufferedReader br;
		int check = 1;
		start = System.currentTimeMillis();
		Scanner mystorageObj = new Scanner(System.in);
		System.out.println("-------------APRIORI ALGORITHM------------");
		System.out.println("Please enter the file name:");
		String input = mystorageObj.nextLine();
		File file = new File(input);
		try {
			// Enter Support and confidence
			//Scanner mystorageObj = new Scanner(System.in);
			System.out.println("Enter Support Value in percentage");//Enter values between 0-100
			support = mystorageObj.nextInt();
			//support = 20;
			System.out.println("Enter Confidence Value in percentage");
			confidence = mystorageObj.nextInt();
			//confidence = 40;
			// mystorageObj.close();
			// END Enter Support and confidence
			// Loading the data from database
			
			br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				String strArray[] = st.split(",");
				HashSet<String> transactionSet = new HashSet<>(Arrays.asList(strArray));
				if (Transaction.containsKey(transactionSet)) {
					Transaction.put(transactionSet, Transaction.get(transactionSet) + 1);
				} else {
					Transaction.put(transactionSet, 1);
				}
				totaltrans++;
				Item.addAll(transactionSet);
			}
			//System.out.println(Transaction);// HashMap containing all the lines of the file.
			System.out.println(Item);
			support = (int) (((float) support / 100) * totaltrans);
			// END Loading the data from database
			// Calculating the support and confidence
			Iterator<String> value = Item.iterator();
			while (value.hasNext()) {
				String itemtemp_var = value.next();
				int val = 0;
				for (Map.Entry<HashSet<String>, Integer> entry : Transaction.entrySet()) {
					if (entry.getKey().contains(itemtemp_var)) {
						val += entry.getValue();
					}
				}
				if (val >= support) {
					ItemAfterStage1.add(itemtemp_var);
					HashSet<String> temp_varStorage = new HashSet<>();
					temp_varStorage.add(itemtemp_var);
					storage.put(temp_varStorage, val);
				} else {
					HashSet<String> temp_varStorage = new HashSet<>();
					temp_varStorage.add(itemtemp_var);
					discardSet.add(temp_varStorage);
				}
			}
			//System.out.println(storage);// the count after the first iteration
			System.out.println("--------------------------------");
			System.out.println("The individual items and their count");
			for (HashSet<String> str : storage.keySet()) {
				System.out.println(str + " = " + storage.get(str));
			}
			System.out.println("--------------------------------");
			temp_var_arr = ItemAfterStage1.toArray(new String[0]);
			//System.out.println("= " + Arrays.toString(temp_var_arr));
			int exit = 0;
			while (exit == 0) {
				exit = 1;
				setIterationValue();
				storageObj = new HashSet<>();
				combination(new String[iteration], 0, 0);
				System.out.println("-------------------------------");
				System.out.println("Item list after the iteration : " + getIterationValue());
				for (HashSet<String> comb : storageObj) {
					System.out.println(comb);
				}
				System.out.println("-------------------------------");
				for (HashSet<String> comb : storageObj) {
					int k = 0;
					for (HashSet<String> elim : discardSet) {
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
							storage.put(comb, val);
							exit = 0;
						} else {
							discardSet.add(comb);
						}
					}
				}
				System.out.println("The individual items and their count after the iteration :" + getIterationValue());
				for (HashSet<String> str : storage.keySet()) {
					System.out.println(str + " = " + storage.get(str));
				}
				System.out.println("--------------------------------");
			}
			System.out.println("The final frequent item list is :");
			for (HashSet<String> frr : storage.keySet()) {
				System.out.println(frr + "-->" + storage.get(frr));
			}
			System.out.println("-------------------------------------------------");
			System.out.println("The items satisfying the association rules are :");
			for (Map.Entry<HashSet<String>, Integer> entry : storage.entrySet()) {
				transit_var = new String[entry.getKey().size()];
				entry.getKey().toArray(transit_var);
				smt = transit_var;
				for (int u = 1; u < transit_var.length; u++) {
					candidateRule(new String[u], 0, 0, u);
				}
				HashSet<String> temp_var = new HashSet<>();
				temp_var.addAll(entry.getKey());
				for (HashSet<String> ls : orr) {
					temp_var.removeAll(ls);
					NumberFormat formatter = new DecimalFormat("#0.00");
					Integer t2 = entry.getValue();
					Integer t3 = 0;
					if (storage.containsKey(ls)) {
						t3 = storage.get(ls);
					} else {
						System.out.println("error");
					}
					float conf = ((float) t2 / t3) * 100;
					float supp = ((float) t2 / totaltrans) * 100;
					if (conf >= confidence) {
						check = 0;
						System.out.println(ls.toString().replace("[", "").replace("]", "") + "-->"
								+ temp_var.toString().replace("[", "").replace("]", "") + ":[" + formatter.format(supp)
								+ "%," + formatter.format(conf) + "%]");
					}
					temp_var.addAll(entry.getKey());
				}
				orr = new HashSet<HashSet<String>>();
			}
			if (check == 1) {
				System.out.println("Nothing to display with the given confidece and support");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		end = System.currentTimeMillis();
		System.out.println("Total time to run the program is: "+(end -start) +"msec");
	}

	static String[] smt = {};
	static HashSet<HashSet<String>> storageObj = new HashSet<>();
	static int iteration = 1;
	static String[] transit_var;
	static HashSet<HashSet<String>> orr = new HashSet<HashSet<String>>();

	public static int getIterationValue() {
		return iteration;
	}

	public static void setIterationValue() {
		Apriori.iteration += 1;
	}
	public static void combination(String data[], int start, int index) {
		if (index == iteration) {
			HashSet<String> temp_var = new HashSet<>();
			for (int j = 0; j < iteration; j++)
				temp_var.add(data[j]);
			storageObj.add(temp_var);
			return;
		}
		for (int i = start; i <= temp_var_arr.length - 1 && temp_var_arr.length - i >= iteration - index; i++) {
			data[index] = temp_var_arr[i];
			combination(data, i + 1, index + 1);
		}
	}
	public static void candidateRule(String data[], int start, int index, int iterator) {
		if (index == iterator) {
			HashSet<String> temp_var = new HashSet<>();
			for (int j = 0; j < iterator; j++)
				temp_var.add(data[j]);
			orr.add(temp_var);
			return;
		}
		for (int i = start; i <= smt.length - 1 && smt.length - i >= iterator - index; i++) {
			data[index] = smt[i];
			candidateRule(data, i + 1, index + 1, iterator);
		}
	}
}