package sayfalar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import main.BenimURL;
import main.Html;


public abstract class SayfaOzet
{
    public static EkranGiris	 ekranGiris;
    public static EkranSonuc	 ekranSonuc;
    protected static String[]	 anahtarKelimeler;
    public static List<BenimURL> urlList = new ArrayList<BenimURL>( );
    public static String	 urlStr;
    

    public static TextField getAramaAlani()
    {
	return ekranGiris.aramaAlani;
    }

    
    protected void YazdirSonuclariTabALLa(Panel panel)
    {
	VerticalLayout sfAll = (VerticalLayout) panel.getContent( );
	
	for (Component kutu : sfAll)
	{
	    kutu.setVisible(false);
	}
	
	int sayi = sfAll.getComponentCount( ) < urlList.size( ) ? sfAll.getComponentCount( ) : urlList.size( );
	for (int i = 0; i < sayi; i++)
	{
	    Link link = (Link) ((VerticalLayout) sfAll.getComponent(i)).getComponent(0);
	    Label label = (Label) ((VerticalLayout) sfAll.getComponent(i)).getComponent(1);
	    Label label2 = (Label) ((VerticalLayout) sfAll.getComponent(i)).getComponent(2);
	    
	    link.setCaption(urlList.get(i).getFirstH( ));
	    label.setValue(urlList.get(i).toString( ));
	    label2.setValue(urlList.get(i).getFirstP( ));
	    
	    sfAll.getComponent(i).setVisible(true);
	}
	
    }
    
    public void AnahtarKelimeSaydırma(BenimURL benimURL)
    {
	if(anahtarKelimeler != null && anahtarKelimeler.length > 0)
	{
	    int sayi = Html.BulSayisiniHtmlde(benimURL, anahtarKelimeler[0]);
	    ekranSonuc.onPageSonuc.setValue("Aranan sayfada bulunan \"" + anahtarKelimeler[0] + "\" sayisi : " + sayi);
	}
	else
	    return;
    }
    
    protected void AyarlaButtonSearchOnPage()
    {
	ekranSonuc.tabAll.setVisible(false);
	ekranSonuc.tabImages.setVisible(false);
	ekranSonuc.tabSearchOnPage.setVisible(true);
	ekranSonuc.tabPage_URL_Rank.setVisible(false);
	ekranSonuc.tabWebSiteRank.setVisible(false);
	ekranSonuc.tabSemanticRank.setVisible(false);
	ekranSonuc.tabSheet.setSelectedTab(2);
	
	AnahtarKelimeSaydırma(urlList.get(0));
    }
    
    protected void AyarlaButtonUrlRank()
    {
	ekranSonuc.tabAll.setVisible(true);
	ekranSonuc.tabImages.setVisible(false);
	ekranSonuc.tabSearchOnPage.setVisible(false);
	ekranSonuc.tabPage_URL_Rank.setVisible(true);
	ekranSonuc.tabWebSiteRank.setVisible(false);
	ekranSonuc.tabSemanticRank.setVisible(false);
	ekranSonuc.tabSheet.setSelectedTab(0);
	
	for (BenimURL benimURL : urlList)
	{
	    benimURL.setAnhtrKlmlrnBlnmaSayisi(HesaplaAnhtrKlmlrnBlnmaSayisi(benimURL));
	}
	
	HesaplaPuanUrl( );
	ekranSonuc.gridUrlRank.setItems(urlList);
	ekranSonuc.gridUrlRank.sort("sirasi", SortDirection.ASCENDING);
	
	YazdirSonuclariTabALLa(ekranSonuc.panelAll);
	
    }
    
    protected List<Integer> HesaplaAnhtrKlmlrnBlnmaSayisi(BenimURL benimURL)
    {
	List<Integer> urlIcindeAnahtarinBulunmaS = new ArrayList<>( );
	
	if(anahtarKelimeler != null && anahtarKelimeler.length > 0)
	{
	    for (int anahtarKlmeNo = 0; anahtarKlmeNo < anahtarKelimeler.length; anahtarKlmeNo++)
	    {
		urlIcindeAnahtarinBulunmaS
		.add(new Integer(Html.BulSayisiniHtmlde(benimURL, anahtarKelimeler[anahtarKlmeNo])));
	    }
	}
	else
	    System.out.println("anahtar kelime yok-----------");
	
	return urlIcindeAnahtarinBulunmaS;
    }
    
