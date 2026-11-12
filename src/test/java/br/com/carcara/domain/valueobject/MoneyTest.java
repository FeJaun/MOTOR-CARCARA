package br.com.carcara.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;


public class MoneyTest {
    
    @Test 
    @DisplayName ("Deve criar Money com sucesso e formatar com 2 casas decimais e arredondamento com HALF_EVEN")
    void deveComEscalaCorreta() {
        Money money = Money.de("100.555");
        // O teste verifica se o objeto arredondou corretamente para 2 casas decimais
        assertThat(money.valor()).isEqualTo(new BigDecimal("100.56"));
    }
    @Test 
    @DisplayName ("Deve rejeitar Money com valor nulo")
    void deveRejeitarMoneyComValorNulo() {
        assertThatThrownBy(() -> Money.de(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Valor não pode ser nulo.");
    }

    @Test 
    @DisplayName ("Deve lançar erro se valor for negativo")
    void deveLancarErroSeValorNegativo() {
        assertThatThrownBy(() -> Money.de("-100.00"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money não pode ser negativo.");  
    }  
    @Test
    @DisplayName ("Deve somar e subtrair corretamente preservando a imutabilidade")
    void deveSomarESubtrairCorretamente() {
        Money money1 = Money.de("100.00");
        Money money2 = Money.de("50.00");
        
        Money soma = money1.somar(money2);
        Money subtracao = money1.subtrair(money2);
        
        // O teste verifica se a soma e subtração estão corretas
        assertThat(soma.valor()).isEqualTo(new BigDecimal("150.00")); 
        assertThat(subtracao.valor()).isEqualTo(new BigDecimal("50.00")); 
        // Garante que o m1 original não foi alterado 
        assertThat(money1.valor()).isEqualTo(new BigDecimal("100.00"));
    }
    @Test
    @DisplayName ("Deve comparar correntamente maiorQue e menorQue")
    void deveCompararCorretamente() {
        Money cem = Money.de("100.00");
        Money duzentos = Money.de("200.00");
        
        // O teste verifica se a comparação está correta
        assertThat(cem.maiorQue(duzentos)).isFalse();
        assertThat(duzentos.menorQue(cem)).isFalse();
        assertThat(duzentos.maiorQue(cem)).isTrue();
    }
    @Test 
    @DisplayName ("Deve formatar corretamente para moeda brasileira")
    void deveFormatarCorretamente() {
        Money money = Money.de("100.00");
        // O teste verifica se a formatação está correta
        assertThat(money.formatar()).contains("R$").contains("100,00");
    }
}
