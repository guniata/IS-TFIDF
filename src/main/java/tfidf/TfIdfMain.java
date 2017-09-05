package tfidf;

import tfidf.entities.TfidfResult;
import tfidf.entities.TfidfTopScores;

import java.util.List;

public class TfIdfMain {

    public static void main(String[] args) throws Exception {
        String appIdsFilePath = args.length == 0 ? TfidfUtils.TEST_ITUNES_APP_IDS : args[0];
        int amountOfExecutors = args.length > 1 ? Integer.parseInt(args[1]) : TfidfUtils.MAX_ALLOWED_EXECUTORS;
        int topK = args.length > 2 ? Integer.parseInt(args[2]) : TfidfUtils.DEFAULT_TOP_K_AMOUNT;

        TfidfCalculator tfidfCalculator = new TfidfCalculator(appIdsFilePath, amountOfExecutors);

        TfidfResult tfidfResult = tfidfCalculator.calculate(new ItunesAppDescriptionRetriever());

        List<TfidfTopScores> allTopTfidf = tfidfResult.getAllTopTfidf(topK);
        for(TfidfTopScores topScores : allTopTfidf) {
            System.out.println(topScores);
        }
    }
}