package com.software.project.service.adapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.software.project.entities.Attributes;
import com.software.project.entities.Entity;
import com.software.project.entities.Ocorrencia;

@Service("PersistenceEntity")
@Transactional(propagation=Propagation.REQUIRED)
public class PersistenceEntityWS implements PersistenceEntity {
    
	private Gson gson;
	private int responseCode = 0;
	private String line = "";
	private HttpClient client;
	private HttpResponse response;
	private BufferedReader rd;

	@Override
	public void createNew(Ocorrencia ocurrence) throws Exception {
		// TODO Auto-generated method stub	
		try {
			String uri = "http://148.6.80.19:1026/v1/contextEntities";
			uri += "/" +ocurrence.getIdOcorrencia();
			client = new DefaultHttpClient();
			String result = "";
			HttpPost httppost = new HttpPost(uri);
		    httppost.setHeader("Accept", "application/json");
			gson = new Gson();
			StringEntity entityPost = new StringEntity(gson.toJson(AdapterOcurrence.toEntity(ocurrence)));
			entityPost.setContentType("application/json");
			httppost.setEntity(entityPost);
			int executeCount = 0;
			do {
				executeCount++;
				response = client.execute(httppost);
				responseCode = response.getStatusLine().getStatusCode();						
			} while (executeCount < 5 && responseCode == 408);
			      rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null){
				result += line.trim();
			}	      
		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Ocorrencia> getAll() throws Exception {
		String result = "";
		try {
			client = new DefaultHttpClient();
			String uri = "http://148.6.80.19:1026/v1/contextEntities";
			HttpGet httpget = new HttpGet(uri);
		    httpget.setHeader("Accept", "application/json");		
		    httpget.setHeader("Content-type", "application/json");

			int executeCount = 0;
			do {
				executeCount++;
				response = client.execute(httpget);
				responseCode = response.getStatusLine().getStatusCode();						
			} while (executeCount < 5 && responseCode == 408);
			      rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null){
				result += line.trim();
			}	      
		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();
		}
	
		List<Entity> contextElement = AdapterOcurrence.parseListEntity(result);
		List<Ocorrencia> ocurrences = new ArrayList<Ocorrencia>();
		for (Entity entity : contextElement) {
			ocurrences.add(AdapterOcurrence.toOcurrence(entity));
		}
		
		// TODO Auto-generated method stub
		return ocurrences;
	}

	@Override
	public List<Ocorrencia> getById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ocorrencia findById(Long id) throws ParseException {
		// TODO Auto-generated method stub
		String uri = "http://148.6.80.19:1026/v1/contextEntities/"+String.valueOf(id);
		int responseCode = 0;
		String result = "";  
		String line = "";
		HttpClient client;

		try {
			client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(uri);
		    httpget.setHeader("Accept", "application/json");		
		    httpget.setHeader("Content-type", "application/json");
		    HttpResponse response;
			BufferedReader rd;

			int executeCount = 0;
			do {
				executeCount++;
				response = client.execute(httpget);
				responseCode = response.getStatusLine().getStatusCode();						
			} while (executeCount < 5 && responseCode == 408);
			      rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null){
				result += line.trim();
			}	      
		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();
		}

		return AdapterOcurrence.toOcurrence(AdapterOcurrence.parseEntity(result));
	}

	@Override
	public Ocorrencia getByLatLng(double lat, double lng) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countOcorrencia() throws Exception {
		// TODO Auto-generated method stub
		int count = 0;
		List<Ocorrencia> l = getAll();
		for (int i = 0; i < l.size(); i++) {
			if(l.get(i)!=null){
				count++;
			}
		}
		return Long.parseLong(String.valueOf(count));
	}

	@Override
	public void deleteById(Long id) throws Exception {
		// TODO Auto-generated method stub
		try {
			String uri = "http://148.6.80.19:1026/v1/contextEntities";
			uri += "/" +String.valueOf(id);
			client = new DefaultHttpClient();
			String result = "";
			HttpDelete httpdelete = new HttpDelete(uri);
			httpdelete.setHeader("Accept", "application/json");
			httpdelete.setHeader("Content-type", "application/json");
			gson = new Gson();
			int executeCount = 0;
			do {
				executeCount++;
				response = client.execute(httpdelete);
				responseCode = response.getStatusLine().getStatusCode();						
			} while (executeCount < 5 && responseCode == 408);
			      rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null){
				result += line.trim();
			}	      
		} catch (Exception e) {
			responseCode = 408;
			e.printStackTrace();
		}
	}

	@Override
	public Long countOcorrenciaByType(String title) throws Exception {
		// TODO Auto-generated method stub
		int count = 0;
		List<Ocorrencia> l = getAll();
		for (int i = 0; i < l.size(); i++) {
			if(l.get(i).getTitle()!=null && l.get(i).getTitle().equalsIgnoreCase(title)){
				count++;
			}
		}
					
		return Long.valueOf(String.valueOf(count));
	}

	@Override
	public List<Ocorrencia> getByUserId(Long id) throws Exception {
		// TODO Auto-generated method stub
		List<Ocorrencia> l = getAll();
		List<Ocorrencia> userOcurrences = new ArrayList<Ocorrencia>();
		for (int i = 0; i < l.size(); i++) {
			if(l.get(i)!=null && l.get(i).getUser().getId() == id){
				userOcurrences.add(l.get(i));
			}
		}
		return userOcurrences;
	}	

}
