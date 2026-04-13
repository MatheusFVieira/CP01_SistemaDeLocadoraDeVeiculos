package com.locadora.cliente;

public class Cliente {
    private String id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Endereco endereco;
    private CNH cnh;

    public Cliente(String id, String nome, String cpf, String telefone, String email, Endereco endereco, CNH cnh) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.cnh = cnh;
    }

    public void atualizarTelefone(String novoTelefone) {
        this.telefone = novoTelefone;
    }

    public void atualizarEmail(String novoEmail) {
        this.email = novoEmail;
    }

    public void atualizarEndereco(Endereco novoEndereco) {
        this.endereco = novoEndereco;
    }

    public boolean temCnhValida() {
        return this.cnh != null && this.cnh.estaValida();
    }

    public void atualizarCnh(CNH novaCnh) { this.cnh = novaCnh;}

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
}