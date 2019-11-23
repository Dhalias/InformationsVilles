package main.java;

import java.io.IOException;

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
    	RequeteAPIMeteo requete = new RequeteAPIMeteo(meteoData.getNomVille());
    	JSONObject data = requete.getRequeteJSON();
    	model.addAttribute( "temp", data.getJSONObject( "main" ).get( "temp" ) );
    	model.addAttribute( "pression", data.getJSONObject( "main" ).get( "pressure" ) );
        return "meteoResultat";
    }

}