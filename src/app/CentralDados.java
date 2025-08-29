
package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CentralDados {
    private static final CentralDados INSTANCE = new CentralDados();
    public static CentralDados get() { return INSTANCE; }
    private CentralDados(){}

    private final AtomicInteger seqClientes = new AtomicInteger(1);
    private final AtomicInteger seqItens = new AtomicInteger(1);
    private final AtomicInteger seqPedidos = new AtomicInteger(100);

    private final Map<Integer, Cliente> clientes = new LinkedHashMap<>();
    private final Map<Integer, ItemCardapio> itens = new LinkedHashMap<>();
    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();
    private final List<String> eventosStatus = new ArrayList<>();

    public Cliente cadastrarCliente(String nome, String telefone){
        int codigo = seqClientes.getAndIncrement();
        Cliente c = new Cliente(codigo, nome, telefone);
        clientes.put(codigo, c);
        return c;
    }
    public Collection<Cliente> listarClientes(){ return Collections.unmodifiableCollection(clientes.values()); }
    public Optional<Cliente> buscarClientePorCodigo(int codigo){ return Optional.ofNullable(clientes.get(codigo)); }

    public ItemCardapio cadastrarItem(String nome, double preco){
        int codigo = seqItens.getAndIncrement();
        ItemCardapio it = new ItemCardapio(codigo, nome, preco);
        itens.put(codigo, it);
        return it;
    }
    public Collection<ItemCardapio> listarItens(){ return Collections.unmodifiableCollection(itens.values()); }
    public Optional<ItemCardapio> buscarItemPorCodigo(int codigo){ return Optional.ofNullable(itens.get(codigo)); }

    public int proximoNumeroPedido(){ return seqPedidos.getAndIncrement(); }

    public void adicionarPedido(Pedido p){
        if (p.getNumero() <= 0) {
            throw new IllegalArgumentException("Pedido ainda não confirmado (sem número).");
        }
        pedidos.put(p.getNumero(), p);
    }

    public Collection<Pedido> listarPedidos(){ return Collections.unmodifiableCollection(pedidos.values()); }

    public List<Pedido> pedidosPorStatus(FluxoEntrega status){
        return pedidos.values().stream().filter(p -> p.getStatus()==status).collect(Collectors.toList());
    }

    public List<Pedido> pedidosDoDia(LocalDate dia){
        return pedidos.values().stream().filter(p ->
            p.getCriadoEm()!=null && p.getCriadoEm().toLocalDate().equals(dia)
        ).collect(Collectors.toList());
    }

    public void registrarEventoStatus(Pedido p){
        String ev = LocalDateTime.now() + " - Pedido " + p.getNumero() + " status: " + p.getStatus();
        eventosStatus.add(ev);
    }

    public List<String> eventosStatus(){ return Collections.unmodifiableList(eventosStatus); }
}
