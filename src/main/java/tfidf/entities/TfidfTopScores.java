package tfidf.entities;

import java.util.SortedSet;

public class TfidfTopScores {

    public static final String SEPERATOR = " ; ";
    private String contentIdentifier;
    private SortedSet<TfidfValue> scores;

    public TfidfTopScores(String contentIdentifier, SortedSet<TfidfValue> scores) {
        this.contentIdentifier = contentIdentifier;
        this.scores = scores;
    }

    public String getContentIdentifier() {
        return contentIdentifier;
    }

    public SortedSet<TfidfValue> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        StringBuilder appIdTopScoresSB = new StringBuilder(contentIdentifier + ": ");
        for (TfidfValue tfidfValue : scores) {
            appIdTopScoresSB.append(tfidfValue);
            appIdTopScoresSB.append(SEPERATOR);
        }
        int lastIndexOfSeparator = appIdTopScoresSB.lastIndexOf(";");
        return appIdTopScoresSB.replace(lastIndexOfSeparator, lastIndexOfSeparator+1, ".").toString();
    }
}
