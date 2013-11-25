package bacci.giovanni.bio.sequencing.pull;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import bacci.giovanni.bio.sequencing.util.FilePointerContainer;
import bacci.giovanni.bio.sequencing.util.MultipleFilePointerContainer;

public class HashFrequencyPullStreamer extends SequencePullStreamerDecorator
		implements Iterable<Collection<FilePointerContainer>> {

	private List<MultipleFilePointerContainer> uniques = null;
	private long sequencesNumber = 0;
	private Set<String> tagSet = null;

	public HashFrequencyPullStreamer(SequencePullStreamer streamer) {
		super(streamer);
		this.uniques = new ArrayList<MultipleFilePointerContainer>();
		this.tagSet = new LinkedHashSet<String>();
	}

	public void pull(String sequence, Object... obj) {
		super.pull(sequence, obj);
		sequencesNumber++;
		String cleanedSequence = FrequencyPullStreamer.cleanSequence(sequence);
		long pointer = (Long) obj[0];
		RandomAccessFile raf = (RandomAccessFile) obj[1];
		String tag = (String) obj[2];
		tagSet.add(tag);
		try {
			boolean newSeq = true;
			for (MultipleFilePointerContainer mfpc : uniques) {
				if (mfpc.getSequence().equals(cleanedSequence)) {
					newSeq = false;
					boolean found = false;
					for (FilePointerContainer fpc : mfpc) {
						if (fpc.getRaf().equals(raf)) {
							fpc.addPointer(pointer);
							found = true;
							break;
						}
					}
					if (!found) {
						FilePointerContainer fpc = new FilePointerContainer(
								raf, tag);
						fpc.addPointer(pointer);
						mfpc.add(fpc);
					}
					break;
				} else {
					continue;
				}
			}
			if(newSeq){
				FilePointerContainer fpc = new FilePointerContainer(raf,
						tag);
				fpc.addPointer(pointer);
				MultipleFilePointerContainer mfpc = new MultipleFilePointerContainer();
				mfpc.add(fpc);
				uniques.add(mfpc);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public Iterator<Collection<FilePointerContainer>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This method returns the number of sequences analyzed using this class.
	 * @return the total number of sequences
	 */
	public long getSequencesNumber() {
		return sequencesNumber;
	}
	
	/**
	 * This method return the count of unique sequences in this {@link FrequencyPullStreamer}
	 * @return the number of unique sequences.
	 */
	public int getUniqueCount(){
		return uniques.size();
	}
	
	/**
	 * Returns the names of the file/s scanned
	 * @return a {@link Set} of unique Strings
	 */
	public Set<String> getTagSet() {
		return tagSet;
	}

}
