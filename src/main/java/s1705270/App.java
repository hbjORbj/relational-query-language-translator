package s1705270;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

import uk.ac.ed.pguaglia.real.db.SchemaException;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

public class App {

	public static void main(String[] args) throws RecognitionException, ReplacementException {
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
		
//		RA -> RC with custom environment
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