package br.com.carcara.domain.enums;

public enum NivelSeveridade {

    BAIXO("Baixo", 1),
    MEDIO("Médio", 2),
    ALTO("Alto", 3),
    CRITICO("Crítico", 4);

    private final String descricao;
    private final int nivel;

    NivelSeveridade(String descricao, int nivel) {
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getNivel() {
        return nivel;
    }

}
