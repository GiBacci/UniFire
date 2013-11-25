package bacci.giovanni.bio.sequencing;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bacci.giovanni.bio.sequencing.io.FrequencyTableWriter;
import bacci.giovanni.bio.sequencing.io.RandomFastaSequenceStreamer;
import bacci.giovanni.bio.sequencing.io.SimpleSequenceWriter;
import bacci.giovanni.bio.sequencing.manipulation.FastaManipulationStrategy;
import bacci.giovanni.bio.sequencing.manipulation.SequenceManipulationStrategy;
import bacci.giovanni.bio.sequencing.pull.FrequencyPullStreamer;
import bacci.giovanni.bio.sequencing.pull.SimpleSequencePullStreamer;
import bacci.giovanni.bio.sequencing.util.FilePointerContainer;

/**
 * Main class that delete redundant sequences from one or multiple sequences
 * files. The ids of each sequence will be collected in a separate file and a
 * frequency table will be written using the file names as tags.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastaFileDereplicator {
	private FrequencyPullStreamer frequencyStreamer = null;
	private Path input = null;
	private Path output = null;
	private boolean multiFileStrategy = false;
	private final String tableSuffix = "frequency.csv";
	private final String sequenceSuffix = "sequences.fasta";
	private final String idSuffix = "ids.txt";
	private SequenceManipulationStrategy writingManipulationStrategy = new FastaManipulationStrategy();
	private SequenceManipulationStrategy readingManipulationStrategy = null;

	/**
	 * This constructor takes two parameters. If the <code>input</code> path is
	 * a directory all the files contained in it will be scanned for redundancy,
	 * otherwise only the selected file will be scanned. If the
	 * <code>output</code> path is a directory 3 files will be generated in it
	 * called:<br>
	 * 1) sequences.fasta (the sequences file)<br>
	 * 2) ids.txt (the ids collection file)<br>
	 * 3) frequency.csv (the frequency table)<br>
	 * Otherwise, if the <code>output</code> path is a file its name will be
	 * used as prefix for the names of the 3 files described above; a '_'
	 * character will be used to separate the prefix from the name of the file.
	 * 
	 * @param input
	 *            the input path
	 * @param output
	 *            the output path
	 */
	public FastaFileDereplicator(Path input, Path output) {
		this.input = input;
		this.output = output;
		this.frequencyStreamer = new FrequencyPullStreamer(
				new SimpleSequencePullStreamer());
		if (Files.isDirectory(input)) {
			multiFileStrategy = true;
		}
	}

	/**
	 * Method that starts the redundancy control.
	 * 
	 * @throws IOException
	 *             if an IO Error occurs.
	 */
	public void dereplicate() throws IOException {
		long begin = System.currentTimeMillis();
		if (multiFileStrategy) {
			multiDereplication();
		} else {
			singleDereplication();
		}
		System.out.println("Done in " + (System.currentTimeMillis() - begin)
				/ 1000 + " seconds.");
		System.out.println(frequencyStreamer.getSequencesNumber()
				+ " sequences have been scanned.");
		System.out.println(frequencyStreamer.getUniqueCount()
				+ " unique sequences have been found.");

		System.out.print("Begin writing...");
		this.write();
		System.out.println("done!");
	}

	/**
	 * Private method for the analysis of multiple files.
	 * 
	 * @throws IOException
	 *             if an IO Error occurs.
	 */
	private void multiDereplication() throws IOException {
		DirectoryStream<Path> stream = Files.newDirectoryStream(input);
		for (Path p : stream) {
			if (Files.isDirectory(p)) {
				continue;
			}
//			String name = p.getFileName().toString();
//			RandomAccessFile raf = new RandomAccessFile(p.toFile(), "r");
//			RandomFastaSequenceStreamer fastaStreamer = new RandomFastaSequenceStreamer(
//					frequencyStreamer);
//			if(readingManipulationStrategy != null){
//				fastaStreamer.setSequenceManipulationStrategy(readingManipulationStrategy);
//			}
//			fastaStreamer.setRaf(raf);
//			fastaStreamer.setTag(name);
//			fastaStreamer.stream();
			initialiseSequenceStreamer(p).stream();
		}
	}

	/**
	 * Private method for the analysis of a single file.
	 * 
	 * @throws IOException
	 *             if an IO Error occurs.
	 */
	private void singleDereplication() throws IOException {
//		String name = input.getFileName().toString();
//		RandomAccessFile raf = new RandomAccessFile(input.toFile(), "r");
//		RandomFastaSequenceStreamer fastaStreamer = new RandomFastaSequenceStreamer(
//				frequencyStreamer);
//		if(readingManipulationStrategy != null){
//			fastaStreamer.setSequenceManipulationStrategy(readingManipulationStrategy);
//		}
//		fastaStreamer.setRaf(raf);
//		fastaStreamer.setTag(name);
//		fastaStreamer.stream();
		initialiseSequenceStreamer(input).stream();
	}
	
	private RandomFastaSequenceStreamer initialiseSequenceStreamer(Path input) throws FileNotFoundException{
		String name = input.getFileName().toString();
		RandomAccessFile raf = new RandomAccessFile(input.toFile(), "r");
		RandomFastaSequenceStreamer fastaStreamer = new RandomFastaSequenceStreamer(
				frequencyStreamer);
		if(readingManipulationStrategy != null){
			fastaStreamer.setSequenceManipulationStrategy(readingManipulationStrategy);
		}
		fastaStreamer.setRaf(raf);
		fastaStreamer.setTag(name);
		return fastaStreamer;
	}

	/**
	 * Private method that write the three file described in
	 * {@link FastaFileDereplicator#FastaFileDereplicator(Path, Path)}
	 * 
	 * @throws IOException
	 *             if an IO Error occurs
	 */
	private void write() throws IOException {
		Path frequencyTable = null;
		Path sequences = null;
		Path ids = null;

		if (Files.isDirectory(output)) {
			frequencyTable = output.resolve(tableSuffix);
			sequences = output.resolve(sequenceSuffix);
			ids = output.resolve(idSuffix);
		} else {
			String fileName = output.getFileName().toString();
			String prefix = null;
			if (fileName.indexOf('.') > 0) {
				prefix = fileName.substring(0, fileName.indexOf('.')) + "_";
			} else {
				prefix = fileName + "_";
			}
			frequencyTable = output.getParent().resolve(prefix + tableSuffix);
			sequences = output.getParent().resolve(prefix + sequenceSuffix);
			ids = output.getParent().resolve(prefix + idSuffix);
		}

		Charset def = Charset.defaultCharset();
		
		FrequencyTableWriter freqWriter = new FrequencyTableWriter(
				Files.newBufferedWriter(frequencyTable, def));
		freqWriter.setTags(frequencyStreamer.getTagSet());
		freqWriter.writeHeader();
		SimpleSequenceWriter seqWriter = new SimpleSequenceWriter(
				Files.newBufferedWriter(sequences, def));
		seqWriter.setSequenceManipulationStrategy(writingManipulationStrategy);
		BufferedWriter idsWriter = Files.newBufferedWriter(ids, def);

		for (List<FilePointerContainer> fpcCollection : frequencyStreamer) {
			Map<String, Integer> entryMap = new LinkedHashMap<String, Integer>();
			boolean first = true;
			String id = null;
			for (FilePointerContainer fpc : fpcCollection) {
				if (first) {
					id = fpc.getId(0);
					String seq = fpc.getSequence(0);
					seqWriter.writeSequence(id, seq);
					idsWriter.write(id);
					idsWriter.newLine();
					first = false;
				}
				entryMap.put(fpc.getTag(), fpc.size());
				for (int i = 0; i < fpc.size(); i++) {
					if (!fpc.getId(i).equals(id)) {
						idsWriter.write("     " + fpc.getId(i));
						idsWriter.newLine();
					}
				}
				idsWriter.flush();
			}
			freqWriter.write(id, entryMap);
		}
		idsWriter.close();
	}

	public void setWritingManipulationStrategy(SequenceManipulationStrategy strategy) {
		this.writingManipulationStrategy = strategy;
	}

	public void setReadingManipulationStrategy(
			SequenceManipulationStrategy readingManipulationStrategy) {
		this.readingManipulationStrategy = readingManipulationStrategy;
	}
}
