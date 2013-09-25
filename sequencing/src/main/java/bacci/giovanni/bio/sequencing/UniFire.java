package bacci.giovanni.bio.sequencing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import bacci.giovanni.bio.sequencing.manipulation.FastaManipulationStrategy;
import bacci.giovanni.bio.sequencing.manipulation.NoManipulationStrategy;
import bacci.giovanni.bio.sequencing.manipulation.UracileConverterDecorator;

public class UniFire {

	private static final String INPUT = "-in";
	private static final String OUTPUT = "-out";
	private static final String HELP = "-h";
	private static final String VERSION = "-v";

	private static final String HELP_MESSAGE = "                                                \n" +
			                                   "usage: java -jar UniFire.jar [options]\n" +
			                                   "------------------------------------------------\n" +
			                                   "UniFire options:                                \n" +
			                                   "                                                \n" +
			                                   "-in          Input path. If it is a directory \n" +
			                                   "             all files contained will be \n" +
			                                   "             analyzed. \n" +
			                                   "                                                 \n" +
			                                   "-out         Output path. If it is a directory \n" +
			                                   "             all generated files will have \n" +
			                                   "             standard names. Otherwise, the \n" +
			                                   "             file name of this path will be used \n" +
			                                   "             as prefix for the generated files. \n" +
			                                   "                                                 \n" +
			                                   "-h           Print this message. \n" +
			                                   "                                                 \n" +
			                                   "-v           Print infos and version number \n" +
			                                   "------------------------------------------------\n";
	private static final String VERSION_MESSAGE = "                                                \n" +
			                                      "------------------------------------------------\n" +
			                                      "Developed by Giovanni Bacci \n" +
			                                      "Dep. of Biology - University of Florence \n" +
			                                      "CRA-RPS - CRA, Rome \n" +
			                                      "email: giovanni.bacci@unifi.it \n" +
			                                      "version: 0.0.1-SNAPSHOT \n" +
			                                      "------------------------------------------------\n";
	private static final String ERROR_MESSAGE = "                                        \n" +
			                                    "Input is bad formatted! Check the help: \n" +
			                                    "java -jar UniFire.jar -h \n";

	private static Path in = null;
	private static Path out = null;

	public static void main(String[] args) {
		checkArgs(args);
		FastaFileDereplicator dereplicator = new FastaFileDereplicator(in, out);
		dereplicator.setReadingManipulationStrategy(new UracileConverterDecorator(new NoManipulationStrategy()));
		dereplicator.setWritingManipulationStrategy(new UracileConverterDecorator(new FastaManipulationStrategy()));
		try {
			dereplicator.dereplicate();
		} catch (FileNotFoundException e) {
			System.out.println("There is no file/s in: " + e.getMessage());
		} catch (NoSuchFileException e) {
			System.out.println("Cannot create output file: " + e.getMessage()
					+ " (No such file or directory)");
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			System.exit(-1);
		}
	}

	private static void checkArgs(String[] args) {
		List<String> arguments = new ArrayList<String>();
		for (String s : args) {
			arguments.add(s);
		}
		if (arguments.contains(HELP)) {
			System.out.println(HELP_MESSAGE);
			System.exit(0);
		}else if (arguments.contains(VERSION)){
			System.out.println(VERSION_MESSAGE);
			System.exit(0);
		} else if ((arguments.contains(INPUT)) && (arguments.contains(OUTPUT))) {
			try {
				in = Paths.get(arguments.get(arguments.indexOf(INPUT) + 1));
				out = Paths.get(arguments.get(arguments.indexOf(OUTPUT) + 1));
			} catch (IndexOutOfBoundsException e) {
				System.out.println(ERROR_MESSAGE);
				System.exit(-1);
			}
		} else {
			System.out.println(ERROR_MESSAGE);
			System.exit(-1);
		}
	}
}
