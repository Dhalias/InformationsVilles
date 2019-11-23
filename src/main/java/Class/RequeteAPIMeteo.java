package main.java.Class;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.JSONObject;


public class RequeteAPIMeteo {
	
	final String API_KEY = "3246b19ca3e8f14797448968106228aa";
	final String API_URL = "http://api.openweathermap.org/data/2.5/weather";
	final String CHARSET = java.nio.charset.StandardCharsets.UTF_8.name();
	
	private String nomVille;
	private JSONObject requeteJSON;
	
	
	public RequeteAPIMeteo (String nomVille) throws IOException {
		this.setNomVille( nomVille );
		this.effectuerRequeteAPI();
		
	}
	
    
    private void effectuerRequeteAPI() throws IOException {
    	
    	String requete = String.format("q=%s&units=metric&APPID=%s", 
    		     URLEncoder.encode(this.getNomVille(), CHARSET),
    		     URLEncoder.encode(API_KEY, CHARSET));
    	String requeteData;
    	
    	URLConnection connexion = new URL(API_URL + "?" + requete).openConnection();
    	connexion.setRequestProperty( "Accept-Charset", CHARSET );
    	
    	InputStream reponse = new URL(API_URL + "?" + requete).openStream();
    	    	
    	try (Scanner scanner = new Scanner(reponse)) {
    	    requeteData = scanner.useDelimiter("\\A").next();
    	}
    	
    	this.setRequeteJSON( new JSONObject(requeteData) );    	
    	    	
    }
	
    private void setRequeteJSON( JSONObject jsonObject ) {
		this.requeteJSON = jsonObject;
		
	}

	public JSONObject getRequeteJSON() {
		return requeteJSON;
	}
	
	private String getNomVille() {
        return this.nomVille;
    }

    private void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }
	
}
