package searchengine.ui.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import searchengine.core.BatchedPagesSearchEngine;
import searchengine.core.repository.IRepositoriesFactory;
import searchengine.core.repository.MySQLRepositoriesFactory;

/**
 * Servlet implementation class SearchEngineServlet
 */
@WebServlet("/SearchEngineServlet")
public class SearchEngineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int RESULTS_PER_PAGE = 20;
	private BatchedPagesSearchEngine searchEngine;
	private IRepositoriesFactory repositoriesFactory;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchEngineServlet() {
		super();
		
		searchEngine = new BatchedPagesSearchEngine(RESULTS_PER_PAGE);
		repositoriesFactory = new MySQLRepositoriesFactory();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String query = null;
		int pagesBatchIndex = 1;

		if (!(request.getParameter("q") == null))
			query = request.getParameter("q");

		if (request.getParameter("page") != null)
			pagesBatchIndex = Integer.parseInt(request.getParameter("page"));

		searchEngine.setPagesBatchIndex(pagesBatchIndex);

		List<String> pagesUrls = null;

		try {
			pagesUrls = searchEngine.search(repositoriesFactory, query); // however you get the data*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		int numOfPages = (int) Math.ceil(searchEngine.getTotalNumberOfPages() * 1.0 / RESULTS_PER_PAGE);
		int listStartNumber = (pagesBatchIndex - 1) * RESULTS_PER_PAGE + 1;

		// set the attributes in the request to access it on the JSP
		request.setAttribute("query", query);
		request.setAttribute("listData", pagesUrls);
		request.setAttribute("numOfPages", numOfPages);
		request.setAttribute("listStart", listStartNumber);
		request.setAttribute("currentPage", pagesBatchIndex);

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/results.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
