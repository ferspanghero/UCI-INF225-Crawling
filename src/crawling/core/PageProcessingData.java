package crawling.core;

/**
 * Represents a lightweight version of a Page that only contains relevant
 * processing data
 */
public class PageProcessingData {
	public PageProcessingData(String url, String text) {
		setUrl(url);
		setText(text);
	}

	private String url;
	private String text;

	public String getUrl() {
		return url;
	}

	public void setUrl(String Url) {
		this.url = Url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
