package it.ermes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Persistence {
	
	public Persistence() {
		super();

	}

	public String obtainProject() throws IOException{
		
		String out="";
		out=readFile("project.txt", Charset.defaultCharset());
		System.out.println(">>>>>>>>>>>>>>>>JSON="+out);
		return out;
	}
public String obtainGrid() throws IOException{
		
		String out="";
		out=readFile("/home/virtual/workspace/RESTServer/src/grid.txt", Charset.defaultCharset());
		System.out.println(">>>>>>>>>>>>>>>>JSON="+out);
		return out;
	}
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

}
