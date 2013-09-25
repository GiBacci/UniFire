package bacci.giovanni.bio.sequencing.manipulation;

/**
 * Dummy class that does nothing neither on id nor on sequence. This class doesn't follow the Decorator pattern
 * and it can be used as last member of a decoration.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class NoManipulationStrategy implements SequenceManipulationStrategy {

	/**
	 * This method accepts also <code>null</code> arguments.
	 */
	public String manipulate(String id, String sequence) {
		if(id == null){
			return sequence;
		} else if(sequence == null){
			return id;
		}else{
			return id + sequence;
		}
	}

}
