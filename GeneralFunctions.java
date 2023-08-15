import java.util.*;

//changed name of class to relatedMethods
public class GeneralFunctions{

//should I just redo this with Generics rather than create method for each data type?
public static void resetArray(boolean arr[], boolean val ){
         for (int i = 0; i < arr.length; i++){
            arr[i] = val;
        }
}


   //given an int[], it returns the biggest value
   public static int largest(int[] arr)
    {
        if (arr == null) return 0;
        if (arr.length == 0) return 0;
        
        int max = arr[0];
         
        for (int i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
         
        return max;
    }



public static void print2DArray(int[][] arr) {
         for (int i = 0;  i < arr.length; i++) {
            System.out.print( i  + "      ");
         }
         for (int i = 0; i < arr.length; i++) {
            System.out.print( i  + ". ");
            for (int j = 0; j < arr[i].length; j++) {
                
                System.out.print("[" + arr[i][j] + "]" + " ");
            }
            System.out.println(); // Move to the next row after printing all elements in the current row
        }
    }

public static void print2DArray(boolean[][] arr) {
/*
         for (int i = 0;  i < arr.length; i++) {
            System.out.print( i  + "       ");
         }
         */
         System.out.println();
         System.out.println("i, j ->");
         System.out.println("|");
         System.out.print("V\n   ");
         
         
         for (int i = 0; i < arr.length; i++) {
           // System.out.print( i  + ". ");
            //if (i == 0) {System.out.print("|  ");}
            //if (i == 1) {System.out.print("V    ");}
            for (int j = 0; j < arr[i].length; j++) {
                
                System.out.print("[" + arr[i][j] + "]" + " ");
            }
            //if (i != 0)
            System.out.print("\n   "); // Move to the next row after printing all elements in the current row
        }
        
        System.out.println();
    }
    
    
    
    public static void printArrayList(ArrayList < FileNode > list) {
        //System.out.println("<");
        System.out.println("Arraylist<>:");
        for (int i = 0; i < list.size(); i++) {
            //if (i % 4 == 0) System.out.println();
            System.out.println(" " + i + ". " + list.get(i).toString());// + ", " );
        }
        //System.out.print(">");
    }
    
        public static void printArrayList2(ArrayList < FolderNode > list) {
        //System.out.println("<");
        System.out.println("Arraylist<>:");
        for (int i = 0; i < list.size(); i++) {
            //if (i % 4 == 0) System.out.println();
            System.out.println(" " + i + ". " + list.get(i).toString());// + ", " );
        }
        //System.out.print(">");
    }
    
}