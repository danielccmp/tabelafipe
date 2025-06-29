package br.com.alura.tabelafipe.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.Modelos;
import br.com.alura.tabelafipe.model.Veiculo;
import br.com.alura.tabelafipe.service.ConsumoAPI;
import br.com.alura.tabelafipe.service.ConverteDados;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() {
        var menu = """
                ==== Bem vindo a Consulta FIPE ====
                Qual o tipo de veículo que deseja consultar? (carro, moto ou caminhão):
                """;
        System.out.print(menu);
        var opcao = leitura.nextLine();

        String endereco;
        if (opcao.toLowerCase().contains("car")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }
        var json = consumo.obterDados(endereco);

        System.out.println("\nMarcas disponíveis de " + opcao + ": ");
        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.print("\nInforme o código da marca que deseja consultar: ");
        var opcaoMarca = leitura.nextLine();
        var nomeMarca = marcas.stream().filter(n -> n.codigo().contains(opcaoMarca)).findFirst();
        if (nomeMarca.isPresent()) {
            System.out.println("Marca escolhida: " + nomeMarca.get());
        } else {
            System.out.println("Marca não encontrada.");
        }

        endereco = endereco + "/" + opcaoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        // System.out.println("\nModelos disponíveis para a marca informada: ");
        // modeloLista.modelos().stream()
        // .sorted(Comparator.comparing(Dados::codigo))
        // .forEach(System.out::println);

        System.out.print("\nInforme o nome do modelo que deseja consultar: ");
        var nomeVeiculo = leitura.nextLine();
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("\nSua busca por " + "'" + nomeVeiculo + "'" + " trouxe os seguintes resultados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.print("\nInforme agora o código do modelo que deseja consultar: ");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\nForam localizadas as seguintes avaliações anuais: ");
        veiculos.forEach(System.out::println);
    }
}
