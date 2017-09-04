package tfidf;

import tfidf.contentRetrievers.ContentRetrieverType;
import tfidf.entities.TfidfResult;
import tfidf.entities.TfidfTopScores;

import java.util.List;

/**
 * The assumption is that when running this program, the first parameter is a path to a file containing all the app ids
 * each one in a separate line.
 * Otpional parameters:
 *      Amount of parallel executors (limit to 20 in TfidfCalculator)
 *      Amount of top tf-idf results for each input
 */
public class TfIdfMain {

    public static void main(String[] args) throws Exception {
        String appIdsFilePath = args[0];
        int amountOfExecutors = args.length > 1 ? Integer.parseInt(args[1]) : 5;
        int topK = args.length > 2 ? Integer.parseInt(args[2]) : 10;

        TfidfCalculator tfidfCalculator = new TfidfCalculator(appIdsFilePath, amountOfExecutors);

        TfidfResult tfidfResult = tfidfCalculator.calculate(ContentRetrieverType.Itunes);

        List<TfidfTopScores> allTopTfidf = tfidfResult.getAllTopTfidf(topK);
        for(TfidfTopScores topScores : allTopTfidf) {
            System.out.println(topScores);
        }
    }
}