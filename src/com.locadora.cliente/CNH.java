package com.locadora.cliente;

import java.time.LocalDate;

public class CNH {
    private final String numero;
    private final LocalDate dataVencimento;

    public CNH(String numero, LocalDate dataVencimento) {
        this.numero = numero;
        this.dataVencimento = dataVencimento;
    }

    public String getNumero() { return numero; }
    public LocalDate getDataVencimento() { return dataVencimento; }

    public boolean estaValida() {
        return !LocalDate.now().isAfter(dataVencimento);
    }
}