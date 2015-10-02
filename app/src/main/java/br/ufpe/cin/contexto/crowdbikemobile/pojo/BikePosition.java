package br.ufpe.cin.contexto.crowdbikemobile.pojo;

public class BikePosition {
	private String imei;
	private String lat;
	private String lng;
	
	public BikePosition(String imei, String lat, String lng) {
		this.imei = imei;
		this.lat = lat;
		this.lng = lng;
	}

	private String getImei() {
		return imei;
	}

	private String getLat() {
		return lat;
	}

	private String getLng() {
		return lng;
	}

	private void setImei(String imei) {
		this.imei = imei;
	}

	private void setLat(String lat) {
		this.lat = lat;
	}

	private void setLng(String lng) {
		this.lng = lng;
	}
	
}
