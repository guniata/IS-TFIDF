package tfidf;

import java.net.MalformedURLException;

public interface IContentRetriever {

    String[] getContent(String contentIdentifier) throws Exception;
}
