package com.recursos;

public class User {
	
	public String nombre;
	public String correo;
	public String pass;
	public String perfil;
	public String telefono;
	public String id;
	public String id_proyecto;
	public String titulo_proyecto;
	public String descripcion_proyecto;

	public User(String nombre, String correo, String pass, String perfil,String telefono, String id, String id_proyecto,String titulo_proyecto,String descripcion_proyecto) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.pass = pass;
		this.perfil = perfil;
		this.telefono = telefono;
		this.id = id;
		this.id_proyecto = id_proyecto;
		this.titulo_proyecto=titulo_proyecto;
		this.descripcion_proyecto=descripcion_proyecto;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String name) {
		this.nombre = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getCorreo() {
		return correo;
	}



	public void setCorreo(String correo) {
		this.correo = correo;
	}



	public String getTitulo_proyecto() {
		return titulo_proyecto;
	}

	public void setTitulo_proyecto(String titulo_proyecto) {
		this.titulo_proyecto = titulo_proyecto;
	}

	public String getDescripcion_proyecto() {
		return descripcion_proyecto;
	}

	public void setDescripcion_proyecto(String descripcion_proyecto) {
		this.descripcion_proyecto = descripcion_proyecto;
	}

	public String getPass() {
		return pass;
	}



	public void setPass(String pass) {
		this.pass = pass;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	
	public String getId_proyecto() {
		return id_proyecto;
	}


	public void setId_proyecto(String id_proyecto) {
		this.id_proyecto = id_proyecto;
	}

	
	

}
