package tfidf.contentRetrievers;

import tfidf.IContentRetriever;
import tfidf.TfidfUtils;

import java.io.FileInputStream;
import java.io.InputStream;

public class LocalContentRetriever implements IContentRetriever {
    @Override
    public String[] getContent(String location) throws Exception {
        try (InputStream stream = new FileInputStream(location)){
            String fileAsString = TfidfUtils.inputStreamToString(stream);
            return TfidfUtils.getWordsArrayOfContent(fileAsString);
        }
    }
}
