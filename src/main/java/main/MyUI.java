package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import sayfalar.SayfaGiris;
import sayfalar.SayfaOzet;
import sayfalar.SayfaSonuc;


/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of an HTML
 * page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component
 * to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI
{
    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
     */
    @Override
    protected void init(VaadinRequest vaadinRequest)
    {
	File file = new File("URLs.txt");
	try
	{
	    SayfaOzet.urlStr = new String(Files.readAllBytes(file.toPath( )));
	    SayfaOzet.urlList = SayfaOzet.AlUrlStringdenUrlleri(SayfaOzet.urlList);
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace( );
	}

	SayfaSonuc sayfaSonuc = SayfaSonuc.getSayfaSonuc(SayfaOzet.urlList);
	SayfaGiris sayfaGiris = SayfaGiris.getSayfaGiris(SayfaOzet.urlList);

	SayfaOzet.getAramaAlani( ).focus( ); // sayfa açıldığında arama alanından yazmaya
	// başlayabilmek için
	setContent(SayfaOzet.ekranGiris);

	Notification.show("hi :)", "Welcome to YSR Search", Notification.Type.TRAY_NOTIFICATION);

    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet
    {
    }
}
