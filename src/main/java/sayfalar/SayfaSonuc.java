package sayfalar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.StreamVariable;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.dnd.FileDropTarget;

import main.BenimURL;


public class SayfaSonuc extends SayfaOzet
{
    private static SayfaSonuc sayfaSonuc;

    public static SayfaSonuc getSayfaSonuc(List<BenimURL> urlList)
    {
	if(sayfaSonuc == null)
	{
	    sayfaSonuc = new SayfaSonuc(urlList);
	    System.out.println("-------------SayfaSonuc oluşturuldu");
	}
	return sayfaSonuc;
    }

    private SayfaSonuc(List<BenimURL> urlList)
    {
	ekranSonuc = new EkranSonuc( );
	String basepath = VaadinService.getCurrent( ).getBaseDirectory( ).getAbsolutePath( );

	FileResource resYsrSearch = new FileResource(new File(basepath + "/WEB-INF/images/ysrSearch.png"));
	ekranSonuc.imageYsrSearch.setSource(resYsrSearch);
	OlusturTabAlUrlDosyadan( );

	// sonuc sayfasından giris sayfasına gider
	ekranSonuc.butonAnaSayfa.addClickListener(e -> {

	    anahtarKelimeler = null;
	    ekranGiris.aramaAlani.setValue("");
	    UI.getCurrent( ).setContent(ekranGiris);
	    SayfaOzet.ekranGiris.aramaAlani.focus( ); // sayfa açıldığında arama alanından yazmaya
	    // başlayabilmek için
	});

	// genel arama yapar
	ekranSonuc.buttonSearchSS.addClickListener(e -> {

	    if(AlKeywordleri(ekranSonuc))
	    {
		ekranSonuc.tabSheet.setSelectedTab(0);
	    }
	});

	// 1. sorunun cevabını verir
	ekranSonuc.buttonSearchOnPageSS.addClickListener(e -> {

	    if(AlKeywordleri(ekranSonuc))
	    {
		AyarlaButtonSearchOnPage( );
	    }
	});

    }


    private boolean AlKeywordleri(EkranSonuc ekranSonuc)
    {
	String aramaAlani = ekranSonuc.aramaAlaniSS.getValue( );

	if(aramaAlani == null || aramaAlani.isEmpty( ))
	{
	    Notification.show("Lütfen önce arama alanına bir şeyler yazınız!");
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
		Notification.show("Arama alanına yazılan yazı geçersiz!");
		return false;
	    }
	}

    }
    
    protected void OlusturTabAlUrlDosyadan()
    {
	final Label infoLabel = new Label( );
	infoLabel.setWidth(240.0f, Unit.PIXELS);
	ProgressBar progress = new ProgressBar( );
	String basepath = VaadinService.getCurrent( ).getBaseDirectory( ).getAbsolutePath( );
	FileResource drag_drop = new FileResource(new File(basepath + "/WEB-INF/images/drag-drop.png"));
	ekranSonuc.drag_drop.setSource(drag_drop);

	final VerticalLayout dropPane = new VerticalLayout(infoLabel);
	dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
	dropPane.addStyleName("drop-area");
	dropPane.setSizeFull( );

	progress.setIndeterminate(true);
	progress.setVisible(false);
	dropPane.addComponent(progress);

	ekranSonuc.panelDosyaAl.addComponent(dropPane);

	new FileDropTarget<>(dropPane, fileDropEvent -> {
	    final int fileSizeLimit = 5 * 1024 * 1024; // 2MB

	    fileDropEvent.getFiles( ).forEach(html5File -> {
		final String fileName = html5File.getFileName( );

		if(html5File.getFileSize( ) > fileSizeLimit)
		{
		    Notification.show("File rejected. Max 5MB files are accepted by Sampler",
			    Notification.Type.WARNING_MESSAGE);
		}
		else
		{
		    final ByteArrayOutputStream bas = new ByteArrayOutputStream( );
		    final StreamVariable streamVariable = new StreamVariable( )
		    {

			@Override
			public OutputStream getOutputStream()
			{
			    return bas;
			}

			@Override
			public boolean listenProgress()
			{
			    return false;
			}

			@Override
			public void onProgress(final StreamingProgressEvent event)
			{
			}

			@Override
			public void streamingStarted(final StreamingStartEvent event)
			{
			}

			@Override
			public void streamingFinished(final StreamingEndEvent event)
			{
			    progress.setVisible(false);
			    showFile(fileName, bas);
			    urlStr = new String(bas.toByteArray( ));
			    try
			    {
				urlList = AlUrlStringdenUrlleri(urlList);
			    }
			    catch (IOException e)
			    {
				// TODO Auto-generated catch block
				e.printStackTrace( );
			    }
			}

			@Override
			public void streamingFailed(final StreamingErrorEvent event)
			{
			    progress.setVisible(false);
			}

			@Override
			public boolean isInterrupted()
			{
			    return false;
			}
		    };
		    html5File.setStreamVariable(streamVariable);
		    progress.setVisible(true);

		}
	    });
	});
    }

    private void showFile(final String name, final ByteArrayOutputStream bas)
    {
	// resource for serving the file contents
	final StreamSource streamSource = () -> {
	    if(bas != null)
	    {
		final byte[] byteArray = bas.toByteArray( );
		return new ByteArrayInputStream(byteArray);
	    }
	    return null;
	};
	final StreamResource resource = new StreamResource(streamSource, name);

	// show the file contents - images only for now
	final Embedded embedded = new Embedded(name, resource);
	showComponent(embedded, name);
    }

    private void showComponent(final Component c, final String name)
    {
	final VerticalLayout layout = new VerticalLayout( );
	layout.setSizeUndefined( );
	layout.setMargin(true);
	final Window w = new Window(name, layout);
	w.addStyleName("dropdisplaywindow");
	w.setSizeUndefined( );
	w.setResizable(false);
	c.setSizeUndefined( );
	layout.addComponent(c);
	UI.getCurrent( ).addWindow(w);
    }
    
}
