package br.com.carcara.domain.entity;

import br.com.carcara.domain.valueobject.CNPJ;
import br.com.carcara.domain.valueobject.Money;
import java.time.LocalDate;
import java.util.Objects;

public record Contrato(
        CNPJ cnpjContratada,
        Money valor,
        LocalDate dataInicio,
        LocalDate dataFim,
        String numeroContrato,
        String orgaoPublico,
        String objeto) {
    public Contrato {
        Objects.requireNonNull(cnpjContratada, "CNPJ não pode ser nulo.");
        // Validação do CNPJ é feita na classe CNPJ
        Objects.requireNonNull(valor, "Valor não pode ser nulo.");
        // Validação do Money é feita na classe Money
        Objects.requireNonNull(dataInicio, "Data de início não pode ser nula.");
        Objects.requireNonNull(dataFim, "Data de fim não pode ser nula.");
        Objects.requireNonNull(numeroContrato, "Número do contrato não pode ser nulo.");
        Objects.requireNonNull(orgaoPublico, "Órgão público não pode ser nulo.");
        Objects.requireNonNull(objeto, "Objeto do contrato não pode ser nulo.");

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim.");
        }
        if (numeroContrato.isBlank()) {
            throw new IllegalArgumentException(
                    "Número do contrato não pode ser vazio ou conter apenas espaços em branco.");
        }
        if (orgaoPublico.isBlank()) {
            throw new IllegalArgumentException(
                    "Órgão público não pode ser vazio ou conter apenas espaços em branco.");
        }
        if (objeto.isBlank()) {
            throw new IllegalArgumentException(
                    "Objeto do contrato não pode ser vazio ou conter apenas espaços em branco.");
        }
    }
}