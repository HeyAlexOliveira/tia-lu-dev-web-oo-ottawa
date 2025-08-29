package app;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class SistemaPedidos {
    private final CentralDados dados = CentralDados.get();

    public Cliente cadastrarCliente(String nome, String telefone) {
        return dados.cadastrarCliente(nome, telefone);
    }

    public Collection<Cliente> listarClientes() {
        return dados.listarClientes();
    }

    public ItemCardapio cadastrarItem(String nome, double preco) {
        return dados.cadastrarItem(nome, preco);
    }

    public Collection<ItemCardapio> listarItens() {
        return dados.listarItens();
    }

    public Pedido criarPedido(int codigoCliente) {
        Cliente cliente = dados.buscarClientePorCodigo(codigoCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        Pedido pedido = new Pedido(cliente);
        
        return pedido;
    }

    public void adicionarItemAoPedido(Pedido pedido, int codigoItem, int quantidade) {
        ItemCardapio it = dados.buscarItemPorCodigo(codigoItem)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));
        pedido.adicionarItem(it, quantidade);
    }

    public void confirmarPedido(Pedido pedido) {
        pedido.confirmar();
    }

    public void avancarStatus(int numeroPedido) {
        Pedido p = dados.listarPedidos().stream()
                .filter(pd -> pd.getNumero() == numeroPedido)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
        p.avancarStatus();
    }

    public List<Pedido> pedidosPorStatus(FluxoEntrega status) {
        return dados.pedidosPorStatus(status);
    }

    public String relatorio(LocalDate dia, Relatorios.TipoRelatorio tipo) {
        return Relatorios.gerar(tipo, dia, dados.pedidosDoDia(dia));
    }
}
