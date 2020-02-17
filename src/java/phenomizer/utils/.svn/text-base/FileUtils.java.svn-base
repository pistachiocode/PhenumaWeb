/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenomizer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import phenomizer.hpo.Phenuma;
/**
 *
 * @author Armando
 */
public class FileUtils {
    
    /**
     * Create folder in the temporal folder
     * 
     * @param pathName 
     */
    public static void createDirectory(File dir)
    {
        Phenuma p = Phenuma.getInstance();
        System.out.println("Creating "+dir.getName()+" in "+dir.getPath());
        dir.mkdir();    
    }
    

    public static void deleteDirectory(File dir)
    {

        if(dir.isDirectory()){
 
            //directory is empty, then delete it
            if(dir.list().length==0){
                
               dir.delete();

            }else{

               //list all the directory contents
               String files[] = dir.list();

               for (String temp : files) 
               {
                  //construct the file structure
                  File fileDelete = new File(dir, temp);

                  //recursive delete
                  deleteDirectory(fileDelete);
               }

               //check the directory again, if empty then delete it
               if(dir.list().length==0)
                 dir.delete();
               
            }
 
    	}else{
            //if file, then delete it
            dir.delete();
    	}
        
    }
    
    /**
     * This method creates a zip file from the content of a directory.
     * 
     * @param dir
     * @return 
     */
    public static ZipOutputStream createZipFromDir(File dir)
    {
        ZipOutputStream zos = null;
        try {
            
            zos = new ZipOutputStream(new FileOutputStream(dir.getPath()+".zip"));
            
            //list all the directory contents
            String files[] = dir.list();

            for (String temp : files) 
            {
                zos.putNextEntry(new ZipEntry(temp));
                
                BufferedReader br = new BufferedReader(new FileReader(dir+"/"+temp));
               
                String str = br.readLine();
                while(str!=null)
                {
                    str = str+"\n";
                    zos.write(str.getBytes(), 0, str.getBytes().length);
                    str = br.readLine();
                }
                br.close();
                
                zos.closeEntry();
            }
     
            zos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return zos;
    }
    
    
}
