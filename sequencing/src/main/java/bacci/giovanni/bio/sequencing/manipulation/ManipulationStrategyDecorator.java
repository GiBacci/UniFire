package bacci.giovanni.bio.sequencing.manipulation;

/**
 * {@link SequenceManipulationStrategy} decorator. This class use the decorator pattern to 
 * secoratre instances of {@link SequenceManipulationStrategy}.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public abstract class ManipulationStrategyDecorator implements SequenceManipulationStrategy {
	
	private SequenceManipulationStrategy basicStrategy = null;

	/**
	 * Constructor that follows the Decorator pattern.
	 * @param basicStrategy
	 */
	public ManipulationStrategyDecorator(
			SequenceManipulationStrategy basicStrategy) {
		this.basicStrategy = basicStrategy;
	}

	public String manipulate(String id, String sequence) {		
		return basicStrategy.manipulate(id, sequence);
	}		
	
	
}
