package learn.design_patterns.factory;

public class LearnFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		SimplePizzaFactory sf = new SimplePizzaFactory();
//		PizzaStore store = new PizzaStore(sf);
//		store.orderPizza("cheese");
//		System.out.println("==================");
//		store.orderPizza("veggie");
		NYPizzaStore nyStore = new NYPizzaStore();
		ChicagoPizzaStore chicagoStore = new ChicagoPizzaStore();
		
		nyStore.orderPizza("cheese");
		System.out.println("==================");
		chicagoStore.orderPizza("peperoni");
	}

}
