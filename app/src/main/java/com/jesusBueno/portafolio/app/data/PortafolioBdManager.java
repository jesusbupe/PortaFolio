package com.jesusBueno.portafolio.app.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jesus on 25/02/14.
 */
public class PortafolioBdManager {
    private static final String NOMBRE_BD ="db_Portafolio";
    private static final int VERSION_BD =1;
    private SQLiteDatabase dbPortafolio;
    private Context mContext;

    public PortafolioBdManager(Context context){
        mContext =context;
        PortafolioBdOpenHelper portafolioBdOpenHelper =new PortafolioBdOpenHelper(context,NOMBRE_BD,null,VERSION_BD);
        dbPortafolio=portafolioBdOpenHelper.getWritableDatabase();
    }

    public void insertDesign(Design design) {


        String sql = "INSERT INTO design "
                + "(id, nombre,descripcion, empresa,comentarios,imagen)"
                + "VALUES ("
                + "'" +design.getId()+ "', "
                + "'" +design.getNombre()+ "', "
                + "'" +design.getDescripcion()+ "', "
                + "'" + design.getEmpresa()+ "', "
                + "'" + design.getComentarios() + "', "
                + "'" +design.getImagen()+ "') ";

        Log.d(PortafolioBdManager.class.getName(), "Executing SQL statement: " + sql);
        dbPortafolio.execSQL(sql);
    }
    public ArrayList<Design> getDesignList() {
        ArrayList<Design> designsList = new ArrayList();
        String sql = "SELECT * FROM design";
        Log.d(PortafolioBdManager.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbPortafolio.rawQuery(sql, null);
        while(rs.moveToNext()) {
            int id = rs.getInt(0);
            String nombre = rs.getString(1);
            String descripcion= rs.getString(2);
            String empresa = rs.getString(3);
            String comentarios = rs.getString(4);
            String imagen = rs.getString(5);

            Design design = new Design(id,nombre,descripcion,empresa,comentarios,imagen);
            designsList.add(design);
        }
        return designsList;
    }
    public Design getDesignByID(int designId) {
        Design design = null;

        String sql = "SELECT * FROM design WHERE id="+designId;
        Log.d(PortafolioBdManager.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbPortafolio.rawQuery(sql, null);

        if(rs.moveToNext()) {
            int id = rs.getInt(0);
            String nombre = rs.getString(1);
            String descripcion= rs.getString(2);
            String empresa = rs.getString(3);
            String comentarios = rs.getString(4);
            String imagen = rs.getString(5);
            design = new Design(id,nombre,descripcion,empresa,comentarios,imagen);
        }
        return design;
    }
    public void updateDesign(Design design) {

        String sql = "UPDATE design SET "
                + "nombre='" + design.getNombre()+ "', "
                + "descripcion='" + design.getDescripcion()+ "', "
                + "empresa='" + design.getEmpresa()+ "', "
                + "comentarios='" + design.getComentarios() + "', "
                + "imagen='" + design.getImagen()+ "' "

                + "WHERE id="+design.getId();
        Log.d(PortafolioBdManager.class.getName(), "Executing SQL statement: " + sql);
        dbPortafolio.execSQL(sql);
    }
    public void deleteDesignById(int id) {
        String sql = "DELETE FROM design WHERE id="+id;
        Log.d(PortafolioBdManager.class.getName(), "Executing SQL statement: " + sql);
        dbPortafolio.execSQL(sql);
    }

    public class PortafolioBdOpenHelper extends SQLiteOpenHelper {

        public PortafolioBdOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS design ("
                    + "id INT PRIMARY KEY, "
                    + "nombre VARCHAR(50), "
                    + "descripcion VARCHAR(5000), "
                    + "empresa VARCHAR(100), "
                    + "comentarios VARCHAR(5000), "
                    + "imagen VARCHAR(50))";
            Log.d(PortafolioBdManager.class.getName(), "Executing SQL statement: " + sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }

}
