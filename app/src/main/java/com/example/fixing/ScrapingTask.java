package com.example.fixing;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class ScrapingTask extends AsyncTask<Void, Void, Void> {
    private static final String URL = "http://www.bnb.bg/statistics/stexternalsector/stexchangerates/sterforeigncurrencies/index.htm";

    protected ScrapingResponse delegate = null;

    private String content;
    private List<String> currencies = new ArrayList<>();

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            Document doc = Jsoup.connect(URL).get();
            content = doc.text();
            extractContent();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        delegate.processFinish();
    }

    //Gets only currencies into one string
    private void extractContent() {
        content = content.replaceAll("[^A-Z0-9. ]", "");
        content = content.substring(content.indexOf("AUD"), content.indexOf("XAU"));

        splitContent();
    }

    //Split content into separate currencies and put them into list
    private void splitContent() {
        for(int i=0;i<content.length();i++) {
            if((Character.isLetter(content.charAt(i)) && content.charAt(Math.abs(i-1)) == ' ') || i == content.length()-1)
            {
                currencies.add(content.substring(0, i-1));
                content = content.substring(i);
                splitContent();
            }
        }
    }

    //Search for the currency that was clicked
    public String searchCurrency(String abbr) {
        for(String currency : currencies) {
            if(currency.substring(0, 3).equals(abbr))
            {
                return currency;
            }
        }
        return "Currency not found";
    }
}
