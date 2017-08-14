package com.redfin.sitemapgenerator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/** One configurable Google Video Search URL.  To configure, use {@link Options}
 * 
 * @author Lakshay Bhambri
 * @see Options
 */
public class GoogleImageSitemapUrl extends WebSitemapUrl {

	private final URL imageUrl;
	private final URL licenseUrl;
	private final String caption;
	private final String geoLocation;
	private final String title;
	
	/** Options to configure Google Image URLs */
	public static class Options extends AbstractSitemapUrlOptions<GoogleImageSitemapUrl, Options> {
		private URL imageUrl;
    private URL licenseUrl;
    private String caption;
    private String geoLocation;
    private String title;
	
		/** Specifies a landing page URL, together with the URL of the underlying video (e.g. FLV)
		 * 
		 * @param url the landing page URL
		 * @param contentUrl the URL of the underlying video (e.g. FLV)
		 */
		public Options(URL url, URL imageUrl) {
			super(url, GoogleImageSitemapUrl.class);
			this.imageUrl = imageUrl;
		}
		
		/** Specifies a player URL (e.g. SWF)
		 *  
		 * @param playerUrl the URL of the "player" (e.g. SWF file)
		 * @param allowEmbed when specifying a player, you must specify whether embedding is allowed
		 */
		public Options imageUrl(URL imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}
		
		/** Specifies the URL of the underlying video (e.g FLV) */
		public Options caption(String caption) {
			this.caption = caption;
			return this;
		}
		
		
		public Options geoLocation(String geoLocation) {
			this.geoLocation = geoLocation;
			return this;
		}
		
		/** The title of the image. Limited to 250 characters. */
		public Options title(String title) {
			if (title != null) {
				if (title.length() > 250) {
					throw new RuntimeException("Video title is limited to 250 characters: " + title);
				}
			}
			this.title = title;
			return this;
		}
		
		/** The description of the video. Descriptions longer than 2048 characters will be truncated. */
		public Options licenseUrl(URL licenseUrl) {
			this.licenseUrl = licenseUrl;
			return this;
		}
		
		
	}

	/** Specifies a landing page URL, together with an image url 
	 *  
	 * @param url the landing page URL
	 * @param imageUrl the URL of the image
	 */
	public GoogleImageSitemapUrl(URL url, URL imageUrl) {
		this(new Options(url,imageUrl));
	}
  
	/** Configures the url with options */
	public GoogleImageSitemapUrl(Options options) {
		super(options);
		locationUrl = options.locationUrl;
		if (locationUrl == null) {
			throw new RuntimeException("You must specify a location url for the image");
		}
    licenseUrl = options.licenseUrl;
    caption = options.caption;
    geoLocation = options.geoLocation;
    title = options.title;
	}
	
	private static String convertBooleanToYesOrNo(Boolean value) {
		if (value == null) return null;
		return value ? "Yes" : "No";
	}
	
	
	/** Retrieves the {@link Options#locationUrl}*/
	public URL getLocationUrl() {
		return locationUrl;
	}
  
  /** Retrieves the {@link Options#licenseUrl}*/
	public URL getLicenseUrl() {
		return licenseUrl;
	}
  
  /** Retrieves the {@link Options#caption}*/
	public String getCaption() {
		return caption;
	}
  
  /** Retrieves the {@link Options#geoLocation}*/
	public String getGeoLocation() {
		return geoLocation;
	}
	
  /** Retrieves the {@link Options#title}*/
	public String getTitle() {
		return title;
	}
  

}
