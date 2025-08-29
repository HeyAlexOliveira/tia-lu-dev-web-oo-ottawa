
package app;

public class ItemCardapio {
    private final int codigo;
    private String nome;
    private double preco;

    public ItemCardapio(int codigo, String nome, double preco) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }

    @Override
    public String toString() {
        return String.format("#%d - %s (R$ %.2f)", codigo, nome, preco);
    }
}
