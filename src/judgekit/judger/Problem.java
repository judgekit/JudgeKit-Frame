package judgekit.judger;

import java.util.HashMap;
import java.util.Map;

public class Problem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String name,path,fileName,ansFileName,inputFileName;
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
	public String getPath() {
		return path;
	}
	public String getFileName() {
		return fileName;
	}
	public String getAnsFileName() {
		return ansFileName;
	}
	public String getInputFileName() {
		return inputFileName;
	}
	
	public void setName(String nName) {
		this.name=nName;
	}
	public void setPath(String nPath) {
		this.path=nPath;
	}
	public void setFileName(String nFileName) {
		this.fileName=nFileName;
	}
	public void setAnsFileName(String nName) {
		this.ansFileName=nName;
	}
	public void setInputFileName(String nName) {
		this.inputFileName=nName;
	}
	
	public Map<Integer,Testdata> getAllTestData(){
		return testdata;
	}
	public Testdata getTestData(Integer ID) {
		return testdata.get(ID);
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
