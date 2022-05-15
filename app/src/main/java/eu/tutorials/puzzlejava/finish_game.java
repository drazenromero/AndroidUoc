package eu.tutorials.puzzlejava;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import entidades_helper.ConexionSQLiteHelper;

public class finish_game extends AppCompatActivity {

    private Button buttonBack;
    private String NameUser;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_game);
        buttonBack = findViewById(R.id.buttonBack);

        class buttonBackClickListener implements View.OnClickListener
        {
            private AppCompatActivity reference;
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(this.reference.getApplicationContext(), home_portal.class);
                startActivity(intent);
            }

            public buttonBackClickListener(AppCompatActivity r){
                this.reference = r;
            }


        }
        buttonBackClickListener listener = new buttonBackClickListener(this);
        buttonBack.setOnClickListener(listener);

        NameUser = getIntent().getStringExtra("NAME_USER").toString();

        //getIntent().getStringExtra("NAME_USER").toString();


        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,10);
        SQLiteDatabase db2 = conn.getReadableDatabase();

        //SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = "drazen" AND Puntaje.nivel = 1 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user ="drazen" AND nivel = 1 )
        Cursor c = db2.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = %s AND Puntaje.nivel = 1 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user = %s AND nivel = 1 )", DatabaseUtils.sqlEscapeString(NameUser),DatabaseUtils.sqlEscapeString(NameUser)),null);

        Log.d("QUERY_PUNTAJE","QUERY NIVEL 1");

        try {
            while (c.moveToNext()) {
                ((TextView)findViewById(R.id.TitleLevel1)).setText(String.valueOf(c.getInt(c.getColumnIndex("nivel"))));
                ((TextView)findViewById(R.id.TitleTime1)).setText(String.valueOf( c.getFloat(c.getColumnIndex("puntaje"))));
                ((TextView)findViewById(R.id.TitleFecha1)).setText(String.valueOf( c.getString(c.getColumnIndex("fecha"))));

                /*c.getString(c.getColumnIndex("user"));
                c.getInt(c.getColumnIndex("nivel"));
                c.getFloat(c.getColumnIndex("puntaje"));
                c.getString(c.getColumnIndex("fecha"));*/
            }
        } finally {
            c.close();
        }
        c = db2.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = %s AND Puntaje.nivel = 2 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user = %s AND nivel = 2 )", DatabaseUtils.sqlEscapeString(NameUser),DatabaseUtils.sqlEscapeString(NameUser)),null);

        Log.d("QUERY_PUNTAJE","QUERY NIVEL 2");

        try {
            while (c.moveToNext()) {
                ((TextView)findViewById(R.id.TitleLevel2)).setText(String.valueOf(c.getInt(c.getColumnIndex("nivel"))));
                ((TextView)findViewById(R.id.TitleTime2)).setText(String.valueOf( c.getFloat(c.getColumnIndex("puntaje"))));
                ((TextView)findViewById(R.id.TitleFecha2)).setText(String.valueOf( c.getString(c.getColumnIndex("fecha"))));

                /*c.getString(c.getColumnIndex("user"));
                c.getInt(c.getColumnIndex("nivel"));
                c.getFloat(c.getColumnIndex("puntaje"));
                c.getString(c.getColumnIndex("fecha"));*/
            }
        } finally {
            c.close();
        }
        c = db2.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = %s AND Puntaje.nivel = 3 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user = %s AND nivel = 3 )", DatabaseUtils.sqlEscapeString(NameUser),DatabaseUtils.sqlEscapeString(NameUser)),null);

        Log.d("QUERY_PUNTAJE","QUERY NIVEL 2");

        try {
            while (c.moveToNext()) {
                ((TextView)findViewById(R.id.TitleLevel3)).setText(String.valueOf(c.getInt(c.getColumnIndex("nivel"))));
                ((TextView)findViewById(R.id.TitleTime3)).setText(String.valueOf( c.getFloat(c.getColumnIndex("puntaje"))));
                ((TextView)findViewById(R.id.TitleFecha3)).setText(String.valueOf( c.getString(c.getColumnIndex("fecha"))));

                /*c.getString(c.getColumnIndex("user"));
                c.getInt(c.getColumnIndex("nivel"));
                c.getFloat(c.getColumnIndex("puntaje"));
                c.getString(c.getColumnIndex("fecha"));*/
            }
        } finally {
            c.close();
        }

//        String parametros[] = {"1"};
//        String campos[] = {UsuarioEsquema.CAMPO_USER, Puntaje_Esquema.CAMPO_FECHA};
//        //Cursor cursor = db.query(UsuarioEsquema.TABLA_USUARIO,);
//
//        Cursor c = db2.rawQuery(
//                "SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.id = Puntaje.id",
//                null
//        );
//        c.moveToFirst();
        /*Cursor c = db2.rawQuery(
              "SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.id = Puntaje.id",
              null
        );
        try {
            while (c.moveToNext()) {
                c.getString(c.getColumnIndex("user"));
                c.getInt(c.getColumnIndex("nivel"));
                c.getFloat(c.getColumnIndex("puntaje"));
                c.getString(c.getColumnIndex("fecha"));
            }
        } finally {
            c.close();
        }*/



    }
}