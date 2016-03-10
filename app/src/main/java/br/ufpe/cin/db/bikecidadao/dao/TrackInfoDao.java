package br.ufpe.cin.db.bikecidadao.dao;

import java.sql.SQLException;

import br.ufpe.cin.db.bikecidadao.model.TrackInfo;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

public class TrackInfoDao extends BaseDaoImpl<TrackInfo, Integer> {
	public TrackInfoDao(ConnectionSource cs) throws SQLException{
		super(TrackInfo.class);
		setConnectionSource(cs);
		initialize();
	}
}
