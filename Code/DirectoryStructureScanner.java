//build the k-ary tree. recusrvily, depth-first-traversal. 
//came to find out that given two image files, its very slow to compare them byte by byte, hence I compare them
//pixel by pixel

//virtually no testing conducted

//there are unessecary imports here 
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.file.FileVisitOption;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.FileInputStream;
import java.util.Scanner;

class DirectoryStructureScanner {

    public static void main(String[] args) {

        //create the tree
        DirectoryKaryTree kary = new DirectoryKaryTree(new File("."));
        //print the tree
        kary.drawTree();

        ArrayList < FileNode > list = kary.getFileNodes(new ArrayList < FileNode > (), kary.root);

        System.out.println("Total Files: " + list.size() + "\n");


        ArrayList < ArrayList < FolderNode >> allFolderLists = new ArrayList < ArrayList < FolderNode >> ();
        //folders with matching contents, on each height level (could be switched to 3D array)
        ArrayList < boolean[][] > matrices = new ArrayList < boolean[][] > ();

        // debug
        //GeneralFunctions.printArrayList(list);

        //****************** Options ******************//

        String line;
        if (args.length > 0) {
            line = args[0].trim();
        } else {
            System.out.println("1. Identical sub-directories");
            System.out.println("2. Files w/ identical content, regardless of name");
            System.out.println("3. Files w/ identical name, grouped by identical content");
            Scanner scanner = new Scanner(System.in); // Create a Scanner object
            System.out.print("Enter option: ");
            line = scanner.nextLine().trim();
        }
        int choice = Integer.parseInt(line);
        System.out.println("\n");




        //****************** Compare Files ******************//


        boolean[][] height0 = new boolean[list.size()][list.size()]; // n x n

        //corresponds to each element in list. If a file belongs to any content match, it beocmes true
        boolean[] hasHome = new boolean[list.size()]; // n
        GeneralFunctions.resetArray(hasHome, false);

        for (int i = 0; i < list.size() - 1; i++) {
            if (hasHome[i]) continue;

            for (int j = i + 1; j < list.size(); j++) {
                try {
                    // debug
                    System.out.println("comparing " + list.get(i).meFile + " & " + list.get(j).meFile);

                    if (compareContent(list.get(i).meFile, list.get(j).meFile)) {
                        height0[i][j] = true;
                        height0[j][i] = true;
                        hasHome[i] = true;
                        hasHome[j] = true;
                        
                        

                        // debug(problem)
                        
                        if (list.get(i).meFile.getName().equals("mike1.txt") && list.get(j).meFile.getName().equals("mike2.txt")){
                               System.out.println("NIGGA: mike1.txt - mike2.txt");
                               System.out.println(i);
                               System.out.println(j);
                               System.out.println(height0[i][j]);
                        }
                        if (list.get(i).meFile.getName().equals("mike2.txt") && list.get(j).meFile.getName().equals("mike1.txt")){
                               System.out.println("NIGGA: mike2.txt - mike1.txt");
                               System.out.println(i);
                               System.out.println(j);
                               System.out.println(height0[i][j]);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // debug
        //GeneralFunctions.print2DArray(height0);

        allFolderLists.add(new ArrayList < FolderNode > ()); //just a spot filler for the files list
        matrices.add(height0);


        //if (1+1 == 2) {System.exit(0);}
        //****************** Compare Folders ******************//

        //i is height level. exclude the root height       
        for (int currHeight = 1; currHeight < kary.root.height; currHeight++) {

            // debug

            System.out.println("-------------------------");
            System.out.println("Height " + currHeight);



            //take all FOLDER nodes at currHeight
            ArrayList < FolderNode > currList = kary.getNodesWithHeightX(new ArrayList < FolderNode > (), currHeight, kary.root);
            allFolderLists.add(currList);


            // debug
            GeneralFunctions.printArrayList2(currList);

            hasHome = new boolean[currList.size()];
            GeneralFunctions.resetArray(hasHome, false);

            //match matrix
            boolean[][] heightx = new boolean[currList.size()][currList.size()];

            for (int i = 0; i < currList.size() - 1; i++) {

                if (hasHome[i]) continue;

                for (int j = i + 1; j < currList.size(); j++) {

                    // i is folder1, j is folder2 
                    FolderNode folderA = currList.get(i);
                    FolderNode folderB = currList.get(j);

                    //debug
                    System.out.println("comparing " + folderA.meFile + " & " + folderB.meFile);

                    //test 1: check number of files match
                    boolean test1 = folderA.files.size() == folderB.files.size();

                    //test 2: each file from folder A should correspond to a file in folder B
                    boolean test2 = true;
                    boolean[] correspondent = new boolean[folderB.files.size()];
                    GeneralFunctions.resetArray(correspondent, false);

                    //debug
                    System.out.println("correspondent.length: " + correspondent.length);
                    System.out.println("folderA.files.size(): " + folderA.files.size());

                    for (int k = 0; k < folderA.files.size(); k++) {

                        boolean fail = true;
                        for (int l = 0; l < correspondent.length; l++) {
                            if (folderA.meFile.getName().equals("bbbab") && folderB.meFile.getName().equals("bbbab")) {
                                System.out.println("k: " + k + ", l: " + l);
                                System.out.println(folderA.files.get(k).meFile.getName());
                                System.out.println(folderB.files.get(l).meFile.getName());
                                System.out.println(!correspondent[l]);
                                System.out.println(checkMatrix0(folderA.files.get(k),
                                    folderB.files.get(l), matrices.get(0), list));
            if (correspondent[l] == false && checkMatrix0(folderA.files.get(k),
                                    folderB.files.get(l), matrices.get(0), list))
                                    
                                System.out.println("therefore, match");
                            }
                            if (correspondent[l] == false && checkMatrix0(folderA.files.get(k),
                                    folderB.files.get(l), matrices.get(0), list)) {

                                correspondent[l] = true;
                                fail = false;
                                break;
                            }
                        }
                        if (fail) {
                            if (folderA.meFile.getName().equals("bbbab") && folderB.meFile.getName().equals("bbbab")) {
                                System.out.println("fail");
                            }
                            test2 = false;
                            break;
                        }
                    }


                    //test 3: number of child nodes (folders) should be equal
                    boolean test3 = folderA.children.size() == folderB.children.size();

                    //test 4: each child from folder A should correspond to a child in folder B

                    /* to compare 2 folders...
                       1. they should have same height (already done)
                       2. they will have matching matrix, so find it there.
                        //Match? reserve the 2, no Match? move onto the next.
                     */
                    boolean test4 = true;
                    correspondent = new boolean[folderB.children.size()];
                    GeneralFunctions.resetArray(correspondent, false);

                    //debug

                    System.out.println("correspondent.length: " + correspondent.length);
                    System.out.println("folderA.children.size(): " + folderA.children.size());

                    for (int k = 0; k < folderA.children.size(); k++) {

                        boolean fail = true;
                        for (int l = 0; l < correspondent.length; l++) {

                            boolean subtest1 = !correspondent[l]; //needs to be empty(false)
                            boolean subtest2 = (folderA.children.get(k).height == folderB.children.get(l).height);
                            boolean subtest3 = checkMatrixX(folderA.children.get(k),
                                folderB.children.get(l), matrices.get(folderA.children.get(k).height), allFolderLists.get(folderA.children.get(k).height));

                            if (subtest1 && subtest2 && subtest3) { //match found
                                correspondent[l] = true;
                                fail = false;
                                break;
                            }
                        }

                        if (fail) { //no match found
                            test4 = false;
                            break;
                        }
                    }

                    // debug
                    System.out.println("test 1: " + test1 + "\ntest 2: " + test2 + "\ntest 3: " + test3 + "\ntest 4: " + test4);



                    if (test1 && test2 && test3 && test4) {
                        heightx[i][j] = true;
                        heightx[j][i] = true;
                        hasHome[i] = true;
                        hasHome[j] = true;
                    }
                }
            }

            // debug
            //GeneralFunctions.print2DArray(heightx);

            matrices.add(heightx);
        }


        // debug
        //System.out.println("\n\n********************************");
        //System.out.println("matrices.size(): " + matrices.size());
        //System.out.println("allFolderLists.size(): " + allFolderLists.size());

        //****************** Print Option 1 ******************//
        if (choice == 1) {
            for (int i = matrices.size() - 1; i > 0; i--) {
                System.out.println("\nFolders of height " + i + " w/ identical content:");

                boolean[][] height_i = matrices.get(i);
                ArrayList < FolderNode > currList = allFolderLists.get(i);

                hasHome = new boolean[currList.size()];
                GeneralFunctions.resetArray(hasHome, false);

                int numMatch = 0;

                for (int j = 0; j < currList.size() - 1; j++) {

                    if (hasHome[j]) continue;

                    boolean aMatch = false;


                    for (int k = j + 1; k < currList.size(); k++) {
                        // debug
                        /*
                        System.out.println("list.size(): " + list.size());
                        System.out.println("j: " + j +"\nk: " + k);
                        System.out.println("height_i.length: " + height_i.length +"\nheight_i[j].length: " + height_i[j].length);
                        */
                        if (height_i[j][k] == true) {
                            if (aMatch == false) {
                                aMatch = true;
                                numMatch++;
                                System.out.println(" match " + (numMatch));
                                System.out.println("   >>" + currList.get(j).meFile.getPath()); //print 1st one
                            }
                            System.out.println("   >>" + currList.get(k).meFile.getPath());
                            hasHome[j] = true;
                            hasHome[k] = true;
                        }
                    }
                }
            }
        }


        //****************** Print Option 2 ******************//
        //print matching files content
        if (choice == 2) {
            hasHome = new boolean[list.size()]; // n
            GeneralFunctions.resetArray(hasHome, false);
            printFileContentMatch(height0, list, hasHome);
        }
        //****************** Compare & Print Option 3 ******************//
        //print matching files names
        if (choice == 3) {
            printFileNameMatch(list);
        }

    }



    public static void printFileContentMatch(boolean[][] matrix, ArrayList < FileNode > files, boolean[] hasHome) {
        System.out.println("\nFiles w/ identical content, regardless of name:");
        int matchesCount = 0;
        for (int i = 0; i < files.size(); i++) {
            if (hasHome[i]) {
                continue;
            }
            boolean matchinrow = false;
            for (int j = 0; j < files.size(); j++) {
                if (matrix[i][j] == true) {
                    if (!matchinrow) {
                        matchinrow = true;
                        System.out.println(" match " + (matchesCount + 1));
                        matchesCount++;
                        System.out.println("   >>" + files.get(i).meFile.getPath());
                        hasHome[i] = true;
                    }
                    System.out.println("   >>" + files.get(j).meFile.getPath());
                    hasHome[j] = true;
                }
            }
        }
        System.out.println();
    }


    public static void printFileNameMatch(ArrayList < FileNode > list) {

        ArrayList < ArrayList < ArrayList < FileNode >>> matchesName = new ArrayList < ArrayList < ArrayList < FileNode >>> ();

        boolean[] hasHome = new boolean[list.size()];
        GeneralFunctions.resetArray(hasHome, false);

        ArrayList < ArrayList < ArrayList < FileNode >>> D1 = new ArrayList < ArrayList < ArrayList < FileNode >>> ();

        for (int i = 0; i < list.size() - 1; i++) {

            if (hasHome[i]) continue;

            ArrayList < ArrayList < FileNode >> matchName = new ArrayList < ArrayList < FileNode >> ();
            ArrayList < FileNode > group1 = new ArrayList < FileNode > ();
            group1.add(list.get(i));
            matchName.add(group1);

            for (int j = i + 1; j < list.size(); j++) {
                try {

                    //if (!compareContent(list.get(i), list.get(j)) && list.get(i).getName().equals(list.get(j).getName())){
                    if (list.get(i).meFile.getName().equals(list.get(j).meFile.getName())) {
                        boolean hasGroup = false;
                        for (int k = 0; k < matchName.size(); k++) {
                            if (compareContent(matchName.get(k).get(0).meFile, list.get(j).meFile)) {
                                hasGroup = true;
                                matchName.get(k).add(list.get(j));
                            }
                        }
                        if (hasGroup == false) {
                            ArrayList < FileNode > groupN = new ArrayList < FileNode > ();
                            groupN.add(list.get(j));
                            matchName.add(groupN);
                        }
                        hasHome[i] = true;
                        hasHome[j] = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!(matchName.size() == 1 && matchName.get(0).size() == 1)) D1.add(matchName);
        }

        System.out.println("\nFiles w/ identical name, grouped by identical content:");


        for (int i = 0; i < D1.size(); i++) {


            ArrayList < ArrayList < FileNode >> names = D1.get(i);

            System.out.println(" match " + (i + 1) + ": " + names.get(0).get(0).meFile.getName());

            for (int j = 0; j < names.size(); j++) {

                System.out.println("    group " + (j + 1));

                ArrayList < FileNode > groupN = names.get(j);

                for (FileNode element: groupN) {
                    System.out.println("      >>" + element.meFile.getPath());
                }

            }

            //System.out.println();
        }

    }

    public static void insertionSort(ArrayList < FileNode > list) {
        int n = list.size();

        for (int i = 1; i < n; i++) {
            FileNode key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).meFile.getName().compareTo(key.meFile.getName()) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }
    }


    public static boolean compareContent(File f1, File f2) throws IOException {

        //check if they don't have same extension
        if (!(f1.getName().substring(f1.getName().lastIndexOf('.') + 1).equals(f2.getName().substring(f2.getName().lastIndexOf('.') + 1))))
            return false;

        //check if they are PNG/JPG/BMP, compare Pixel by Pixel
        if (f1.getName().toLowerCase().endsWith(".png") || f1.getName().toLowerCase().endsWith(".jpg") || f1.getName().toLowerCase().endsWith(".bmp"))
            return comparePixelByPixel(f1, f2);

        //compare byte by byte
        return compareByteByByte(f1, f2);
    }

    public static boolean compareByteByByte(File f1, File f2) throws IOException {


        //now compare the files Byte by Byte (given by ChatGPT)

        String filePath1 = f1.getPath();
        String filePath2 = f2.getPath();

        // Debug
        // System.out.println("comparing " + filePath1 + " and " + filePath2);

        try (FileInputStream fis1 = new FileInputStream(filePath1); FileInputStream fis2 = new FileInputStream(filePath2)) {

            int byte1;
            int byte2;

            do {
                byte1 = fis1.read();
                byte2 = fis2.read();

                if (byte1 != byte2) {

                    // Debug
                    // System.out.println("Finished comparing " + filePath1 + " and " + filePath2 + ": False\n");

                    return false;
                }

            } while (byte1 != -1 && byte2 != -1);


            // Debug
            // System.out.println("Finished comparing " + filePath1 + " and " + filePath2 + ": " + (byte1 == -1 && byte2 == -1) + "\n");

            return byte1 == -1 && byte2 == -1;
        }

    }

    public static boolean comparePixelByPixel(File f1, File f2) throws IOException {
        BufferedImage img1 = ImageIO.read(f1);
        BufferedImage img2 = ImageIO.read(f2);

        // check if ! same dimensions
        if (img1.getHeight() != img2.getHeight() ||
            img1.getWidth() != img2.getWidth()) {
            return false;
        }

        // check if colors matching, pixel by pixel
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {

                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);

                Color color1 = new Color(p1, true);
                Color color2 = new Color(p2, true);

                if (!color1.equals(color2)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkMatrix0(FileNode f1, FileNode f2, boolean[][] matrix, ArrayList < FileNode > list) {
        int i1 = 0, i2 = 0;

        
        //find their indexes in list<>
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == f1) {
                i1 = i;
            }
            if (list.get(i) == f2) {
                i2 = i;
            }
        }

        // debug(problem)
                        if (f1.meFile.getName().equals("mike1.txt") && f2.meFile.getName().equals("mike2.txt")){
                               System.out.println("mike1.txt - mike2.txt");
                               System.out.println(i1);
                               System.out.println(i2);
                             
                        }
                        if (f1.meFile.getName().equals("mike2.txt") && f2.meFile.getName().equals("mike1.txt")){
                               System.out.println("mike2.txt - mike1.txt");
                               System.out.println(i1);
                               System.out.println(i2);
                            
                        }


        if (matrix[i1][i2] || matrix[i2][i1]) {return true;}
        
        /* They may be connected indirectly via an alliance
If they are not directly connect, then, go through each match from file1, and see if THAT match matches with file2.
One of those matches must be the master that sent the rest home.
 */         
         for (int i = 0; i < list.size(); i++) {
              if (matrix[i1][i] && matrix[i][i2]){
                  return true;
              }
         }
         
         return false;

    }

    public static boolean checkMatrixX(FolderNode f1, FolderNode f2, boolean[][] matrix, ArrayList < FolderNode > currList) {
        int i1 = 0, i2 = 0;

        for (int i = 0; i < currList.size(); i++) {
            if (currList.get(i) == f1) {
                i1 = i;
            }
            if (currList.get(i) == f2) {
                i2 = i;
            }
        }

        if (matrix[i1][i2] || matrix[i2][i1]) {return true;}
        
          for (int i = 0; i < currList.size(); i++) {
              if (matrix[i1][i] && matrix[i][i2]){
                  return true;
              }
         }
         
         return false;
    }
}