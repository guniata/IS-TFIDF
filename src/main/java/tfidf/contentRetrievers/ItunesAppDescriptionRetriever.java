package tfidf.contentRetrievers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tfidf.IContentRetriever;
import tfidf.TfidfUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class ItunesAppDescriptionRetriever implements IContentRetriever {

    private static final String LOOKUP_URL_PREFIX = "http://itunes.apple.com/lookup?id=";
    private static final String JSON_RESULTS_KEY = "results";
    private static final String JSON_DESCRIPTION_KEY = "description";

    @Override
    public String[] getContent(String appId) throws Exception {
        String urlString = LOOKUP_URL_PREFIX + appId;
        URL url = new URL(urlString);
        JSONObject jsonObject = TfidfUtils.fetchJsonFromUrl(url);
        JSONArray results = (JSONArray) jsonObject.get(JSON_RESULTS_KEY);
        JSONObject object = (JSONObject) results.get(0);
        String description = object.get(JSON_DESCRIPTION_KEY).toString();
        return TfidfUtils.getWordsArrayOfContent(description);
    }
}
