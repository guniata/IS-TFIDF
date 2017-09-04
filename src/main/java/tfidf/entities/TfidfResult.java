package tfidf.entities;

import tfidf.TfidfUtils;

import java.util.*;

public class TfidfResult {

    private Map<String, Set<String>> corpus; // word to set of app ids where this word is found
    private Map<String, Map<String, Double>> contentIdentifiers2TfMaps;
    private int amountOfCorpusDocuments;

    public TfidfResult(Map<String, Set<String>> corpus, Map<String, Map<String, Double>> contentIdentifiers2TfMaps, int amountOfCorpusDocuments) {
        this.corpus = corpus;
        this.contentIdentifiers2TfMaps = contentIdentifiers2TfMaps;
        this.amountOfCorpusDocuments = amountOfCorpusDocuments;
    }

    public Map<String, Set<String>> getCorpus() {
        return corpus;
    }

    public Map<String, Map<String, Double>> getContentIdentifiers2TfMaps() {
        return contentIdentifiers2TfMaps;
    }

    public int getAmountOfCorpusDocuments() {
        return amountOfCorpusDocuments;
    }

    public List<TfidfTopScores> getAllTopTfidf(int topAmount) {
        List<TfidfTopScores> topTfidfs = new ArrayList<>(amountOfCorpusDocuments);
        for(String contentIdentifier : contentIdentifiers2TfMaps.keySet()){
            topTfidfs.add(getContentTopTfidfScoredWords(contentIdentifier, topAmount));
        }
        return topTfidfs;
    }

    public TfidfTopScores getContentTopTfidfScoredWords(String contentIdentifier, int topAmount){
        SortedSet<TfidfValue> appTfidfSortedSet = new TreeSet<>();

        for (String wordInContent : contentIdentifiers2TfMaps.get(contentIdentifier).keySet()) {
            appTfidfSortedSet.add(getTfidfScore(wordInContent, contentIdentifier));
        }

        Iterator<TfidfValue> iterator = appTfidfSortedSet.iterator();

        int actualTopAmountToUse = Math.min(topAmount, appTfidfSortedSet.size());
        SortedSet<TfidfValue> topTfIdfScoredWordsInContent = new TreeSet<>();
        for(int countToTop = 1; countToTop <= actualTopAmountToUse ; countToTop++){
            topTfIdfScoredWordsInContent.add(iterator.next());
        }
        return new TfidfTopScores(contentIdentifier, topTfIdfScoredWordsInContent);

    }

    public TfidfValue getTfidfScore(String word, String contentIdentifier){
        Set<String> occurrences = corpus.get(word);
        TfidfValue result;
        if(occurrences == null || occurrences.isEmpty() || occurrences.size() == amountOfCorpusDocuments){
            result = new TfidfValue(word, 0);
        } else{
            // Using log10 to match the example in wikipedia
            result = new TfidfValue(
                    word,
                    TfidfUtils.doubleToPrecision(contentIdentifiers2TfMaps.get(contentIdentifier).get(word) * Math.log10((double)amountOfCorpusDocuments / occurrences.size())));
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TfidfResult that = (TfidfResult) o;

        if (amountOfCorpusDocuments != that.amountOfCorpusDocuments) return false;
        if (corpus != null ? !corpus.equals(that.corpus) : that.corpus != null) return false;
        return contentIdentifiers2TfMaps != null ? contentIdentifiers2TfMaps.equals(that.contentIdentifiers2TfMaps) : that.contentIdentifiers2TfMaps == null;
    }

    @Override
    public int hashCode() {
        int result = corpus != null ? corpus.hashCode() : 0;
        result = 31 * result + (contentIdentifiers2TfMaps != null ? contentIdentifiers2TfMaps.hashCode() : 0);
        result = 31 * result + amountOfCorpusDocuments;
        return result;
    }
}
