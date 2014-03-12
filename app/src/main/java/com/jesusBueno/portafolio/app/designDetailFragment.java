package com.jesusBueno.portafolio.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesusBueno.portafolio.app.data.Design;
import com.jesusBueno.portafolio.app.data.DesignList;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class designDetailFragment extends Fragment {


    public static final String ARG_ITEM_ID = "item_id";
    private ImageView imagen;

    //    private DummyContent.DummyItem mItem;
    private Design design;


    public designDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            int designIndex = Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            design = DesignList.getDesignList().get(designIndex);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_design_detail, container, false);

        // Show the dummy content as text in a TextView.
        if(design != null) {
            ((EditText) rootView.findViewById(R.id.editTextNombre)).setText(design.getNombre());
            ((EditText) rootView.findViewById(R.id.editTextDescripcion)).setText(design.getDescripcion());
            ((EditText) rootView.findViewById(R.id.editTextEmpresa)).setText(design.getEmpresa());
            ((EditText) rootView.findViewById(R.id.editTextComentarios)).setText(design.getComentarios());

            //Descargar foto y mostrarla al finalizar la descarga, sólo si hay un nombre de archivo para la imagen
            if(!design.getImagen().trim().isEmpty() ||design.getImagen()!=null) {
                imagen = ((ImageView)rootView.findViewById(R.id.imageViewimagen));
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.execute(designListFragment.URL_IMAGES + design.getImagen());
            }
        }

        return rootView;
    }
    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private Bitmap bitmap;

        @Override
        protected Void doInBackground(String... strings) {
            String urlImage = strings[0];
            bitmap = getImageBitmap(urlImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imagen.setImageBitmap(bitmap);
        }

        private Bitmap getImageBitmap(String url) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(ImageDownloader.class.getName(), "Error getting bitmap", e);
            }
            return bm;
        }
    }
}
