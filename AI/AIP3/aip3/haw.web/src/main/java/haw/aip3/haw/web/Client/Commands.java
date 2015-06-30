package haw.aip3.haw.web.Client;

public class Commands {

	public Commands() {
		// TODO Auto-generated constructor stub
	}
	
	public static CommandType whichCommand(String input){
		if(input.toLowerCase().equals(CommandType.AUFTRAEGE_EINSEHEN.toString().toLowerCase())){
			return CommandType.AUFTRAEGE_EINSEHEN;
		}else if(input.toLowerCase().equals(CommandType.AUFTRAG_ERSTELLEN.toString().toLowerCase())) {
			return CommandType.AUFTRAG_ERSTELLEN;
		}else if(input.toLowerCase().equals(CommandType.AUFTRAG_ERSTELLEN.toString().toLowerCase())){
			return CommandType.STOP;
		}else {
			return CommandType.UNGUELTIG;
		}
	}
	
	
	public enum CommandType {
		AUFTRAG_ERSTELLEN, AUFTRAEGE_EINSEHEN, STOP, UNGUELTIG;
	}

}
