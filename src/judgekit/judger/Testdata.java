package judgekit.judger;

import java.io.File;

public class Testdata implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer ID;
	private File in,out;
	private int timeLimit,memoryLimit,outputLimit;
	
	public void setID(Integer ID) {
		this.ID=ID;
	}
	public void setStandardInputFile(File stdIn) {
		this.in=stdIn;
	}
	public void setStandardOutputFile(File stdOut) {
		this.out=stdOut;
	}
	public void setTimeLimit(int nTimeLimit) {
		this.timeLimit=nTimeLimit;
	}
	public void setMemoryLimit(int nMemoryLimit) {
		this.memoryLimit=nMemoryLimit;
	}
	public void getOutputLimit(int nOutputLimit) {
		this.outputLimit=nOutputLimit;
	}
	
	public Integer getID() {
		return ID;
	}
	public File getStandardInputFile() {
		return in;
	}
	public File getStandardOutputFile() {
		return out;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public int getMemoryLimit() {
		return memoryLimit;
	}
	public int getOutputLimit() {
		return outputLimit;
	}
}
