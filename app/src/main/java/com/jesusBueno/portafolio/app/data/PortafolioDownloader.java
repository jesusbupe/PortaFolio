package com.jesusBueno.portafolio.app.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.jesusBueno.portafolio.app.designListFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jesus on 9/03/14.
 */
public class PortafolioDownloader extends AsyncTask<String,Void,Void> {
    private final String NAMESPACE = null;
    private XmlPullParser parser = Xml.newPullParser();

    private static final String TAG_XML = "designs";
    private static final String TAG_REGISTRO = "design";
    private ArrayList<Design> listaDatos;
    private Context context;
    private designListFragment designListFragment;

    public PortafolioDownloader(Context context, designListFragment designListFragment) {
        this.context = context;
        this.designListFragment = designListFragment;
    }
    private Design leerRegistro() {

        int id = -1;
        String nombre = "";
        String descripcion  = "";
        String empresa = "";
        String comentarios = "";
        String imagen = "";

        try {
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_REGISTRO);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String tagName = parser.getName();
                if (tagName.equals("id")) {
                    id = Integer.valueOf(readText(tagName));
                } else if (tagName.equals("nombre")) {
                    nombre = readText(tagName);
                } else if (tagName.equals("descripcion")) {
                    descripcion = readText(tagName);
                } else if (tagName.equals("empresa")) {
                    empresa = readText(tagName);
                } else if (tagName.equals("comentarios")) {
                    comentarios = readText(tagName);
                } else if (tagName.equals("imagen")) {
                    imagen = readText(tagName);

                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Design design = new Design(id,nombre,descripcion,empresa,comentarios,imagen);
        return design;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(PortafolioDownloader.class.getName(), "Descarga de datos finalizada. Iniciando procesamiento");
        Log.d(PortafolioDownloader.class.getName(), "Nº registros descargados: "+listaDatos.size());
        //Conectar con la BD y recorrer los elementos descargados desde el XML
       PortafolioBdManager portafolioBdManager = new PortafolioBdManager(context);
        for (Design design : listaDatos) {

            Log.d(PortafolioDownloader.class.getName(), "Comprobando si exite el id: "+design.getId());
            if (portafolioBdManager.getDesignByID(design.getId()) == null) {
                Log.d(PortafolioDownloader.class.getName(), "Id no encontrado. Se insertará el registro");

                portafolioBdManager.insertDesign(design);
            } else {
                Log.d(PortafolioDownloader.class.getName(), "Id existente. Se actualizará el registro");

                portafolioBdManager.updateDesign(design);
            }
        }


        ArrayList<Design> designsListDB = portafolioBdManager.getDesignList();

        for (Design design : designsListDB) {

            if(listaDatos.indexOf(design)==-1) {
                Log.d(PortafolioDownloader.class.getName(), "design a eliminar: " +design.toString());

                portafolioBdManager.deleteDesignById(design.getId());
            }
        }

        //Mostrar la lista una vez finalizada la descarga
        designListFragment.showDesignList();
    }




    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private String readText(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }


    private ArrayList xmlToList(InputStream stream) {
        Log.d(PortafolioDownloader.class.getName(), "Iniciando interpretación de datos XML");
        ArrayList list = new ArrayList();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            Log.d(PortafolioDownloader.class.getName(), "Primera etiqueta encontrada: "+parser.getName());
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_XML);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d(PortafolioDownloader.class.getName(), "Etiqueta encontrada: "+parser.getName());
                if (name.equals(TAG_REGISTRO)) {
                    list.add(leerRegistro());
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }



    @Override
    protected Void doInBackground(String... urls) {
        Log.d(PortafolioDownloader.class.getName(), "Iniciando descarga de datos en segundo plano");
        InputStream stream = null;
        try {
            Log.d(PortafolioDownloader.class.getName(), "Dirección de descarga: "+urls[0]);
            stream = downloadUrl(urls[0]);
            listaDatos = xmlToList(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
