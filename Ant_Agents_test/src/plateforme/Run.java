package plateforme;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import entite.Ant;

public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ClassLoader classLoader = Ant.class.getClassLoader();
		    File classpathRoot = new File(classLoader.getResource("").getPath());
			File root = new File(classpathRoot.getParent());
			File[] listOfFiles = root.listFiles();

			String[] ext = new String[]{"txt","ant"};
			for (File file : listOfFiles) {
			       if(FilenameUtils.getExtension(file.getName()).equals("txt") || 
			    	  FilenameUtils.getExtension(file.getName()).equals("ant") || 
			    	  file.getName().equals("bestants")){
			    	   if(file.isFile()){
			    		   file.delete();
			    	   }else{
			    		  // FileUtils.deleteDirectory(file);
			    	   }
			       }
			}
			
			new main();
			new contoneur();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
