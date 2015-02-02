/* Some of this code is lifted directly from the crawler4j website and we do not profess
 * to have done it ourselves.
 * */
package ui.console;
import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.robotstxt.*;
import edu.uci.ics.crawler4j.fetcher.*;

public class CrawlerController {

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = ".\\data\\crawl\\root";
        String userAgent = "UCI WebCrawler 93082117/30489978/rahan";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(300); //Set it to 300ms as per instructions
        config.setUserAgentString(userAgent); 
        
        /* Not sure if below is necessary
        confsigh.setProxyHost("https:////vpn.nacs.uci.edu//+CSCOE+//logon.html");
        config.setProxyUsername(rsingerh); 
        config.getProxyPassword(password);
        */

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */

        controller.addSeed("http://localhost:80/index.html");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        
        controller.start(MyCrawler.class, numberOfCrawlers);    
}
}
