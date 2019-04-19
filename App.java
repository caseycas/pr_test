package neural.helper.train;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import slp.core.io.Writer;
import slp.core.lexing.code.JavaLexer;
import slp.core.lexing.runners.LexerRunner;
import slp.core.util.Pair;

/**
 * Helper app to save a lex of the training set to file for the neural models.
 * @author caseycas
 *
 */
public class App {
	
	public static Integer nextId = 0;
	
	private static CommandLine parseArgs(String[] args) {
		Options mainOptions = new Options();
		
		//Option inputDirArg = new Option("i", "training", true, "Directory path pointing to the input directory where the training code is stored.");
		//inputDirArg.setRequired(true);
		//mainOptions.addOption(inputDirArg);
		
		//Option outputDirArg = new Option("o", "Output", true, "Directory path pointing to the output directory (does not need to exist, files will be overwritten if already exists)");
		//outputDirArg.setRequired(true);
		//mainOptions.addOption(outputDirArg);
		
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine mainCmd = null;
		
		try {
			mainCmd = parser.parse(mainOptions, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("JSON Lexer", mainOptions);
	
			System.exit(1);
		}
		return mainCmd;
	}
	
	public static File nextFileId(File outputDir)
	{   
		File nextOut = Paths.get(outputDir.getAbsolutePath(), nextId.toString() + ".java").toFile();
		nextId++;
		return nextOut;
	}
	
	public static void main(String[] args)
	{
		CommandLine mainCmd = parseArgs(args);
		File inDir = new File(mainCmd.getOptionValue("i"));
		File outDir = new File(mainCmd.getOptionValue("o"));
		
		//if(!inDir.exists())
		//{
		//	throw new IllegalArgumentException("Input directory must exist.");
		//}
		
		//if(!outDir.exists())
		//{
		//	outDir.mkdirs();
		//}
		
		LexerRunner runner = new LexerRunner(new JavaLexer(), false);
		runner.setExtension("java");
		Stream<Pair<File, Stream<Stream<String>>>> lexedCorpus = runner.lexDirectory(inDir);
		lexedCorpus.forEach(x -> {
			try {
				Writer.writeTokenized(nextFileId(outDir), x.right());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}

