package br.com.carcara.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.com.carcara.domain.valueobject.CNPJ;
import br.com.carcara.domain.valueobject.Money;

public class ContratoTest {
    @Test
    @DisplayName("Deve criar um contrato válido com número, data de início, data de término, valor e empresa válidos")
    public void deveCriarContratoValido() {
        CNPJ cnpj = new CNPJ("12.345.678/0001-95");
        Money valor = new Money(new BigDecimal("1000.00"));
        LocalDate dataInicio = LocalDate.of(2023, 1, 1);
        LocalDate dataTermino = LocalDate.of(2024, 1, 1);
        String numeroContrato = "12345";
        String orgaoPublico = "Prefeitura Municipal";
        String objeto = "Prestação de serviços de limpeza";

        Contrato contrato = new Contrato(cnpj, valor, dataInicio, dataTermino, numeroContrato, orgaoPublico, objeto);
        assertThat(contrato).isNotNull();

        assertThat(contrato.cnpjContratada()).isEqualTo(cnpj);
        assertThat(contrato.valor()).isEqualTo(valor);
        assertThat(contrato.dataInicio()).isEqualTo(dataInicio);
        assertThat(contrato.dataFim()).isEqualTo(dataTermino);
        assertThat(contrato.numeroContrato()).isEqualTo(numeroContrato);
        assertThat(contrato.orgaoPublico()).isEqualTo(orgaoPublico);
        assertThat(contrato.objeto()).isEqualTo(objeto);
    }

    @Test
    @DisplayName("Deve rejeitar contrato com datas nulas, data de início posterior à data de término")
    public void deveRejeitarContratoComDataInicioPosteriorDataTermino() {
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2023, 1, 1),
                "12345",
                "Prefeitura Municipal",
                "Prestação de serviços de limpeza"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início não pode ser posterior à data de fim.");
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                null,
                LocalDate.of(2024, 1, 1),
                "12345",
                "Prefeitura Municipal",
                "Prestação de serviços de limpeza"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Data de início não pode ser nula.");
    }

    @Test
    @DisplayName("Deve rejeitar contrato com número nulo,vazio ou contendo apenas espaços em branco")
    public void deveRejeitarContratoComNumeroVazio() {
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                "   ",
                "Prefeitura Municipal",
                "Prestação de serviços de limpeza"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número do contrato não pode ser vazio ou conter apenas espaços em branco.");
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                null,
                "Prefeitura Municipal",
                "Prestação de serviços de limpeza"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Número do contrato não pode ser nulo.");
    }

    @Test
    @DisplayName("Órgão público não pode ser nulo,vazio ou conter apenas espaços em branco")
    public void deveRejeitarContratoComOrgaoPublicoVazio() {
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                "12345",
                "   ",
                "Prestação de serviços de limpeza"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Órgão público não pode ser vazio ou conter apenas espaços em branco.");
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                "12345",
                null,
                "Prestação de serviços de limpeza"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Órgão público não pode ser nulo.");
    }

    @Test
    @DisplayName("Objeto do contrato não pode ser nulo, vazio ou conter apenas espaços em branco")
    public void deveRejeitarContratoComObjetoVazio() {
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                "12345",
                "Prefeitura Municipal",
                "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Objeto do contrato não pode ser vazio ou conter apenas espaços em branco.");
        assertThatThrownBy(() -> new Contrato(
                new CNPJ("12.345.678/0001-95"),
                new Money(new BigDecimal("1000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                "12345",
                "Prefeitura Municipal",
                null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Objeto do contrato não pode ser nulo.");
    }
}
