package bacci.giovanni.bio.sequencing;

import java.io.IOException;

/**
 * Object that streams a series of sequences one by one.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface SequenceStreamer {

	/**
	 * Streams all the sequences in a file or in a list of sequences. 
	 * @throws IOException if an IO error occurs
	 */
	public void stream() throws IOException;
}
