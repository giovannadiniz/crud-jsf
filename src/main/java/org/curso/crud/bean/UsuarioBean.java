package org.curso.crud.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.curso.crud.model.Usuario;

import java.util.List;

@Named
@RequestScoped
public class UsuarioBean {
    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("crudPU");

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<Usuario> getUsuarios() {
        if (usuarios == null) {
            carregarUsuarios();
        }
        return usuarios;
    }

    public void salvar() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                em.merge(usuario);
            }
            em.getTransaction().commit();
            usuario = new Usuario();
            carregarUsuarios();
        } finally {
            em.close();
        }
    }

    public void editar(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            usuario = em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            if (u != null) {
                em.remove(u);
            }
            em.getTransaction().commit();
            carregarUsuarios();
        } finally {
            em.close();
        }
    }

    public void carregarUsuarios() {
        EntityManager em = emf.createEntityManager();
        try {
            usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

    
    public UsuarioBean() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (Exception e) {
        
            }
        }
    }
}
