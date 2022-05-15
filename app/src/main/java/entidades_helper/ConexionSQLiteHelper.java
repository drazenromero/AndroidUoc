package entidades_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    //String creacion de base de datos
    //final String CREAR_TABLA_USUARIO = "CREATE TABLE Usuario (id INTEGER, user TEXT , TEXT fecha, nivel INTEGER, puntaje REAL)";
    private static final int DATABASE_VERSION = 2;

    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL(UsuarioEsquema.CREAR_TABLA_USUARIO);
       sqLiteDatabase.execSQL(Puntaje_Esquema.CREAR_TABLA_PUNTAJE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int versionAntigua, int versionNueva) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Usuario");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Puntaje");
        onCreate(sqLiteDatabase);
    }
}
