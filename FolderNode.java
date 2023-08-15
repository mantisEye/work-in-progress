//to clarify confusion, in context of java, a 'File' could be both as a file with an extension (txt, png, java), 
//and it could mean a folder.

import java.util.*;
import java.io.File;

class FolderNode {

    //attributes
    public File meFile; // pointer to the File represented by this object
    public ArrayList<FileNode> files; // files only, no directories/folders
    public ArrayList<FolderNode> children; // directories only
    public FolderNode parent;
    public int height; // what's the diff between 'depth' and 'height'?
    public boolean childrenDrawnComplete; // for drawTree() in DirectoryKaryTree.java

    //constructor(s)
    public FolderNode(File meFile, ArrayList<File> files, FolderNode parent){
        this.meFile = meFile;
        
        if (files != null){
        this.files = new ArrayList<FileNode>();
        for (File file: files){
            this.files.add(new  FileNode(file, this));
        }} else { this.files = null; }

        this.children = new ArrayList<FolderNode>();
        this.parent = parent;
        this.height = 0;
        this.childrenDrawnComplete = false;
    }
    
    
    //other useful methods 
    public void addChildNode(FolderNode folderChild){
       children.add(folderChild);
       
       //maybe I can set the parent here instead of there
       
       //folderChild.parent = this;
    }
    
    public FolderNode getLastChild(){
      if (children.size() > 0){
            return children.get(children.size()-1);
         }
      return null;
    }
    
    @Override
    public String toString(){
      //I could do path directory as well
      return meFile.getName();
    }
}