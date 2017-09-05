package tfidf;

import tfidf.entities.TfidfResult;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TfidfCalculator {

    private static final int MAIN_THREAD_WAIT_MS = 500;

    private String allContentIdentifiersFilePath;
    private ConcurrentHashMap<String, Set<String>> corpus; // map from each word to the content ids it is found in
    private ConcurrentHashMap<String, Map<String, Double>> contentIdentifiers2TfMaps;
    private int amountOfExecutors;

    public TfidfCalculator(String allContentIdentifiersFilePath, int amountOfExecutors) {
        this.allContentIdentifiersFilePath = allContentIdentifiersFilePath;
        this.amountOfExecutors = Math.min(amountOfExecutors, TfidfUtils.MAX_ALLOWED_EXECUTORS);
        this.corpus = new ConcurrentHashMap<>();
        this.contentIdentifiers2TfMaps = new ConcurrentHashMap<>();
    }

    public TfidfResult calculate(IContentRetriever contentRetriever) throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfExecutors);

        AtomicInteger latchCounter = new AtomicInteger(0);
        AtomicInteger documentsCounter = new AtomicInteger(0);
        try (BufferedReader br = new BufferedReader(new FileReader(allContentIdentifiersFilePath))) {
            String singleContentIdentifier;
            while ((singleContentIdentifier = br.readLine()) != null) {
                executorService.execute(new SingleAppCalculator(
                        singleContentIdentifier,
                        documentsCounter,
                        latchCounter,
                        contentRetriever));
            }
            while (latchCounter.get() > 0) {
                Thread.sleep(MAIN_THREAD_WAIT_MS);
            }
        } finally {
            executorService.shutdown();
        }

        return new TfidfResult(corpus, contentIdentifiers2TfMaps, documentsCounter.get());
    }

    private class SingleAppCalculator implements Runnable {
        private String singleContentIdentifier;
        private AtomicInteger documentsCounter;
        private AtomicInteger latchCounter;
        private IContentRetriever retriever;

        public SingleAppCalculator(String singleContentIdentifier, AtomicInteger documentsCounter, AtomicInteger latchCounter, IContentRetriever retriever) {
            this.singleContentIdentifier = singleContentIdentifier;
            this.documentsCounter = documentsCounter;
            latchCounter.incrementAndGet();
            this.latchCounter = latchCounter;
            this.retriever = retriever;
        }

        @Override
        public void run() {
            documentsCounter.incrementAndGet();
            try {
                System.out.println("*** Running calculation for singleContentIdentifier " + singleContentIdentifier + " ***");
                String[] distinctWordsAndTotalCount = retriever.getContent(singleContentIdentifier);
                Map<String, Double> singleContentTfMap = TfidfUtils.getTfMap(distinctWordsAndTotalCount);
                updateCorpus(singleContentIdentifier, singleContentTfMap.keySet());
                contentIdentifiers2TfMaps.put(singleContentIdentifier, singleContentTfMap);
            } catch (Exception e) {
                System.out.println("Failed calculating tfidf data for singleContentIdentifier " + singleContentIdentifier);
                documentsCounter.decrementAndGet();
            } finally {
                latchCounter.decrementAndGet();
            }
        }

        private void updateCorpus(String singleContentIdentifier, Set<String> words) {
            for (String word : words) {
                // Update corpus
                if (!corpus.containsKey(word)) {
                    Set<String> appIdsForWord = new HashSet<>();
                    appIdsForWord.add(singleContentIdentifier);
                    corpus.put(word, appIdsForWord);
                } else {
                    corpus.get(word).add(singleContentIdentifier);
                }
            }
        }
    }
}
