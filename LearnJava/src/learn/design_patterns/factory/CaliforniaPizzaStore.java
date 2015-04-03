package learn.design_patterns.factory;

public class CaliforniaPizzaStore extends PizzaStore {

	protected Pizza createPizza(String type) {
		Pizza pizza = null;
		if (type.equals("cheese")) pizza = new CaliforniaStyleCheesePizza();
		else if (type.equals("peperoni")) pizza = new CaliforniaStylePeperoniPizza();
		return pizza;	}

}
