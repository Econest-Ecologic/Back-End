package com.ecommerce.equipe.model;

public enum Estado {
    AC(35.00),
    AL(28.00),
    AP(32.00),
    AM(40.00),
    BA(28.00),
    CE(27.00),
    DF(25.00),
    ES(22.00),
    GO(23.00),
    MA(30.00),
    MT(26.00),
    MS(24.00),
    MG(20.00),
    PA(33.00),
    PB(27.00),
    PR(18.00),
    PE(26.00),
    PI(29.00),
    RJ(30.00),
    RN(28.00),
    RS(23.00),
    RO(34.00),
    RR(36.00),
    SC(20.00),
    SP(25.00),
    SE(27.00),
    TO(31.00);

    private final double valorFrete;

    Estado(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public double getValorFrete() {
        return valorFrete;
    }
}


