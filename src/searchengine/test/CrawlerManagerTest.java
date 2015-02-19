package searchengine.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import searchengine.core.crawling.CrawlParameters;
import searchengine.core.crawling.Crawler;
import searchengine.core.crawling.CrawlerManager;
import searchengine.core.crawling.ICrawlControllerBuilder;
import searchengine.core.repository.IPagesRepository;
import searchengine.core.repository.IRepositoriesFactory;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class CrawlerManagerTest {
	private CrawlParameters parameters;
	private ICrawlControllerBuilder crawlControllerBuilder;
	private IRepositoriesFactory repositoriesFactory;
	private CrawlController controller;
	
	@Before
	public final void initialize() throws Exception {
		parameters = mock(CrawlParameters.class);
		crawlControllerBuilder = mock(ICrawlControllerBuilder.class);
		repositoriesFactory = mock(IRepositoriesFactory.class);
		controller = mock(CrawlController.class);
		
		when(parameters.validate()).thenReturn(null);
		when(crawlControllerBuilder.build(parameters)).thenReturn(controller);
		when(repositoriesFactory.getPagesRepository()).thenReturn(mock(IPagesRepository.class));
	}

	@Test
	public final void testRun() throws Exception {
		CrawlerManager manager = new CrawlerManager();
		
		// Act
		manager.Run(parameters, crawlControllerBuilder, repositoriesFactory, Crawler.class);
		
		// Assert
		verify(crawlControllerBuilder).build(parameters);		
		verify(controller).addSeed(parameters.getBaseDomain());
		verify(controller).setCustomData(repositoriesFactory);
		verify(controller).start(Crawler.class, parameters.getNumberOfCrawlers());
	}

}
