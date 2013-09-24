package bacci.giovanni.bio.sequencing;

import java.io.IOException;
import java.io.RandomAccessFile;

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

	/**
	 * Constructor
	 * 
	 * @param sps
	 *            the {@link SequencePullStreamer} to use in the
	 *            {@link RandomFastaSequenceStreamer#stream()} method
	 */
	public RandomFastaSequenceStreamer(SequencePullStreamer sps) {
		this.sps = sps;
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
						sps.pull(buffer.toString(), point, raf, tag);
						point = position;
					}
					buffer = new StringBuffer();
				} else if ((!line.startsWith(">")) && (buffer != null)) {
					buffer.append(line);
					position = raf.getFilePointer();
				}
			}
			if(buffer != null){
				sps.pull(buffer.toString(), point, raf, tag);
			}

		}

	}

}
