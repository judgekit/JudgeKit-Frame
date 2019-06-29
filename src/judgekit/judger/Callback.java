package judgekit.judger;

@FunctionalInterface
public interface Callback extends java.io.Serializable{
	void callback(Problem problem,Contestant contestant,Testdata testdata,String state,double score);
}