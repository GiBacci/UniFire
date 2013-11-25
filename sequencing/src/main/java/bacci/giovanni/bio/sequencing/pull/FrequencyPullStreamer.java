package bacci.giovanni.bio.sequencing.pull;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bacci.giovanni.bio.sequencing.io.RandomFastaSequenceStreamer;
import bacci.giovanni.bio.sequencing.io.SequenceStreamer;
import bacci.giovanni.bio.sequencing.util.FilePointerContainer;
import bacci.giovanni.bio.sequencing.util.DigestedSequence;

/**
 * Object that store each sequence in a frequency table using a
 * {@link FilePointerContainer} for each file. This class can be used with these
 * {@link SequenceStreamer}:<br>
 * {@link RandomFastaSequenceStreamer}<br>
 * 
 * This class convert each sequence passed through the
 * {@link FrequencyPullStreamer#pull(String, Object...)} method into an
 * hashCode. If you are planning to pass more than 2^32 unique sequences this
 * class may generate "String collision".
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FrequencyPullStreamer extends SequencePullStreamerDecorator
		implements Iterable<List<FilePointerContainer>> {

	private Map<DigestedSequence, List<FilePointerContainer>> entryMap = null;
	private long sequencesNumber = 0;
	private Set<String> tagSet = null;

	/**
	 * Standard constructor. All fields will be initialized here.
	 * 
	 * @param stream
	 *            a {@link SequencePullStreamer}. Standard parameter for the
	 *            Decorator pattern
	 */
	public FrequencyPullStreamer(SequencePullStreamer stream) {
		super(stream);
		this.entryMap = new HashMap<DigestedSequence, List<FilePointerContainer>>();
		this.tagSet = new LinkedHashSet<String>();
	}

//	/**
//	 * This implementation of the <code>pull</code> method needs 3 elements. For
//	 * more information see the {@link RandomFastaSequenceStreamer} description
//	 * 
//	 * @see RandomFastaSequenceStreamer
//	 */
//	public void pull(String sequence, Object... obj) {
//		super.pull(sequence, obj);
//		sequencesNumber++;
//		DigestedSequence cleanedSequence = DigestedSequence
//				.newSeq(cleanSequence(sequence));
//		long pointer = (Long) obj[0];
//		RandomAccessFile raf = (RandomAccessFile) obj[1];
//		String tag = (String) obj[2];
//		tagSet.add(tag);
//		if (entryMap.containsKey(cleanedSequence)) {
//			boolean found = false;
//			for (FilePointerContainer fpc : entryMap.get(cleanedSequence)) {
//				if (fpc.getRaf().equals(raf)) {
//					fpc.addPointer(pointer);
//					found = true;
//					break;
//				}
//			}
//			if (!found) {
//				FilePointerContainer fpc = new FilePointerContainer(raf, tag);
//				fpc.addPointer(pointer);
//				entryMap.get(cleanedSequence).add(fpc);
//			}
//		} else {
//			Collection<FilePointerContainer> collection = new ArrayList<FilePointerContainer>();
//			FilePointerContainer fpc = new FilePointerContainer(raf, tag);
//			fpc.addPointer(pointer);
//			collection.add(fpc);
//			entryMap.put(cleanedSequence, collection);
//		}
//	}
	
	/**
	 * This implementation of the <code>pull</code> method needs 3 elements. For
	 * more information see the {@link RandomFastaSequenceStreamer} description
	 * 
	 * @see RandomFastaSequenceStreamer
	 */
	public void pull(String sequence, Object... obj) {
		super.pull(sequence, obj);
		sequencesNumber++;
		DigestedSequence cleanedSequence = DigestedSequence
				.newSeq(cleanSequence(sequence));
		long pointer = (Long) obj[0];
		RandomAccessFile raf = (RandomAccessFile) obj[1];
		String tag = (String) obj[2];
		tagSet.add(tag);
		FilePointerContainer fpc = new FilePointerContainer(raf, tag);
		if (entryMap.containsKey(cleanedSequence)) {
			int index = entryMap.get(cleanedSequence).indexOf(fpc);
			if(index >= 0){
				fpc = entryMap.get(cleanedSequence).get(index);
				fpc.addPointer(pointer);
			}else{				
				fpc.addPointer(pointer);
				entryMap.get(cleanedSequence).add(fpc);
			}
		} else {
			List<FilePointerContainer> collection = new ArrayList<FilePointerContainer>();
			fpc.addPointer(pointer);
			collection.add(fpc);
			entryMap.put(cleanedSequence, collection);
		}
	}

	/**
	 * This method return the count of unique sequences in this
	 * {@link FrequencyPullStreamer}
	 * 
	 * @return the number of unique sequences.
	 */
	public int getUniqueCount() {
		return entryMap.size();
	}

	/**
	 * Implementation of the {@link Iterable#iterator()} method.
	 */
	public Iterator<List<FilePointerContainer>> iterator() {
		return entryMap.values().iterator();
	}

	/**
	 * This method returns the number of sequences analyzed using this class.
	 * 
	 * @return the total number of sequences
	 */
	public long getSequencesNumber() {
		return sequencesNumber;
	}

	/**
	 * Returns the names of the file/s scanned
	 * 
	 * @return a {@link Set} of unique Strings
	 */
	public Set<String> getTagSet() {
		return tagSet;
	}

	/**
	 * Static method for cleaning a String.
	 * 
	 * @param sequence
	 *            the sequence that has to be cleaned
	 * @return a trimmed sequence without spaces and with lower case characters.
	 */
	public static String cleanSequence(String sequence) {
		return sequence.trim().replaceAll("\\s", "").toLowerCase();
	}
}
