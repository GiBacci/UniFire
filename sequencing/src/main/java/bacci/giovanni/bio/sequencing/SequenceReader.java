package bacci.giovanni.bio.sequencing;

/**
 * An Object that reads sequences as String. Doesn't matter how the sequences are reported. 
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface SequenceReader {
	/**
	 * Returns the next sequence in a sequences file as a String
	 * @return the next sequence in a file
	 */
	public String readSequence();
	
	/**
	 * Close the link to the file.
	 */
	public void close();
}
