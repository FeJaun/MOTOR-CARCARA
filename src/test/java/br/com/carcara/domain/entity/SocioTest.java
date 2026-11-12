package br.com.carcara.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SocioTest {
    @Test
    @DisplayName("Deve criar um sócio válido com nome, identificador e qualificação válidos")
    public void deveCriarSocioValido() {
        String nome = "João da Silva";
        String identificador = "12345678901"; // CPF válido
        String qualificacao = "Administrador";
        Socio socio = new Socio(nome, identificador, qualificacao);
        assertThat(socio.nome()).isEqualTo(nome);
        assertThat(socio.identificador()).isEqualTo(identificador);
        assertThat(socio.qualificacao()).isEqualTo(qualificacao);
    }

    @Test
    @DisplayName("Deve sanitizar indentificador contendo pontos, traços e barras")
    public void deveSanitizarIdentificador() {
        Socio socioCpf = new Socio("João da Silva", "123.456.789-01", "Administrador");
        Socio socioCnpj = new Socio("Maria de Souza", "12.345.678/0001-90", "Sócio");

        assertThat(socioCpf.identificador()).isEqualTo("12345678901");
        assertThat(socioCnpj.identificador()).isEqualTo("12345678000190");
    }

    @Test
    @DisplayName("Deve rejeitar sócio com identificador nulo, vazio ou inválido")
    public void deveRejeitarSocioComIdentificadorNuloOuInvalido() {
        assertThatThrownBy(() -> new Socio("João da Silva", null, "Administrador"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Identificador não pode ser nulo.");
        assertThatThrownBy(() -> new Socio("João da Silva", "   ", "Administrador"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Identificador inválido: deve conter apenas números.");
        assertThatThrownBy(() -> new Socio("João da Silva", "123456789", "Administrador"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Identificador inválido: deve conter 11 ou 14 dígitos.");
    }

    @Test
    @DisplayName("Deve rejeitar sócio com nome nulo ou vazio")
    public void deveRejeitarSocioComNomeNuloOuVazio() {
        assertThatThrownBy(() -> new Socio(null, "12345678901", "Administrador"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Nome não pode ser nulo.");
        assertThatThrownBy(() -> new Socio("   ", "12345678901", "Administrador"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome não pode ser vazio ou conter apenas espaços em branco.");
    }

    @Test
    @DisplayName("Deve rejeitar sócio com qualificação nula ou vazia")
    public void deveRejeitarSocioComQualificacaoNulaOuVazia() {
        assertThatThrownBy(() -> new Socio("João da Silva", "12345678901", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Qualificação não pode ser nula.");
        assertThatThrownBy(() -> new Socio("João da Silva", "12345678901", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Qualificação não pode ser vazia ou conter apenas espaços em branco.");
    }
}
