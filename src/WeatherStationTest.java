package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Tests all methods in WeatherStation
 * @author Jason Shaffer
 *
 */
public class WeatherStationTest {

    private WeatherStation test;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        test = new WeatherStation(30);
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * Test method for the constructor.
     */
    @Test
    public void testWeatherStationConstructor() throws JSONException {
        assertTrue("Constructor test failed", test.getWeatherDescrip().length() > 1);
    }

    /**
     * Test method for all the getters
     */
    @Test
    public void testGetters() throws JSONException {


        assertTrue(test.getCurrentTemp().length() > 0);
        assertTrue(test.getCurrentHumid().length() > 0);
        assertTrue(test.getCurrentPressure().length() > 0);
        assertTrue(test.getSunrise().length() > 0);
        assertTrue(test.getSunset().length() > 0);
        assertTrue(test.getCurrentFeelsLike().length() > 0);
        assertTrue(test.getWeatherDescrip().length() > 0);
        assertTrue(test.getWindSpeed().length() > 0);
        assertTrue(test.getCurrentTempMax().length() > 0);
        assertTrue(test.getCurrentTempMin().length() > 0);
        assertTrue(test.getWindDir() > 0);


    }

}
