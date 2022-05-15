package entidades_helper;

public class Puntaje_Esquema {
    public static final String CREAR_TABLA_PUNTAJE = "CREATE TABLE Puntaje(" +
            "user TEXT, nivel INTEGER, puntaje REAL,fecha TEXT, FOREIGN KEY (user) REFERENCES Usuario (user)   )";

    //FOREIGN KEY (user) REFERENCES Usuario (user)
    public static final String TABLA_PUNTAJE = "Puntaje";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_NIVEL =  "nivel";
    public static final String CAMPO_PUNTAJE = "puntaje";
    public static final String CAMPO_USER = "user";

}
