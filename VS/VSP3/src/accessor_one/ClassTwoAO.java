package accessor_one;

import java.util.Random;

public class ClassTwoAO extends ClassTwoImplBase {

	public ClassTwoAO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int methodOne(double param1) throws SomeException110 {
		// TODO Auto-generated method stub
		if((param1 / 2) > (Integer.MAX_VALUE / 2)){
			throw new SomeException110("ClassTwoAO-methodOne-SomeException110:::(param1 / 2) > (Integer.MAX_VALUE / 2)");
		}
		return (int)param1*2;
	}

	@Override
	public double methodTwo() throws SomeException112 {
		double max = Double.MAX_VALUE;
		Random rand = new Random();
		double result = 0 + (max * rand.nextDouble());
		if(result % 2 != 0){
			throw new SomeException112("ClassTwoAO-methodTwo-SomeException112:::rand % 2 != 0");
		}
		
		return result;
	}

}
