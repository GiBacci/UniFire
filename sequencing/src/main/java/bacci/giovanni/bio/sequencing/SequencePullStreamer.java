package bacci.giovanni.bio.sequencing;

/**
 * Object that work on sequences. The {@link SequencePullStreamer#pull(String, Object...)} method 
 * has to be used inside the {@link SequenceStreamer#stream()} method  
 * in order to "pull" informations about each sequence
 * streamed.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface SequencePullStreamer {
	
	/**
	 * Pull a sequence as String an other object/s from a {@link SequenceStreamer}
	 * @param sequence the sequence as a string 
	 * @param obj argument/s to pass to this object every time that a sequence is found
	 */
	public void pull(String sequence, Object... obj);
}
