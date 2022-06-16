/**
 * Gale Shapley algorithm implementation for Stable Match
 * @author Zain Salman - 7790429
 */

import java.io.*;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Scanner;

public class GaleShapley {
    
    private Stack<Integer> Sue; 	//Integer Stack created to track matches
    private int[] employers; 	//Array containing number of employees	
    private int[] students;		//Array containing number of students 
    private int[][] A;		//Matrix of student rank given to employees
    private String[] match; 	//Array used to keep all stable matches 
    private int size; 	//Number of employees and students
    private String[] emp; 	//Array containing employer names 
    private String[] std; 	//Array containing student names
    PriorityQueue<Pair>[] pq; 	//Priority Que that holds employer ranking for each student


    /**
    * Initializes the Stack, Matrix, Array of employer and student names, and Priority Que.
    * @param String  the name of the txt file that will be read
    * @throws IOException if the file is not found or any IO errors
    */
    public void initialize(String filename) throws IOException 
    {
        File files = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(files));
        size = Integer.parseInt(br.readLine()); //Reads the first line to gather how many students and employers there are


        employers = new int[size]; 
        students = new int[size];
        Sue = new Stack<>();
        for(int i=0; i<size; i++) {
            Sue.push(i); //Initializes the stack from 0 - size-1 
            employers[i] = -1; //Initializes every element for employer array in to -1
            students[i] = -1; //Initializes every element for the student array to -1
        }

        emp = new String[size];
        for(int i=0; i<size; i++) {
            emp[i] = br.readLine(); //Stores employer names into the array
        }

        std = new String[size];
        for(int i=0; i<size; i++) {
            std[i] = br.readLine(); //Stores student names into the array
        }

        String[] flag;
        Pair[][] M = new Pair[size][size];
        int e = 0;
        while(e<size) {
            flag = br.readLine().replace(",", " ").split(" "); //Converts the matrix into a String array
            int s = 0;
            for(int i=0; i<size*2; i+=2) {
                int key = Integer.parseInt(flag[i]);
                int value = Integer.parseInt(flag[i+1]);
                Pair pair = new Pair(key,value); //Creates a new key, value pair for every pair of integers in the matrix
                M[e][s] = pair; //Stores the pair into the base matrix
                s++;
            }
            e++;
        }

        A = new int[size][size];
        for(int i =0; i<size; i++) {
            for(int j=0; j<size; j++) {
                A[i][j] = M[j][i].getValue(); //Inserts Student ranking for a given employer into the matrix
            }
        }

        pq = new PriorityQueue[size];
        for(int i=0; i<size; i++) {
            pq[i] = new PriorityQueue<>(); //Initializes a new Priority Que Set for each employer
            for(int j=0; j<size; j++) {
                int a = M[i][j].getKey(); 
                pq[i].add(new Pair(a,j)); //Inserts a pair employer ranking for each student
            }
        }
    }


    /**
    * Executes the Gale Shapley algorithm using the data from the initialize method.
    * @throws NullPointerException if any stack, array, or PQ has not been initalized
    */
    public void execute() throws NullPointerException
    {
        int e;
        int e1;
        int s;
        match = new String[size];
        while(!Sue.empty()) {
            e = Sue.pop(); // e is looking for a student
            s = pq[e].poll().getValue(); // most preferred student of e
            e1 = students[s];
            if(students[s]== -1) { // student is unmatched
                students[s] = e;
                employers[e] = s;

            }
            else if(A[s][e] < (A[s][e1])) { // s prefers e to employer e’
                students[s] = e;
                employers[e] = s; // Replace the match
                employers[e1] = -1; // now unmatched
                Sue.push(e1);
            }
            else {
                Sue.push(e);
            }
        }
    }

    /**
    * Wrties a new file displaying the Stable Match 
    * @param String  the name of the txt file that will be read
    * @throws if the file is not found or any IO errors
    */
    public void save(String filename) throws IOException 
    {
        FileWriter newFile = new FileWriter("matches_"+filename); //Creates a new file
        PrintWriter printWriter = new PrintWriter(newFile);
        for(int i=0; i<size; i++) {
        	match[i] = emp[i]+" - "+std[employers[i]]; //Stores the String representation of each stable match employer - student format
            printWriter.printf("Match %s: %s %n", i,match[i]); //writes each match on the file 
        }
        printWriter.close();
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter filename: ");
        String filename = sc.nextLine();

        GaleShapley matches = new GaleShapley();

        try {
            matches.initialize(filename);
        } catch (IOException e) {
            System.out.println("Error: File Not Found");
            e.printStackTrace();
        }

        try{
        	matches.execute();
        } catch(NullPointerException e){
        	System.out.println("Null Exception");
        }

        try {
            matches.save(filename);
        } catch (IOException e) {
            System.out.println("Error: File Not Found");
            e.printStackTrace();
        }
    }
}
