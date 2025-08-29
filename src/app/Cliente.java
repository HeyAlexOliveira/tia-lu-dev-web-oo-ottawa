
package app;

public class Cliente {
    private final int codigo;
    private String nome;
    private String telefone;

    public Cliente(int codigo, String nome, String telefone) {
        this.codigo = codigo;
        this.nome = nome;
        this.telefone = telefone;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }

    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return String.format("#%d - %s (%s)", codigo, nome, telefone);
    }
}
