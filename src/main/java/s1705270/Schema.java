package s1705270;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Schema {

	private Map<String,List<String>> map; // this maps a relation name to a list of attribute names
	
	public Schema(Map<String,List<String>> map) {
		this.map = map;
	}
	
	public Set<String> getRelations() {
		return map.keySet();
	}
	
	public List<String> getAttributes(String rel) {
		return map.get(rel);
	}
	
	public List<String> getAllAttributes() {
		List<String> attrs = new ArrayList<String>();
		for (String r : map.keySet()) {
			attrs.addAll(map.get(r));
		}
		return attrs;
	}
	
	public uk.ac.ed.pguaglia.real.db.Schema convert() {
		uk.ac.ed.pguaglia.real.db.Schema sch = new uk.ac.ed.pguaglia.real.db.Schema();
		for (String tbl : map.keySet()) {
			sch.addTable(tbl, map.get(tbl));
		}
		return sch;
	}
}
