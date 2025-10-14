package com.smarttruck.presentation.dto;

public class ClienteResponse {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    public ClienteResponse(Long id, String nome, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
}

