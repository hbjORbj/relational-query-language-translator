package s1705270;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import uk.ac.ed.pguaglia.real.db.SchemaException;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;
import uk.ac.ed.pguaglia.real.parsing.RALexer;
import uk.ac.ed.pguaglia.real.parsing.RAParser;

public class CommandLineApp {
	private static RAParser getParserRA(String s) {
		RALexer lexer = new RALexer(CharStreams.fromString(s));
		//lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
		RAParser parser = new RAParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		//parser.addErrorListener(ThrowingErrorListener.INSTANCE);
		return parser;
	}
	
	private static RCParser getParserRC(String s) {
		RCLexer lexer = new RCLexer(CharStreams.fromString(s));
		//lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
		RCParser parser = new RCParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		//parser.addErrorListener(ThrowingErrorListener.INSTANCE);
		return parser;
	}

	private static Schema parseSchema(String str) {
		HashMap<String,List<String>> map = new HashMap<>();
		for (String spec : str.split(";")) {
			int k = spec.indexOf(":");
			String relation = spec.substring(0,k).trim();
			ArrayList<String> attributes = new ArrayList<>();
			for (String attr : spec.substring(k+1).split(",")) {
				attributes.add(attr.trim());
			}
			map.put(relation,attributes);
		}
		return new Schema(map);
	}
	
	private static Map<String,String> parseEnvironment(String str) {
		HashMap<String,String> map = new HashMap<>();
		for (String mapping : str.split(",")) {
			String[] pair = mapping.split("->");
			String k = pair[0].trim();
			String v = pair[1].trim();
			map.put(k,v);
		}
		return map;
	}
	
	private static boolean validateEnvRA(Map<String,String> map, Schema sch) throws TranslationException {
		// Check that attribute given follows the rule specified in parser
		for (String k : map.keySet()) {
			RAParser pRA = getParserRA(k);
			try {
				pRA.attribute();
			} catch (RuntimeException e) {
				Throwable cause = e.getCause();
				if (cause instanceof RecognitionException) {
					throw (RecognitionException) cause;
				} else {
					throw e;
				}
			}
			RCParser pRC = getParserRC(map.get(k));
			try {
				pRC.variable();
			} catch (RuntimeException e) {
				Throwable cause = e.getCause();
				if (cause instanceof RecognitionException) {
					throw (RecognitionException) cause;
				} else {
					throw e;
				}
			}
		}
		
		// Check that given environment is injective
		if (map.size() > 0) {
			Set<String> set = new HashSet<String>(map.values());
			if (set.size() != map.size()) {
				return false;
			}
		}
		// TODO: give a warning for each attribute in the environment that is not among the attributes in the schema
		return true;
	}
	
	private enum Commands {
		QUIT, SCHEMA, ENV, RCTORA, RATORC
	}
	
	private static final String APP_COMMAND = "java -jar <path/to/honours-project-X.Y.Z.jar>";
	private static final String OPT_EXPR_SHORT = "q";
	private static final String OPT_EXPR_LONG  = "query";

	private static final Options setupCliOptions() {
		final Options options = new Options();
		options.addOption(Option.builder(OPT_EXPR_SHORT).longOpt(OPT_EXPR_LONG)
				.hasArg().argName("expression").build());
		return options;
	}
	private static Schema sch = null;
	private static Map<String,String> env = new HashMap<String, String>();
	private static TranslatorRC transRCtoRA = null;
	private static TranslatorRA transRAtoRC = null;
	
	public static void main(String[] args) throws RecognitionException, ReplacementException, TranslationException {
		Options opts = setupCliOptions();
		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser optParser = new DefaultParser();
		CommandLine cliCmd = null;
		try {
			cliCmd = optParser.parse( opts, args );
		} catch (MissingOptionException e2) {
			formatter.printHelp( APP_COMMAND, opts );
			System.exit(-1);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
		
		final boolean batch = cliCmd.hasOption(OPT_EXPR_SHORT);
		Terminal terminal = null;
		try {
			terminal = TerminalBuilder.terminal();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		LineReader lineReader = LineReaderBuilder.builder()
				.terminal(terminal)
				.build();
		String prompt = "$> ";
		
		mainLoop: do {
			String line = null;
			if (batch == false) {
				line = lineReader.readLine(prompt).strip();
				if (line.startsWith(".")) {
					int spc = line.indexOf(" ");
					String cmdName;
					if (spc > 0) {
						cmdName = line.substring(1,spc);
						line = line.substring(spc).strip();
					} else {
						cmdName = line.substring(1);
						line = "";
					}
					Commands cmd;
					try {
						cmd = Commands.valueOf(cmdName.toUpperCase());
					} catch (IllegalArgumentException e) {
						System.err.println(String.format("Unrecognized command \".%s\"", cmdName));
						continue mainLoop;
					}
					cmdSwitch: switch (cmd) {
						case QUIT:
							if (line.isBlank() == false) {
								System.err.println(String.format("WARNING: ignoring \"%s\"", line));
							}
							break mainLoop;
						case SCHEMA:
							if (line.isBlank() == true) {
								System.err.println("WARNING: Enter a schema.\n" 
							+ "Ex) .SCHEMA <RelationName1>:<Attribute1>,<Attribute2>;<RelationName2>:<Attribute3>\n");
							} else {
								sch = parseSchema(line);								
							}
							break cmdSwitch;
						case ENV:
							if (line.isBlank() == true) {
								System.err.println("WARNING: Enter an environment.\n" + "Ex) .ENV A->?B, B->?C\n");
							} else if (sch == null) {
								System.err.println("WARNING: Schema is required to set a custom environment.\n");
							} else {
								Map<String,String> tempEnv = parseEnvironment(line);
								if (validateEnvRA(tempEnv, sch) == false) {
									System.err.println("WARNING: No two keys can have the same value in environment.");
								} else {
									env = tempEnv;
								}
							}
							break cmdSwitch;
						case RATORC:
							if (line.isBlank() == true) {
								System.err.println("WARNING: Enter a RA expression.\n"
								+ "Ex) .RATORC <P>[A](R)\n");
							} else if (sch == null) {
								System.err.println("WARNING: Schema is required to perform RA to RC translation.\n");
							} else {
								transRAtoRC = new TranslatorRA(sch);
								Expression e = Expression.parse(line);
								try {
									e.signature(sch.convert());
								} catch (SchemaException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println(transRAtoRC.translate(e, env));
							}
							break cmdSwitch;
						case RCTORA:
							if (line.isBlank() == true) {
								System.err.println("WARNING: Enter a RA expression.\n"
								+ "Ex) .RCTORA [E]?x2(Customer(?x1,?x2)\\n");
							} else if (sch == null) {
								System.err.println("WARNING: Schema is required to perform RC to RA translation.\n");
							} else {								
								transRCtoRA = new TranslatorRC(sch);
								Formula f = Formula.parse(line);
								try {
									System.out.println(transRCtoRA.translate(f, env));
								} catch (TranslationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							break cmdSwitch;
					}
					
					continue mainLoop;
				}
			} else {
				line = cliCmd.getOptionValue(OPT_EXPR_SHORT);
			}
			if (line.isBlank() == true) {
				continue mainLoop;
			}
		} while (batch == false);
	}
}
