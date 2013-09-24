package bacci.giovanni.bio.sequencing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrequencyTableWriter {
	private BufferedWriter writer = null;
	private List<String> tags = null;
	private String separator = ";";

	public FrequencyTableWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	public void setTags(Set<String> tags) {
		this.tags = new ArrayList<String>(tags);
	}
	
	public void writeHeader() throws IOException{
		writer.write("id");
		Collections.sort(tags);
		for(String s : tags){
			writer.write(separator + s);
		}
		writer.newLine();
		writer.flush();
	}
	
	public void write(String id, Map<String, Integer> entry) throws IOException{
		writer.write(id);
		for(String s : tags){
			if(entry.get(s) != null){
				writer.write(separator + entry.get(s));
			}else{
				writer.write(separator + 0);
			}
		}
		writer.newLine();
		writer.flush();
	}
	
}
