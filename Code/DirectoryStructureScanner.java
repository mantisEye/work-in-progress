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

class DirectoryStructureScanner {

    public static void main(String[] args) {

        DirectoryKaryTree kary = new DirectoryKaryTree(new File("."));

        kary.drawTree();

        ArrayList < FileNode > list = kary.getFileNodes(new ArrayList < FileNode > (), kary.root);
        
        System.out.println("Total Files: " + list.size() + "\n");
        
        
        
        ArrayList <boolean[][]> contentMatch = new  ArrayList <boolean[][]> ();

        boolean[][] depth0 = new boolean [list.size()][list.size()];

        //set of sets | matches:
        //1 - Same content, sorted by name
        ArrayList < ArrayList < FileNode >> matchesContent = new ArrayList < ArrayList < FileNode >> ();

        //printArrayList(list);
        //matrixes for comparisons
        //boolean[][] matchingNames = new boolean[list.size()][list.size()];
        //boolean[][] matchingCotnent = new boolean[list.size()][list.size()];

        //corresponds to each element in list. If a file belongs to any match, it beocmes true
        boolean[] hasHome = new boolean[list.size()];
        GeneralFunctions.resetArray(hasHome, false);

        
        for (int i = 0; i < list.size() - 1; i++) {
            //System.out.println(i + " " + list.get(i).getName());
            if (hasHome[i]) continue;

            ArrayList < FileNode > match = new ArrayList < FileNode > ();
            match.add(list.get(i));

            for (int j = i + 1; j < list.size(); j++) {
                try {
                    if (compareContent(list.get(i).meFile, list.get(j).meFile)) {
                        match.add(list.get(j));
                        //depth0[i][j] = true;
                        hasHome[i] = true;
                        hasHome[j] = true;
                        
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (match.size() > 1) matchesContent.add(match);
        }

        //now sort each set based on name repetition. 

        System.out.println("Files w/ identical content, regardless of name:\n");
        printMatches1(matchesContent);

        //System.exit(0);

        ///////////////////////////lets do same name but diff content////////////////////////////////////////////////


        //2 - Same name, grouped by content
        //set of sets / sets of groups / groups are sets, so 3D arraylists. 
        ArrayList < ArrayList < ArrayList < FileNode >>> matchesName = new ArrayList < ArrayList < ArrayList < FileNode >>> ();

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



        //System.out.println("//*********************** FINDINGS ***********************//\n");




        System.out.println("Files w/ identical name, grouped by identical content:\n");

        printMatches2(D1);
        
        
        /////////////////////////// NOW FOLDERS ////////////////////////////////////////////////
        
        //take all child nodes. 
        //compare each one with other, not including last one
            //check number of files match, if so, 
            //each file from folder one should correspond to a file in folder 2
        //Match? put them in an array and continue.
        //you gonna end up with 2d arraylist for content matches of depth 1 folders
        
        //then put all folders with depth 2 in an arraylist
        //repeat the above for files
        // for folders, you need to find a match for each, 
            //to compare 2 folders, first they should have same depth, then...
            //they will have matching matrix, so find it there.
        //Match? reserve the 2, no Match? move onto the next.

    }

    /*
    public static void printArrayList(ArrayList < FileNode > list) {
        for (FileNode element: list) {
            System.out.println(element.getName());
        }
    }
    */



    public static void printMatches1(ArrayList < ArrayList < FileNode >> list) {
        for (int i = 0; i < list.size(); i++) {
            ArrayList < FileNode > row = list.get(i);
            insertionSort(row);
            System.out.println(" match " + (i + 1));
            for (FileNode element: row) {
                System.out.println("   >>" + element.meFile.getPath());
            }
            System.out.println();
        }
    }


    public static void printMatches2(ArrayList < ArrayList < ArrayList < FileNode >>> list) {

        for (int i = 0; i < list.size(); i++) {


            ArrayList < ArrayList < FileNode >> names = list.get(i);

            System.out.println(" match " + (i + 1) + ": " + names.get(0).get(0).meFile.getName());

            for (int j = 0; j < names.size(); j++) {

                System.out.println("    group " + (j + 1));

                ArrayList < FileNode > groupN = names.get(j);

                for (FileNode element: groupN) {
                    System.out.println("      >>" + element.meFile.getPath());
                }

            }
            
            System.out.println();
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

        //check if they are PNG, compare Pixel by Pixel
        if (f1.getName().toLowerCase().endsWith(".png"))
            return comparePixelByPixel(f1, f2);

        //compare byte by byte
        return compareByteByByte(f1, f2);
    }

    public static boolean compareByteByByte(File f1, File f2) throws IOException {


        //now compare the files Byte by Byte (given by ChatGPT)

        String filePath1 = f1.getPath();
        String filePath2 = f2.getPath();

        // Debug
        //System.out.println("comparing " + filePath1 + " and " + filePath2);

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
}