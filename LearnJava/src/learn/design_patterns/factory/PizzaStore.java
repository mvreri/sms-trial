package learn.design_patterns.factory;

public abstract class PizzaStore {
	
//	SimplePizzaFactory sf;
//	
//	public PizzaStore(SimplePizzaFactory sf) {
//		super();
//		this.sf = sf;
//	}

	public Pizza orderPizza(String type) {
//		Pizza pizza = sf.createPizza(type);
		Pizza pizza = createPizza(type);
		
		pizza.prepare();
		pizza.bake();
		pizza.cut();
		pizza.box();
		return pizza;
	}

	protected abstract Pizza createPizza(String type);
}
