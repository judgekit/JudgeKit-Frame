package judgekit.judger;

import java.util.HashMap;
import java.util.Map;

public class Problem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Map<Integer,Testdata> testdata = new HashMap<Integer,Testdata>();
	
	public Problem() {
		name=null;
	}
	public Problem(String nName) {
		name=nName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String nName) {
		this.name=nName;
	}
	
	public Map<Integer,Testdata> getAllTestData(){
		return testdata;
	}
	
	public void addTestData(Testdata data) {
		testdata.put(data.getID(), data);
	}
	
	public void removeTestData(Testdata data) {
		if(testdata.containsKey(data.getID()))testdata.remove(data.getID());
	}
	public void removeTestData(Integer ID) {
		if(testdata.containsKey(ID))testdata.remove(ID);
	}
}
