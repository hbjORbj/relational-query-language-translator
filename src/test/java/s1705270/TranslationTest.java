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

import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

// 2017 December
// RC -> RA
// RC: "R(?x) | ([E]?z(S(?x,?z)) & ~(R(?z)))"
// RA: "( <R>[B->Ax]( R ) <X> Adom_Az ) <U> ( ( <P>[Ax]( <R>[A->Ax,C->Az]( S ) ) <X> Adom_Az ) <I> ( ( Adom_Az <D> <R>[B->Az]( R ) ) <X> Adom_Ax ) )"

// 2018 August
// RC -> RA 
// RC: "~([E]?y(~([E]?z(~(R(?x,?y)) | R(?y,?z)))))"
// Output RA: "( Adom_Ax <X> Adom_Ay ) <D> <P>[Ax,Ay]( ( Adom_Ax <X> Adom_Ay ) <D> <P>[Ax,Ay]( ( ( ( Adom_Ax <X> Adom_Ay ) <D> <R>[A->Ax,B->Ay]( R ) ) <X> Adom_Az ) <U> ( <R>[A->Ay,B->Az]( R ) <X> Adom_Ax ) ) )"

// 2018 August
// RA -> RC
// RA:
// RC: "(R(?x,?y,?z) & ~(S(?z) & [E]?z(R(?x,?y,?z)))) & (S(?x) & [E]?x(R(?x,?y,?z)) & (?x = ?z | ?x = ?y))"

public class TranslationTest {

	static HashMap<Formula,Expression> translationPairsRC = new HashMap<>();
	static HashMap<Expression,Formula> translationPairsRA = new HashMap<>();
	static HashMap<Formula, Schema> schemaPairsRC = new HashMap<>();
	static HashMap<Expression, Schema> schemaPairsRA = new HashMap<>();

	// take care of schemas
	// maybe another file needed for RA/RC pairs
//	public TranslationTest(File f) throws FileNotFoundException { 
		// read file f with pairs (stringRC_i, stringRA_i) of RC/RA strings
		// for each pair (stringRC_i, stringRA_i):
		//   parse RC string stringRC_i to get Formula formula_i
		//   parse RA string stringRA_i to get Expression expr_i
		//   put (formula_i,expr_i) into translationPairs map
		
		// similarly for RA->RC
//	}
	
	@BeforeAll
	public static void prepareTestCases() throws FileNotFoundException, ReplacementException {
		Scanner translations = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/translations.txt"));
		Scanner schemas = new Scanner(new File(System.getProperty("user.dir") + "/src/main/resources/schemas.txt"));
		while (translations.hasNextLine()) {
			String stringRC = translations.nextLine();
			Formula f = Formula.parse(stringRC);
			if (translations.hasNextLine()) {
				String stringRA = translations.nextLine();
				Expression e = Expression.parse(stringRA);
				translationPairsRC.put(f, e);
			}
			if (schemas.hasNextLine()) {
				String schema = schemas.nextLine();
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
	}
	
	@Test
	public void RCTranslationTest() {
		// for each pair (formula_i,expr_i):
		//   translate formula_i to trans_expr_i
		//   compare trans_expr_i.toString() with expr_i.toString()
		for (Formula f : translationPairsRC.keySet()) {
			try {
				Schema sch = schemaPairsRC.get(f);
				TranslatorRC trans = new TranslatorRC(sch);
				assertEquals(trans.translate(f).toString(), translationPairsRC.get(f).toString());
			} catch (TranslationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void RATranslationTest() {
		// similar to RCTranslationTest
	}
}

