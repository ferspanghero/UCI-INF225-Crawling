package searchengine.core;

/**
 * Represents a lightweight version of a Page that only contains relevant
 * processing data
 */
public class PageProcessingData {
	public PageProcessingData(String url, String text, String html) {
		setUrl(url);
		setText(text);
		setHtml(html);
	}

	private String url;
	private String text;
	private String html;

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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

}
