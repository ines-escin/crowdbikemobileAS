package br.ufpe.cin.br.adapter.bikecidadao;

import java.util.Date;


public class Ocorrencia {

	  private Long idOcorrencia;
	  
	  private String title;
	  
	  private String endereco;
	  
      private String lat;
      
	  private String lng;

	  private Date dataOcorrencia;

	  private User user;

	  private Integer occurrenceCode;

	public Long getIdOcorrencia() {
		return idOcorrencia;
	}

	public void setIdOcorrencia(Long idOcorrencia) {
		this.idOcorrencia = idOcorrencia;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}

	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setOccurenceCode(Integer occurenceCode) {
		this.occurrenceCode = occurenceCode;
	}

	public Integer getOccurenceCode() {
		return occurrenceCode;
	}
}
