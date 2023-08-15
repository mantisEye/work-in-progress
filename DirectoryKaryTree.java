//imports
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

public class DirectoryKaryTree {

    //attributes
    public FolderNode root;
    //for internal use, drawing purposes
    private FolderNode pointer;
    private int horiD, vertD;

    //constructor(s)
    public DirectoryKaryTree(File rootFolder) {
        
        this.horiD = 0;
        this.vertD = 0;
        this.pointer = null;
        this.root = buildTreeRec(rootFolder, null);

        //useless dummy node, just so that root has a parent, needed for drawTree()      
        FolderNode dummyNode = new FolderNode(rootFolder, null, null);
        dummyNode.addChildNode(root);
        this.root.parent = dummyNode;
        
        //make each node know it's height
        setHeights(root);
    }

    //other methods 
    public FolderNode buildTreeRec(File currentFolder, FolderNode parent) {
        /* at a current level */
        
        ArrayList < File > fileList = new ArrayList < File > (); //files only, not folders
        ArrayList < File > directoryList = new ArrayList < File > (); // Folders only/children
        
        for (File file: currentFolder.listFiles()) {
            if (file.isFile()) fileList.add(file);
            else if (file.isDirectory()) directoryList.add(file);
        }

        FolderNode now = new FolderNode(currentFolder, fileList, parent);

        for (File file: directoryList) {
            now.addChildNode(buildTreeRec(file, now));
        }

        return now;
    }

   //leafe node has height of 1
    private void setHeights(FolderNode node){
        node.height = nodeHeight(node);
        for (FolderNode child: node.children)
            setHeights(child);
    }
    //count leafe, root, and everything in between.
    private int nodeHeight(FolderNode node)
    {
        if (node == null)
            return 0;
            
        else {
            /* compute the height of each subtree */
            int[] heights = new int[node.children.size()];
            
            for (int i = 0; i < heights.length; i++){
               heights[i] = nodeHeight(node.children.get(i));
            }
            
            return GeneralFunctions.largest(heights)+1;
        }
    }
    

    public void drawTree() {

        System.out.println("Folder Hierarchy");
        System.out.println("----------------\n");
        
        Path currentPath = Paths.get("").toAbsolutePath();
        String currentDirectoryName = currentPath.getFileName().toString();

        this.horiD = 1;
        this.vertD = 0;
        this.pointer = root;

        System.out.println(currentDirectoryName + "(" + root.files.size() + " files)"); // //You are here

        drawRec(root);
        
        System.out.println("\n");

        resetNodeDrawnStatus(root);
    }
    
    private void drawRec(FolderNode node) { //using depth-first traversal

        if (!node.meFile.getName().equals(".")) // skip root directory
            System.out.println(node.meFile.getName() + "(" + node.files.size() + " files)");

        //if im the last child of my parent, i will set them done
        boolean imLastChild = (node == node.parent.children.get(node.parent.children.size() - 1));
        if (imLastChild) {
            node.parent.childrenDrawnComplete = true;
            //System.out.println(node.parent.meFile.getName() + " done" );
        }
        
        // Recursively process each child
        for (FolderNode child: node.children) {

            //System.out.println("child: " + child.meFile.getName());
            
            //first bone
            //get the 1st ancestor after root, see if it's its last child.
            FolderNode pointer = child;
            while(pointer.parent != root){
               pointer = pointer.parent;
            }
            if (pointer == root.getLastChild() && pointer!=child){ 
            System.out.print(" ");
            }
            else{
            System.out.print("|");
            }

            //in-between spaces or |
            // i is current dimension we're on. The root is already 1st d
            for (int i = 2; i <= horiD; i++) {

                System.out.print(" ");

                pointer = node;
                for (int j = 0; j < horiD - i; j++)
                    pointer = pointer.parent;

                if (pointer.childrenDrawnComplete) {
                    System.out.print("  ");
                } else {
                    System.out.print(" |");
                }

            }

            System.out.print("__");

            this.horiD++;
            drawRec(child);
            this.horiD--;
        }
    }


    private void resetNodeDrawnStatus(FolderNode node) { //using depth-first traversal

        node.childrenDrawnComplete = false;
        for (FolderNode child: node.children) {
            resetNodeDrawnStatus(child);
        }
    }
    
    public ArrayList<FileNode> getFileNodes(ArrayList<FileNode> list, FolderNode node){
        
        list.addAll(node.files); 
        for (FolderNode child: node.children) {
            list = getFileNodes(list, child);
        }
        return list;
    }
    
     //return all file nodes combined
     public ArrayList<FolderNode> getNodesWithHeightX(ArrayList<FolderNode> ret, int height, FolderNode node){
        
        if (node.height == height){ret.add(node);}
        for (FolderNode child: node.children)
            getNodesWithHeightX(ret, height, child);
            
        return ret;
     }
    


}


//*************************** J U N K *******************************//

/*
        if (imLastChild){
              // if (node.children.size() == 0 ) 
        //System.out.println("child: " + child.meFile.getName());
             System.out.print("|");
             
            //in-between spaces or |
            // i is current dimension we're on. The root is already 1st d
            for (int i = 2; i <= horiD; i++) {
              
               System.out.print(" ");
               
               pointer = node;
               for (int j = 0; j < horiD - i; j++)
                  pointer = pointer.parent;
               
               if (pointer.childrenDrawnComplete){ System.out.print(" "); }
               else { System.out.print("|"); }
                  
            }
            
            System.out.println("");

        }
        */
/*
    private FolderNode<T> root;
    //private int maxChildCount;

    public KaryTree(T data, int maxChildCount) {
        this.root = new FolderNode<>(data, maxChildCount);
        this.maxChildCount = maxChildCount;
    }

    public FolderNode<T> getRoot() {
        return root;
    }

    public void addChild(FolderNode<T> parent, T data) {
        if (parent.childCount < maxChildCount) {
            FolderNode<T> child = new FolderNode<>(data, maxChildCount);
            parent.children[parent.childCount] = child;
            parent.childCount++;
        } else {
            throw new IllegalArgumentException("Maximum child count exceeded for parent node.");
        }
    }

    public FolderNode<T>[] getChildren(FolderNode<T> node) {
        return node.children;
    }
    
    */


/*
            //System.out.print(" ");
        
        //bbb will think: I will stop drawing last bone if there is no siblings after me
        
        //                                                          a.k.a im last child.
        
        // so for every D bone piece, we need to ? parent to check if last child ? 
        
        // every node strictly draws thier children's lines
        
          
        for (int i = 0; i < horiD; i++) {
           
           System.out.print(" ");
           else System.out.print("|");
           
           System.out.print(" ");
        }
      
    
        //System.out.print("|");
        
    */
    
    
    /* this one works before changing how to set up the parent
        public FolderNode buildTreeRec(File currentFolder, FolderNode parent) {

        ArrayList < File > fileList = new ArrayList < File > (); //files only, not folders
        ArrayList < File > directoryList = new ArrayList < File > (); // Folders only/children
        
        for (File file: currentFolder.listFiles()) {
            if (file.isFile()) fileList.add(file);
            else if (file.isDirectory()) directoryList.add(file);
        }

        FolderNode now = new FolderNode(currentFolder, fileList, parent);

        for (File file: directoryList) {
            now.addChildNode(buildTreeRec(file, now));
        }

        return now;
    }
    */