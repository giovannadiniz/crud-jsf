package org.curso.crud.bean;

import jakarta.annotation.ManagedBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.curso.crud.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    private String email;
    private String senha;
    private String loggedUser;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("crudPU");

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getLoggedUser() { return loggedUser; }

    public String login() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.email = :email");
            query.setParameter("email", email);
            List<Usuario> users = query.getResultList();
            if (!users.isEmpty()) {
                Usuario user = users.get(0);
                if (BCrypt.checkpw(senha, user.getSenha())) {
                    loggedUser = user.getEmail();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", loggedUser);
                    return "index?faces-redirect=true";
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login inválido", "Email ou senha incorretos"));
            return "login";
        } finally {
            em.close();
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }
}
