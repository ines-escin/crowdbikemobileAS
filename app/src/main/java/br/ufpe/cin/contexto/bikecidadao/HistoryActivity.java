package br.ufpe.cin.contexto.bikecidadao;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikecidadao.R;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import br.ufpe.cin.db.bikecidadao.LocalRepositoryController;
import br.ufpe.cin.db.bikecidadao.dao.DatabaseHelper;
import br.ufpe.cin.db.bikecidadao.dao.TrackInfoDao;
import br.ufpe.cin.db.bikecidadao.model.TrackInfo;

public class HistoryActivity extends AppCompatActivity {

    private Gson gson;
    private RecyclerView rv;
    private LocalRepositoryController localRepositoryController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setActivityEnvironment();
        initVariables();
        showTrackCards();
    }

    private void setActivityEnvironment() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    private void initVariables() {

        gson = new Gson();
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        localRepositoryController = new LocalRepositoryController(this);
    }

    private void showTrackCards(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List tracks = null;
        try{
            TrackInfoDao trackInfoDao = new TrackInfoDao(databaseHelper.getConnectionSource());
            tracks = trackInfoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.sort(tracks, TrackInfo.DATE_COMPARATOR);

        RVAdapter adapter = new RVAdapter(tracks);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        databaseHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

        List<TrackInfo> tracks;

        RVAdapter(List<TrackInfo> tracks){
            this.tracks = tracks;
        }

        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_card_layout, viewGroup, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PersonViewHolder personViewHolder, int position) {
            final TrackInfo trackInfo = tracks.get(position);
            double distance = trackInfo.getDistance();
            double avgSpeed = (trackInfo.getDistance()/(trackInfo.getElapsedTime()/1000.0))*3.6;
            String content = trackInfo.getElapsedTime()/1000.0 + " s" + distance + " m\n" + (int)avgSpeed+" km/h";

            personViewHolder.chronometer.setBase(SystemClock.elapsedRealtime()-trackInfo.getElapsedTime());
            personViewHolder.distance.setText(new DecimalFormat("#.##").format(distance / 1000.0));
            personViewHolder.avgSpeed.setText(new DecimalFormat("#.##").format(avgSpeed));
            personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ResultsActivity.class);
                    intent.putExtra("trackId", trackInfo.getId());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tracks.size();
        }

        public static class PersonViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            Chronometer chronometer;
            TextView distance;
            TextView avgSpeed;

            PersonViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                chronometer = (Chronometer) itemView.findViewById(R.id.chronometer);
                distance = (TextView)itemView.findViewById(R.id.distance);
                avgSpeed = (TextView)itemView.findViewById(R.id.avg_speed);
            }
        }

    }



}
