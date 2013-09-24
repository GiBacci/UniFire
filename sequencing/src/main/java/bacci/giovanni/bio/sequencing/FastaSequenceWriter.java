package bacci.giovanni.bio.sequencing;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Writer able to write a sequence and ad id into a file respecting the fasta format.
 * This is a wrapper class for a {@link BufferedWriter}.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class FastaSequenceWriter implements SequenceWriter {
	private BufferedWriter writer = null;

	/**
	 * Constructor. 
	 * @param writer {@link BufferedWriter} wrapped in the object
	 */
	public FastaSequenceWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	/**
	 * Main method that write a sequence in a sequence file. The sequence will be cleaned
	 * using the {@link FrequencyPullStreamer#cleanSequence(String)} method.
	 */
	public void writeSequence(String id, String sequence) throws IOException {
		String seq = FrequencyPullStreamer.cleanSequence(sequence)
				.toUpperCase();
		if (seq.length() > 70) {
			StringBuffer buffer = new StringBuffer();
			int pieces = seq.length() / 70;
			int rest = seq.length() % 70;
			for (int i = 0; i < pieces; i++) {
				int begin = i * 70;
				int end = begin + 70;
				if (i < pieces - 1) {
					buffer.append(seq.substring(begin, end)
							+ System.lineSeparator());
				} else {
					buffer.append(seq.substring(begin, end));
				}
			}
			if (rest > 0) {
				buffer.append(System.lineSeparator() + seq.substring(seq.length() - rest));
			}
			seq = new String(buffer.toString());
		}
		String seqString = String.format("%1$s%n%2$s%n",
				new Object[] { id, seq });
		this.writer.write(seqString);
		this.writer.flush();
	}

}
