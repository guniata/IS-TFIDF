package tfidf.entities;

public class TfidfValue implements Comparable<TfidfValue> {
    private String word;
    private double tfidf;

    public TfidfValue(String word, double tfidf) {
        this.word = word;
        this.tfidf = tfidf;
    }

    public String getWord() {
        return word;
    }

    public double getTfidf() {
        return tfidf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TfidfValue that = (TfidfValue) o;

        if (Double.compare(that.tfidf, tfidf) != 0) return false;
        return word != null ? word.equals(that.word) : that.word == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = word != null ? word.hashCode() : 0;
        temp = Double.doubleToLongBits(tfidf);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int compareTo(TfidfValue o) {
        int result = Double.compare(o.getTfidf(), this.tfidf);
        if(result == 0)
            result = o.getWord().compareTo(this.word);
        return result;
    }

    @Override
    public String toString() {
        return word + " " + tfidf;
    }
}