    protected void HesaplaPuanUrl()
    {
	for (BenimURL benimURL : urlList)
	{
	    float puani;
	    
	    benimURL.getAnhtrKlmlrnBlnmaSayisi( ).sort((Integer bS1, Integer bS2) -> bS1.intValue( ) - bS2.intValue( ));
	    float fark = (benimURL.getAnhtrKlmlrnBlnmaSayisi( ).get(benimURL.getAnhtrKlmlrnBlnmaSayisi( ).size( ) - 1)
		    .intValue( ) - benimURL.getAnhtrKlmlrnBlnmaSayisi( ).get(0).intValue( )) + 1;
	    puani = 1 / fark;
	    
	    int toplam = 0;
	    for (Integer intC : benimURL.getAnhtrKlmlrnBlnmaSayisi( ))
	    {
		toplam += intC.intValue( );
	    }
	    puani = puani * toplam;
	    
	    puani = Math.round(puani * 100);
	    benimURL.setPuani(puani);
	}
	
	SiralaUrlPuanaGore( );
    }
    
    public static void SiralaUrlPuanaGore()
    {
	SayfaOzet.urlList.sort((BenimURL url1, BenimURL url2) -> url1.compare(url2, url1));
	
	int sayi = 1;
	for (BenimURL benimURL : SayfaOzet.urlList)
	{
	    benimURL.setSirasi(sayi);
	    sayi++;
	}
    }
    
    protected void AlUrlWebSite() throws Exception
    {
	FileWriter fw = null;
	BufferedWriter bw = null;
	PrintWriter out = null;
	String altURLKlasoruStr = "Verilerim/AltURLler/";
	try
	{
	    fw = new FileWriter(altURLKlasoruStr + "/derinlik1.txt", false);
	    bw = new BufferedWriter(fw);
	    out = new PrintWriter(bw);
	    
	    urlList.get(0).getAltURLleri( )
		    .sort((BenimURL altUrl1, BenimURL altUrl2) -> altUrl1.compareURLstr(altUrl1, altUrl2));
	    
	    for (BenimURL altBenimURL : urlList.get(0).getAltURLleri( ))
	    {
		System.out.println("url : " + altBenimURL.getUrl( ).toString( ));
		out.println(altBenimURL.getUrl( ).toString( ));
	    }
	    
	}
	catch (IOException e)
	{
	    // exception handling left as an exercise for the reader
	}
	finally
	{
	    out.close( );
	    bw.close( );
	    fw.close( );
	}
	
	
    }
    
    
    public static List<BenimURL> AlUrlStringdenUrlleri(List<BenimURL> urlList) throws IOException
    {
	// urlList.clear( );
	
	BufferedReader rdr = new BufferedReader(new StringReader(urlStr));
	for (String url_StringSatirindan = rdr.readLine( ); url_StringSatirindan != null; url_StringSatirindan = rdr
		.readLine( ))
	{
	    try
	    {
		new URL(url_StringSatirindan).openConnection( ).connect( );
	    }
	    catch (IOException e)
	    {
		// TODO Auto-generated catch block
		// e.printStackTrace( );
		continue;
	    }
	    
	    if(!VarMiUrl(url_StringSatirindan, urlList))
		urlList.add(new BenimURL(url_StringSatirindan));
	}
	
	rdr.close( );
	
	return urlList;
    }
    
    public static boolean VarMiUrl(String urlStringSatiri, List<BenimURL> urlList)
    {
	
	for (BenimURL benimURL : urlList)
	{
	    if(benimURL.toString( ).compareToIgnoreCase(urlStringSatiri) == 0)
		return true;
	}
	
	return false;
    }
    
    public static void YazDosyaya(String fileStr, String text)
    {
	FileWriter fw = null;
	BufferedWriter bw = null;
	PrintWriter out = null;
	try
	{
	    fw = new FileWriter(fileStr, false);
	    bw = new BufferedWriter(fw);
	    out = new PrintWriter(bw);
	    out.println(text);
	    out.close( );
	}
	catch (IOException e)
	{
	    // exception handling left as an exercise for the reader
	}
	finally
	{
	    if(out != null)
		out.close( );
	    try
	    {
		if(bw != null)
		    bw.close( );
	    }
	    catch (IOException e)
	    {
		// exception handling left as an exercise for the reader
	    }
	    try
	    {
		if(fw != null)
		    fw.close( );
	    }
	    catch (IOException e)
	    {
		// exception handling left as an exercise for the reader
	    }
	}
    }
    
}
