package lib.web.bookstore.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import lib.web.bookstore.entities.WordOfDay;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Component
public class WordnikService {

    @Value("${wordnik}")
    private String wordnik;

    public String wordnikApi() {

        HttpHeaders header = new HttpHeaders();
        //header.add("no-cache", "content-type": "application/json; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<String>("parameters", header);
        RestTemplate rt = new RestTemplate();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        
        
        ResponseEntity<WordOfDay> wordOfDay = rt.exchange("https://api.wordnik.com/v4/words.json/wordOfTheDay?date=" + formatter.format(date) + "&api_key=" + wordnik, HttpMethod.GET, entity, WordOfDay.class);

        return wordOfDay.getBody().toString();

    }

}
