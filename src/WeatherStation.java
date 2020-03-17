package src;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import java.util.TimeZone;

/**
 * A class used to retrieve live weather data from the internet.
 * @author Jason Shaffer
 * @version 1.0
 */
public class WeatherStation {

    /**
     * a JSON object containing temperatures, humidity, and barometric pressure readings
     */
    private JSONObject weatherObject;
    /**
     * a JSON object containing sunset/sunrise times
     */
    private JSONObject sysObject;
    /**
     * a JSON object containing forecast and weather icons
     */
    private JSONObject currentWeatherObject;
    /**
     * a JSON object containing wind data
     */
    private JSONObject windObject;
    /**
     * a boolean value that is flipped to true when the first data pull from the API has been finished
     */
    private boolean ready;

    /**
     * Constructor for WeatherStation, creates an object with various getters for all weather data.
     * @param interval an int that represents how often data is pulled from the API
     */
    WeatherStation(int interval){
        WeatherWorker(interval);
    }
    private void WeatherWorker(int time) {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    //System.out.println("pulling fresh data");
                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Tacoma,USA&APPID=75ec4ff690501bd5f4572e39713bca2f&units=imperial");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    int responsecode = conn.getResponseCode();
                    StringBuilder response = new StringBuilder();
                    if(responsecode != 200)
                        throw new RuntimeException("HttpResponseCode: " +responsecode);
                    else {

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        in.close();

                    }
                    JSONObject request = new JSONObject(response.toString());
                    weatherObject = request.getJSONObject("main");
                    sysObject = request.getJSONObject("sys");
                    JSONArray weatherDescripObject  = request.getJSONArray("weather");
                    currentWeatherObject = weatherDescripObject.getJSONObject(0);
                    windObject = request.getJSONObject("wind");
                    ready = true;
                } catch(Exception e){
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }

            }
        }, 0, time * 1000);
    }

    /**
     * Getter method that returns weather data
     * @return String current outside temperature
     */
    public String getCurrentTemp() throws JSONException {
        return ready ? weatherObject.get("temp").toString() + " F" : "";
    }
    /**
     * Getter method that returns weather data
     * @return String current outside minimum temperature
     */
    public String getCurrentTempMin() throws JSONException {
        return ready ? weatherObject.get("temp_min").toString() + " F" : "";
    }
    /**
     * Getter method that returns weather data
     * @return String current outside maximum temperature
     */
    public String getCurrentTempMax() throws JSONException {
        return ready ? weatherObject.get("temp_max").toString() + " F" : "";
    }
    /**
     * Getter method that returns weather data
     * @return String current outside "feels like" temperature
     */
    public String getCurrentFeelsLike() throws JSONException {
        return ready ? weatherObject.get("feels_like").toString() + " F" : "";
    }
    /**
     * Getter method that returns weather data
     * @return String current outside humidity
     */
    public String getCurrentHumid() throws JSONException {
        return ready ? weatherObject.get("humidity").toString() + " %" : "";
    }
    /**
     * Getter method that returns weather data
     * @return String current outside air pressure
     */
    public String getCurrentPressure() throws JSONException {
        return ready ? weatherObject.get("pressure").toString() : "";
    }

    /**
     * Getter method that returns weather data
     * @return String current sunrise time
     */
    public String getSunrise() throws JSONException {

        if(ready){
            Date dateSunrise = new Date(sysObject.getLong("sunrise") * 1000);
            SimpleDateFormat sunriseSDF = new SimpleDateFormat("hh:mm:ss a");
            sunriseSDF.setTimeZone(TimeZone.getTimeZone("PST"));
            return sunriseSDF.format(dateSunrise);
        } else {
            return "";
        }


    }

    /**
     * Getter method that returns weather data
     * @return String current sunset time
     */
    public String getSunset() throws JSONException {
        if(ready){
            Date dateSunset = new Date(sysObject.getLong("sunset") * 1000);
            SimpleDateFormat sunsetSDF = new SimpleDateFormat("hh:mm:ss a");
            sunsetSDF.setTimeZone(TimeZone.getTimeZone("PST"));
            return sunsetSDF.format(dateSunset);
        } else {
            return "";
        }

    }

    /**
     * Getter method that returns weather data
     * @return String containing weather icon name
     */
    public String getWeatherIcon() throws JSONException {
        if(ready){
            String weatherIcon = currentWeatherObject.getString("icon");
            return "https://openweathermap.org/img/wn/" + weatherIcon +"@2x.png";
        } else {
            return "https://openweathermap.org/img/wn/03d@2x.png";
        }
    }

    /**
     * Getter method that returns weather data
     * @return String forecast description
     */
    public String getWeatherDescrip() throws JSONException {
        if(ready){
            return currentWeatherObject.getString("description");
        } else {
            return "";
        }
    }
    /**
     * Getter method that returns weather data
     * @return String a single word description of the weather
     */
    public String getWeatherMain() throws JSONException {
        if(ready){
            return currentWeatherObject.getString("main");
        } else {
            return "";
        }
    }

    /**
     * Getter method that returns weather data
     * @return Double wind direction in degrees, 0-360
     */
    public Double getWindDir() {
        try {
            if(ready){
                return windObject.getDouble("deg");
            } else {
                return 0.0;
            }
        } catch (Exception ex){
            //System.out.println("Error parsing wind object");
            return 0.0;
        }

    }
    /**
     * Getter method that returns weather data
     * @return String wind speed
     */
    public String getWindSpeed() throws JSONException {
        if(ready){
            return Integer.toString((int)Math.ceil(windObject.getDouble("speed")));
        } else {
            return "";
        }
    }
}
