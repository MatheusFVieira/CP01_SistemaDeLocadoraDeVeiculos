package com.locadora;

import com.locadora.cliente.*;
import com.locadora.locacao.Locacao;
import com.locadora.veiculo.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //Frota
        List<Veiculo> frota = new ArrayList<>(Arrays.asList(
                new Veiculo("ABC-001", "Fiat Mobi", "Fiat", 2022, CategoriaVeiculo.ECONOMICO),
                new Veiculo("DEF-002", "VW Virtus", "VW", 2023, CategoriaVeiculo.INTERMEDIARIO),
                new Veiculo("GHI-003", "BMW 320i", "BMW", 2024, CategoriaVeiculo.EXECUTIVO),
                new Veiculo("JKL-004", "Yamaha Fazer", "Yamaha", 2023, CategoriaVeiculo.MOTO),
                new Veiculo("MNO-005", "Ford Transit", "Ford", 2021, CategoriaVeiculo.VAN)
        ));

        //Clientes
        List<Cliente> clientesCadastrados = new ArrayList<>();
        List<Locacao> ativas = new ArrayList<>();

        LocalDate dataHoje = LocalDate.now();
        int seqLoc = 1;

        System.out.println("=== SISTEMA DE GESTÃO - LOCADORA DO SEU CARLOS ===");

        while (true) {
            System.out.println("\n================================================");
            System.out.println("DATA ATUAL: " + dataHoje.format(fmt));
            System.out.println("CLIENTES: " + clientesCadastrados.size() + " | LOCAÇÕES: " + ativas.size() + " | FROTA: " + frota.size());
            System.out.println("================================================");
            System.out.println("1 - Nova Locação");
            System.out.println("2 - Finalizar Aluguel (Devolver)");
            System.out.println("3 - Avançar 1 Dia no Tempo");
            System.out.println("4 - Cadastrar Novo Veículo");
            System.out.println("5 - Atualizar Dados do Cliente");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                Cliente clienteFinal = null;

                System.out.print("\nO cliente já possui cadastro no sistema? (S/N): ");
                String jaEhCliente = scanner.nextLine().toUpperCase();

                if (jaEhCliente.equals("S")) {
                    System.out.print("Digite o CPF do cliente: ");
                    String cpfBusca = scanner.nextLine();

                    for (Cliente cli : clientesCadastrados) {
                        if (cli.getCpf().equals(cpfBusca)) {
                            clienteFinal = cli;
                            break;
                        }
                    }

                    if (clienteFinal == null) {
                        System.out.println("\n❌ Cliente não encontrado. Verifique o CPF ou faça um novo cadastro.");
                        continue;
                    } else {
                        System.out.println("\n✅ Cliente encontrado: " + clienteFinal.getNome());
                    }

                } else {
                    System.out.println("\n--- NOVO CADASTRO ---");
                    System.out.print("Digite o CPF: ");
                    String novoCpf = scanner.nextLine();

                    boolean cpfExiste = false;
                    for (Cliente cli : clientesCadastrados) {
                        if (cli.getCpf().equals(novoCpf)) {
                            cpfExiste = true;
                            break;
                        }
                    }

                    if (cpfExiste) {
                        System.out.println("\n❌ ERRO: Já existe um cliente cadastrado com este CPF.");
                        continue;
                    }

                    System.out.print("Nome Completo: "); String nome = scanner.nextLine();
                    System.out.print("Telefone: "); String tel = scanner.nextLine();
                    System.out.print("E-mail: "); String email = scanner.nextLine();

                    System.out.println("\n--- ENDEREÇO COMPLETO ---");
                    System.out.print("Rua: "); String rua = scanner.nextLine();
                    System.out.print("Número: "); String num = scanner.nextLine();
                    System.out.print("Bairro: "); String bairro = scanner.nextLine();
                    System.out.print("Cidade: "); String cidade = scanner.nextLine();
                    System.out.print("CEP: "); String cep = scanner.nextLine();
                    Endereco endereco = new Endereco(rua, num, bairro, cidade, cep);

                    System.out.println("\n--- DADOS DA CNH ---");
                    System.out.print("Número da CNH: "); String cnhNum = scanner.nextLine();
                    System.out.print("Vencimento da CNH (dd/mm/aaaa): ");
                    LocalDate venc = LocalDate.parse(scanner.nextLine(), fmt);
                    CNH cnh = new CNH(cnhNum, venc);

                    clienteFinal = new Cliente("C" + (clientesCadastrados.size() + 1), nome, novoCpf, tel, email, endereco, cnh);
                    clientesCadastrados.add(clienteFinal);
                    System.out.println("\n✅ Cliente " + nome + " cadastrado com sucesso!");
                }

                System.out.println("\n--- FROTA DISPONÍVEL ---");
                for (int i = 0; i < frota.size(); i++) {
                    Veiculo v = frota.get(i);
                    System.out.println(i + " - " + v.getModelo() + " [" + (v.estaDisponivel() ? "LIVRE" : "ALUGADO") + "] - Diária: R$" + v.getValorDiaria());
                }

                System.out.print("Selecione o número do veículo: ");
                int vIdx = Integer.parseInt(scanner.nextLine());
                System.out.print("Dias de locação: ");
                int dias = Integer.parseInt(scanner.nextLine());

                try {
                    Locacao l = new Locacao("L" + (seqLoc++), clienteFinal, frota.get(vIdx), dataHoje, dataHoje.plusDays(dias));
                    ativas.add(l);
                    System.out.println("\n✅ Locação realizada com sucesso!");
                    System.out.println("Valor parcial estimado (sem multas): R$ " + l.getValorTotal());
                } catch (Exception e) {
                    System.out.println("\n❌ ERRO: " + e.getMessage());
                }

            } else if (op.equals("2")) {
                if (ativas.isEmpty()) {
                    System.out.println("\nNenhuma locação ativa no momento.");
                    continue;
                }
                System.out.println("\n--- DEVOLUÇÃO DE VEÍCULO ---");
                for(int i = 0; i < ativas.size(); i++) {
                    System.out.println(i + " - Locação " + ativas.get(i).getId());
                }
                System.out.print("Qual locação deseja finalizar? Digite o número: ");
                int idx = Integer.parseInt(scanner.nextLine());

                try {
                    Locacao loc = ativas.get(idx);
                    loc.finalizarLocacao(dataHoje);
                    System.out.println("\n✅ Veículo devolvido com sucesso!");
                    System.out.println("💰 VALOR FINAL A PAGAR: R$ " + loc.getValorTotal());
                    ativas.remove(idx);
                } catch (Exception e) {
                    System.out.println("\n❌ ERRO: " + e.getMessage());
                }

            } else if (op.equals("3")) {
                dataHoje = dataHoje.plusDays(1);
                System.out.println("\n⏳ O tempo avançou. Hoje é dia " + dataHoje.format(fmt));

            } else if (op.equals("4")) {
                System.out.println("\n--- CADASTRAR NOVO VEÍCULO ---");
                try {
                    System.out.print("Placa: "); String placaInput = scanner.nextLine();

                    boolean placaJaExiste = false;
                    for (Veiculo v : frota) {
                        if (v.getPlaca().equalsIgnoreCase(placaInput)) {
                            placaJaExiste = true;
                            break;
                        }
                    }

                    if (placaJaExiste) {
                        System.out.println("\n❌ ERRO: A placa " + placaInput + " já está cadastrada.");
                        continue;
                    }

                    System.out.print("Marca: "); String marca = scanner.nextLine();
                    System.out.print("Modelo: "); String modelo = scanner.nextLine();
                    System.out.print("Ano de Fabricação: "); int ano = Integer.parseInt(scanner.nextLine());

                    System.out.println("\nSelecione a Categoria:");
                    System.out.println("1 - ECONOMICO (R$ 80/dia)");
                    System.out.println("2 - INTERMEDIARIO (R$ 120/dia)");
                    System.out.println("3 - EXECUTIVO (R$ 200/dia)");
                    System.out.println("4 - MOTO (R$ 50/dia)");
                    System.out.println("5 - VAN (R$ 180/dia)");
                    System.out.print("Opção: ");
                    int opCat = Integer.parseInt(scanner.nextLine());

                    CategoriaVeiculo cat = CategoriaVeiculo.ECONOMICO;
                    if(opCat == 2) cat = CategoriaVeiculo.INTERMEDIARIO;
                    if(opCat == 3) cat = CategoriaVeiculo.EXECUTIVO;
                    if(opCat == 4) cat = CategoriaVeiculo.MOTO;
                    if(opCat == 5) cat = CategoriaVeiculo.VAN;

                    Veiculo novoVeiculo = new Veiculo(placaInput, modelo, marca, ano, cat);
                    frota.add(novoVeiculo);
                    System.out.println("\n✅ Veículo " + modelo + " adicionado à frota com sucesso!");

                } catch (Exception e) {
                    System.out.println("\n❌ Erro ao cadastrar. Verifique os dados inseridos.");
                }

            } else if (op.equals("5")) {
                System.out.println("\n--- ATUALIZAR DADOS DO CLIENTE ---");
                System.out.print("Digite o CPF do cliente: ");
                String cpfBusca = scanner.nextLine();

                Cliente clienteParaAtualizar = null;
                for (Cliente cli : clientesCadastrados) {
                    if (cli.getCpf().equals(cpfBusca)) {
                        clienteParaAtualizar = cli;
                        break;
                    }
                }

                if (clienteParaAtualizar == null) {
                    System.out.println("\n❌ Cliente não encontrado com este CPF.");
                } else {
                    System.out.println("\nAtualizando dados de: " + clienteParaAtualizar.getNome());
                    System.out.println("1 - E-mail");
                    System.out.println("2 - Telefone");
                    System.out.println("3 - Endereço Completo");
                    System.out.println("4 - Vencimento da CNH");
                    System.out.print("Escolha o que deseja atualizar: ");
                    String opAtualizar = scanner.nextLine();

                    if (opAtualizar.equals("1")) {
                        System.out.print("Novo E-mail: ");
                        clienteParaAtualizar.atualizarEmail(scanner.nextLine());
                        System.out.println("\n✅ E-mail atualizado com sucesso!");
                    } else if (opAtualizar.equals("2")) {
                        System.out.print("Novo Telefone: ");
                        clienteParaAtualizar.atualizarTelefone(scanner.nextLine());
                        System.out.println("\n✅ Telefone atualizado com sucesso!");
                    } else if (opAtualizar.equals("3")) {
                        System.out.println("\n--- NOVO ENDEREÇO ---");
                        System.out.print("Rua: "); String r = scanner.nextLine();
                        System.out.print("Número: "); String n = scanner.nextLine();
                        System.out.print("Bairro: "); String b = scanner.nextLine();
                        System.out.print("Cidade: "); String c = scanner.nextLine();
                        System.out.print("CEP: "); String cep = scanner.nextLine();
                        clienteParaAtualizar.atualizarEndereco(new Endereco(r, n, b, c, cep));
                        System.out.println("\n✅ Endereço atualizado com sucesso!");
                    } else if (opAtualizar.equals("4")) {
                        System.out.print("Número da CNH (confirme o número atual): ");
                        String numCnh = scanner.nextLine();
                        System.out.print("Nova Data de Vencimento da CNH (dd/mm/aaaa): ");
                        LocalDate novaData = LocalDate.parse(scanner.nextLine(), fmt);
                        clienteParaAtualizar.atualizarCnh(new CNH(numCnh, novaData));
                        System.out.println("\n✅ Vencimento da CNH atualizado com sucesso!");
                    } else {
                        System.out.println("\n❌ Opção inválida.");
                    }
                }

            } else if (op.equals("6")) {
                System.out.println("\nEncerrando o sistema.");
                break;
            } else {
                System.out.println("\n❌ Opção inválida!");
            }
        }
        scanner.close();
    }
}