package br.ufpe.cin.contexto.bikecidadao.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.ufpe.cin.br.adapter.bikecidadao.Ocorrencia;
import br.ufpe.cin.contexto.bikecidadao.MainActivity;
import br.ufpe.cin.contexto.bikecidadao.MapDisplayActivity;
import br.ufpe.cin.util.bikecidadao.OnGetOccurrencesCompletedCallback;

/**
 * Created by jpms2 on 06/06/2016.
 */
public class AsyncCreateAndWriteFile extends AsyncTask<String,Void, Boolean> {

    Context context;
    public AsyncCreateAndWriteFile(Context context){
      this.context = context;

    }

    @Override
    protected Boolean doInBackground(String ... Params) {

        try {
            String text = makeCvsFile();
            createAndWriteFile("exportedMap.csv",text,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void onPostExecute(Boolean bool) {
        Log.v("AsyncCreateAndWriteFile", "Retorno do servidor");
        super.onPostExecute(bool);
  }

    public String makeCvsFile(){
        AsyncGetOcurrences getOcurrences = new AsyncGetOcurrences(context);
        String myInputText = "Latitude,Longitude,Ocorrencia" + "\n";
        try {
            List<Ocorrencia> ocurrencesList = getOcurrences.getAllOccurrences();
            for(int i = 0;i < ocurrencesList.size();i++){                String lat = ocurrencesList.get(i).getLat().toString();
                String lng = ocurrencesList.get(i).getLng().toString();
                String ocorrencia = ocurrencesList.get(i).getOccurenceCode().toString();
                switch (ocorrencia){
                    case "0":
                        ocorrencia = "acidente";
                        break;
                    case "1":
                        ocorrencia = "trânsito pesado";
                        break;
                    case "2":
                        ocorrencia = "sinalização ruim";
                        break;
                    case "3":
                        ocorrencia = "rota danificada";
                        break;

                }
                myInputText +=  lat + "," + lng + "," + ocorrencia + "\n" ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myInputText;
    }

    public void createAndWriteFile(String filename, String text, Context context){
        try {
            File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

