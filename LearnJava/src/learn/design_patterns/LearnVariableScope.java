package learn.design_patterns;

import java.lang.ref.WeakReference;

public class LearnVariableScope {
	static int instance = 0;
	public static class Orange {
		private String loc;
		private int ic;

		public Orange(String loc) {
			this.loc = loc;
			this.ic = instance++;
		}
		
		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			System.out.println("Gabarge collected: " + loc + " #" + ic);
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		addOn();
		System.gc();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.gc();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	protected static void addOn() {
		Orange o = new Orange("USA");
		doSomething(o);
	}


	/*
	 * Cau hoi: 
	 * 1. var scope tinh theo location hay time ?
	 * 2. Tai sao dung final parameter khi dung trong thread khac
	 * 	2.1. final la gi
	 * 	2.2. Tra loi truc tiep cau hoi
	 */
	private static void doSomething(final Orange o) {
//		Orange o = new Orange("USA");
		//final WeakReference<Orange> wo = new WeakReference<LearnVariableScope.Orange>(o);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Sleeping...");
					Thread.sleep(3000);					
					System.out.println("Weaken up !");
//					if ( wo.get() == null ) {
//						System.out.println("The scope: according to location");
//					} else {
//						System.out.println("The scope: according to time");
//					}
					System.out.println(o.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
		
	}
	

}
