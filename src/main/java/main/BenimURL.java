package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;


public class BenimURL implements Comparator<BenimURL>
{
    private int		   sirasi, docid, parentDocid;
    private float	   puani;
    private String	   htmlStr, firstP, firstH;
    private URL		   url;
    private List<Integer>  anhtrKlmlrnBlnmaSayisi;
    private List<BenimURL> altURLleri;
    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
	    + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    
    public BenimURL(URL url)
    {
	this.url = url;
    }
    
    public BenimURL(String urlStr)
    {
	try
	{
	    this.url = new URL(urlStr);
	}
	catch (MalformedURLException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace( );
	}
    }
    
    public BenimURL(int sirasi, float puani, URL url, List<Integer> anhtrKlmlrnBlnmaSayisi)
    {
	this.sirasi = sirasi;
	this.puani = puani;
	this.url = url;
	this.anhtrKlmlrnBlnmaSayisi = anhtrKlmlrnBlnmaSayisi;
    }
    
    public BenimURL(int docid, int parentDocid, String urlStr)
    {
	super( );
	this.docid = docid;
	this.parentDocid = parentDocid;
	try
	{
	    this.url = new URL(urlStr);
	}
	catch (MalformedURLException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace( );
	}
    }
    
    public URL getUrl()
    {
	return url;
    }
    
    public void setUrl(URL url)
    {
	this.url = url;
    }
    
    public List<BenimURL> getAltURLleri()
    {
	if(altURLleri == null)
	{
	    try
	    {
		AlAltUrlleri( );
	    }
	    catch (Exception e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace( );
	    }
	}

	return altURLleri;
    }
    
    public void setAltURLleri(List<BenimURL> altURLleri)
    {
	this.altURLleri = altURLleri;
    }
    
    public List<Integer> getAnhtrKlmlrnBlnmaSayisi()
    {
	return anhtrKlmlrnBlnmaSayisi;
    }
    
    public String getHtmlStr()
    {
	return htmlStr;
    }
    
    public void setHtmlStr(String htmlStr)
    {
	this.htmlStr = htmlStr;
    }
    
    public String getFirstP()
    {
	return firstP;
    }

    public void setFirstP(String firstP)
    {
	this.firstP = firstP;
    }

    public String getFirstH()
    {
	return firstH;
    }
    
    public void setFirstH(String firstH)
    {
	this.firstH = firstH;
    }
    
    public void setAnhtrKlmlrnBlnmaSayisi(List<Integer> anhtrKlmlrnBlnmaSayisi)
    {
	this.anhtrKlmlrnBlnmaSayisi = anhtrKlmlrnBlnmaSayisi;
    }
    
    public int getDocid()
    {
	return docid;
    }
    
    public void setDocid(int docid)
    {
	this.docid = docid;
    }
    
    public int getParentDocid()
    {
	return parentDocid;
    }
    
    public void setParentDocid(int parentDocid)
    {
	this.parentDocid = parentDocid;
    }
    
    public float getPuani()
    {
	return puani;
    }
    
    public void setPuani(float puani)
    {
	this.puani = puani;
    }
    
    public int getSirasi()
    {
	return sirasi;
    }
    
    public void setSirasi(int sirasi)
    {
	this.sirasi = sirasi;
    }
    
    @Override
    public String toString()
    {
	return url.toString( );
    }
    
    @Override
    public int compare(BenimURL o1, BenimURL o2)
    {
	if(o1.puani > o2.puani)
	    return 1;
	else if(o1.puani == o2.puani)
	    return 0;
	else
	    return -1;
    }
    
    public int compareDocID(BenimURL o1, BenimURL o2)
    {
	if(o1.docid > o2.docid)
	    return 1;
	else if(o1.docid == o2.docid)
	    return 0;
	else
	    return -1;
    }
    
    public int compareURLstr(BenimURL o1, BenimURL o2)
    {
	return o1.url.toString( ).compareToIgnoreCase(o2.url.toString( ));
    }
    
    public String toProtocolAndDomain()
    {
	
	return url.getProtocol( ) + "://" + url.getHost( );
    }
    
    
    private static final Logger logger = LoggerFactory.getLogger(Website.class);
    
    
    public void AlAltUrlleri() throws Exception
    {
	String crawlStorageFolder = "Verilerim/Crawler";
	CrawlConfig config = new CrawlConfig( );
	config.setCrawlStorageFolder(crawlStorageFolder + "/docid" + getDocid( ));
	config.setPolitenessDelay(200);
	config.setMaxPagesToFetch(1);
	config.setMaxDepthOfCrawling(1);
	config.setCleanupDelaySeconds(4);
	config.setThreadMonitoringDelaySeconds(3);
	config.setThreadShutdownDelaySeconds(2);
	PageFetcher pageFetcher1 = new PageFetcher(config);
	
	RobotstxtConfig robotstxtConfig = new RobotstxtConfig( );
	RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);
	
	CrawlController controller1 = new CrawlController(config, pageFetcher1, robotstxtServer);
	
	String[] crawler1Domains = { toProtocolAndDomain( ) };
	
	controller1.setCustomData(crawler1Domains);
	
	controller1.addSeed(toProtocolAndDomain( ));
	
	
	controller1.startNonBlocking(BasicCrawler.class, 1);
	
	controller1.waitUntilFinish( );
	
	setAltURLleri(AlveDonusturAltURLleri(BasicCrawler.links));
	
	logger.info("Crawler 1 is finished.");
	
    }

    /**
     * dizi listesinden aldığı url'leri alıp domaine göre seçip benimUrl arraylist'esine atar.
     *
     * @param altWebURLleri
     *            alınan alt url'ler
     */
    private List<BenimURL> AlveDonusturAltURLleri(Set<WebURL> altWebURLleri)
    {
	List<BenimURL> altURLler = new ArrayList<>( );
	
	for (WebURL webURL : altWebURLleri)
	{
	    String href = webURL.getURL( ).toLowerCase( );
	    if(FILTERS.matcher(href).matches( ) || !href.contains(url.getHost( ).toLowerCase( )))
	    {
		continue;
	    }

	    altURLler.add(new BenimURL(webURL.getURL( )));
	}

	return altURLler;
    }

    
}
