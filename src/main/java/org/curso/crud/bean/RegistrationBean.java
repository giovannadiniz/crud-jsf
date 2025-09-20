package org.curso.crud.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.curso.crud.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

@Named
@RequestScoped
public class RegistrationBean {
    private Usuario usuario = new Usuario();

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("crudPU");

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String register() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            String hashedSenha = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            usuario.setSenha(hashedSenha);
            em.persist(usuario);
            em.getTransaction().commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário cadastrado com sucesso!"));
            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar", e.getMessage()));
            return "registration";
        } finally {
            em.close();
        }
    }
}