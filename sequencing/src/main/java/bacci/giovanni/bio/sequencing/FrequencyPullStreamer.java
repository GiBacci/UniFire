package bacci.giovanni.bio.sequencing;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Object that store each sequence in a frequency table using a {@link FilePointerContainer} for each
 * file. This class can be used with these {@link SequenceStreamer}:<br>
 * {@link RandomFastaSequenceStreamer}<br>
 * 
 * This class convert each sequence passed through the {@link FrequencyPullStreamer#pull(String, Object...)} method
 * into an hashCode. If you are planning to pass more than 2^32 unique sequences this class may generate "String collision". 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class FrequencyPullStreamer extends SequencePullStreamerDecorator implements Iterable<Collection<FilePointerContainer>> {	
	
	private Map<Integer, Collection<FilePointerContainer>> entryMap = null;
	private long sequencesNumber = 0;
	private Set<String> tagSet = null;
	
	
	/**
	 * Standard constructor. All fields will be initialized here.
	 * @param stream a {@link SequencePullStreamer}. Standard parameter for the Decorator pattern
	 */
	public FrequencyPullStreamer(SequencePullStreamer stream) {
		super(stream);
		this.entryMap = new HashMap<Integer, Collection<FilePointerContainer>>();
		this.tagSet = new LinkedHashSet<String>();
	}

	/**
	 * This implementation of the <code>pull</code> method needs 3 elements. For
	 * more information see the {@link RandomFastaSequenceStreamer} description
	 * @see RandomFastaSequenceStreamer
	 */
	public void pull(String sequence, Object... obj) {
		super.pull(sequence, obj);
		sequencesNumber++;
		String cleanedSequence = cleanSequence(sequence);
		long pointer = (Long)obj[0];
		RandomAccessFile raf = (RandomAccessFile)obj[1];
		String tag = (String)obj[2];
		tagSet.add(tag);
		if(entryMap.containsKey(cleanedSequence.hashCode())){
			boolean found = false;
			for(FilePointerContainer fpc : entryMap.get(cleanedSequence.hashCode())){
				if(fpc.getRaf().equals(raf)){
					fpc.addPointer(pointer);
					found = true;
					break;
				}				
			}
			if(!found){
				FilePointerContainer fpc = new FilePointerContainer(raf, tag);
				fpc.addPointer(pointer);
				entryMap.get(cleanedSequence.hashCode()).add(fpc);
			}
		}else{
			Collection<FilePointerContainer> collection = new ArrayList<FilePointerContainer>();
			FilePointerContainer fpc = new FilePointerContainer(raf, tag);
			fpc.addPointer(pointer);
			collection.add(fpc);
			entryMap.put(cleanedSequence.hashCode(), collection);
		}
	}
	
	/**
	 * This method return the count of unique sequences in this {@link FrequencyPullStreamer}
	 * @return the number of unique sequences.
	 */
	public int getUniqueCount(){
		return entryMap.size();
	}
	
	/**
	 * Implementation of the {@link Iterable#iterator()} method.
	 */
	public Iterator<Collection<FilePointerContainer>> iterator() {
		return entryMap.values().iterator();
	}

	/**
	 * This method returns the number of sequences analyzed using this class.
	 * @return the total number of sequences
	 */
	public long getSequencesNumber() {
		return sequencesNumber;
	}

	public Set<String> getTagSet() {
		return tagSet;
	}
	
	public static String cleanSequence(String sequence){
		return sequence.trim().replaceAll("\\s", "").toLowerCase();
	}
}
