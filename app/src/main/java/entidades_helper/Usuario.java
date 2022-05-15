package entidades_helper;

import java.util.Date;

public class Usuario {





    private Integer id;
    private String user;
    private Date fecha;
    private Integer nivel;
    private Float puntaje;

    public Usuario(Integer id, String user, Date fecha, Integer nivel, Float puntaje) {
        this.id = id;
        this.user = user;
        this.fecha = fecha;
        this.nivel = nivel;
        this.puntaje = puntaje;
    }

    public Float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Float puntaje) {
        this.puntaje = puntaje;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}
