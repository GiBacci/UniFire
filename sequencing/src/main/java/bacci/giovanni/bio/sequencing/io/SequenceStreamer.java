package bacci.giovanni.bio.sequencing.io;

import java.io.IOException;

import bacci.giovanni.bio.sequencing.manipulation.SequenceManipulator;

/**
 * Object that streams a series of sequences one by one. This interface supports
 * the {@link SequenceManipulator} interface designed using the Strategy pattern.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface SequenceStreamer extends SequenceManipulator {

	/**
	 * Streams all the sequences in a file or in a list of sequences.
	 * 
	 * @throws IOException
	 *             if an IO error occurs
	 */
	public void stream() throws IOException;
	
	public void addObserver(StreamerObserver observer);
	
	public void removeObserver(StreamerObserver observer);
	
	public void notifyObservers(long seqNum);
}
