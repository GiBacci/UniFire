package bacci.giovanni.bio.sequencing.manipulation;

/**
 * Class that replace each <code>U</code> character with a <code>T</code> character in a sequence.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class UracileConverterDecorator extends ManipulationStrategyDecorator {

	public UracileConverterDecorator(SequenceManipulationStrategy basicStrategy) {		
		super(basicStrategy);		
	}

	@Override
	public String manipulate(String id, String sequence) {
		return super.manipulate(id, sequence.toUpperCase().replace('U', 'T'));
	}
	
	

}
