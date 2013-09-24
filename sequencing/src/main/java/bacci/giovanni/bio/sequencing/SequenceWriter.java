package bacci.giovanni.bio.sequencing;

import java.io.IOException;
/**
 * Object able to write a sequence in a file given the id and the sequence.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface SequenceWriter {
	/**
	 * This method write a sequence in a file.
	 * @param id the id of the sequence
	 * @param sequence the sequence
	 * @throws IOException if abn IO Error occurs
	 */
	public void writeSequence(String id, String sequence) throws IOException;
}
