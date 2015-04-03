package learn.design_patterns.factory;

public class SimplePizzaFactory {
	public Pizza createPizza(String type) {
		Pizza pizza = null;
		if (type.equals("cheese")) pizza = new CheesePizza();
		else if (type.equals("peperoni")) pizza = new PeperoniPizza();
		return pizza;
	}
}