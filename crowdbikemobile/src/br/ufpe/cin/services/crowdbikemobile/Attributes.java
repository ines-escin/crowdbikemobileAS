package br.ufpe.cin.services.crowdbikemobile;

import java.util.List;

public class Attributes {
    
	private String name;
	private String type;
	private String value;
	List<Metadata> metadatas;

	public Attributes(String name, String type, String value, List<Metadata> metadatas) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
		this.metadatas = metadatas;
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

	public List<Metadata> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(List<Metadata> metadatas) {
		this.metadatas = metadatas;
	}

    
}
