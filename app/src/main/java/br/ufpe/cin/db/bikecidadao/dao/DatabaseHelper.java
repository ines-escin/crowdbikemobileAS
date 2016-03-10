package br.ufpe.cin.db.bikecidadao.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.ufpe.cin.db.bikecidadao.model.GeoLocation;
import br.ufpe.cin.db.bikecidadao.model.TrackInfo;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String databaseName = "bikecidadao.db";
	private static final int databaseVersion = 1;
	
	public DatabaseHelper(Context context) {
		super(context, databaseName, null, databaseVersion);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase sd, ConnectionSource cs) {
		try {
			TableUtils.createTable(cs, TrackInfo.class);
			TableUtils.createTable(cs, GeoLocation.class);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase sd, ConnectionSource cs, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(cs, TrackInfo.class, true);
			TableUtils.dropTable(cs, GeoLocation.class, true);
			onCreate(sd, cs);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void close(){
		super.close();
	}
}
