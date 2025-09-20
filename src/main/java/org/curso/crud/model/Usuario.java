package org.curso.crud.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Usuario {

    @NotNull
    @Size(min = 2, max= 100)
    private String nome;

    @NotNull
    @Size(min= 2, max= 100)
    private String email;

    @NotNull
    @Size(min=12, max= 100)
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
