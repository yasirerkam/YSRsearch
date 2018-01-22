package sayfalar;

import java.io.File;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import main.BenimURL;


public class SayfaGiris extends SayfaOzet
{
    private FileResource[]    arkaPlanlar;
    private int		      arkaPlanNo;
    private static SayfaGiris sayfaGiris;
    
    
    public static SayfaGiris getSayfaGiris(List<BenimURL> urlList)
    {
	if(sayfaGiris == null)
	{
	    sayfaGiris = new SayfaGiris(urlList);
	    System.out.println("SayfaGiris oluşturuldu-------------");
	}
	return sayfaGiris;
    }
    
    private SayfaGiris(List<BenimURL> urlList)
    {
	ekranGiris = new EkranGiris( );
	String basepath = VaadinService.getCurrent( ).getBaseDirectory( ).getAbsolutePath( );
	
	arkaPlanNo = 0;
	arkaPlanlar = new FileResource[5];
	arkaPlanlar[0] = null;
	arkaPlanlar[1] = new FileResource(new File(basepath + "/WEB-INF/images/bg01.jpg"));
	arkaPlanlar[2] = new FileResource(new File(basepath + "/WEB-INF/images/bg02.png"));
	arkaPlanlar[3] = new FileResource(new File(basepath + "/WEB-INF/images/bg03.jpg"));
	arkaPlanlar[4] = new FileResource(new File(basepath + "/WEB-INF/images/bg04.jpg"));
	
	FileResource resYsrSearch = new FileResource(new File(basepath + "/WEB-INF/images/ysrSearch.png"));
	ekranGiris.ysrSearch.setSource(resYsrSearch);
	
	ekranGiris.buttonSearch.setClickShortcut(KeyCode.ENTER);
	
	// arkaaplan değiştirir sağ
	ekranGiris.buttonChangeRight.addClickListener(e -> {
	    
	    DegistirArkaPlan(true);
	    
	});
	// arkaaplan değiştirir sol
	ekranGiris.buttonChangeLeft.addClickListener(e -> {
	    
	    DegistirArkaPlan(false);
	    
	});
	
	// genel arama yapar
	ekranGiris.buttonSearch.addClickListener(e -> {
	    if(AlKeywordleri(ekranGiris))
	    {
		UI.getCurrent( ).setContent(ekranSonuc);
		ekranSonuc.aramaAlaniSS.setValue(ekranGiris.aramaAlani.getValue( ));
		
		ekranSonuc.tabSheet.setSelectedTab(0);
	    }
	});
	
	// url dosyası al
	ekranGiris.buttonUrlGir.addClickListener(e -> {
	    
	    UI.getCurrent( ).setContent(ekranSonuc);
	    ekranSonuc.aramaAlaniSS.setValue(ekranGiris.aramaAlani.getValue( ));
	    
	    ekranSonuc.tabSheet.setSelectedTab(6);
	    
	});
	
	// 1. sorunun cevabını verir
	ekranGiris.buttonSearchOnPage.addClickListener(e -> {
	    if(AlKeywordleri(ekranGiris))
	    {
		UI.getCurrent( ).setContent(ekranSonuc);
		ekranSonuc.aramaAlaniSS.setValue(ekranGiris.aramaAlani.getValue( ));
		
		AyarlaButtonSearchOnPage( );
	    }
	});
	
	// 2. sorunun cevabını verir
	ekranGiris.buttonSearchURL.addClickListener(e -> {
	    if(AlKeywordleri(ekranGiris))
	    {
		UI.getCurrent( ).setContent(ekranSonuc);
		ekranSonuc.aramaAlaniSS.setValue(ekranGiris.aramaAlani.getValue( ));
		
		AyarlaButtonUrlRank( );
	    }
	});
	
	// 3. sorunun cevabını verir
	ekranGiris.buttonSearchSite.addClickListener(e -> {
	    if(AlKeywordleri(ekranGiris))
	    {
		UI.getCurrent( ).setContent(ekranSonuc);
		ekranSonuc.aramaAlaniSS.setValue(ekranGiris.aramaAlani.getValue( ));
		
		try
		{
		    AlUrlWebSite( );
		}
		catch (Exception e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace( );
		}
		
	    }
	});
	
    }


    private boolean AlKeywordleri(EkranGiris ekranGiris)
    {
	String aramaAlani = ekranGiris.aramaAlani.getValue( );
	
	if(aramaAlani == null || aramaAlani.isEmpty( ))
	{
	    Notification.show("Arama Alanı Boş", "Lütfen önce arama alanına bir şeyler yazınız!",
		    Notification.Type.WARNING_MESSAGE);
	    return false;
	}
	else
	{
	    anahtarKelimeler = aramaAlani.split("\\s+");
	    
	    if(anahtarKelimeler != null && anahtarKelimeler.length > 0)
	    {
		return true;
	    }
	    else
	    {
		Notification.show("Arama Alanı Boş", "Arama alanına yazılan yazı geçersiz!",
			Notification.Type.WARNING_MESSAGE);
		return false;
	    }
	}
	
    }
    
    private void DegistirArkaPlan(boolean sag)
    {
	if(sag)
	{
	    arkaPlanNo++;
	    if(arkaPlanNo > 4)
		arkaPlanNo = 0;
	}
	else
	{
	    arkaPlanNo--;
	    if(arkaPlanNo < 0)
		arkaPlanNo = 4;
	}
	
	ekranGiris.arkaPlan.setSource(arkaPlanlar[arkaPlanNo]);
    }
    
}
