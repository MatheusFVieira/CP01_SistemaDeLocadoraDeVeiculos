package com.locadora.veiculo;

public class Veiculo {
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private CategoriaVeiculo categoria;
    private String status;

    public Veiculo(String placa, String modelo, String marca, int ano, CategoriaVeiculo categoria) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.categoria = categoria;
        this.status = "DISPONIVEL";
    }

    public double getValorDiaria() {
        return categoria.getValorDiaria();
    }

    public boolean estaDisponivel() {
        return "DISPONIVEL".equals(status);
    }

    public void alugar() {
        if (!estaDisponivel()) {
            throw new IllegalStateException("Erro: O veículo já está alugado.");
        }
        this.status = "ALUGADO";
    }

    public void devolver() {
        this.status = "DISPONIVEL";
    }

    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
}