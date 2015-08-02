package npu.support.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 
 * The purpose of this file are: allow copy or move a file
 * reference from: http://www.java2s.com/Code/Java/File-Input-Output/MoveaFile.htm
 */

public class FileTools {
	  public static boolean copyFile(File source, File dest){
          try{
                  // Declaration et ouverture des flux
                  java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);

                  try{
                          java.io.FileOutputStream destinationFile = null;

                          try{
                                  destinationFile = new FileOutputStream(dest);

                                  // Lecture par segment de 0.5Mo
                                  byte buffer[] = new byte[512 * 1024];
                                  int nbLecture;

                                  while ((nbLecture = sourceFile.read(buffer)) != -1){
                                          destinationFile.write(buffer, 0, nbLecture);
                                  }
                          } finally {
                                  destinationFile.close();
                          }
                  } finally {
                          sourceFile.close();
                  }
          } catch (IOException e){
                  e.printStackTrace();
                  return false; // Erreur
          }

          return true; // Result OK
  }


  /**
   * D le fichier source dans le fichier rsultat
   */
  public static boolean moveFile(File source,File destination) {
          if( !destination.exists() ) {
                  // On essaye avec renameTo
                  boolean result = source.renameTo(destination);
                  if( !result ) {
                          // On essaye de copier
                          result = true;
                          result &= copyFile(source,destination);
                          if(result) result &= source.delete();

                  } return(result);
          } else {
                  // Si le fichier destination existe, on annule ...
                  return(false);
          }
  }
}// end class
