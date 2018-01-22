package main;

public class Website
{
    /*  private static final Logger logger = LoggerFactory.getLogger(Website.class);


    public static void AlUrlleri(List<BenimURL> urlList) throws Exception
    {
    String crawlStorageFolder = "Crawler/Veri";
    
    
    for (int i = 0; i < urlList.size( ); i++)
    {
        CrawlConfig config = new CrawlConfig( );
        config.setCrawlStorageFolder(crawlStorageFolder + "/crawler" + i);
        config.setPolitenessDelay(2000);
        config.setMaxPagesToFetch(100);
        config.setMaxDepthOfCrawling(3);
        PageFetcher pageFetcher1 = new PageFetcher(config);
        
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig( );
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);
        
        CrawlController controller1 = new CrawlController(config, pageFetcher1, robotstxtServer);
        
        String[] crawler1Domains = { urlList.get(i).toProtocolAndDomain( ) };
        
        controller1.setCustomData(crawler1Domains);
        
        controller1.addSeed(urlList.get(i).toProtocolAndDomain( ));
        
        
        controller1.startNonBlocking(BasicCrawler.class, 5);
        
        controller1.waitUntilFinish( );
        logger.info("Crawler 1 is finished.");
    }
    
    }

     */
}
