
package app;

public class ItemPedido {
    private final ItemCardapio item;
    private final int quantidade;

    public ItemPedido(ItemCardapio item, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        this.item = item;
        this.quantidade = quantidade;
    }

    public ItemCardapio getItem() { return item; }
    public int getQuantidade() { return quantidade; }
    public double getSubtotal() { return item.getPreco() * quantidade; }

    @Override
    public String toString() {
        return String.format("%dx %s (R$ %.2f) = R$ %.2f",
            quantidade, item.getNome(), getItem().getPreco(), getSubtotal());
    }
}
