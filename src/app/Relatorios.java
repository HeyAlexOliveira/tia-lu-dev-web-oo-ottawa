
package app;

import java.time.LocalDate;
import java.util.Collection;

public final class Relatorios {
    public enum TipoRelatorio { SIMPLIFICADO, DETALHADO }

    public static String gerar(TipoRelatorio tipo, LocalDate dia, Collection<Pedido> pedidos){
        switch(tipo){
            case SIMPLIFICADO: return gerarSimplificado(dia, pedidos);
            case DETALHADO: return gerarDetalhado(dia, pedidos);
            default: throw new IllegalArgumentException("Tipo inválido");
        }
    }

    private static String gerarSimplificado(LocalDate dia, Collection<Pedido> pedidos){
        long qtd = pedidos.size();
        double total = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
        return String.format("Relatório Simplificado (%s)\nPedidos: %d\nTotal: R$ %.2f", dia, qtd, total);
    }

    private static String gerarDetalhado(LocalDate dia, Collection<Pedido> pedidos){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Relatório Detalhado (%s)\n", dia));
        if (pedidos.isEmpty()) { sb.append("Nenhum pedido\n"); return sb.toString(); }
        for (Pedido p : pedidos){
            sb.append(p).append("\n\n");
        }
        return sb.toString();
    }
}
