package learn.design_patterns.factory;

public abstract class Pizza {

	String pizzaName;
	public void prepare() {
		println("Prepare");
	}

	public void bake() {
		println("Bake");
	}

	public void cut() {
		println("Cut");
	}

	public void box() {
		println("Box: " + pizzaName);
	}

	private void println(String string) {
		System.out.println(string);
	}
	
	protected void setName(String pizzaName) {
		this.pizzaName = pizzaName;
	}


}
