package com.redfin.sitemapgenerator;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.redfin.sitemapgenerator.MultipleLangSitemapUrl.Options;

import junit.framework.TestCase;

public class MultipleLangSitemapUrlTest extends TestCase {
	
	private static final String HOST = "https://example.com",
			URL = "https://example.com/inner-terms-and-conditions";
	File dir;
	
	List<String> langs = Arrays.asList("es", "en");
	
	String defaultLang = "es";
	
	MultipleLangSitemapGenerator wsg;
	
	public void setUp() throws Exception {
		dir = File.createTempFile(MultipleLangSitemapUrlTest.class.getSimpleName(), "");
		dir.delete();
		dir.mkdir();
		dir.deleteOnExit();
	}
	
	public void tearDown() {
		wsg = null;
		for (File file : dir.listFiles()) {
			file.deleteOnExit();
			file.delete();
		}
		dir.delete();
		dir = null;
	}
	
	public void testSimpleUrl() throws Exception {
		wsg = new MultipleLangSitemapGenerator(HOST, dir);
		MultipleLangSitemapUrl url = new MultipleLangSitemapUrl(URL, langs, defaultLang);
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" \n"+
			"  xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n" + 
			"  <url>\n" + 
			"    <loc>https://example.com/inner-terms-and-conditions</loc>\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"es\"\n" +
	        "      href=\"https://example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"en\"\n" +
	        "      href=\"https://en.example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"  </url>\n" +
			"  <url>\n" + 
			"    <loc>https://en.example.com/inner-terms-and-conditions</loc>\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"es\"\n" +
	        "      href=\"https://example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"en\"\n" +
	        "      href=\"https://en.example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"  </url>\n" +
			"</urlset>";
		
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}

	public void testOptions() throws Exception {
		wsg = MultipleLangSitemapGenerator.builder(HOST, dir).build();
		MultipleLangSitemapUrl url = new Options(URL, langs, defaultLang).build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" \n"+
			"  xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n" + 
			"  <url>\n" + 
			"    <loc>https://example.com/inner-terms-and-conditions</loc>\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"es\"\n" +
	        "      href=\"https://example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"en\"\n" +
	        "      href=\"https://en.example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"  </url>\n" +
			"  <url>\n" + 
			"    <loc>https://en.example.com/inner-terms-and-conditions</loc>\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"es\"\n" +
	        "      href=\"https://example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"    <xhtml:link\n" + 
	        "      rel=\"alternate\"\n" +
	        "      hreflang=\"en\"\n" +
	        "      href=\"https://en.example.com/inner-terms-and-conditions\"\n" + 
			"    />\n" + 
			"  </url>\n" +
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	private String writeSingleSiteMap(MultipleLangSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
