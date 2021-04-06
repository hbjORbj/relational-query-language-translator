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
	
	private static Map<String,String> parseEnv(String str) {
		HashMap<String,String> map = new HashMap<>();
		for (String mapping : str.split(",")) {
			String[] pair = mapping.split("->");
			String k = pair[0].trim();
			String v = pair[1].trim();
			map.put(k,v);
		}
		return map;
	}
	
	private static boolean validateEnv(Map<String,String> map, Schema sch) throws TranslationException {
		Boolean RAtoRC = false;
		Boolean RCtoRA = false;
		for (String k : map.keySet()) {
			if (k.charAt(0) == '?') {
				RCtoRA = true;
			}
			if (map.get(k).charAt(0) == '?') {
				RAtoRC = true;
			}
		}
		if ((RAtoRC && RCtoRA) || (!RAtoRC && !RCtoRA)) {
			System.err.println("Assign variables to attributes: ?x -> A, ?y -> B for RC to RA translation. \n"
				+ "OR \n"
				+ "Assign attributes to variables: A -> ?x, B -> ?y for RA to RC translation. \n"
				+ "Such custom environment is not allowed: ?x -> A, B -> ?y \n");
			return false;
		}
		if (RAtoRC == true) {
			// Validate Attribute to Variable Environment
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
		} else {
			// Validate Variable to Attribute Environment
			for (String k : map.keySet()) {
				RCParser pRC = getParserRC(k);
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
				RAParser pRA = getParserRA(map.get(k));
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
			}
		}
		
		// Check that given environment is injective
		if (map.size() > 0) {
			Set<String> set = new HashSet<String>(map.values());
			if (set.size() != map.size()) {
				if (RAtoRC) {
					System.err.println("WARNING: More than one attribute cannot be assigned to the same variable.");
				} else {					
					System.err.println("WARNING: More than one variable cannot be assigned to the same attribute.");
				}
				return false;
			}
		}
		// TODO: give a warning for each attribute in the environment that is not among the attributes in the schema
		return true;
	}
	
	private static String schemaToDisplay() {
		if (sch == null) {
			return "{}";
		}
		List<String> listSchema = new ArrayList<String>();
		for (String r : sch.getRelations()) {
			listSchema.add(r + sch.getAttributes(r));
		}
		return String.join(", ", listSchema);
	}
	
	private static String envToDisplay() {
		if (env.size() == 0) {
			return "{}";
		}
		List<String> listEnv = new ArrayList<String>();
		for (String key : env.keySet()) {
			listEnv.add(key + " -> " + env.get(key));
		}
		return String.join(", ", listEnv);
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
							String strSchema;
							if (line.isBlank() == true) {
								strSchema = schemaToDisplay();
								System.out.println("CURRENT SCHEMA: " + strSchema + "\n");
								System.out.println("You can set a new schema in the following format: \n" 
								+ ".SCHEMA <RelationName1>:<Attribute1>,<Attribute2>;<RelationName2>:<Attribute3> \n"
								+ "Ex) .SCHEMA R:A,B; S:B,C \n");							
							} else {
								if (line.equals("{}")) {
									// reset schema
									sch = null;
									System.out.println("CURRENT SCHEMA: {} \n");
								} else {
									try {
										sch = parseSchema(line);
										strSchema = schemaToDisplay();
										System.out.println("CURRENT SCHEMA: " + strSchema + "\n");
									} catch (Exception e) {
										System.err.println("WARNING: Enter a schema in the following format: \n" 
										+ ".SCHEMA <RelationName1>:<Attribute1>,<Attribute2>;<RelationName2>:<Attribute3> \n"
										+ "Ex) .SCHEMA R:A,B; S:B,C \n");
									}
								}

							}
							break cmdSwitch;
						case ENV:
							String strEnv;
							if (line.isBlank() == true) {
								strEnv = envToDisplay();
								System.out.println("CURRENT ENVIRONMENT: " + strEnv + "\n");
								System.out.println("You can set a new environment in the following format: \n"
								+ ".ENV <NAME1> -> <NAME2>, <NAME3> -> <NAME4> \n"
								+ "Ex) .ENV A->?B, B->?C \n");
							} else if (line.equals("{}")) {
								// reset env
								env = new HashMap<String, String>();
								System.out.println("CURRENT ENVIRONMENT: {} \n");
							} else if (sch == null) {
								System.out.println("WARNING: Schema is required to set a custom environment. \n"
								+ "You can set a new schema in the following format: \n" 
								+ ".SCHEMA <RelationName1>:<Attribute1>,<Attribute2>;<RelationName2>:<Attribute3> \n"
								+ "Ex) .SCHEMA R:A,B; S:B,C \n");
							} else {
								Map<String,String> tempEnv = null;
								try {
									tempEnv = parseEnv(line);								
								} catch (Exception e) {
									System.err.println("WARNING: Enter an environment in the following format: \n"
									+ ".ENV <NAME1> -> <NAME2>, <NAME3> -> <NAME4> \n"
									+ "Ex) .ENV A->?B, B->?C \n");
								}
								try {
									if (validateEnv(tempEnv, sch) == true) {
										env = tempEnv;
										strEnv = envToDisplay();
										System.out.println("CURRENT ENVIRONMENT: " + strEnv + "\n");
									}	
								} catch (RecognitionException e1) {
									e1.printStackTrace();
								} catch (RuntimeException e2) {
									e2.printStackTrace();
								}
							}
							break cmdSwitch;
						case RATORC:
							if (line.isBlank() == true) {
								System.out.println("Provide a RA expression to perform translation. \n"
								+ "Ex) .RATORC <P>[A](R) \n");
							} else if (sch == null) {
								System.err.println("WARNING: Schema is required to perform translation. \n"
								+ "You can set a new schema in the following format: \n" 
								+ ".SCHEMA <RelationName1>:<Attribute1>,<Attribute2>;<RelationName2>:<Attribute3> \n"
								+ "Ex) .SCHEMA R:A,B; S:B,C \n");
							} else {
								transRAtoRC = new TranslatorRA(sch);
								try {
									Expression e = Expression.parse(line);
									e.signature(sch.convert());
									System.out.println("Provided RA expression: " + e + "\n");
									System.out.println("Translated RC query: " + transRAtoRC.translate(e, env) + "\n");
								} catch (SchemaException e1) {
									e1.printStackTrace();
								} catch (Exception e2) {
									System.err.println("Provide a valid RA expression. \n"
									+ "Ex) .RATORC <P>[A](R) \n");
								}
							}
							break cmdSwitch;
						case RCTORA:
							if (line.isBlank() == true) {
								System.out.println("Provide a RC query to perform translation. \n"
								+ "Ex) .RCTORA [E]?x2(Customer(?x1,?x2) \n");
							} else if (sch == null) {
								System.err.println("WARNING: Schema is required to perform translation. \n"
								+ "You can set a new schema in the following format: \n" 
								+ ".SCHEMA <RelationName1>:<Attribute1>,<Attribute2>;<RelationName2>:<Attribute3> \n"
								+ "Ex) .SCHEMA R:A,B; S:B,C \n");
							} else {								
								transRCtoRA = new TranslatorRC(sch);
								try {
									Formula f = Formula.parse(line);
									System.out.println("Provided RC query: " + f + "\n");
									System.out.println("Translated RA expression: " + transRCtoRA.translate(f, env) + "\n");
								} catch (TranslationException e1) {
									e1.printStackTrace();
								} catch (Exception e2) {
									System.err.println("Provide a valid RC query. \n"
									+ "Ex) .RCTORA [E]?x2(Customer(?x1,?x2) \n");
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