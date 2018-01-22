package main;

import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class BasicCrawler extends WebCrawler
{
    private static final Logger	 logger	 = LoggerFactory.getLogger(BasicCrawler.class);
    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
	    + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private String[]		 myCrawlDomains;
    private BenimURL		 benimURL;
    public static Set<WebURL>	 links;


    @Override
    public void onStart()
    {
	myCrawlDomains = (String[]) myController.getCustomData( );
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url)
    {
	String href = url.getURL( ).toLowerCase( );
	if(FILTERS.matcher(href).matches( )) { return false; }

	for (String crawlDomain : myCrawlDomains)
	{
	    if(href.startsWith(crawlDomain)) { return true; }
	}

	return false;
    }

    @Override
    public void visit(Page page)
    {
	int parentDocid = page.getWebURL( ).getParentDocid( );
	int docid = page.getWebURL( ).getDocid( );
	String urlStr = page.getWebURL( ).getURL( );

	benimURL = new BenimURL(docid, parentDocid, urlStr);

	if(page.getParseData( ) instanceof HtmlParseData)
	{
	    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData( );
	    links = htmlParseData.getOutgoingUrls( );

	    logger.debug("Number of outgoing links: {}", links.size( ));
	}

	System.out.println("url beklenen : " + benimURL);
	logger.debug("=============");
    }



}
