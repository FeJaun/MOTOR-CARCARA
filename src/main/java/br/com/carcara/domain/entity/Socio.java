package br.com.carcara.domain.entity;

import java.util.Objects;

public record Socio(String nome, String identificador, String qualificacao) {

    public Socio {
        Objects.requireNonNull(nome, "Nome não pode ser nulo.");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio ou conter apenas espaços em branco.");
        }

        Objects.requireNonNull(identificador, "Identificador não pode ser nulo.");
        identificador = identificador.replaceAll("\\D", "");
        if (identificador.isEmpty()) {
            throw new IllegalArgumentException("Identificador inválido: deve conter apenas números.");
        }
        if (identificador.length() != 14 && identificador.length() != 11) {
            throw new IllegalArgumentException("Identificador inválido: deve conter 11 ou 14 dígitos.");
        }
        Objects.requireNonNull(qualificacao, "Qualificação não pode ser nula.");
        if (qualificacao.isBlank()) {
            throw new IllegalArgumentException("Qualificação não pode ser vazia ou conter apenas espaços em branco.");
        }
    }

}
