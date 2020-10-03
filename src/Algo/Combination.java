package Algo;


import java.util.HashSet;

class Combination { 
  
	static int lit=1;
	static String arrw[] = {"A", "B", "C", "D", "E"}; 
	static HashSet<HashSet<String>> obj=new HashSet<>(); 
	
	public static void combinationGenerator(String data[], int start, int index, int a) { 
        if (index == a) {
        	HashSet<String> temp=new HashSet<>();
            for (int j=0; j<a; j++)
                temp.add(data[j]);
            obj.add(temp);
            return ; 
        } 
        for (int i=start; i<=arrw.length-1 && arrw.length-i >= a-index; i++){ 
            data[index] = arrw[i]; 
            combinationGenerator(data, i+1, index+1,a); 
        }
    } 
	
	
    public static void main (String[] args) { 
    	int a=3;
        combinationGenerator(new String[a],0,0,a);
        for (HashSet<String> stock : obj) { 
        	System.out.println(stock.toString());
        }

    } 
} 