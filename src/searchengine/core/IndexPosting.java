package searchengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a posting entry from the words index
 */
public class IndexPosting {
	public IndexPosting(int pageId) {
		this(pageId, -1, 0, 0, new ArrayList<Integer>());
	}
	
	public IndexPosting(int pageId, int wordId, int wordFrequency, double tfIdf, List<Integer> wordPagePositions) {
		setPageId(pageId);
		setWordId(wordId);
		
		this.wordFrequency = wordFrequency;
		
		setTfIdf(tfIdf);
		this.wordPagePositions = wordPagePositions;
	}

	private int pageId;
	private int wordId;
	private int wordFrequency;
	private double tfIdf;
	private List<Integer> wordPagePositions;

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public int getWordFrequency() {
		return wordFrequency;
	}

	public void incrementWordFrequency() {
		wordFrequency++;
	}
	
	public double getTfIdf() {
		return tfIdf;
	}

	public void setTfIdf(double tfIdf) {
		this.tfIdf = tfIdf;
	}

	public List<Integer> getWordPagePositions() {
		return Collections.unmodifiableList(wordPagePositions);
	}

	public void addWordPagePosition(int wordPagePosition) {
		if (wordPagePosition <= 0)
			throw new IllegalArgumentException("A word position in a page cannot be lower than 1");

		wordPagePositions.add(wordPagePosition);
	}
}
