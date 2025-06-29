package br.com.alura.tabelafipe.model;

public record Dados(String codigo, String nome) {

    @Override
    public String toString() {
        return "Código " + codigo + " | " + nome;
    }

}
