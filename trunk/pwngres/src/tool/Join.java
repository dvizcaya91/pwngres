package tool;

public class Join extends Operator{

	String condition;
	
	public Join(String id) {
		super(id);
		condition = null;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public String toString() {
		return "{JOIN-" + id  + "," + condition + " || Inputs: " + inputs + "}";
	}
	
}
