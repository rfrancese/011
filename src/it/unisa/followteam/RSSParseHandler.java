package it.unisa.followteam;

import it.unisa.followteam.support.RSSItem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import android.os.AsyncTask;

public class RSSParseHandler extends AsyncTask<String, Void, ArrayList<RSSItem>>{

	@Override
	protected ArrayList<RSSItem> doInBackground(String... feedUrl) {
		 ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
         
	        try {
	             
	            URL url = new URL(feedUrl[0]);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	             
	            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                InputStream is = conn.getInputStream();
	                 
	                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	                DocumentBuilder db = dbf.newDocumentBuilder();
	                 
	                 
	                Document document = db.parse(is);
	                Element element = document.getDocumentElement();
	                 
	                 
	                NodeList nodeList = element.getElementsByTagName("item");
	                 
	                if (nodeList.getLength() > 0) {
	                    for (int i = 0; i < nodeList.getLength(); i++) {
	                         
	                        Element entry = (Element) nodeList.item(i);
	                        Element _titleE       = (Element)entry.getElementsByTagName("title").item(0);
	                        Element _descriptionE = (Element)entry.getElementsByTagName("description").item(0);
	                        Element _pubDateE       = (Element) entry.getElementsByTagName("pubDate").item(0);
	                        Element _linkE           = (Element) entry.getElementsByTagName("link").item(0);
	                        String _title           = _titleE.getFirstChild().getNodeValue();
	                        String _description   = _descriptionE.getFirstChild().getNodeValue();
	                        Date _pubDate           = new Date(_pubDateE.getFirstChild().getNodeValue());
	                        String _link           = _linkE.getFirstChild().getNodeValue();
	                         
	                        RSSItem rssItem = new RSSItem(_title, _description, _pubDate, _link);
	                         
	                        rssItems.add(rssItem);
	                         
	                    }
	                }
	                 
	            }
	             
	            } catch (Exception e) {
	             
	            e.printStackTrace();
	             
	        }
	         
	        return rssItems;
	    }
}
