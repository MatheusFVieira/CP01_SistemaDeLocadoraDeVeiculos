package com.locadora.locacao;

import com.locadora.cliente.Cliente;
import com.locadora.veiculo.Veiculo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {
    private String id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim; // Data prevista
    private double valorTotal;
    private boolean ativa;

    public Locacao(String id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        if (!veiculo.estaDisponivel()) {
            throw new IllegalArgumentException("Erro: Veículo já está alugado.");
        }
        if (!cliente.temCnhValida()) {
            throw new IllegalArgumentException("Regra: Não é permitido alugar para clientes com CNH vencida.");
        }

        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = true;

        veiculo.alugar();

        long diasPrevistos = ChronoUnit.DAYS.between(dataInicio, dataFim);
        if (diasPrevistos <= 0) diasPrevistos = 1;
        this.valorTotal = diasPrevistos * veiculo.getValorDiaria();
    }

    public void finalizarLocacao(LocalDate dataRealDevolucao) {
        if (!ativa) throw new IllegalStateException("Locação já encerrada.");

        veiculo.devolver();
        this.ativa = false;

        long totalDiasDeFato = ChronoUnit.DAYS.between(dataInicio, dataRealDevolucao);
        if (totalDiasDeFato <= 0) totalDiasDeFato = 1;

        double valorDiariasTotais = totalDiasDeFato * veiculo.getValorDiaria();
        double multaTotal = 0;

        if (dataRealDevolucao.isAfter(dataFim)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataFim, dataRealDevolucao);
            multaTotal = diasAtraso * 50.0;
            System.out.println("\n⚠️ ATRASO DETECTADO: " + diasAtraso + " dia(s).");
            System.out.println("Multa aplicada: R$ 50,00 por dia.");
        }

        this.valorTotal = valorDiariasTotais + multaTotal;
    }

    public double getValorTotal() { return valorTotal; }
    public String getId() { return id; }
}