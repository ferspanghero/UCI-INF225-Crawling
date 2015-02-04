package crawling.core;

import java.util.HashSet;

/**
 * Represents a configuration that determines the behaviour of a pages processor
 */
public class PagesProcessorConfiguration {
	public PagesProcessorConfiguration(HashSet<String> stopWords, int nGramsType) {
		setStopWords(stopWords);
		setnGramsType(nGramsType);
	}

	private HashSet<String> stopWords;
	private int nGramsType;

	/**
	 * @return the stopWords
	 */
	public HashSet<String> getStopWords() {
		return stopWords;
	}

	/**
	 * @param stopWords
	 *            A collection of words that should be ignored during processing
	 */
	public void setStopWords(HashSet<String> stopWords) {
		this.stopWords = stopWords;
	}

	/**
	 * @return the nGramsType
	 */
	public int getNGramsType() {
		return nGramsType;
	}

	/**
	 * @param nGramsType
	 *            The type of N-grams that should be processed (i.e: 2-grams,
	 *            3-grams, etc.)
	 */
	public void setnGramsType(int nGramsType) {
		if (nGramsType < 2)
			throw new IllegalArgumentException("A valid N-gram type must be at least 2");

		this.nGramsType = nGramsType;
	}
}
