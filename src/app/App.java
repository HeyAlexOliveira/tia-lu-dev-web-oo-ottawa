
package app;

import java.time.LocalDate;
import java.util.Scanner;

public class App {
    private static final SistemaPedidos sistema = new SistemaPedidos();
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        seed();
        menu();
    }

    private static void seed(){
        sistema.cadastrarItem("Hambúrguer", 22.0);
        sistema.cadastrarItem("Batata Frita", 12.5);
        sistema.cadastrarItem("Refrigerante", 8.0);
    }

    private static void menu(){
        while (true){
            System.out.println(
                "\n===== FoodDelivery Ottawa =====\n" +
                "1) Cadastrar cliente\n" +
                "2) Listar clientes\n" +
                "3) Cadastrar item do cardápio\n" +
                "4) Listar itens do cardápio\n" +
                "5) Criar novo pedido\n" +
                "6) Avançar status de um pedido\n" +
                "7) Listar pedidos por status\n" +
                "8) Relatório do dia (simplificado)\n" +
                "9) Relatório do dia (detalhado)\n" +
                "0) Sair\n"
            );
            System.out.print("Opção: ");
            String op = in.nextLine().trim();
            try {
                switch (op){
                    case "1" -> cadastrarCliente();
                    case "2" -> listarClientes();
                    case "3" -> cadastrarItem();
                    case "4" -> listarItens();
                    case "5" -> criarPedido();
                    case "6" -> avancarStatus();
                    case "7" -> listarPorStatus();
                    case "8" -> relatorioSimplificado();
                    case "9" -> relatorioDetalhado();
                    case "0" -> { System.out.println("Tchau!"); return; }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e){
                System.out.println("[ERRO] " + e.getMessage());
            }
        }
    }

    private static void cadastrarCliente(){
        System.out.print("Nome: ");
        String nome = in.nextLine();
        System.out.print("Telefone: ");
        String tel = in.nextLine();
        var c = sistema.cadastrarCliente(nome, tel);
        System.out.println("Cliente cadastrado: " + c);
    }

    private static void listarClientes(){
        System.out.println("Clientes:");
        sistema.listarClientes().forEach(System.out::println);
    }

    private static void cadastrarItem(){
        System.out.print("Nome do item: ");
        String nome = in.nextLine();
        System.out.print("Preço: ");
        double preco = Double.parseDouble(in.nextLine());
        var it = sistema.cadastrarItem(nome, preco);
        System.out.println("Item cadastrado: " + it);
    }

    private static void listarItens(){
        System.out.println("Cardápio:");
        sistema.listarItens().forEach(System.out::println);
    }

    private static void criarPedido(){
        System.out.print("Código do cliente: ");
        int cid = Integer.parseInt(in.nextLine());
        Pedido p = sistema.criarPedido(cid);
        while (true){
            System.out.println(p);
            System.out.println("a) Adicionar item  b) Confirmar pedido  c) Cancelar rascunho");
            String op = in.nextLine().trim().toLowerCase();
            if (op.equals("a")){
                listarItens();
                System.out.print("Código do item: ");
                int iid = Integer.parseInt(in.nextLine());
                System.out.print("Quantidade: ");
                int q = Integer.parseInt(in.nextLine());
                sistema.adicionarItemAoPedido(p, iid, q);
            } else if (op.equals("b")){
                sistema.confirmarPedido(p);
                System.out.println("Pedido confirmado! Número: " + p.getNumero());
                break;
            } else if (op.equals("c")){
                System.out.println("Rascunho descartado.");
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    private static void avancarStatus(){
        System.out.print("Número do pedido: ");
        int n = Integer.parseInt(in.nextLine());
        sistema.avancarStatus(n);
        System.out.println("Status avançado com sucesso.");
    }

    private static void listarPorStatus(){
        System.out.println("Status disponíveis:");
        for (var s : FluxoEntrega.values()) System.out.println(" - " + s);
        System.out.print("Informe o status: ");
        var st = FluxoEntrega.valueOf(in.nextLine().trim().toUpperCase().replace(' ', '_'));
        var lista = sistema.pedidosPorStatus(st);
        if (lista.isEmpty()) System.out.println("Nenhum pedido no status " + st);
        else lista.forEach(p -> System.out.println(p + "\n"));
    }

    private static void relatorioSimplificado(){
        var texto = sistema.relatorio(LocalDate.now(), Relatorios.TipoRelatorio.SIMPLIFICADO);
        System.out.println(texto);
    }
    private static void relatorioDetalhado(){
        var texto = sistema.relatorio(LocalDate.now(), Relatorios.TipoRelatorio.DETALHADO);
        System.out.println(texto);
    }
}
