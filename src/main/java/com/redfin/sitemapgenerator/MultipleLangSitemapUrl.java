package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** One configurable Multilanguage URL.  To configure, use {@link Options}
 * 
 * @author Noel Alonso
 * @see Options
 */
public class MultipleLangSitemapUrl extends WebSitemapUrl {

	//Languages allow 
	private List<String> langs = new ArrayList<String>();
	
	// Default lang
	private String defaultLang;
	
	/** Options to configure langs URLs */
	public static class Options extends AbstractSitemapUrlOptions<MultipleLangSitemapUrl, Options> {
		
		private List<String> langs;
		
		private String defaultLang;
		
		public Options(String url, List<String> langs, String defaultLang) throws MalformedURLException {
			super(new URL(url), MultipleLangSitemapUrl.class);
			this.langs = langs;
			this.defaultLang = defaultLang;
		}
		
		public Options langs(List<String> langs) {
			this.langs = langs;
			return this;
		}
		
		public Options defaultLang(String defaultLang) {
			this.defaultLang = defaultLang;
			return this;
		}
	}

	/** Specifies a landing page URL, together with an image url 
	 *  
	 * @param url the landing page URL
	 * @param imageUrl the URL of the image
	 * @throws MalformedURLException 
	 */
	public MultipleLangSitemapUrl(String url, List<String> langs, String defaultLang) throws MalformedURLException {
		this(new Options(url,langs,defaultLang));
	}
  
	/** Configures the url with options */
	public MultipleLangSitemapUrl(Options options) {
		super(options);
		langs = options.langs;
		defaultLang = options.defaultLang;
	}
	
	
	/** Retrieves the {@link Options#langs}*/
	public List<String> getLangs() {
		return langs;
	}
	
	public String getDefaultLang() {
		return defaultLang;
	}
}
