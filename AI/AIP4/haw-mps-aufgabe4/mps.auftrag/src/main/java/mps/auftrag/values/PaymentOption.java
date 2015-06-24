package mps.auftrag.values;

public final class PaymentOption {
	private final String type;

	PaymentOption(String type) {
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
	public String toString() {
		return "PaymentOption [type=" + type + "]";
	}
	
}
