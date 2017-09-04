package tfidf.contentRetrievers;

import tfidf.IContentRetriever;
import tfidf.LocalContentRetriever;

public class ContentRetrieverFactory {

    public static IContentRetriever getRetriever(ContentRetrieverType type){

        IContentRetriever result = null;

        switch (type){
            case Itunes:
                result = new ItunesAppDescriptionRetriever();
                break;
            case Local:
                result = new LocalContentRetriever();
                break;
            default:

        }
        return result;

    }
}
