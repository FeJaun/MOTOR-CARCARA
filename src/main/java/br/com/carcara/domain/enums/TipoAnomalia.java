package br.com.carcara.domain.enums;

public enum TipoAnomalia {
    EMPRESA_RECEM_CRIADA("Empresa recém criada", 1),
    CAPITAL_SOCIAL_INCOMPATIVEL("Capital social incompatível",2),
    SOCIOS_CONCORRENTES("Sócios concorrentes", 3),
    SOBREPRECO_ESTATISTICO("Sobrepreço estatístico", 4);

    private final String descricao;
    private final int codigo;

    TipoAnomalia(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCodigo() {
        return codigo;
    }
}
