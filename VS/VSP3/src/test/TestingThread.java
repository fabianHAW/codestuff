package test;

public class TestingThread {

	private int value;

	public static void main(String[] args) {
		NeuerThread n = new NeuerThread();
		n.start();
//		try {
//			Thread.sleep(1000);
//			NeuerThread.test();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
		synchronized (n) {
			try {
				n.wait();
				System.out.println("end waiting");
				System.out.println(n.getVal());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	public void setValue(int v){
		value = v;
	}

}
