package br.com.carcara.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Locale;
import java.text.NumberFormat;

public record Money(BigDecimal valor) implements Comparable<Money> {

    private static final Locale LOCALE_BR = Locale.forLanguageTag("pt-BR");


    public Money {
        Objects.requireNonNull(valor, "Money não pode ser nulo.");
        valor = valor.setScale(2, RoundingMode.HALF_EVEN);
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money não pode ser negativo.");
        }
    }

    public Money somar(Money outro) {
        Objects.requireNonNull(outro, "Outro Money não pode ser nulo.");
        return new Money(this.valor.add(outro.valor));
    }

    public Money subtrair(Money outro) {
        Objects.requireNonNull(outro, "Outro Money não pode ser nulo.");
        return new Money(this.valor.subtract(outro.valor));
    }

    public boolean menorQue(Money outro) {
        Objects.requireNonNull(outro, "Outro Money não pode ser nulo.");
        return this.valor.compareTo(outro.valor) < 0;
    }

    public boolean maiorQue(Money outro) {
        Objects.requireNonNull(outro, "Outro Money não pode ser nulo.");
        return this.valor.compareTo(outro.valor) > 0;
    }

    public static Money de(String valor) {
        Objects.requireNonNull(valor, "Valor não pode ser nulo.");
        return new Money(new BigDecimal(valor));
    }

    public static Money de(double valor) {
        Objects.requireNonNull(valor, "Valor não pode ser nulo.");
        return new Money(BigDecimal.valueOf(valor));
    }
    @Override 
    public int compareTo(Money outro) {
        Objects.requireNonNull(outro, "Outro Money não pode ser nulo.");
        return this.valor.compareTo(outro.valor);
    }


    public String formatar() {
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(this.valor);
    }
}