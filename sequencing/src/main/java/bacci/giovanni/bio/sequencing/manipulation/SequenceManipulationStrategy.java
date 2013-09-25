package bacci.giovanni.bio.sequencing.manipulation;

/**
 * Object able to manipulate a Sequence and generate a single <code>String</code>.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface SequenceManipulationStrategy {
	
	/**
	 * The manipulation method.
	 * @param id the sequence id
	 * @param sequence the sequence
	 * @return a <code>String</code> 
	 */
	public String manipulate(String id, String sequence);
}
