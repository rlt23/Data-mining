package Algo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class MainApriori {
	public static HashMap<ArrayList<String>, Integer> frequentItems = new HashMap<ArrayList<String>, Integer>();
	public static HashMap<ArrayList<String>, Integer> nonFrequentItems = new HashMap<ArrayList<String>, Integer>();
	public static HashMap<String[], Integer> finalMapping = new HashMap<String[], Integer>();
	public static int databaseSize = 0;
	public static HashSet<String> uniqueFeatures = new HashSet<String>();
	public static long start;
	public static long end;

	public static void main(String[] args) {
		Scanner obj = new Scanner(System.in);
		Scanner obj1 = new Scanner(System.in);
		System.out.println("Enter the value for the support, example (.1 for 10%),(.7 for 70%)");
		double support = obj.nextDouble();
		System.out.println("Enter the value for the confidence, example (.1 for 10%),(.7 for 70%)");
		double confidence = obj.nextDouble();
		System.out.println(
				"Enter the DataBase from the given list of databases :DataBase1.csv,DataBase2.csv,DataBase3.csv,DataBase4.csv,DataBase5.csv");
		String dataBaseName=obj1.nextLine();
		obj.close();
		start = System.currentTimeMillis();
		String[] dataBases = { "DataBase1.csv", "DataBase2.csv", "DataBase3.csv", "DataBase4.csv", "DataBase5.csv" };
		HashMap<ArrayList<String>, Integer> dataBase1 = new HashMap<ArrayList<String>, Integer>();

		// reading the database1 and getting the unique elements
		System.out.println("+++++++++++++++++Reading DataBase+++++++++");
		readDataBases(dataBase1, dataBaseName);
		databaseSize = dataBase1.size();
		getUniqueElements(dataBase1, uniqueFeatures);
		generateAssociation(dataBase1, uniqueFeatures, support, confidence);

		end = System.currentTimeMillis();
		System.out.println("---------------------------------------------------------------------");
		System.out.println("Total time elapsed while the running of the program = " + (end - start));
	}

	// This is the method to read the data from the file and populate the HashMap
	public static void readDataBases(HashMap<ArrayList<String>, Integer> dataBase, String fileName) {

		try {
			File file = new File(fileName);
			String[] factors;

			Scanner fileData = new Scanner(file);
			while (fileData.hasNext()) {
				String data = fileData.next();
				factors = data.split(",");
				ArrayList<String> itemsList = new ArrayList<String>();
				for (int i = 1; i < factors.length; i++) {
					itemsList.add(factors[i]);
				}
				// We are entering the data in the HashMap here.
				dataBase.put(itemsList, Integer.parseInt(factors[0]));

			}
		} catch (Exception ex) {
			System.out.println(ex);
		}

	}

	// Thus is the method to get the list of unique from the list of all the
	// elements
	public static void getUniqueElements(HashMap<ArrayList<String>, Integer> dataBase, HashSet<String> hashList) {

		for (ArrayList<String> arr : dataBase.keySet()) {
			for (String str : arr) {
				hashList.add(str);
			}
		}

	}

	// This is the main driver method for the running the algorithm
	public static void generateAssociation(HashMap<ArrayList<String>, Integer> dataBase, HashSet<String> hashList,
			double support, double confidence) {

		HashMap<ArrayList<String>, Integer> dynamicMapping = new HashMap<ArrayList<String>, Integer>();
		System.out.println("The List of items in the database :");
		for (String str : hashList) {
			ArrayList<String> transfer = new ArrayList<String>();
			transfer.add(str);
			System.out.println(transfer);
			// This is creating the dynamic HashMap that we will use throughout the code.
			// For the start we are entering count as 0 for the elements
			dynamicMapping.put(transfer, 0);
		}

		// iteration will keep the count for size of each row in itemList
		int iteration = 1;
		boolean stop = true;
		while (stop) {
			// calculate the support of already generated HashMap of items.
			calculateSupport(dataBase, dynamicMapping, iteration);
			// we will validate the support count of the HashMap to the user entered support
			// value.
			validateSupport(dynamicMapping, support, iteration);
			iteration = iteration + 1;
			// make the k+1 HasMap of items after eliminating on the basis of support.
			makeNewFrequentList(dynamicMapping, iteration);
			if (dynamicMapping.size() < 1) {
				stop = false;
			}

		}

//		System.out.println(frequentItems);
//		System.out.println(dynamicMapping.size());

		makeFinalList(iteration, confidence, support);
		calculateConfidence(dataBase, confidence, support);

	}

	// This method will calculate the support of each generated row in the map
	public static void calculateSupport(HashMap<ArrayList<String>, Integer> dataBase,
			HashMap<ArrayList<String>, Integer> dynamicMapping, int iteration) {

		int length = 0;
		int count = 0;
		for (ArrayList<String> outer : dynamicMapping.keySet()) {
			for (ArrayList<String> inner : dataBase.keySet()) {
				length = outer.size();
				for (String str : outer) {
					if (inner.contains(str)) {
						count = count + 1;
					}
				}
				// if all the elements of the row of dynamic HashMap is present in a row of
				// database.
				if (count == length) {
					dynamicMapping.put(outer, dynamicMapping.get(outer) + 1);
				}
				count = 0;
			}

		}

		System.out.println("--------------------------------------------------------");
		System.out.println("The support count of the elements after " + iteration + " iteration : " + "C" + iteration);
		int dyna = 0;
		for (Map.Entry element : dynamicMapping.entrySet()) {
			dyna = (int) element.getValue();
			System.out.println(
					element.getKey() + "   " + element.getValue() + " ||Support = " + (dyna / (double) databaseSize));
		}
	}

	// This method will remove the items which do not validate to the user passed
	// support values.
	public static void validateSupport(HashMap<ArrayList<String>, Integer> dynamicMapping, double support,
			int iteration) {

		for (ArrayList<String> arr : dynamicMapping.keySet()) {
			if ((dynamicMapping.get(arr) / (double) databaseSize) < support) {
				// adding the elements not passing the support into nonFrequentItems list.
				nonFrequentItems.put(arr, dynamicMapping.get(arr));
			} else {
				frequentItems.put(arr, dynamicMapping.get(arr));
			}
		}

		// removing the elements which don't pass the support values from the
		// dynamicMapping.
		for (ArrayList<String> arr : nonFrequentItems.keySet()) {
			if (dynamicMapping.containsKey(arr)) {
				dynamicMapping.remove(arr);
			}
		}

		System.out.println("--------------------------------------------------------");
		if (dynamicMapping.size() == 0) {
			System.out.println("None of the items passed the spport value validation.");
		} else {
			System.out.println(
					"The items which pass the support  after " + iteration + " iteration : " + "L" + iteration);
			for (Map.Entry element : dynamicMapping.entrySet()) {
				System.out.println(element.getKey() + "   " + element.getValue());
			}

		}
	}

	// This is the Method to generate the itemlist from the previous iteration
	public static void makeNewFrequentList(HashMap<ArrayList<String>, Integer> dynamicMapping, int iteration) {

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> str1 : dynamicMapping.keySet()) {
			list.add(str1);
		}

		// here we are clearing the HashMap so that we can refill it with new Items.
		dynamicMapping.clear();
		HashSet<String> hashInterface = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				for (int k = 0; k < list.get(i).size(); k++) {
					hashInterface.add(list.get(i).get(k));
				}
				for (int k = 0; k < list.get(j).size(); k++) {
					hashInterface.add(list.get(j).get(k));
				}
				if (hashInterface.size() == iteration) {
					ArrayList<String> newList = new ArrayList<String>();
					for (String stri : hashInterface) {
						newList.add(stri);
					}
					// Collections.sort(newList);
					dynamicMapping.put(newList, 0);
				}
				hashInterface.clear();
			}
		}
		System.out.println("--------------------------------------------------------");
		System.out.println("The List of items after " + iteration + " iteration : ");
		if (dynamicMapping.size() == 0) {
			System.out.println("No combinations are formed in this iteration");
		}
		for (Map.Entry element : dynamicMapping.entrySet()) {
			System.out.println(element.getKey() + "   " + element.getValue());
		}

	}

	// In this method we will make the association from the frequent itemlist.
	public static void makeFinalList(int iteration, double confidence, double support) {

		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> itemCount = new ArrayList<Integer>();

		// storing the elements and the count in the respective arraylist.
		System.out.println("-----------------------------------------");
		System.out.println("The list of frequent items and their support count is given below");
		for (ArrayList<String> str : frequentItems.keySet()) {
			System.out.println(str + " , " + frequentItems.get(str));
		}
		for (ArrayList<String> arr : frequentItems.keySet()) {
			array.add(arr);
			itemCount.add(frequentItems.get(arr));
		}

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).size() > 1) {
				for (int j = 0; j < array.get(i).size(); j++) {
					String[] staging = new String[2];
					String[] staging1 = new String[2];
					staging[0] = array.get(i).get(j);
					staging1[1] = array.get(i).get(j);
					// System.out.println(array.get(i).get(j));
					for (int k = 0; k < array.get(i).size(); k++) {
						if (k != j) {
							str.append(array.get(i).get(k) + ",");
						}
					}
					staging[1] = str.toString();
					staging1[0] = str.toString();
					// System.out.println(Arrays.toString(staging));//keep
					finalMapping.put(staging, itemCount.get(i));

					// Enter a bilateral relation only when elements are more than 2.
					if (array.get(i).size() > 2) {
						// System.out.println(Arrays.toString(staging1));//keep
						finalMapping.put(staging1, itemCount.get(i));
					}
					str.delete(0, str.length());
					// System.out.println("----------");//keep
				}

			}
		}

		System.out.println("--------------------------------------------------------");
		System.out.println("The associations from the list of frequent items :");
		for (Map.Entry element : finalMapping.entrySet()) {
			String[] arrena = (String[]) element.getKey();
			// System.out.println(Arrays.toString(arrena));
			System.out.println(arrena[0] + "->" + arrena[1]);
			System.out.println(" ");
		}
		System.out.println("--------------------------------------------------------");
	}

	public static void calculateConfidence(HashMap<ArrayList<String>, Integer> dataBase, double confidence,
			double support) {

		int elementCount;
		int finalCount;
		int desiredCount;
		HashMap<String[], Integer> confidenceCount = new HashMap<String[], Integer>();
		System.out.println("The final list which pass the support and confidnce validation are.");
		for (Map.Entry element : finalMapping.entrySet()) {
			String[] arrena = (String[]) element.getKey();
			// System.out.println(arrena[0]);
			if (arrena[0].indexOf(",") != -1) {
				elementCount = 0;
				String[] parts = arrena[0].split(",");
				desiredCount = 0;
				finalCount = 0;
				for (ArrayList<String> arr : dataBase.keySet()) {
					for (int i = 0; i < parts.length; i++) {
						if (arr.contains(parts[i])) {
							finalCount = finalCount + 1;
						}
					}
					// System.out.println("final "+finalCount);
					if (finalCount == parts.length) {
						elementCount = elementCount + 1;
					}
					finalCount = 0;
				}

			} else {
				elementCount = 0;
				for (ArrayList<String> arr : dataBase.keySet()) {
					if (arr.contains(arrena[0])) {
						elementCount = elementCount + 1;
					}
				}

			}
			if (((int) element.getValue() / (double) elementCount) >= confidence) {
				System.out.println(arrena[0] + " --> " + arrena[1] + " || Support = "
						+ (((int) element.getValue() / (double) databaseSize) * 100) + "%" + " || Confidence ="
						+ (((int) element.getValue() / (double) elementCount) * 100) + "%");
			}
		}

	}

}