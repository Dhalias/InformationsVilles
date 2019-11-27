package main.java.Class;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.JSONObject;

public class RequeteAPI_News {
	
	final String API_KEY = "a3f2727ffafd4dc194aea13e58b42ae9";
	final String API_URL = "https://newsapi.org/v2/top-headlines";
	final String CHARSET = java.nio.charset.StandardCharsets.UTF_8.name();

	private String nomVille;
	private JSONObject requeteJSON;
	private boolean requeteAPI_Succes = false;

	public RequeteAPI_News( String nomVille ) throws IOException {
		
		this.setNomVille( nomVille );
		this.effectuerRequeteAPI();

	}

	private void effectuerRequeteAPI() throws IOException {

		String requete = String.format( "country=%s&apiKey=%s",
				URLEncoder.encode( this.getNomVille(), CHARSET ),
				URLEncoder.encode( API_KEY, CHARSET ) );
		String requeteData;

		HttpURLConnection connexion = (HttpURLConnection) new URL( API_URL + "?" + requete ).openConnection();
		connexion.setRequestProperty( "Accept-Charset", CHARSET );
		connexion.setRequestMethod( "GET" );
		
		System.out.println( "\n[HTTP]\tInitialisation a "+( API_URL + "?" + requete ) );
		System.out.println( "[HTTP]\tCode d'erreur : " + connexion.getResponseCode() );

		if ( connexion.getResponseCode() == 200 ) {
			
			connexion.connect();
			System.out.println( "[HTTP]\tConnexion au site." );
			
			InputStream reponse = new URL( API_URL + "?" + requete ).openStream();
			System.out.println( "[HTTP]\tRecuperation des donnees de la requete." );

			try (Scanner scanner = new Scanner( reponse )) {
				requeteData = scanner.useDelimiter( "\\A" ).next();
			}

			this.setRequeteJSON( new JSONObject( requeteData ) );
			this.requeteAPI_Succes = true;
			
			connexion.disconnect();
			System.out.println( "[HTTP]\tFermeture de la connexion au site." );

		}else {
			System.out.println( "[HTTP]\tImpossible d'ouvrir la connexion au site. Erreur dans la requete.\n"
							   +"[HTTP]\tAssurez-vous que le nom de la ville entree est valide." );
		}

		

	}

	private void setRequeteJSON( JSONObject jsonObject ) {
		this.requeteJSON = jsonObject;

	}

	public boolean isRequeteAPI_Succes() {
		return this.requeteAPI_Succes;
	}

	public JSONObject getRequeteJSON() {
		return this.requeteJSON;
	}

	private String getNomVille() {
		return this.nomVille;
	}

	private void setNomVille( String nomVille ) {
		this.nomVille = nomVille;
	}

	
}
