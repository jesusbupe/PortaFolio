package com.jesusBueno.portafolio.app.data;

/**
 * Created by Jesus on 25/02/14.
 */
public class Design {
    private int id;
    private String nombre;
    private String empresa;
    private String descripcion;
    private String comentarios;
    private String imagen;

    public Design(int id,String nombre,String descripcion,String empresa,String comentarios,String imagen){
        this.id=id;
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.empresa=empresa;
        this.comentarios=comentarios;
        this.imagen=imagen;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {    this.empresa = empresa;
    }
    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return
                 nombre ;
    }

    @Override
    public boolean equals(Object o) {
        return this.id==((Design)o).getId();
    }
}
