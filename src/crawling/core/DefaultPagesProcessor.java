package crawling.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Represents a basic processor that does a set of operations with the crawled pages
 */
public class DefaultPagesProcessor implements IPagesProcessor {
	public DefaultPagesProcessor() {
		pagesCount = 0;
		longestPageUrl = null;
		subdomainsCount = new HashMap<String, Integer>();
		mostCommonWords = new HashMap<String, Integer>();
		mostCommonNGrams = new HashMap<String, Integer>();
	}

	private final static int PAGES_CHUNK_SIZE = 128;
	private int pagesCount;
	private String longestPageUrl;
	private HashMap<String, Integer> subdomainsCount;
	private HashMap<String, Integer> mostCommonWords;
	private HashMap<String, Integer> mostCommonNGrams;

	@Override
	public void processPages(IPagesRepository repository, PagesProcessorConfiguration config) throws SQLException {
		if (repository == null)
			throw new IllegalArgumentException("The pages processor cannot be initialized with a null pages repository");

		if (config == null)
			throw new IllegalArgumentException("The pages processor cannot be initialized with a null configuration");

		int longestPageLength = 0;
		List<PageProcessingData> pages = repository.retrieveNextPages(PAGES_CHUNK_SIZE);

		while (pages != null && pages.size() > 0) {
			// Computes pages count
			processUniquePagesCount(pages);

			// Computes subdomains count
			processSubdomains(pages, config.getBaseSubdomain());

			// Computes most common elements
			processMostCommonElements(pages, config);

			// Computes longest page
			longestPageLength = processLongestPage(pages, longestPageLength);

			pages = repository.retrieveNextPages(PAGES_CHUNK_SIZE);
		}
	}

	@Override
	public int getUniquePagesCount() {
		return pagesCount;
	}

	@Override
	public Map<String, Integer> getSubdomains() {
		// Sorts the map by its keys
		TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(subdomainsCount);

		// Makes sure an unmodifiable result is returned
		return Collections.unmodifiableMap(sortedMap);
	}

	@Override
	public String getLongestPage() {
		return longestPageUrl;
	}

	@Override
	public Map<String, Integer> getMostCommonWords(int count) {
		// Makes sure an unmodifiable result is returned
		return Collections.unmodifiableMap(getMapFirstEntries(sortMapByValueDescending(mostCommonWords), count));
	}

	@Override
	public Map<String, Integer> getMostCommonNGrams(int count) {
		// Makes sure an unmodifiable result is returned
		return Collections.unmodifiableMap(getMapFirstEntries(sortMapByValueDescending(mostCommonNGrams), count));
	}

	private void processUniquePagesCount(List<PageProcessingData> pages) {
		pagesCount += pages.size();
	}

	private void processSubdomains(List<PageProcessingData> pages, String baseSubdomain) {
		for (PageProcessingData page : pages) {
			String url = page.getUrl();
			String[] urlParts = url.split(baseSubdomain);

			if (urlParts != null && urlParts.length > 0) {
				String key = urlParts[0] + baseSubdomain;

				if (subdomainsCount.containsKey(key))
					subdomainsCount.put(key, subdomainsCount.get(key) + 1);
				else
					subdomainsCount.put(key, 1);
			}
		}
	}

	private void processMostCommonElements(List<PageProcessingData> pages, PagesProcessorConfiguration config) {
		for (PageProcessingData page : pages) {
			char[] textChars = page.getText().toCharArray();
			int textLength = textChars.length;
			int wordStartIndex = -1;
			Queue<String> nGramWordsQueue = new LinkedList<String>();

			for (int i = 0; i < textLength; i++) {
				// Only considers alphanumerical characters as valid for terms
				if (Character.isLetterOrDigit(textChars[i])) {
					// Stores the word's first letter index
					if (wordStartIndex < 0)
						wordStartIndex = i;

					// If it reads the last character of the page and it is
					// alphanumerical, then we have a word
					// OBS: It is necessary to increment the current index + 1
					// to allow the substring method to consider the last
					// character
					if (i == textLength - 1) {
						computeWord(config, page, wordStartIndex, nGramWordsQueue, i + 1);

						wordStartIndex = -1;
					}
				}

				// If it hits a non-alphanumerical character and there is a
				// word's first letter index detected, then we have a word
				else if (wordStartIndex >= 0) {
					computeWord(config, page, wordStartIndex, nGramWordsQueue, i);

					wordStartIndex = -1;
				}
			}
		}
	}

	private void computeWord(PagesProcessorConfiguration config, PageProcessingData page, int wordStartIndex, Queue<String> nGramWordsQueue, int i) {
		// Extract the word from the text and converts it to lower case
		String word = page.getText().substring(wordStartIndex, i).toLowerCase();

		// Only considers non stop words
		if (config.getStopWords() == null || !config.getStopWords().contains(word)) {
			// Computes word frequency
			addToMap(mostCommonWords, word);

			// Computes N-gram frequency
			// Enqueues the word
			nGramWordsQueue.add(word);

			if (nGramWordsQueue.size() == config.getNGramsType()) {
				String nGram = String.join(" ", nGramWordsQueue);

				addToMap(mostCommonNGrams, nGram);

				// Dequeues the first enqueued word
				nGramWordsQueue.remove();
			}
		}
	}

	private void addToMap(Map<String, Integer> map, String key) {
		if (map.containsKey(key))
			map.put(key, map.get(key) + 1);
		else
			map.put(key, 1);
	}

	private int processLongestPage(List<PageProcessingData> pages, int longestPageLength) {
		for (PageProcessingData page : pages) {
			if (page.getText().length() > longestPageLength) {
				longestPageLength = page.getText().length();
				longestPageUrl = page.getUrl();
			}
		}

		return longestPageLength;
	}

	private <K, V extends Comparable<? super V>> Map<K, V> sortMapByValueDescending(Map<K, V> map) {
		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		sortedEntries.forEach(e -> sortedMap.put(e.getKey(), e.getValue()));

		return sortedMap;
	}

	private <K, V> Map<K, V> getMapFirstEntries(Map<K, V> map, int elementsToReturn) {
		List<Entry<K, V>> entries = new ArrayList<Entry<K, V>>(map.entrySet());
		Map<K, V> reducedMap = new LinkedHashMap<K, V>();

		entries.stream().limit(elementsToReturn).forEach(e -> reducedMap.put(e.getKey(), e.getValue()));

		return reducedMap;
	}
}
