package bacci.giovanni.bio.sequencing.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import bacci.giovanni.bio.sequencing.manipulation.SequenceManipulationStrategy;
import bacci.giovanni.bio.sequencing.pull.SequencePullStreamer;

/**
 * Object that streams a Fasta sequence file and call the
 * {@link SequencePullStreamer#pull(String, Object...)} method every time that
 * it finds a sequence. This class will pass 2 additional args to the pull
 * method:<br>
 * 1) A <code>long</code> that is the pointer of the sequence in the
 * {@link RandomAccessFile}<br>
 * 2) The {@link RandomAccessFile} that points to the sequence<br>
 * 3) A tag for the sequences as a {@link String}<br>
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class RandomFastaSequenceStreamer implements SequenceStreamer {

	private RandomAccessFile raf = null;
	private SequencePullStreamer sps = null;
	private String tag = null;
	private SequenceManipulationStrategy strategy = null;
	private List<StreamerObserver> observers = null;
	private long seqNum = 0;

	/**
	 * Constructor
	 * 
	 * @param sps
	 *            the {@link SequencePullStreamer} to use in the
	 *            {@link RandomFastaSequenceStreamer#stream()} method
	 */
	public RandomFastaSequenceStreamer(SequencePullStreamer sps) {
		this.sps = sps;
		this.observers = new ArrayList<StreamerObserver>();
	}

	/**
	 * This method set the {@link RandomAccessFile} to stream
	 * 
	 * @param raf
	 *            the {@link RandomAccessFile}
	 */
	public void setRaf(RandomAccessFile raf) {
		this.raf = raf;
	}

	/**
	 * This method set a tag associated with the stream
	 * 
	 * @param tag
	 *            a String
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	public void stream() throws IOException {
		if (raf == null) {
			throw new IOException("No RandomAccessFile set");
		} else {
			String line = null;
			StringBuffer buffer = null;
			long position = raf.getFilePointer();
			long point = position;
			while ((line = raf.readLine()) != null) {
				if (line.startsWith(">")) {
					if (buffer != null) {																			
						pull(buffer.toString(), point, raf, tag);
						point = position;
					}
					buffer = new StringBuffer();
				} else if ((!line.startsWith(">")) && (buffer != null)) {
					buffer.append(line);
					position = raf.getFilePointer();
				}
			}
			if (buffer != null) {
				pull(buffer.toString(), point, raf, tag);
			}

		}

	}
	
	private void pull(String seq, long point, RandomAccessFile raf, String tag){
		if (strategy != null) {
			sps.pull(strategy.manipulate(null,
					seq), point, raf, tag);			
		} else {
			sps.pull(seq, point, raf, tag);
		}
		seqNum++;
		this.notifyObservers(seqNum);
	}

	public void setSequenceManipulationStrategy(
			SequenceManipulationStrategy strategy) {
		this.strategy = strategy;

	}

	public void addObserver(StreamerObserver observer) {
		if(observers.indexOf(observer) < 0){
			observers.add(observer);
		}		
	}

	public void removeObserver(StreamerObserver observer) {
		int index = observers.indexOf(observer);
		if(index >= 0){
			observers.remove(index);
		}		
	}

	public void notifyObservers(long seqNum) {
		for(StreamerObserver obs : observers){
			obs.update(seqNum);
		}
		
	}

}
