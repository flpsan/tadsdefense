package util;

public class Dimensao {

    private int largura;
    private int altura;

    public Dimensao(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
    }

    public Dimensao(float largura, float altura) {
        this.largura = (int) largura;
        this.altura = (int) altura;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

}
