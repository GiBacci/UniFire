package bacci.giovanni.bio.sequencing.manipulation;

import bacci.giovanni.bio.sequencing.pull.FrequencyPullStreamer;

/**
 * Sequence manipulator able to convert an unformatted Sequence into a fasta
 * formatted sequence using the
 * {@link SequenceManipulationStrategy#manipulate(String, String)} method
 * implemented in the strategy pattern.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastaManipulationStrategy implements SequenceManipulationStrategy {

	public String manipulate(String id, String sequence) {
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
				buffer.append(System.lineSeparator()
						+ seq.substring(seq.length() - rest));
			}
			seq = new String(buffer.toString());
		}
		return String.format("%1$s%n%2$s%n", new Object[] { id, seq });
	}

}
