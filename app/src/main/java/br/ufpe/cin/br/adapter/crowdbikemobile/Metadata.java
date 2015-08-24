package br.ufpe.cin.br.adapter.crowdbikemobile;

public class Metadata {
	private String name;
	private String type;
	private String value;

	public Metadata(String name, String type, String value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
