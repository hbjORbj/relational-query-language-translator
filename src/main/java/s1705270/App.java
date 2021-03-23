package s1705270;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import uk.ac.ed.pguaglia.real.db.SchemaException;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;
import uk.ac.ed.pguaglia.real.parsing.RALexer;
import uk.ac.ed.pguaglia.real.parsing.RAParser;

public class App {
	
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

	public static Schema parseSchema(String str) {
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
	
	public static Map<String,String> parseEnvironment(String str) {
		HashMap<String,String> map = new HashMap<>();
		for (String mapping : str.split(",")) {
			String[] pair = mapping.split("->");
			String k = pair[0].trim();
			String v = pair[1].trim();
			map.put(k,v);
		}
		return map;
	}
	
	public static boolean validateEnvRA(Map<String,String> map, Schema sch) throws TranslationException {
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
				throw new TranslationException("No two keys can have the same value in environment.");
			}
		}
		// TODO: give a warning for each attribute in the environment that is not among the attributes in the schema
		return true;
	}
	
	public static void main(String[] args) throws RecognitionException, ReplacementException, TranslationException {
// 		RC -> RA Example
//		HashMap<String,List<String>> map1 = new HashMap<String, List<String>>();
//		map1.put("R", Arrays.asList(new String[] {"A", "B"}));
//		Schema sch1 = new Schema(map1);
//		TranslatorRC trans1 = new TranslatorRC(sch1);
//		Formula f1 = Formula.parse("~([E]?y(~([E]?z(~(R(?x,?y)) | R(?y,?z)))))");
//
//		try {
//			System.out.println(trans1.translate(f1));
//		} catch (TranslationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Scanner scan = new Scanner(System.in);
		System.out.print("INPUT SCHEMA: ");
		String inputSchema = "R:A,B,C;S:D,C"; //scan.nextLine();
		Schema sch = parseSchema(inputSchema);
		for (String r : sch.getRelations()) {
			System.out.println(r + sch.getAttributes(r));
		}
		System.out.print("INPUT EXPRESSION: ");
		String inputExpr = scan.nextLine();
		Expression e = Expression.parse(inputExpr);
		System.out.println(e);
		TranslatorRA trans = new TranslatorRA(sch);
		System.out.print("INPUT ENVIROMENT (empty line for default): ");
		String inputEnv = scan.nextLine();
		Map<String,String> env = new HashMap<>();
		if (inputEnv.isEmpty() == false) {
			env = parseEnvironment(inputEnv);
		}
		System.out.println(validateEnvRA(env, sch));
		try {
			e.signature(sch.convert());
			System.out.println(trans.translate(e, env));
		} catch (TranslationException | SchemaException error) {
			error.printStackTrace();
		}
		scan.close();
		
		// RA -> RC with custom environment
//		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
//		map.put("R", Arrays.asList(new String[] {"A", "B","C"}));
//		map.put("S", Arrays.asList(new String[] {"C"}));
//		Schema sch = new Schema(map);
//		TranslatorRA trans = new TranslatorRA(sch);
//		Expression e = Expression.parse("<R>[B->C](<P>[A,B](R)) <D> <S>[A = C | A = '1'](<P>[A](R) <X> (S))");
//		HashMap<String,String> env = new HashMap<String, String>();
//		env.put("A", "?x");
//		env.put("B", "?y");
//		env.put("C", "?z");
//		try {
//			e.signature(sch.convert());
//			System.out.println(trans.translate(e, env));
//		} catch (TranslationException | SchemaException error) {
//			error.printStackTrace();
//		}
		
//		RA -> RC with no custom environment
//		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
//		map.put("R", Arrays.asList(new String[] {"A", "B","C"}));
//		map.put("S", Arrays.asList(new String[] {"C"}));
//		Schema sch = new Schema(map);
//		TranslatorRA trans = new TranslatorRA(sch);
//		Expression e = Expression.parse("<R>[B->C](<P>[A,B](R)) <D> <S>[A = C | A = '1'](<P>[A](R) <X> (S))");
//		HashMap<String,String> env = new HashMap<String, String>();
//		try {
//			e.signature(sch.convert());
//			System.out.println(trans.translate(e, env));
//		} catch (TranslationException | SchemaException error) {
//			error.printStackTrace();
//		}
		
	}
}