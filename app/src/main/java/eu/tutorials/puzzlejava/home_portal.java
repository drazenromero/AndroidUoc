package eu.tutorials.puzzlejava;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import entidades_helper.ConexionSQLiteHelper;
import entidades_helper.Puntaje_Esquema;
import entidades_helper.UsuarioEsquema;

public class home_portal extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart;
    private EditText editTextUser;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_portal);

//        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,5);
//        conn.close();
//        ConexionSQLiteHelper conn2 = new ConexionSQLiteHelper(this,"bd_usuarios",null,5);
//        SQLiteDatabase db = conn2.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        //values.put(UsuarioEsquema.CAMPO_ID,1);
//        values.put(UsuarioEsquema.CAMPO_USER, "USUARIO");
//        /*values.put(UsuarioEsquema.CAMPO_FECHA,"Una fecha");
//        values.put(UsuarioEsquema.CAMPO_NIVEL,1);
//        values.put(UsuarioEsquema.CAMPO_PUNTAJE,2.5);*/
//
//        ContentValues values2 = new ContentValues();
//
//        values2.put(Puntaje_Esquema.CAMPO_FECHA,"10/10/1998");
//        values2.put(Puntaje_Esquema.CAMPO_PUNTAJE,2.4);
//        values2.put(Puntaje_Esquema.CAMPO_NIVEL,3);
//
//        /*public static final String TABLA_USUARIO = "Usuario";
//        public static final String CAMPO_ID = "id";
//        public static final String CAMPO_USER = "user";
//        public static final String CAMPO_FECHA = "fecha";
//        public static final String CAMPO_NIVEL =  "nivel";
//        public static final String CAMPO_PUNTAJE = "puntaje";*/
//
//        Long id_resultante = db.insert(UsuarioEsquema.TABLA_USUARIO,UsuarioEsquema.CAMPO_ID,values);
//        Toast.makeText(getApplicationContext(),"Id Registro: " + id_resultante,Toast.LENGTH_LONG).show();
//
//        Long id_resultante2 = db.insert(Puntaje_Esquema.TABLA_PUNTAJE,Puntaje_Esquema.TABLA_PUNTAJE,values2);
//        Toast.makeText(getApplicationContext(),"Id Registro2: " + id_resultante2,Toast.LENGTH_LONG).show();
//
//
//
//        //String insert = "INSERT INTO Usuario(user, fecha, nivel, puntaje) VALUES('otroNombre', '10/10/1999', 75, 3.2);";
//        String insert = "INSERT INTO Usuario(user) VALUES('otroNombre');";
//        db.execSQL(insert);
//
//        SQLiteDatabase db2 = conn2.getReadableDatabase();
//        String parametros[] = {"1"};
//        String campos[] = {UsuarioEsquema.CAMPO_USER, Puntaje_Esquema.CAMPO_FECHA};
//        //Cursor cursor = db.query(UsuarioEsquema.TABLA_USUARIO,);
//
//        Cursor c = db2.rawQuery(
//                "SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.id = Puntaje.id",
//                null
//        );
//        c.moveToFirst();
//        //Toast.makeText(getApplicationContext(),"xxxx " + c.getColumnIndex("user"),Toast.LENGTH_LONG).show();
//        //Toast.makeText(getApplicationContext(),"xxxx " + c.getString(c.getColumnIndex("user")),Toast.LENGTH_LONG).show();
//        conn2.close();
        editTextUser = findViewById(R.id.editUser);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(this);




    }


    @SuppressLint("Range")
    public void checkExistenceUserGame(){
       ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,10);
       SQLiteDatabase db = conn.getWritableDatabase();


        //Cursor c = db.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.id = Puntaje.id WHERE Usuario.user= %s",DatabaseUtils.sqlEscapeString(editTextUser.getText().toString())),null);
        //Cursor c2 = db.rawQuery(String.format("SELECT * count(user) from Usuario where user=%s"))
        long countRows =  DatabaseUtils.queryNumEntries(db,String.format("Usuario where user= %s;",DatabaseUtils.sqlEscapeString(editTextUser.getText().toString())), null);
        //c.moveToFirst();
        if (countRows > 0 ){
            //Toast.makeText(getApplicationContext(),String.format("EXISTE UN USUARIO LLAMADO: %s",c.getString(c.getColumnIndex("user"))),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),String.format("EXISTE UN USUARIO LLAMADO: %s",editTextUser.getText()),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),String.format("NO EXISTE UN USUARIO LLAMADO: %s %s",editTextUser.getText().toString(),countRows),Toast.LENGTH_LONG).show();




            ContentValues values = new ContentValues();

            values.put(UsuarioEsquema.CAMPO_USER, editTextUser.getText().toString());

            Long id_resultante = db.insert(UsuarioEsquema.TABLA_USUARIO,UsuarioEsquema.CAMPO_ID,values);
            if(id_resultante != -1){
                Toast.makeText(getApplicationContext(),String.format("CREADO USUARIO %s",editTextUser.getText().toString()),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),String.format("NO CREADO USUARIO %s",editTextUser.getText().toString()),Toast.LENGTH_LONG).show();
            }










        }
        conn.close();
        //DatabaseUtils.sqlEscapeString();
       //db.rawQuery("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.id = Puntaje.id WHERE Usuario.user= %s")

    }



    @Override
    public void onClick(View view) {
        checkExistenceUserGame();
        Log.d("click_event","Se hizo click en el botom");

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("NAME_USER",editTextUser.getText().toString() );
        startActivity(intent);





    }
}