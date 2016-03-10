package br.ufpe.cin.db.bikecidadao.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import br.ufpe.cin.db.bikecidadao.model.GeoLocation;
import br.ufpe.cin.db.bikecidadao.model.TrackInfo;

public class GeoLocationDao extends BaseDaoImpl<GeoLocation, Integer> {
	public GeoLocationDao(ConnectionSource cs) throws SQLException{
		super(GeoLocation.class);
		setConnectionSource(cs);
		initialize();
	}
}
