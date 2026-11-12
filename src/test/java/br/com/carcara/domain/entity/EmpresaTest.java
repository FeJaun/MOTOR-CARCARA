package br.com.carcara.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import br.com.carcara.domain.valueobject.CNPJ;
import br.com.carcara.domain.valueobject.Money;

public class EmpresaTest {
    @Test
    @DisplayName("Deve criar uma empresa válida com nome, CNPJ, data de abertura, capital social e lista de sócios válidos ")
    public void deveCriarEmpresaValida() {
        String nome = "Empresa Teste";
        CNPJ cnpj = new CNPJ("00.000.000/0001-91");
        LocalDate dataAbertura = LocalDate.of(2020, 1, 1);
        Money capitalSocial = new Money(BigDecimal.valueOf(100000.00));
        Socio socio = new Socio("João da Silva", "12345678901", "Administrador");
        List<Socio> socios = List.of(socio);

        Empresa empresa = new Empresa(nome, cnpj, dataAbertura, capitalSocial, socios);

        assertThat(empresa.nome()).isEqualTo(nome);
        assertThat(empresa.cnpj()).isEqualTo(cnpj);
        assertThat(empresa.dataAbertura()).isEqualTo(dataAbertura);
        assertThat(empresa.capitalSocial()).isEqualTo(capitalSocial);
        assertThat(empresa.socios()).containsExactly(socio);
    }

    @Test
    @DisplayName("Nome da empresa não pode ser nulo,vazio ou conter apenas espaços em branco")
    public void deveRejeitarEmpresaComNomeNuloOuVazio() {
        assertThatThrownBy(() -> new Empresa(null, new CNPJ("00.000.000/0001-91"), LocalDate.of(2020, 1, 1),
                new Money(BigDecimal.valueOf(100000.00)),
                List.of(new Socio("João da Silva", "12345678901", "Administrador"))))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Nome não pode ser nulo.");
        assertThatThrownBy(() -> new Empresa("   ", new CNPJ(        "00.000.000/0001-91"), LocalDate.of(2020, 1, 1),
                new Money(BigDecimal.valueOf(100000.00)),
                List.of(new Socio("João da Silva", "12345678901", "Administrador"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome não pode ser vazio ou conter apenas espaços em branco.");
    }

    @Test
    @DisplayName("Deve rejeitar empresa com CNPJ nulo")
    public void deveRejeitarEmpresaComCNPJNulo() {
        assertThatThrownBy(() -> new Empresa("Empresa Teste", null, LocalDate.of(2020, 1, 1),
                new Money(BigDecimal.valueOf(100000.00)),
                List.of(new Socio("João da Silva", "12345678901", "Administrador"))))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("CNPJ não pode ser nulo.");
    }

    @Test
    @DisplayName("Deve rejeitar empresa com data de abertura nula ou futura")
    public void deveRejeitarEmpresaComDataAberturaNulaOuFutura() {
        assertThatThrownBy(() -> new Empresa("Empresa Teste", new CNPJ("00.000.000/0001-91"), null,
                new Money(BigDecimal.valueOf(100000.00)),
                List.of(new Socio("João da Silva", "12345678901", "Administrador"))))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Data de abertura não pode ser nula.");
        assertThatThrownBy(() -> new Empresa("Empresa Teste", new CNPJ("00.000.000/0001-91"),
                LocalDate.now().plusDays(1), new Money(BigDecimal.valueOf(100000.00)),
                List.of(new Socio("João da Silva", "12345678901", "Administrador"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de abertura não pode ser futura.");
    }

    @Test
    @DisplayName("Deve rejeitar empresa com capital social nulo ou negativo")
    public void deveRejeitarEmpresaComCapitalSocialNuloOuNegativo() {
        assertThatThrownBy(() -> new Empresa("Empresa Teste", new CNPJ("00.000.000/0001-91"), LocalDate.of(2020, 1, 1),
                null, List.of(new Socio("João da Silva", "12345678901", "Administrador"))))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Capital social não pode ser nulo.");
                }

    @Test
    @DisplayName("Deve rejeitar empresa com lista de sócios nula ou vazia")
    public void deveRejeitarEmpresaComListaSociosNulaOuVazia() {
        assertThatThrownBy(() -> new Empresa("Empresa Teste", new CNPJ("00.000.000/0001-91"), LocalDate.of(2020, 1, 1),
                new Money(BigDecimal.valueOf(100000.00)), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Lista de sócios não pode ser nula.");
        assertThatThrownBy(() -> new Empresa("Empresa Teste", new CNPJ("00.000.000/0001-91"), LocalDate.of(2020, 1, 1),
                new Money(BigDecimal.valueOf(100000.00)), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A empresa deve ter pelo menos um sócio no QSA.");
    }
}
