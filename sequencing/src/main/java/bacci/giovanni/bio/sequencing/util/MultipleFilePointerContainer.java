package bacci.giovanni.bio.sequencing.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultipleFilePointerContainer implements Iterable<FilePointerContainer>{
	
	private List<FilePointerContainer> uniques = null;

	public MultipleFilePointerContainer() {
		this.uniques = new ArrayList<FilePointerContainer>();
	}
	
	public void add(FilePointerContainer f){
		if(uniques.indexOf(f) < 0){
			uniques.add(f);
		}
	}
	
	public String getSequence() throws IOException{
		if(uniques.size() > 0){
			FilePointerContainer c = uniques.get(0);
			if(c.size() > 0){
				return c.getSequence(0);
			}
		}
		return null;
	}

	public Iterator<FilePointerContainer> iterator() {		
		return uniques.iterator();
	}
	
	

}
