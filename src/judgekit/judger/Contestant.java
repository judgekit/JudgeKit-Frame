package judgekit.judger;

public class Contestant implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String name,path;
	
	public Contestant(String nName,String nPath){
		name=nName;
		path=nPath;
	}
	
	public Contestant(String nName){
		name=nName;
		path=null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String nName) {
		this.name=nName;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String nPath) {
		this.path=nPath;
	}
}
