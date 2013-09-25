package bacci.giovanni.bio.sequencing.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that wraps a {@link BufferedWriter} instance and uses it for writing a frequency table.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class FrequencyTableWriter {
	private BufferedWriter writer = null;
	private List<String> tags = null;
	private String separator = "\t";

	/**
	 * Constructor.
	 * @param writer wrapped {@link BufferedWriter}
	 */
	public FrequencyTableWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	/**
	 * Set the tags used to recreate the frequency table. Each tag will be used as
	 * column name.
	 * @param tags a set of unique tags
	 */
	public void setTags(Set<String> tags) {
		this.tags = new ArrayList<String>(tags);
	}
	
	/**
	 * Write the header of the table using the tags as column names.
	 * @throws IOException if an IO Error occurs writing in the file
	 */
	public void writeHeader() throws IOException{
		writer.write("id");
		Collections.sort(tags);
		for(String s : tags){
			writer.write(separator + s);
		}
		writer.newLine();
		writer.flush();
	}
	
	/**
	 * Write a row in the frequency table.
	 * @param id the id of the entry
	 * @param entry a {@link Map} with <code>String</code> as keys. Each key has to be a tag. 
	 * @throws IOException if an IO Error occurs writing in the file
	 */
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

	/**
	 * Set the separator for each cell of the table.
	 * @param separator a <code>String</code>
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
}
