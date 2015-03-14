package searchengine.core;

/**
 * Represents a page returned as a result of a search
 */
public class SearchedPage {
	public SearchedPage(int pageId, String URL, double rankingScore) {
		setPageId(pageId);
		setURL(URL);
		setRankingScore(rankingScore);
	}

	private int pageId;
	private String URL;
	private double rankingScore;

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public double getRankingScore() {
		return rankingScore;
	}

	public void setRankingScore(double rankingScore) {
		this.rankingScore = rankingScore;
	}
}
