package main.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    public static String obtenirImageArrierePlan(String weather_main) {
    	
    	String cheminImage = "/img/lightClouds.jpg";
    	
    	switch ( weather_main ) {
    	
		case "Thunderstorm":
			cheminImage = "/img/conditionMeteo/thunderstorm.jpg";
			break;
			
		case "Drizzle":
			cheminImage = "/img/conditionMeteo/light_rain.jpg";
			break;
		
		case "Rain":
			cheminImage = "/img/conditionMeteo/rain.jpg";
			break;
			
		case "Snow":
			cheminImage = "/img/conditionMeteo/snow.jpg";
			break;
	
		case "Clear":
			cheminImage = "/img/conditionMeteo/clear.jpg";
			break;
			
		case "Clouds":
			cheminImage = "/img/conditionMeteo/clouds.jpg";
			break;

		default :
			cheminImage = "/img/conditionMeteo/default.jpg";
			break;
		}
    	
    	return cheminImage;
    }


}
