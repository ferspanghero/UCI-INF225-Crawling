package crawling.ui.console;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import crawling.core.CrawlParameters;
import crawling.core.Crawler;
import crawling.core.CrawlerManager;
import crawling.core.DefaultCrawlControllerBuilder;
import crawling.core.DefaultPagesProcessor;
import crawling.core.ICrawlControllerBuilder;
import crawling.core.IPagesProcessor;
import crawling.core.IPagesRepository;
import crawling.core.MySQLPagesRepository;
import crawling.core.PagesProcessorConfiguration;
import crawling.test.DefaultPagesProcessorTest;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;

public class Program {
	private final static int POLITENESS_DELAY = 500;
	private final static int MAX_DEPTH_OF_CRAWLING = 40;
	private final static int MAX_PAGES_TO_FETCH = 10;
	private final static int NUMBER_OF_CRAWLERS = 5;
	private final static int N_GRAM_TYPE = 2;
	private final static int MOST_FREQUENT_WORDS_COUNT = 500;
	private final static int MOST_FREQUENT_N_GRAMS_COUNT = 20;
	private final static String CRAWLING_AGENT_NAME = "UCI WebCrawler 93082117/30489978/12409858";
	private final static String BASE_DOMAIN = "http://www.ics.uci.edu";

	public static void main(String[] args) {
		try {
			int option;
			final String NOT_PROCESSED_ERROR_MESSAGE = "\nPages must be processed first\n";
			IPagesRepository repository = new MySQLPagesRepository();
			IPagesProcessor processor = null;

			try (Scanner stdin = new Scanner(System.in)) {
				do {
					printOptions();

					option = stdin.nextInt();

					switch (option) {
					case 1:
						long startTime = System.currentTimeMillis();

						crawl(repository);

						long elapsedTime = System.currentTimeMillis() - startTime;

						String formattedElapsedTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedTime),
								TimeUnit.MILLISECONDS.toMinutes(elapsedTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
								TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));

						System.out.println("\nPages crawled in " + formattedElapsedTime + "\n");
						break;
					case 2:
						processor = processPages(repository);

						System.out.println("\nPages processed\n");
						break;
					case 3:
						if (processor == null) {
							System.out.println(NOT_PROCESSED_ERROR_MESSAGE);
						} else {
							System.out.println("\n" + processor.getUniquePagesCount() + "\n");
						}
						break;
					case 4:
						if (processor == null) {
							System.out.println(NOT_PROCESSED_ERROR_MESSAGE);
						} else {
							System.out.println("\n" + processor.getLongestPage() + "\n");
						}
						break;
					case 5:
						if (processor == null) {
							System.out.println(NOT_PROCESSED_ERROR_MESSAGE);
						} else {
							Map<String, Integer> mostFrequentWords = processor.getMostCommonWords(MOST_FREQUENT_WORDS_COUNT);
							Set<Entry<String, Integer>> entries = mostFrequentWords.entrySet();

							System.out.println();

							for (Entry<String, Integer> entry : entries) {
								System.out.println(entry.getKey() + " - " + entry.getValue());
							}

							System.out.println();
						}
						break;
					case 6:
						if (processor == null) {
							System.out.println(NOT_PROCESSED_ERROR_MESSAGE);
						} else {
							Map<String, Integer> mostFrequent2Grams = processor.getMostCommonNGrams(MOST_FREQUENT_N_GRAMS_COUNT);
							Set<Entry<String, Integer>> entries = mostFrequent2Grams.entrySet();

							System.out.println();

							for (Entry<String, Integer> entry : entries) {
								System.out.println(entry.getKey() + " - " + entry.getValue());
							}

							System.out.println();
						}
						break;
					case 7:
						if (processor == null) {
							System.out.println(NOT_PROCESSED_ERROR_MESSAGE);
						} else {
							Map<String, Integer> mostFrequent2Grams = processor.getSubdomainsCount();
							Set<Entry<String, Integer>> entries = mostFrequent2Grams.entrySet();

							System.out.println();

							try (PrintWriter printer = new PrintWriter("Subdomains.txt")) {
								for (Entry<String, Integer> entry : entries) {
									printer.println(entry.getKey() + " - " + entry.getValue());
								}
							}

							System.out.println();
						}
						break;
					}
				} while (option != 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printOptions() {
		System.out.println("(1) - Crawl UCI's domain");
		System.out.println("(2) - Process crawled pages data");
		System.out.println("(3) - Display unique pages count");
		System.out.println("(4) - Display longest page URL");
		System.out.println("(5) - Display top " + MOST_FREQUENT_WORDS_COUNT + " most frequent words");
		System.out.println("(6) - Display top " + MOST_FREQUENT_N_GRAMS_COUNT + " most frequent " + N_GRAM_TYPE + "-grams");
		System.out.println("(7) - Save subdomains");

		System.out.println("(0) - Exit");
	}

	private static void crawl(IPagesRepository repository) throws Exception {
		CrawlerManager manager = new CrawlerManager();
		CrawlConfig config = new CrawlConfig();
		ICrawlControllerBuilder crawlControllerBuilder = new DefaultCrawlControllerBuilder();

		config.setCrawlStorageFolder(".\\data\\crawl\\root");
		config.setPolitenessDelay(POLITENESS_DELAY);
		config.setMaxDepthOfCrawling(MAX_DEPTH_OF_CRAWLING);
		config.setMaxPagesToFetch(MAX_PAGES_TO_FETCH);
		config.setUserAgentString(CRAWLING_AGENT_NAME);
		config.setResumableCrawling(true);

		manager.Run(new CrawlParameters(config, NUMBER_OF_CRAWLERS, BASE_DOMAIN), crawlControllerBuilder, repository, Crawler.class);
	}

	private static IPagesProcessor processPages(IPagesRepository repository) throws SQLException {
		IPagesProcessor processor = new DefaultPagesProcessor();
		HashSet<String> stopWords = new HashSet<String>();

		try (Scanner scanner = new Scanner(DefaultPagesProcessorTest.class.getResourceAsStream("/resources/stopwords.txt"))) {
			while (scanner.hasNextLine()) {
				stopWords.add(scanner.nextLine());
			}
		}

		processor.processPages(repository, new PagesProcessorConfiguration(stopWords, N_GRAM_TYPE));

		return processor;
	}
}
