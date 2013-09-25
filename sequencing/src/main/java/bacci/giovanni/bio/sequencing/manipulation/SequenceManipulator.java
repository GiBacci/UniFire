package bacci.giovanni.bio.sequencing.manipulation;

/**
 * Object able to manipulate sequences using a {@link SequenceManipulationStrategy}. This interface
 * has only one method that set a {@link SequenceManipulationStrategy}. 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface SequenceManipulator {
	
	/**
	 * Setter method for {@link SequenceManipulationStrategy}.
	 * @param strategy the strategy
	 */
	public void setSequenceManipulationStrategy(SequenceManipulationStrategy strategy);
}
