package fmi.project.food.analyzer.server;

public enum ClientCommands {
	CONNECT("connect"),
	FOOD_INFO("get-food"),
	FOOD_REPORT("get-food-report"),
	BY_BARCODE("get-food-by-barcode"),
	EXIT("exit"),
	INVALID("invalid");
	
	private final String command;

	ClientCommands(String example) {
		command = example;
	}

	public String getCommand() {
		return command;
	}

	public static ClientCommands findCommand(String text) {
		for (ClientCommands cmd : ClientCommands.values()) {
			if (cmd.getCommand().equals(text)) {
				return cmd;
			}

		}
		return  ClientCommands.INVALID;
	}
}
