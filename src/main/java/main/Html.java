package main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import sayfalar.SayfaOzet;


public class Html
{
    private static int BulSayisini(String str, String bulunacankStr)
    {

	int sayisi = 0;

	if(str == null || str.isEmpty( )) { return 0; }

	String[] words = str.split("\\s+");

	for (String word : words)
	{
	    if(word.startsWith(bulunacankStr) || word.equalsIgnoreCase(bulunacankStr))
		sayisi++;
	}

	return sayisi;
    }

    public static int BulSayisiniHtmlde(BenimURL benimURL, String bulunacankStr)
    {
	return BulSayisini(DonusturHtmlToText(benimURL), bulunacankStr);
    }


    private static String DonusturHtmlToText(BenimURL benimURL)
    {
	Document document = null;
	Element prgrfElement, baslikElement;

	try
	{
	    document = Jsoup.connect(benimURL.toString( )).get( );
	}
	catch (IOException ioe)
	{
	    System.out.println("jsoup veri çekme hatası : \n ");
	    ioe.printStackTrace( );
	}


	// ---- ilk başlık alınıyor
	baslikElement = document.getElementsByTag("h1").first( );
	if(baslikElement != null)
	{
	    benimURL.setFirstH(baslikElement.ownText( ));
	}
	else
	{
	    benimURL.setFirstH(document.title( ));
	}
	// ---- ilk başlık alınıyor bitiş

	// ---- ilk paragrag alınıyor
	prgrfElement = document.getElementsByTag("p").first( );
	if(prgrfElement != null)
	{
	    benimURL.setFirstP(prgrfElement.ownText( ));
	}
	else
	{
	    prgrfElement = document.getElementsByTag("h1").first( );
	    if(prgrfElement != null)
	    {
		benimURL.setFirstP(prgrfElement.ownText( ));
	    }
	    else
	    {
		benimURL.setFirstP(document.title( ));
	    }
	}
	// ---- ilk paragrag alınıyor bitiş


	SayfaOzet.YazDosyaya("Verilerim/HtmlText/" + benimURL.getDocid( ) + ".txt",
		new HtmlToPlainText( ).getPlainText(document));

	return new HtmlToPlainText( ).getPlainText(document);
    }

}
