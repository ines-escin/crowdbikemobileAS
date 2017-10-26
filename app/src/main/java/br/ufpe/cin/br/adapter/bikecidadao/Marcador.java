package br.ufpe.cin.br.adapter.bikecidadao;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Marcador implements ClusterItem {
    private final LatLng mPosition;
    private String title;
    private int idTipoMarcador;
    private Long idOcurrence;


    public Marcador(LatLng latLng, String title, int idTipoMarcador, Long idOcurrence) {
        mPosition = latLng;
        this.title = title;
        this.idTipoMarcador = idTipoMarcador;
        this.idOcurrence = idOcurrence;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


    public String getTitle(){
      return title;
    }

    public int getIdTipoMarcador() {
        return idTipoMarcador;
    }

    public void setIdTipoMarcador(int idTipoMarcador) {
        this.idTipoMarcador = idTipoMarcador;
    }

    public Long getIdOcurrence() {
        return idOcurrence;
    }

    public void setIdOcurrence(Long idOcurrence) {
        this.idOcurrence = idOcurrence;
    }
}