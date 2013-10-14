package org.lmtx.util;

import java.util.ArrayList;
import java.util.List;

import org.lmtx.data.RssItem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


public class RssParseHandler extends DefaultHandler {

	private List<RssItem> rssItems;
	
	// Used to reference item while parsing
	private RssItem currentItem;
	
	// Parsing title indicator
	private boolean parsingTitle;
	// A buffer used to build current title being parsed
	private StringBuffer currentTitleSb,currentDescriptionSb;
	
	// Parsing link indicator
	private boolean parsingLink;

	// Parsing description indicator
	private boolean parsingDescription;
	
	public RssParseHandler() {
		rssItems = new ArrayList<RssItem>();
	}
	
	public List<RssItem> getItems() {
		return rssItems;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("item".equals(qName)) {
			currentItem = new RssItem();
		} else if ("title".equals(qName)) {
			parsingTitle = true;
			
			currentTitleSb = new StringBuffer();
		} else if ("link".equals(qName)) {
			parsingLink = true;
		} else if ("description".equals(qName)) {
			parsingDescription = true;
			currentDescriptionSb = new StringBuffer();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("item".equals(qName)) {
			rssItems.add(currentItem);
			currentItem = null;
		} else if ("title".equals(qName)) {
			
			parsingTitle = false;
			
			// Set item's title when we parse item->title tag not the channel title tag
			if (currentItem != null) {
				// Set item's title here
				currentItem.setTitle(currentTitleSb.toString());
			}
			
		} else if ("description".equals(qName)) {
			
			parsingDescription = false;
			
			// Set item's description when we parse item->description tag not the channel description tag
			if (currentItem != null) {
				// Set item's description here
				currentItem.setDescription(currentDescriptionSb.toString());
			}
			
		} else if ("link".equals(qName)) {
			parsingLink = false;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (parsingTitle) {
			if (currentItem != null) {
				// Here we append the title to the buffer due to network issues.
				// Sometimes this characters method is called multiple times for a tag contents.
				currentTitleSb.append(new String(ch, start, length));
			}
		} else if (parsingLink) {
			if (currentItem != null) {
				currentItem.setLink(new String(ch, start, length));
				parsingLink = false;
			}
		} else if (parsingDescription) {
			if (currentItem != null) {
				currentDescriptionSb.append(new String(ch, start, length));
				Log.d("PARSE",currentDescriptionSb.toString());
				parsingDescription = false;
			}
		}
	}
	
}