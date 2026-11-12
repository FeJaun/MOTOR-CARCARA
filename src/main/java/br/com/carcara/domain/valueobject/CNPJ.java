package br.com.carcara.domain.valueobject;

import java.util.Objects;

public record CNPJ(String valor) {

    private static final int[] PESOS_PRIMEIRO_DIGITO = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
    private static final int[] PESOS_SEGUNDO_DIGITO = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

    public CNPJ {
        Objects.requireNonNull(valor, "CNPJ não pode ser nulo.");

        valor = valor.replaceAll("[\\D]", ""); // Remove caracteres não numéricos

        if (valor.length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido: deve conter 14 dígitos.");

        }

        if (valor.matches("(\\d)\\1{13}")) {
            throw new IllegalArgumentException("CNPJ inválido: não pode conter todos os dígitos iguais.");

        }
        if (!validarDigitosVerificadores(valor)) {
            throw new IllegalArgumentException("Dígitos verificadores do CNPJ são inválidos.");
        }

    }

    private static boolean validarDigitosVerificadores(String cnpj) {
        int digito1 = calcularDigito(cnpj.substring(0, 12), PESOS_PRIMEIRO_DIGITO);
        int digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, PESOS_SEGUNDO_DIGITO);

        return cnpj.equals(cnpj.substring(0, 12) + digito1 + digito2);
    }

    private static int calcularDigito(String cnpjParcial, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(cnpjParcial.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    public String formatar() {
        return valor.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}