package learn.design_patterns.factory;

public class NYPizzaStore extends PizzaStore {

	protected Pizza createPizza(String type) {
		Pizza pizza = null;
		if (type.equals("cheese")) pizza = new NYStyleCheesePizza();
		else if (type.equals("peperoni")) pizza = new NYStylePeperoniPizza();
		return pizza;

	}

}
