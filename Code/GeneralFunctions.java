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

}