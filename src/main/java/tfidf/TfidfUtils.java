package tfidf;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TfidfUtils {

    private static final int PRECISION = 4;
    private static final String WORDS_ONLY_REGEX = "\\W+";//"[^\\p{L}\\p{Z}]";
    private static final String WORDS_INITIALS_AND_URLS_REGEX = "(?!=^((http(s|)?|ftp|file):\\/\\/){0,1}[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|])(?!=(\\w+\\.){2,})(?=\\W+)";
    private static final String SPLIT_REGEX = "\\s+";

    public static Map<String, Double> getTfMap(String[] words) {
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : words) {
            Integer wordCount = wordCounts.get(word);
            if (wordCount == null) {
                wordCounts.put(word, 1);
            } else {
                wordCounts.put(word, wordCount + 1);
            }
        }
        Map<String, Double> tfMap = new HashMap<>(wordCounts.size());
        int amountOfWordsInDescription = words.length;
        for (String word : wordCounts.keySet()) {
            tfMap.put(word, TfidfUtils.doubleToPrecision((double) wordCounts.get(word) / (double) amountOfWordsInDescription));
        }
        return tfMap;
    }

    public static double doubleToPrecision(double value){
        return BigDecimal.valueOf(value)
                .setScale(PRECISION, RoundingMode.HALF_UP).doubleValue();
    }

    public static String[] getWordsArrayOfContent(String content){
        String wordsOnly = content.replaceAll(TfidfUtils.WORDS_ONLY_REGEX, " ");
        return wordsOnly.split(TfidfUtils.SPLIT_REGEX);
    }

    public static JSONObject fetchJsonFromUrl(URL url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "application/json");

        JSONObject result;
        try (InputStream stream = connection.getInputStream()) {
            JSONParser parser = new JSONParser();
            final Object parse = parser.parse(inputStreamToString(stream));
            result = (JSONObject) parse;
        }

        return result;
    }

    public static String inputStreamToString(InputStream in) throws IOException {
        Reader reader = null;
        char[] buf = new char[64 * 1024];
        int read;
        StringWriter sw = new StringWriter();
        try {
            reader = new InputStreamReader(in, "UTF-8");
            while ((read = reader.read(buf)) > -1) {
                sw.write(buf, 0, read);
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        return sw.toString();
    }
}
