package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.entity.Socio;
import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.enums.TipoAnomalia;
import br.com.carcara.domain.valueobject.CNPJ;
import br.com.carcara.domain.valueobject.Money;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegraVinculoSocietarioStrategyTest {

    Socio socioComum = new Socio("Joao", "12345678900", "Socio-Administrador");
    Socio socioOutro = new Socio("Maria Santos", "98765432100", "Sócia");

    Empresa emp1 = new Empresa(
            "Alpha Suprimentos LTDA",
            new CNPJ("00.000.000/0001-91"),
            LocalDate.of(2023, 1, 15),
            new Money(new BigDecimal("50000.00")),
            List.of(socioComum));
    Empresa emp2 = new Empresa(
            "Beta Distribuicao e Servicos LTDA",
            new CNPJ("33.000.167/0001-01"),
            LocalDate.of(2022, 6, 10),
            new Money(new BigDecimal("100000.00")),
            List.of(socioComum, socioOutro));

    @Test
    @DisplayName("Deve gerar alerta crítico quando duas empresas concorrentes possuem sócio em comum")
    void deveGerarAlertaQuandoEmpresasPossuemSociosEmComum() {
        RegraVinculoSocietarioStrategy regra = new RegraVinculoSocietarioStrategy();

        List<AlertaAnomalia> alertas = regra.verificarAnomalias(List.of(emp1, emp2));

        assertFalse(alertas.isEmpty());
        assertEquals(1, alertas.size());
        assertEquals(TipoAnomalia.SOCIOS_CONCORRENTES, alertas.get(0).tipoAnomalia());
        assertEquals(NivelSeveridade.CRITICO, alertas.get(0).nivelSeveridade());

    }

    @Test
    @DisplayName("Não deve gerar alerta quando concorrentes possuem sócios distintos")
    void naoDeveGerarAlertaQuandoSociosForemDistintos() {
        RegraVinculoSocietarioStrategy regra = new RegraVinculoSocietarioStrategy();

        Socio socioA = new Socio("Carlos Souza", "11122233344", "Administrador");
        Socio socioB = new Socio("Fernanda Lima", "55566677788", "Administradora");

        Empresa empA = new Empresa(
                "Fornecedora A LTDA",
                new CNPJ("00.000.000/0001-91"),
                LocalDate.of(2021, 5, 10),
                new Money(new BigDecimal("100000.00")),
                List.of(socioA));

        Empresa empB = new Empresa(
                "Fornecedora B LTDA",
                new CNPJ("33.000.167/0001-01"),
                LocalDate.of(2020, 8, 20),
                new Money(new BigDecimal("150000.00")),
                List.of(socioB));

        List<AlertaAnomalia> alertas = regra.verificarAnomalias(List.of(empA, empB));

        assertTrue(alertas.isEmpty(), "Não deveria gerar alertas para empresas com sócios distintos");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando lista de participantes for nula, vazia ou unitária")
    void deveRetornarVazioQuandoEntradaForInsuficiente() {
        RegraVinculoSocietarioStrategy regra = new RegraVinculoSocietarioStrategy();

        assertTrue(regra.verificarAnomalias(null).isEmpty());
        assertTrue(regra.verificarAnomalias(List.of()).isEmpty());
        assertTrue(regra.verificarAnomalias(List.of(emp1)).isEmpty());
    }

}
