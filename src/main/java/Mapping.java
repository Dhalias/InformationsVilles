package main.java;

import java.io.IOException;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import main.java.Class.MeteoData;
import main.java.Class.RequeteAPI_Meteo;
import main.java.Class.RequeteAPI_News;
import main.java.Application;

@Controller
public class Mapping {

	@RequestMapping(value = { "/accueil", "/" }, method = RequestMethod.GET)
	public String meteo( @RequestParam(name = "erreur", required = false) String erreur, Model model ) {
		model.addAttribute( "erreur", erreur );
		model.addAttribute( "meteoData", new MeteoData() );
		return "meteo";
	}

	@PostMapping(value = { "/accueil", "/" })
	public String meteoSubmit( @ModelAttribute MeteoData meteoData, RedirectAttributes attribut, Model model )
			throws IOException {

		String nomVille = meteoData.getNomVille();
		String modeleRendu = "redirect:/";
		attribut.addAttribute( "erreur", "true" );

		RequeteAPI_Meteo requete = new RequeteAPI_Meteo( nomVille );

		if ( requete.isRequeteAPI_Succes() ) {

			String temperature, pression, path_image, weatherCode, humidite, vent, pays, longitude, latitude;

			JSONObject data = requete.getRequeteJSON();

			System.out.println( "\n[HTTP]\tRecuperation des donnees meteorologiques\n" );

			temperature = data.getJSONObject( "main" ).get( "temp" ).toString();
			System.out.println( "[HTTP]\tTemperature recuperee pour " + nomVille );

			pression = data.getJSONObject( "main" ).get( "pressure" ).toString();
			System.out.println( "[HTTP]\tPression recuperee pour " + nomVille );

			weatherCode = data.getJSONArray( "weather" ).getJSONObject( 0 ).getString( "main" );
			System.out.println( "[HTTP]\tCode meteorologique recupere pour " + nomVille );

			path_image = Application.obtenirImageArrierePlan( weatherCode );
			System.out.println( "[HTTP]\tImageArrierePlan recuperee pour " + nomVille );

			humidite = data.getJSONObject( "main" ).get( "humidity" ).toString();
			System.out.println( "[HTTP]\tHumidite recuperee pour " + nomVille );

			vent = data.getJSONObject( "wind" ).get( "speed" ).toString();
			System.out.println( "[HTTP]\tVent recuperee pour " + nomVille );

			pays = new Locale( "", data.getJSONObject( "sys" ).get( "country" ).toString() ).getDisplayCountry();
			System.out.println( "[HTTP]\tPays recupere pour " + nomVille );

			longitude = data.getJSONObject( "coord" ).get( "lon" ).toString();
			System.out.println( "[HTTP]\tLongitude recuperee pour " + nomVille );

			latitude = data.getJSONObject( "coord" ).get( "lat" ).toString();
			System.out.println( "[HTTP]\tLatitude recuperee pour " + nomVille );

			model.addAttribute( "temp", temperature );
			model.addAttribute( "pression", pression );
			model.addAttribute( "path_image", path_image );
			model.addAttribute( "humidite", humidite );
			model.addAttribute( "vent", vent );
			model.addAttribute( "pays", pays );
			model.addAttribute( "longitude", longitude );
			model.addAttribute( "latitude", latitude );
			System.out.println( "\n[HTTP]\tToutes les donnees ont ete ajoutees au model" );

			RequeteAPI_News requete2 = new RequeteAPI_News( data.getJSONObject( "sys" ).get( "country" ).toString() );

			if ( requete2.isRequeteAPI_Succes() ) {

				data = requete2.getRequeteJSON();

				if ( !data.get( "totalResults" ).toString().equals( "0" ) ) {
					
					JSONArray articlesJSON = data.getJSONArray( "articles" );
					String[][] tabArticle = new String[3][5];

					for ( int i = 0; i < tabArticle.length; i++ ) {
						JSONObject articleCourantJSON = articlesJSON.getJSONObject( i );

						tabArticle[i][0] = articleCourantJSON.get( "title" ).toString();
						tabArticle[i][1] = articleCourantJSON.get( "description" ).toString();
						tabArticle[i][2] = articleCourantJSON.get( "url" ).toString();
						tabArticle[i][3] = articleCourantJSON.get( "urlToImage" ).toString();
						tabArticle[i][4] = articleCourantJSON.get( "publishedAt" ).toString();

					}

					model.addAttribute( "article", tabArticle );
				}

			}

			modeleRendu = "meteoResultat";

		}

		return modeleRendu;
	}

}