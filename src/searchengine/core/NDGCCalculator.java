package searchengine.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import searchengine.core.repository.IRepositoriesFactory;

/**
 * Represents a calculator of nDCG values
 */
public class NDGCCalculator {
	public NDGCCalculator() {
		googleUrls = null;
		customSearchEngineUrls = null;
		customSearchEngineNDCG = 0d;
		accumulatedCustomSearchEngineNDCG = 0d;
	}

	private List<String> googleUrls;
	private List<String> customSearchEngineUrls;
	private double customSearchEngineNDCG;
	private double accumulatedCustomSearchEngineNDCG;

	public List<String> getGoogleUrls() {
		return googleUrls;
	}

	public List<String> getCustomSearchEngineUrls() {
		return customSearchEngineUrls;
	}

	public double getCustomSearchEngineNDCG() {
		return customSearchEngineNDCG;
	}

	public double getAccumulatedCustomSearchEngineNDCG() {
		return accumulatedCustomSearchEngineNDCG;
	}

	public void calculate(IRepositoriesFactory repositoriesFactory, String query, int searchQueryResultsLimit) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, IOException {
		if (repositoriesFactory == null)
			throw new IllegalArgumentException("The search engine cannot be initialized with a null repositories factory");

		if (searchQueryResultsLimit <= 0)
			throw new IllegalArgumentException("The search query results limit cannot be lower than 1");

		googleUrls = retrieveGoogleUrls(query, searchQueryResultsLimit);
		customSearchEngineUrls = retrieveCustomSearchEngineUrls(repositoriesFactory, query, searchQueryResultsLimit);

		Map<String, Integer> googleRelevancyScores = new HashMap<String, Integer>();

		customSearchEngineNDCG = 0d;

		if (googleUrls != null) {
			int i = searchQueryResultsLimit;
			int j = 1;
			double idealDCG = 0d;

			for (String googleUrl : googleUrls) {
				googleRelevancyScores.put(googleUrl, i);

				// Gets the iDCG for later normalization
				idealDCG += (Math.pow(2, i--) - 1) / (Math.log(j + 1) / Math.log(2));

				j++;
			}

			if (customSearchEngineUrls != null) {
				int k = 1;

				for (String customSearchEngineUrl : customSearchEngineUrls) {
					double googleUrlRelevancyScore = googleRelevancyScores.containsKey(customSearchEngineUrl) ? googleRelevancyScores.get(customSearchEngineUrl) : 0d;

					if (googleUrlRelevancyScore > 0d) {
						// Formula: dcg / idcg = (googleRelevancyScore / logBase2(googleRelevancyScore)) / idcg
						customSearchEngineNDCG += ((Math.pow(2, googleUrlRelevancyScore) - 1) / (Math.log(k + 1) / Math.log(2))) / idealDCG;
					}

					k++;
				}

				accumulatedCustomSearchEngineNDCG += customSearchEngineNDCG;
			}
		}
	}

	private List<String> retrieveGoogleUrls(String query, int searchQueryResultsLimit) throws UnsupportedEncodingException, IOException {
		String google = "http://www.google.com/search?q=";
		String search = "site:ics.uci.edu " + query;
		String charset = "UTF-8";
		String userAgent = "UCI INF225 bot 1.0 (+http://www.ics.uci.edu/~lopes/teaching/cs221W15/)";
		List<String> googleUrls = new ArrayList<String>(searchQueryResultsLimit);

		Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select("li.g>h3>a");

		int i = 1;

		for (Element link : links) {
			String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
			url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

			if (url.startsWith("http") && !url.endsWith(".pdf") && !url.endsWith(".ppt")) {
				googleUrls.add(url);

				i++;

				if (i > searchQueryResultsLimit)
					break;
			}
		}

		return googleUrls;
	}

	private List<String> retrieveCustomSearchEngineUrls(IRepositoriesFactory repositoriesFactory, String query, int searchQueryResultsLimit) throws ClassNotFoundException, SQLException {
		BatchedPagesSearchEngine searchEngine = new BatchedPagesSearchEngine(searchQueryResultsLimit);
		List<SearchedPage> searchedPages;

		searchEngine.setPagesBatchIndex(1);
		searchedPages = searchEngine.search(repositoriesFactory, query);

		return searchedPages != null ? searchedPages.stream().map(page -> page.getURL()).collect(Collectors.toList()) : null;
	}
}
