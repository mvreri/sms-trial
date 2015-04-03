package learn.design_patterns.factory;

public class ChicagoPizzaStore extends PizzaStore {

	protected Pizza createPizza(String type) {
		Pizza pizza = null;
		if (type.equals("cheese")) pizza = new ChicagoStyleCheesePizza();
		else if (type.equals("peperoni")) pizza = new ChicagoStylePeperoniPizza();
		return pizza;	}

}
