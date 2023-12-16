package animat;

public class Action {
	private String ac;
	private int id;

	public Action() {
		//this.ac = ac;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}
	
	public String toSting() {
		return ""+ac;
	}
	
	public String toSting(int x) {
		return x+" "+ac;
	}
	
	public Action getAc(int x) {
		id = x;
		return this;
	}
}
