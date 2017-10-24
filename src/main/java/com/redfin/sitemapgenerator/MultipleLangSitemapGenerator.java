package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.redfin.sitemapgenerator.MultipleLangSitemapUrl.Options;

/**
 * Builds a image sitemap for Multilanguage website. To configure options, use
 * {@link #builder(URL, File)}
 * 
 * @author Noel Alonso
 */
public class MultipleLangSitemapGenerator
		extends SitemapGenerator<MultipleLangSitemapUrl, MultipleLangSitemapGenerator> {

	MultipleLangSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
		super(options, new Renderer());
	}

	/**
	 * Configures the generator with a base URL and directory to write the
	 * sitemap files.
	 * 
	 * @param baseUrl
	 *            All URLs in the generated sitemap(s) should appear under this
	 *            base URL
	 * @param baseDir
	 *            Sitemap files will be generated in this directory as either
	 *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @throws MalformedURLException
	 */
	public MultipleLangSitemapGenerator(String baseUrl, File baseDir) throws MalformedURLException {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/**
	 * Configures the generator with a base URL and directory to write the
	 * sitemap files.
	 * 
	 * @param baseUrl
	 *            All URLs in the generated sitemap(s) should appear under this
	 *            base URL
	 * @param baseDir
	 *            Sitemap files will be generated in this directory as either
	 *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 */
	public MultipleLangSitemapGenerator(URL baseUrl, File baseDir) {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/**
	 * Configures the generator with a base URL and a null directory. The object
	 * constructed is not intended to be used to write to files. Rather, it is
	 * intended to be used to obtain XML-formatted strings that represent
	 * sitemaps.
	 * 
	 * @param baseUrl
	 *            All URLs in the generated sitemap(s) should appear under this
	 *            base URL
	 */
	public MultipleLangSitemapGenerator(String baseUrl) throws MalformedURLException {
		this(new SitemapGeneratorOptions(new URL(baseUrl)));
	}

	/**
	 * Configures the generator with a base URL and a null directory. The object
	 * constructed is not intended to be used to write to files. Rather, it is
	 * intended to be used to obtain XML-formatted strings that represent
	 * sitemaps.
	 * 
	 * @param baseUrl
	 *            All URLs in the generated sitemap(s) should appear under this
	 *            base URL
	 */
	public MultipleLangSitemapGenerator(URL baseUrl) {
		this(new SitemapGeneratorOptions(baseUrl));
	}

	/**
	 * Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl
	 *            All URLs in the generated sitemap(s) should appear under this
	 *            base URL
	 * @param baseDir
	 *            Sitemap files will be generated in this directory as either
	 *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<MultipleLangSitemapGenerator> builder(URL baseUrl, File baseDir) {
		return new SitemapGeneratorBuilder<MultipleLangSitemapGenerator>(baseUrl, baseDir,
				MultipleLangSitemapGenerator.class);
	}

	/**
	 * Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl
	 *            All URLs in the generated sitemap(s) should appear under this
	 *            base URL
	 * @param baseDir
	 *            Sitemap files will be generated in this directory as either
	 *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 * @throws MalformedURLException
	 */
	public static SitemapGeneratorBuilder<MultipleLangSitemapGenerator> builder(String baseUrl, File baseDir)
			throws MalformedURLException {
		return new SitemapGeneratorBuilder<MultipleLangSitemapGenerator>(baseUrl, baseDir,
				MultipleLangSitemapGenerator.class);
	}

	private static class Renderer extends AbstractSitemapUrlRenderer<MultipleLangSitemapUrl>
			implements ISitemapUrlRenderer<MultipleLangSitemapUrl> {

		public Class<MultipleLangSitemapUrl> getUrlClass() {
			return MultipleLangSitemapUrl.class;
		}

		public String getXmlNamespaces() {
			return "\n  xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"";
		}

		public void render(MultipleLangSitemapUrl url, StringBuilder sb, W3CDateFormat dateFormat) {

			List<String> langs = url.getLangs();

			URL originalUrl = url.getUrl();

			for (String langForUrl : langs) {
				//new config for each URL
				url = newUrl(url, originalUrl, langForUrl);
				
				StringBuilder tagSb = new StringBuilder();

				for (String langForTag : langs) {
					addAlternate(tagSb, langForTag, getUrlByLang(originalUrl, langForTag, url.getDefaultLang()));
				}
				super.render(url, sb, dateFormat, tagSb.toString());
			}
		}

		/*
		 * Add lang tags
		 */

		private void addAlternate(StringBuilder tagSb, String lang, String url) {
			tagSb.append("    <xhtml:link\n");
			tagSb.append("      rel=\"alternate\"\n");
			tagSb.append("      hreflang=\"" + lang + "\"\n");
			tagSb.append("      href=\"" + url + "\"\n");
			tagSb.append("    />\n");
		}

	}
	
	/*
	 * Copy config for add new url tag with other language
	 * */
	public static MultipleLangSitemapUrl newUrl(MultipleLangSitemapUrl url, URL originalUrl, String lang) {
		
		try {
			String newUrl = getUrlByLang(originalUrl, lang, url.getDefaultLang());
			return new Options(newUrl, url.getLangs(), url.getDefaultLang())
				.changeFreq(url.getChangeFreq())
				// .priority(url.getPriority()) //TODO: fail when priority is null
				.lastMod(url.getLastMod())
				.build();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/*
	 * Create url depend on lang and defaultLang 
	 */

	public static String getUrlByLang(URL url, String lang, String defaultLang) {

		if (lang.equals(defaultLang)) {
			return url.toString();
		}

		// TODO: support others language url format. This case is a different language per domain
		return url.toString().replaceAll("//", "//" + lang + ".");
	}
}
