package bacci.giovanni.bio.sequencing.io;

import java.io.BufferedWriter;
import java.io.IOException;

import bacci.giovanni.bio.sequencing.manipulation.SequenceManipulationStrategy;
import bacci.giovanni.bio.sequencing.pull.FrequencyPullStreamer;

/**
 * Writer able to write a sequence and ad id into a file.
 * This is a wrapper class for a {@link BufferedWriter}.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class SimpleSequenceWriter implements SequenceWriter {
	private BufferedWriter writer = null;
	private SequenceManipulationStrategy manipulationStrategy = null;

	/**
	 * Constructor.
	 * 
	 * @param writer
	 *            {@link BufferedWriter} wrapped in the object
	 */
	public SimpleSequenceWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	/**
	 * Main method that write a sequence in a sequence file. The sequence will
	 * be cleaned using the {@link FrequencyPullStreamer#cleanSequence(String)}
	 * method.
	 */
	public void writeSequence(String id, String sequence) throws IOException {
		String seqString = null;
		if (manipulationStrategy == null) {
			this.writer.write(id);
			this.writer.newLine();
			this.writer.write(sequence);
			this.writer.newLine();
		} else {
			seqString = manipulationStrategy.manipulate(id, sequence);
			this.writer.write(seqString);
		}
		this.writer.flush();
	}

	/**
	 * Sets a {@link SequenceManipulationStrategy} in order to modify the sequences
	 * while they are read
	 */
	public void setSequenceManipulationStrategy(
			SequenceManipulationStrategy strategy) {
		this.manipulationStrategy = strategy;
	}

}
