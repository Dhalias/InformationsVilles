package main.java;

import java.io.IOException;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.Class.MeteoData;
import main.java.Class.RequeteAPIMeteo;
import main.java.Application;

@Controller
public class Mapping {

    @RequestMapping(value= {"/accueil","/"} , method = RequestMethod.GET)
    public String accueil(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "accueil";
    }
    
    
    @GetMapping("/projet/meteo")
    public String meteo(Model model){
    	model.addAttribute("meteoData", new MeteoData());
    	return "meteo";
    }
    
    @PostMapping("/projet/meteo")
    public String meteoSubmit(@ModelAttribute MeteoData meteoData, Model model) throws IOException {
    	
    	String temperature,pression,path_image,weatherCode,humidite,vent,pays,longitude,latitude;
    	RequeteAPIMeteo requete = new RequeteAPIMeteo(meteoData.getNomVille());
    	JSONObject data = requete.getRequeteJSON();
    	
    	temperature = data.getJSONObject( "main" ).get( "temp" ).toString();
    	pression = data.getJSONObject( "main" ).get( "pressure" ).toString();
    	weatherCode = data.getJSONArray( "weather" ).getJSONObject( 0 ).getString( "main" );
    	path_image = Application.obtenirImageArrierePlan( weatherCode );
    	humidite = data.getJSONObject( "main" ).get( "humidity" ).toString();
    	vent = data.getJSONObject( "wind" ).get( "speed" ).toString();
    	pays = new Locale("",data.getJSONObject( "sys" ).get( "country" ).toString()).getDisplayCountry();
    	longitude = data.getJSONObject( "coord" ).get("lon").toString();
    	latitude = data.getJSONObject( "coord" ).get("lat").toString();

    	
    	model.addAttribute( "temp", temperature );
    	model.addAttribute( "pression", pression );
    	model.addAttribute( "path_image", path_image );
    	model.addAttribute( "humidite", humidite );
    	model.addAttribute( "vent", vent );
    	model.addAttribute( "pays",pays );
    	model.addAttribute( "longitude",longitude );
    	model.addAttribute( "latitude", latitude );
    	
        return "meteoResultat";
    }

}