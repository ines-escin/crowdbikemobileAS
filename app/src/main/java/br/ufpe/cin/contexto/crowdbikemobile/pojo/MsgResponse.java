package br.ufpe.cin.contexto.crowdbikemobile.pojo;

public class MsgResponse {
	
	private String idUser;
	private String title;
	private String lat;
	private String lng;
	private String endereco;
	private String distance;
	
	public MsgResponse() {
	}

	public String getIdUser() {
		return idUser;
	}

	public String getTitle() {
		return title;
	}

	public String getLat() {
		return lat;
	}

	public String getLng() {
		return lng;
	}

	public String getEndereco() {
		return endereco;
	}

	public String getDistance() {
		return distance;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}
