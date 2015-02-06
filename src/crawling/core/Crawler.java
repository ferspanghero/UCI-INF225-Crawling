package crawling.core;

import java.util.List;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.parser.*;
import edu.uci.ics.crawler4j.url.*;
import java.io.*;

/**
 * Represents a crawler that visits and collects information about web pages
 */
public class Crawler extends WebCrawler {

	
	private static int count = 0;

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|csv|data|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf|pde"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");


	private IPagesRepository repository;
	
	@Override
	public void onStart() { 
		Object data = myController.getCustomData();
		
		if (!(data instanceof IPagesRepository)) {
			throw new IllegalArgumentException("The web crawler must be supplied with a valid pages repository");
		}
		
		repository = (IPagesRepository)data;
	}
	

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS.matcher(href).matches()
				&& href.contains("ics.uci.edu")
				&& !href.contains("?");

	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		// TODO: uncomment the line below when the repository is properly implemented
		// repository.insertPage(page);
		
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
			System.out.println("Link number: " + count++);
			
			try{
			PrintWriter writer = new PrintWriter((new BufferedWriter(new FileWriter("output/myfile.txt", true))));
			writer.println(url);
			writer.println(text);
			writer.println("\n");
			writer.close();
			
			PrintWriter writer2 = new PrintWriter((new BufferedWriter(new FileWriter("output/file"+ count + ".txt"))));
			writer2.println(url);
			writer2.println(html);
			writer2.close();
			} catch (IOException e) {
				
				e.printStackTrace();
				System.exit(0);
			} finally {
				
			}

		}
	}

}
