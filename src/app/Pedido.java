
package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private int numero;
    private final Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();
    private FluxoEntrega status;
    private boolean aberta = true;
    private LocalDateTime criadoEm;

    public Pedido(Cliente cliente){
        this.cliente = cliente;
    }

    public void adicionarItem(ItemCardapio item, int quantidade){
        if (!aberta) throw new IllegalStateException("Pedido já foi confirmado, não é possível adicionar itens.");
        itens.add(new ItemPedido(item, quantidade));
    }

    public double getTotal(){
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public void confirmar(){
        if (cliente==null) throw new IllegalStateException("Pedido sem cliente.");
        if (itens.isEmpty()) throw new IllegalStateException("Pedido sem itens.");
        if (!aberta) throw new IllegalStateException("Pedido já confirmado.");
        this.numero = CentralDados.get().proximoNumeroPedido();
        this.criadoEm = LocalDateTime.now();
        this.status = FluxoEntrega.ACEITO;
        this.aberta = false;
        CentralDados.get().adicionarPedido(this);
        CentralDados.get().registrarEventoStatus(this);
    }

    public void avancarStatus(){
        if (aberta) throw new IllegalStateException("Pedido ainda não confirmado.");
        switch (status){
            case ACEITO -> status = FluxoEntrega.PREPARANDO;
            case PREPARANDO -> status = FluxoEntrega.FEITO;
            case FEITO -> status = FluxoEntrega.AGUARDANDO_ENTREGADOR;
            case AGUARDANDO_ENTREGADOR -> status = FluxoEntrega.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> status = FluxoEntrega.ENTREGUE;
            case ENTREGUE -> throw new IllegalStateException("Pedido já entregue.");
            default -> throw new IllegalStateException("Status inválido.");
        }
        CentralDados.get().registrarEventoStatus(this);
    }

    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public FluxoEntrega getStatus() { return status; }
    public boolean isAberta() { return aberta; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(numero == 0 ? "(rascunho)" : numero)
          .append(" - Cliente: ").append(cliente.getNome())
          .append(" - Status: ").append(aberta ? "ABERTO" : status)
          .append("\n");
        for (ItemPedido ip : itens) sb.append("  ").append(ip).append("\n");
        sb.append(String.format("Total: R$ %.2f", getTotal()));
        if (criadoEm != null) sb.append(" - Criado em: ").append(criadoEm);
        return sb.toString();
    }
}
