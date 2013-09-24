package bacci.giovanni.bio.sequencing;

/**
 * Decorator class for {@link SequencePullStreamer} implementations.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public abstract class SequencePullStreamerDecorator implements
		SequencePullStreamer {

	private SequencePullStreamer streamer = null;

	/**
	 * Standard constructor for decorator pattern.
	 * 
	 * @param streamer
	 *            a {@link SequencePullStreamer}
	 */
	public SequencePullStreamerDecorator(SequencePullStreamer streamer) {
		this.streamer = streamer;
	}

	/**
	 * Decorator pottern implementation of the
	 * {@link SequencePullStreamer#pull(String, Object...)} method
	 */
	public void pull(String sequence, Object... obj) {
		streamer.pull(sequence, obj);
	}

}
