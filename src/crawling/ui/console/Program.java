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

						Thread.sleep(1000);

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
							Map<String, Integer> mostFrequentWords = processor.getMostCommonWords(500);
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
							Map<String, Integer> mostFrequent2Grams = processor.getMostCommonNGrams(20);
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
		System.out.println("(5) - Display top 500 most frequent words");
		System.out.println("(6) - Display top 20 most frequent 2-grams");
		System.out.println("(7) - Save subdomains");

		System.out.println("(0) - Exit");
	}

	private static void crawl(IPagesRepository repository) throws Exception {
		CrawlerManager manager = new CrawlerManager();
		CrawlConfig config = new CrawlConfig();
		ICrawlControllerBuilder crawlControllerBuilder = new DefaultCrawlControllerBuilder();

		config.setCrawlStorageFolder(".\\data\\crawl\\root");
		config.setPolitenessDelay(500);
		config.setMaxDepthOfCrawling(40);
		config.setMaxPagesToFetch(10);
		config.setUserAgentString("UCI WebCrawler 93082117/30489978/12409858");
		config.setResumableCrawling(true);

		manager.Run(new CrawlParameters(config, 10, "http://www.ics.uci.edu"), crawlControllerBuilder, repository, Crawler.class);
	}

	private static IPagesProcessor processPages(IPagesRepository repository) throws SQLException {
		IPagesProcessor processor = new DefaultPagesProcessor();
		HashSet<String> stopWords = new HashSet<String>();

		try (Scanner scanner = new Scanner(DefaultPagesProcessorTest.class.getResourceAsStream("/resources/stopwords.txt"))) {
			while (scanner.hasNextLine()) {
				stopWords.add(scanner.nextLine());
			}
		}

		processor.processPages(repository, new PagesProcessorConfiguration(stopWords, 2));

		return processor;
	}
}
