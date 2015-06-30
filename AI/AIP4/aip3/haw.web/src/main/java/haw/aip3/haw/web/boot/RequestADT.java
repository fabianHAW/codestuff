package haw.aip3.haw.web.boot;

import java.io.Serializable;

import org.h2.command.Command;

public class RequestADT implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Command command;
	private long kundenId;
	private String bauteil;

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public long getKundenId() {
		return kundenId;
	}

	public void setKundenId(long kundenId) {
		this.kundenId = kundenId;
	}

	public String getBauteil() {
		return bauteil;
	}

	public void setBauteil(String bauteil) {
		this.bauteil = bauteil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bauteil == null) ? 0 : bauteil.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + (int) (kundenId ^ (kundenId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestADT other = (RequestADT) obj;
		if (bauteil == null) {
			if (other.bauteil != null)
				return false;
		} else if (!bauteil.equals(other.bauteil))
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (kundenId != other.kundenId)
			return false;
		return true;
	}

}
