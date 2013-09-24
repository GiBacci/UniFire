package bacci.giovanni.bio.sequencing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility object that store a {@link RandomAccessFile} and a list of pointers. Each pointer 
 * is the begin of a sequence in the {@link RandomAccessFile}
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class FilePointerContainer  {
	private RandomAccessFile raf = null;
	private List<Long> pointers = null;
	private String tag = null;

	/**
	 * Constructor.
	 * @param raf the {@link RandomAccessFile}
	 */
	public FilePointerContainer(RandomAccessFile raf, String tag) {
		this.raf = raf;
		this.tag = tag;
		this.pointers = new ArrayList<Long>();
	}

	/**
	 * Add a pointer to the pointers list.
 	 * @param pointer the pointer
	 */
	public void addPointer(long pointer) {
		if (pointers.indexOf(pointer) < 0) {
			pointers.add(pointer);
		}
	}

	/**
	 * Reads the {@link RandomAccessFile} ad returns the sequence corresponding to the index.
	 * @param index the index
	 * @return a sequence as a <code>String</code>
	 * @throws IOException if an IO error occurs reading the file
	 */
	public String getSequence(int index) throws IOException {
		if (raf.getFilePointer() != pointers.get(index)) {
			raf.seek(pointers.get(index));
		}
		StringBuffer buffer = null;
		String line = null;
		while ((line = raf.readLine()) != null) {
			if ((line.startsWith(">")) && (buffer == null)) {
				buffer = new StringBuffer();
			} else if ((!line.startsWith(">")) && (buffer != null)) {
				buffer.append(line);
			} else if ((line.startsWith(">")) && (buffer != null)) {
				break;
			}
		}
		return buffer.toString();
	}

	/**
	 * Same as {@link FilePointerContainer#getSequence(int)} but with the id
	 * @param index the index
	 * @return the id
	 * @throws IOException if an IO Error occurs
	 */
	public String getId(int index) throws IOException {
		if (raf.getFilePointer() != pointers.get(index)) {
			raf.seek(pointers.get(index));
		}
		String line = null;
		while ((line = raf.readLine()) != null) {
			if ((line.startsWith(">"))) {
				return line;
			}
		}
		return null;
	}

	/**
	 * Returns the {@link RandomAccessFile} associated with this object.
	 * @return the {@link RandomAccessFile}
	 */
	public RandomAccessFile getRaf() {
		return raf;
	}

	/**
	 * Returns the tag associated with this collection
	 * @return the tag as a <code>String</code>
	 */
	public String getTag() {
		return tag;
	}
	
	
	public int size(){
		return pointers.size();
	}

}
