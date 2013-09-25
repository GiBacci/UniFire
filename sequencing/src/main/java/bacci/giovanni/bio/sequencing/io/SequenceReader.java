package bacci.giovanni.bio.sequencing.io;

import bacci.giovanni.bio.sequencing.manipulation.SequenceManipulator;

/**
 * An Object that reads sequences as String. Doesn't matter how the sequences
 * are reported. This interface supports the {@link SequenceManipulator} interface
 * designed using the Strategy pattern.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface SequenceReader extends SequenceManipulator {
	/**
	 * Returns the next sequence in a sequences file as a String
	 * 
	 * @return the next sequence in a file
	 */
	public String readSequence();

	/**
	 * Close the link to the file.
	 */
	public void close();
}
