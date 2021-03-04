package s1705270;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import uk.ac.ed.pguaglia.real.db.SchemaException;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

public class TranslationTest {

	static HashMap<Formula,Expression> translationPairsRC = new HashMap<>();
	static HashMap<Expression,Formula> translationPairsRA = new HashMap<>();
	static HashMap<Formula, Schema> schemaPairsRC = new HashMap<>();
	static HashMap<Expression, Schema> schemaPairsRA = new HashMap<>();
	static HashMap<Formula, Map<String,String>> envPairsRC = new HashMap<>();
	static HashMap<Expression, Map<String,String>> envPairsRA = new HashMap<>();
	@BeforeAll
	public static void prepareTestCases() throws FileNotFoundException, ReplacementException {
		Scanner translationsRC = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/translationsRC.txt"));
		Scanner schemasRC = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/schemasRC.txt"));
		while (translationsRC.hasNextLine()) {
			String stringRC = translationsRC.nextLine();
			Formula f = Formula.parse(stringRC);
			if (translationsRC.hasNextLine()) {
				String stringRA = translationsRC.nextLine();
				Expression e = Expression.parse(stringRA);
				translationPairsRC.put(f, e);
			}
			if (schemasRC.hasNextLine()) {
				String schema = schemasRC.nextLine();
				String[] relations = schema.split(",");
				HashMap<String,List<String>> map = new HashMap<String, List<String>>();

				for (String relation : relations) {
					Integer idx = relation.indexOf(":");
					String name = relation.substring(0, idx);
					String attrString = relation.substring(idx + 1);
					map.put(name, Arrays.asList(attrString.split(" ")));
				}
				schemaPairsRC.put(f, new Schema(map));
			}
        }
		
		Scanner translationsRA = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/translationsRA.txt"));
		Scanner schemasRA = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/schemasRA.txt"));
		Scanner envsRA = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/envsRA.txt"));
		while (translationsRA.hasNextLine()) {
			String stringRA = translationsRA.nextLine();
			Expression e = Expression.parse(stringRA);
			if (translationsRA.hasNextLine()) {
				String stringRC = translationsRA.nextLine();
				Formula f = Formula.parse(stringRC);
				translationPairsRA.put(e, f);
			}
			if (schemasRA.hasNextLine()) {
				String schema = schemasRA.nextLine();
				String[] relations = schema.split(",");
				HashMap<String,List<String>> map = new HashMap<String, List<String>>();

				for (String relation : relations) {
					Integer idx = relation.indexOf(":");
					String name = relation.substring(0, idx);
					String attrString = relation.substring(idx + 1);
					map.put(name, Arrays.asList(attrString.split(" ")));
				}
				schemaPairsRA.put(e, new Schema(map));
			}
			if (envsRA.hasNextLine()) {
				String env = envsRA.nextLine();
				String[] pairs = env.split(",");
				HashMap<String,String> map = new HashMap<String,String>();
				if (pairs.length > 0) {				
					for (String pair : pairs) {
						Integer idx = pair.indexOf("->");
						String left = pair.substring(0, idx);
						String right = pair.substring(idx + 1);
						map.put(left, right);
					}
				}
				envPairsRA.put(e, map);
			}
        }
	}
	
	@Test
	public void RCTranslationTest() {
		for (Formula f : translationPairsRC.keySet()) {
			try {
				Schema sch = schemaPairsRC.get(f);
				TranslatorRC transRC = new TranslatorRC(sch);
				assertEquals(transRC.translate(f).toString(), translationPairsRC.get(f).toString());
			} catch (TranslationException error) {
				error.printStackTrace();
			}
		}
	}
	
	@Test
	public void RATranslationTest() {
		for (Expression e : translationPairsRA.keySet()) {
			try {
				Schema sch = schemaPairsRA.get(e);
				Map<String,String> env = envPairsRA.get(e);
				e.signature(sch.convert());
				TranslatorRA transRA = new TranslatorRA(sch);
				assertEquals(transRA.translate(e, env).toString(), translationPairsRA.get(e).toString());
			} catch (TranslationException | SchemaException error) {
				error.printStackTrace();
			}
		}
	}
}

