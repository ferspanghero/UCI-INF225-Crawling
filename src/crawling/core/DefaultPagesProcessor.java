package crawling.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a basic processor that does a set of operations with the crawled
 * pages
 */
public class DefaultPagesProcessor implements IPagesProcessor {
	public DefaultPagesProcessor() {
		pagesCount = 0;
		longestPageUrl = null;
		subdomainsCount = new HashMap<String, Integer>();
		mostCommonWords = new HashMap<String, Integer>();
		mostCommonNGrams = new HashMap<String, Integer>();
	}

	private final int PAGES_CHUNK_SIZE = 100;
	private int pagesCount;
	private String longestPageUrl;
	private HashMap<String, Integer> subdomainsCount;
	private HashMap<String, Integer> mostCommonWords;
	private HashMap<String, Integer> mostCommonNGrams;

	@Override
	public void processPages(IPagesRepository repository, PagesProcessorConfiguration config) {
		if (repository == null)
			throw new IllegalArgumentException("The pages processor cannot be initialized with a null pages repository");

		if (config == null)
			throw new IllegalArgumentException("The pages processor cannot be initialized with a null configuration");

		int longestPageLength = 0;
		List<PageProcessingData> pages = repository.retrieveNextPages(PAGES_CHUNK_SIZE);

		while (pages != null && pages.size() > 0) {
			// Computes pages count
			// TODO: Make sure that duplications will be treated in the
			// repository level
			processUniquePagesCount(pages);

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
	public Map<String, Integer> getSubdomainsCount() {
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
		return Collections.unmodifiableMap(getMapFirstEntries(sortMapByValue(mostCommonWords), count));
	}

	@Override
	public Map<String, Integer> getMostCommonNGrams(int count) {
		// Makes sure an unmodifiable result is returned
		return Collections.unmodifiableMap(getMapFirstEntries(sortMapByValue(mostCommonNGrams), count));
	}

	private void processUniquePagesCount(List<PageProcessingData> pages) {
		pagesCount += pages.size();
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
					if (i == textLength - 1)
						computeWord(config, page, wordStartIndex, nGramWordsQueue, i);
				}

				// If it hits a non-alphanumerical character and there is a
				// word's first letter index detected, then we have a word
				else if (wordStartIndex >= 0) {
					computeWord(config, page, wordStartIndex, nGramWordsQueue, i);
				}
			}
		}
	}

	private void computeWord(PagesProcessorConfiguration config, PageProcessingData page, int wordStartIndex, Queue<String> nGramWordsQueue, int i) {
		// Extract the word from the text and converts it to lower case
		String word = page.getText().substring(wordStartIndex, i - 1).toLowerCase();

		// Only considers non stop words
		if (!config.getStopWords().contains(word)) {
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

		wordStartIndex = -1;
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

	private <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

	private <K, V> Map<K, V> getMapFirstEntries(Map<K, V> map, int elementsToReturn) {
		return map
				.entrySet()
				.stream()
				.limit(elementsToReturn)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));
	}
}
