//imports
import java.util.*;
import java.io.File;

class FileNode {

   //attributes
   public File meFile; //no 2 nodes will have same meFile; unique.
   public FolderNode home;

   //constructor(s)
   public FileNode(File meFile, FolderNode home){
      this.meFile = meFile;
      this.home = home;
   }
}