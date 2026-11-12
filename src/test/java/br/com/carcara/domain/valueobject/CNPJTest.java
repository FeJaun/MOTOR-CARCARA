package br.com.carcara.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CNPJTest {

    @Test
    void deveCriarCnpjComSucessoERemoverFormatacao() {
        // O teste só instancia passando a máscara
        CNPJ cnpj = new CNPJ("12.345.678/0001-95");

        // O teste verifica se o objeto limpou sozinho
        assertThat(cnpj.valor()).isEqualTo("12345678000195");
        assertThat(cnpj.formatar()).isEqualTo("12.345.678/0001-95");
    }

    @Test
    void deveLancarErroSeTiverMenosDe14Digitos() {
        // O teste verifica se o objeto barrou o dado inválido
        assertThatThrownBy(() -> new CNPJ("12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CNPJ inválido: deve conter 14 dígitos.");
    }

    @Test
    void deveLancarErroSeDigitosVerificadoresForemInvalidos() {
        assertThatThrownBy(() -> new CNPJ("00.000.000/0001-92"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dígitos verificadores do CNPJ são inválidos.");
    }

    @Test 
    @DisplayName("Deve rejeitar CNPJ com dígitos repetidos") 
    void deveLancarErroSeDigitosForemRepetidos() { 
        assertThatThrownBy(() -> new CNPJ("11.111.111/1111-11"))
            .isInstanceOf(IllegalArgumentException.class) 
            .hasMessage("CNPJ inválido: não pode conter todos os dígitos iguais."); 
    }

}