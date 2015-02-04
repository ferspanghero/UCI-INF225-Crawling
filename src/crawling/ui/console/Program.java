package crawling.ui.console;

import java.util.Scanner;

import crawling.core.*;

public class Program {

	public static void main(String[] args) {
		CrawlerController controller = new CrawlerController();		
		
		try {
			// TODO: see how logger can be injected into the controller
			// TODO: see how the Crawler can be injected into the controller (ICrawlerBuilder?)
			// controller.Run();
			System.out.println("Hello!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
