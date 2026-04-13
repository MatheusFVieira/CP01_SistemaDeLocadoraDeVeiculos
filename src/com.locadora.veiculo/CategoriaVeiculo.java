package com.locadora.veiculo;

public enum CategoriaVeiculo {
    ECONOMICO(80.0),
    INTERMEDIARIO(120.0),
    EXECUTIVO(200.0),
    MOTO(50.0),
    VAN(180.0);

    private final double valorDiaria;

    CategoriaVeiculo(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }
}