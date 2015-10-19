package br.ufpe.cin.br.adapter.bikecidadao;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class AdapterOcurrence {
	
	public static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
    
    private static Entity parseEntity(String s) {
    	Entity e = new Entity(); 
    	Gson gson = new Gson();
    	List<Attributes> lAtt = new ArrayList<Attributes>();
    	try {
    		JSONParser jsonParser = new JSONParser();
    		JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
    		JSONObject structure = (JSONObject) jsonObject.get("contextElement");
    		Type listType = new TypeToken<ArrayList<Attributes>>() {}.getType();
            lAtt =  gson.fromJson(structure.get("attributes").toString(), listType);
     
            e.setId(structure.get("id").toString());
            e.setType(structure.get("type").toString());
            e.setAttributes(lAtt);
            
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
		return e;

    }

    
   
    public static List<Entity> parseListEntity(String s) throws Exception {
		List<Entity> listEntity = new ArrayList<Entity>();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(s.trim());
		JSONArray lang = (JSONArray) jsonObject.get("contextResponses");
		if(lang != null){
			Iterator i = lang.iterator();
			// take each value from the json array separately
		    while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				if(innerObj != null)
				listEntity.add(AdapterOcurrence.parseEntity(innerObj.toString()));
			}
		}
		return listEntity;

    }
	public static Ocorrencia toOcurrence(Entity e) throws ParseException {
		Ocorrencia o = new Ocorrencia();
		o.setIdOcorrencia(Long.parseLong(e.getId()));
		for (Attributes att : e.getAttributes()) {
			switch (att.getName()) {
				case "title":
					o.setTitle(att.getValue());
					break;
				case "GPSCoord":
					String[] tokensVal = att.getValue().split(",");
					o.setLat(tokensVal[0].trim());
					o.setLng(tokensVal[1].trim());
					break;
				case "endereco":
					o.setEndereco(att.getValue());
					break;
				case "dataOcorrencia":
					Date date = null;
					date = df.parse(att.getValue());
					o.setDataOcorrencia(date);
					break;
				case "userId":
					User u = new User();
					u.setId(Long.parseLong(att.getValue()));
					o.setUser(u);
					break;
				case "occurrenceCode":
					o.setOccurenceCode((int)Long.parseLong(att.getValue()));
			}

		}
		return o;

	}

}
