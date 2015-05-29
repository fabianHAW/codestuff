package test;

public class MiddlewareTest {

	public MiddlewareTest() {
	}

	public static void main(String[] args) {
		System.out.println("args: " + args.length);
		if (args.length != 3) {
			usage();
			System.exit(-1);
		}

		switch (Integer.valueOf(args[0])) {
		case 0:
			ServerStart server = new ServerStart(args[1],
					Integer.valueOf(args[2]));
			server.start();
			break;
		case 1:
			ClientStart client = new ClientStart(args[1],
					Integer.valueOf(args[2]));
			client.start();
			ClientStart client2 = new ClientStart(args[1],Integer.valueOf(args[2]));
			client2.start();
			break;
		}
	}

	private static void usage() {
		System.out
				.println("ServerStart: java/MiddlewareTest 0 <nameservice-host> <nameservice-port>");
		System.out
				.println("ClientStart: java/MiddlewareTest 1 <nameservice-host> <nameservice-port>");
	}
}
