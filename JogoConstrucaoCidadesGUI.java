import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class JogoConstrucaoCidadesGUI extends JFrame {
    private JTextArea areaTexto;
    private JPanel painelOpcoes;
    private ArrayList<Edificio> edificios;
    private DefaultListModel<Edificio> listModel;
    private JList<Edificio> listaEdificios;
    private Cidade cidade;
    private JLabel lblOrcamento;
    private JLabel lblRecursos;
    private NumberFormat formatadorMoeda;

    // Enum para Níveis de Construção
    private enum Nivel {
        BAIXO(10000), MEDIO(20000), ALTO(50000);

        private final double custo;

        Nivel(double custo) {
            this.custo = custo;
        }

        public double getCusto() {
            return custo;
        }
    }

    // Classe interna para representar um Edifício
    private static class Edificio {
        private String nome;
        private String tipo;
        private Nivel nivel;

        public Edificio(String nome, String tipo, Nivel nivel) {
            this.nome = nome;
            this.tipo = tipo;
            this.nivel = nivel;
        }

        public String getNome() {
            return nome;
        }

        public String getTipo() {
            return tipo;
        }

        public Nivel getNivel() {
            return nivel;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public void setNivel(Nivel nivel) {
            this.nivel = nivel;
        }

        @Override
        public String toString() {
            return nome + " (" + tipo + ", Nível: " + nivel + ")";
        }
    }

    private static class Parque extends Edificio {
        private int areaVerde;
    
        public Parque(String nome, int areaVerde, Nivel nivel) {
            super(nome, "Parque", nivel);
            this.areaVerde = areaVerde;
        }
    
        @Override
        public String toString() {
            return super.toString() + " - Area verde: " + areaVerde + "m²";
        }
    }
    
    private static class Fabrica extends Edificio {
        private int capacidadeProducao;
    
        public Fabrica(String nome, int capacidadeProducao, Nivel nivel) {
            super(nome, "Fábrica", nivel);
            this.capacidadeProducao = capacidadeProducao;
        }
    
        @Override
        public String toString() {
            return super.toString() + " - Capacidade de Produção: " + capacidadeProducao + " unidades";
        }
    }
    

    // Classe para representar a cidade com orçamento e recursos
    private static class Cidade {
        private double orcamento;
        private int recursos;

        public Cidade(double orcamentoInicial, int recursosIniciais) {
            this.orcamento = orcamentoInicial;
            this.recursos = recursosIniciais;
        }

        public double getOrcamento() {
            return orcamento;
        }

        public int getRecursos() {
            return recursos;
        }

        public void adicionarOrcamento(double valor) {
            orcamento += valor;
        }

        public void gastarOrcamento(double valor) {
            if (orcamento >= valor) {
                orcamento -= valor;
            } else {
                JOptionPane.showMessageDialog(null, "Orçamento insuficiente!");
            }
        }

        public void adicionarRecursos(int quantidade) {
            recursos += quantidade;
        }

        public void consumirRecursos(int quantidade) {
            if (recursos >= quantidade) {
                recursos -= quantidade;
            } else {
                JOptionPane.showMessageDialog(null, "Recursos insuficientes!");
            }
        }
    }

    // Classe para representar Transportes
    private static class Transporte extends Edificio {
        private int capacidade;
        private String tipoTransporte;

        public Transporte(String nome, int capacidade, String tipoTransporte, Nivel nivel) {
            super(nome, "Transporte", nivel);
            this.capacidade = capacidade;
            this.tipoTransporte = tipoTransporte;
        }

        @Override
        public String toString() {
            return super.toString() + " - Tipo: " + tipoTransporte + " (Capacidade: " + capacidade + ")";
        }
    }

    // Classe para representar Serviços Públicos
    private static class ServicoPublico extends Edificio {
        private String tipoServico;
        private int capacidadeAtendimento;

        public ServicoPublico(String nome, String tipoServico, int capacidadeAtendimento, Nivel nivel) {
            super(nome, "Serviço Público", nivel);
            this.tipoServico = tipoServico;
            this.capacidadeAtendimento = capacidadeAtendimento;
        }

        @Override
        public String toString() {
            return super.toString() + " - Tipo: " + tipoServico + " (Capacidade: " + capacidadeAtendimento + ")";
        }
    }

    // Classe para representar eventos e crises
    private static class Evento {
        private String nome;
        private String descricao;
        private double impactoOrcamento;

        public Evento(String nome, String descricao, double impactoOrcamento) {
            this.nome = nome;
            this.descricao = descricao;
            this.impactoOrcamento = impactoOrcamento;
        }

        public void aplicarEvento(Cidade cidade) {
            cidade.gastarOrcamento(impactoOrcamento);
            JOptionPane.showMessageDialog(null, "Evento: " + nome + "\nDescrição: " + descricao + "\nImpacto no orçamento: -" + impactoOrcamento);
        }
    }

    public JogoConstrucaoCidadesGUI() {
        this.edificios = new ArrayList<>();
        this.listModel = new DefaultListModel<>();
        this.cidade = new Cidade(100000, 100); // Orçamento inicial: 100000, Recursos iniciais: 100
        

        // Formatação de valores monetários em Real (R$)
        formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        setTitle("Jogo de Construção de Cidades");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Cor de fundo
        getContentPane().setBackground(new Color(60, 63, 65));

        // Adicionar painel de status no topo
        JPanel painelStatus = new JPanel(new GridLayout(1, 2));
        lblOrcamento = new JLabel("Orçamento: " + formatadorMoeda.format(cidade.getOrcamento()));
        lblOrcamento.setForeground(Color.WHITE);
        lblRecursos = new JLabel("Recursos: " + cidade.getRecursos());
        lblRecursos.setForeground(Color.WHITE);
        painelStatus.add(lblOrcamento);
        painelStatus.add(lblRecursos);
        painelStatus.setBackground(new Color(43, 43, 43));
        painelStatus.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(painelStatus, BorderLayout.NORTH);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("SansSerif", Font.PLAIN, 16));
        areaTexto.setForeground(Color.WHITE);
        areaTexto.setBackground(new Color(43, 43, 43));
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new GridLayout(0, 1, 10, 10)); // Espaçamento entre botões
        painelOpcoes.setBackground(new Color(60, 63, 65));
        painelOpcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem interna
        add(painelOpcoes, BorderLayout.WEST);

        listaEdificios = new JList<>(listModel);
        listaEdificios.setFont(new Font("SansSerif", Font.PLAIN, 14));
        listaEdificios.setForeground(Color.WHITE);
        listaEdificios.setBackground(new Color(43, 43, 43));
        add(new JScrollPane(listaEdificios), BorderLayout.EAST);

        adicionarBotao("Adicionar Edifício", e -> adicionarEdificio());
        adicionarBotao("Adicionar Transporte", e -> adicionarTransporte());
        adicionarBotao("Adicionar Parque", e -> adicionarParque());
        adicionarBotao("Adicionar Fábrica", e -> adicionarFabrica());
        adicionarBotao("Adicionar Serviço Público", e -> adicionarServicoPublico());
        adicionarBotao("Desencadear Evento", e -> desencadearEventoAleatorio());
        adicionarBotao("Atualizar Edifício", e -> atualizarEdificio());
        adicionarBotao("Remover Edifício", e -> removerEdificio());

        atualizarLista();
    }

    private void adicionarBotao(String texto, ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(acao);
        botao.setBackground(new Color(100, 149, 237));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelOpcoes.add(botao);
    }

    private void adicionarEdificio() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Edifício:");
        String tipo = JOptionPane.showInputDialog(this, "Tipo do Edifício:");
        Nivel nivel = escolherNivel();
        if (nome != null && tipo != null && nivel != null) {
            double custo = nivel.getCusto();
            if (cidade.getOrcamento() >= custo) {
                cidade.gastarOrcamento(custo);
                Edificio edificio = new Edificio(nome, tipo, nivel);
                edificios.add(edificio);
                atualizarLista();
                atualizarStatus();
            } else {
                JOptionPane.showMessageDialog(this, "Orçamento insuficiente para construir esse edifício.");
            }
        }
    }

    private void adicionarParque() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Parque:");
        try {
            int areaVerde = Integer.parseInt(JOptionPane.showInputDialog(this, "Area Verde (em m²):"));
            Nivel nivel = escolherNivel();
            if (nome != null && nivel != null) {
                double custo = nivel.getCusto();
                if (cidade.getOrcamento() >= custo) {
                    cidade.gastarOrcamento(custo);
                    Parque parque = new Parque(nome, areaVerde, nivel);
                    edificios.add(parque);
                    atualizarLista();
                    atualizarStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Orçamento insuficiente para construir o parque.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para a área verde.");
        }
    }
    
    private void adicionarFabrica() {
        String nome = JOptionPane.showInputDialog(this, "Nome da Fábrica:");
        try {
            int capacidadeProducao = Integer.parseInt(JOptionPane.showInputDialog(this, "Capacidade de Produção:"));
            Nivel nivel = escolherNivel();
            if (nome != null && nivel != null) {
                double custo = nivel.getCusto();
                if (cidade.getOrcamento() >= custo) {
                    cidade.gastarOrcamento(custo);
                    Fabrica fabrica = new Fabrica(nome, capacidadeProducao, nivel);
                    edificios.add(fabrica);
                    atualizarLista();
                    atualizarStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Orçamento insuficiente para construir a fábrica.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para a capacidade de produção.");
        }
    }
    

    private void adicionarTransporte() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Transporte:");
        try {
            int capacidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Capacidade do Transporte:"));
            String tipoTransporte = JOptionPane.showInputDialog(this, "Tipo de Transporte:");
            Nivel nivel = escolherNivel();
            if (nome != null && tipoTransporte != null && nivel != null) {
                double custo = nivel.getCusto();
                if (cidade.getOrcamento() >= custo) {
                    cidade.gastarOrcamento(custo);
                    Transporte transporte = new Transporte(nome, capacidade, tipoTransporte, nivel);
                    edificios.add(transporte);
                    atualizarLista();
                    atualizarStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Orçamento insuficiente para construir esse transporte.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para a capacidade.");
        }
    }

    private void adicionarServicoPublico() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Serviço Público:");
        try {
            int capacidadeAtendimento = Integer.parseInt(JOptionPane.showInputDialog(this, "Capacidade de Atendimento:"));
            String tipoServico = JOptionPane.showInputDialog(this, "Tipo de Serviço Público:");
            Nivel nivel = escolherNivel();
            if (nome != null && tipoServico != null && nivel != null) {
                double custo = nivel.getCusto();
                if (cidade.getOrcamento() >= custo) {
                    cidade.gastarOrcamento(custo);
                    ServicoPublico servicoPublico = new ServicoPublico(nome, tipoServico, capacidadeAtendimento, nivel);
                    edificios.add(servicoPublico);
                    atualizarLista();
                    atualizarStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Orçamento insuficiente para construir esse serviço público.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para a capacidade de atendimento.");
        }
    }

    private void atualizarEdificio() {
        Edificio edificioSelecionado = listaEdificios.getSelectedValue();
        if (edificioSelecionado != null) {
            String novoNome = JOptionPane.showInputDialog(this, "Novo Nome:", edificioSelecionado.getNome());
            String novoTipo = JOptionPane.showInputDialog(this, "Novo Tipo:", edificioSelecionado.getTipo());
            Nivel novoNivel = escolherNivel();

            if (novoNome != null && novoTipo != null && novoNivel != null) {
                double diferencaCusto = novoNivel.getCusto() - edificioSelecionado.getNivel().getCusto();
                if (cidade.getOrcamento() >= diferencaCusto) {
                    cidade.gastarOrcamento(diferencaCusto);
                    edificioSelecionado.setNome(novoNome);
                    edificioSelecionado.setTipo(novoTipo);
                    edificioSelecionado.setNivel(novoNivel);
                    atualizarLista();
                    atualizarStatus();
                } else {
                    JOptionPane.showMessageDialog(this, "Orçamento insuficiente para atualizar o nível.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum edifício selecionado.");
        }
    }

    private void removerEdificio() {
        Edificio edificioSelecionado = listaEdificios.getSelectedValue();
        if (edificioSelecionado != null) {
            edificios.remove(edificioSelecionado);
            atualizarLista();
            atualizarStatus();
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum edifício selecionado.");
        }
    }

    private void desencadearEventoAleatorio() {
        Random rand = new Random();
    int eventoAleatorio = rand.nextInt(4); // Removi o evento 'YOU LOSE!!'
    Evento evento;

    switch (eventoAleatorio) {
        case 0:
            evento = new Evento("Crise Econômica", "Queda abrupta na economia!", 20000);
            break;
        case 1:
            evento = new Evento("Desastre Natural", "Tempestades severas atingem a cidade!", 15000);
            break;
        case 2:
            evento = new Evento("Greve Geral", "Funcionários paralisam atividades!", 10000);
            break;
        case 3:
        default:
            evento = new Evento("Investimento Externo", "Um grande investimento chegou à cidade!", -25000);
            break;
    }

    evento.aplicarEvento(cidade);
    atualizarStatus();
    
    verificarGameOver(); // Verifica se o orçamento zerou após o evento
}

// Método para verificar se o orçamento zerou
private void verificarGameOver() {
    if (cidade.getOrcamento() <= 0) {
        JOptionPane.showMessageDialog(this, "Você perdeu! O orçamento da cidade chegou a zero.");
        System.exit(0); // Encerra o jogo
    }
}

    private Nivel escolherNivel() {
        String[] opcoes = {"BAIXO", "MEDIO", "ALTO"};
        int escolha = JOptionPane.showOptionDialog(this, "Escolha o nível de construção:", "Escolher Nível",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);

        switch (escolha) {
            case 0:
                return Nivel.BAIXO;
            case 1:
                return Nivel.MEDIO;
            case 2:
                return Nivel.ALTO;
            default:
                return null;
        }
    }

    private void atualizarLista() {
        listModel.clear();
        for (Edificio edificio : edificios) {
            listModel.addElement(edificio);
        }
    }

    private void atualizarStatus() {
        lblOrcamento.setText("Orçamento: " + formatadorMoeda.format(cidade.getOrcamento()));
        lblRecursos.setText("Recursos: " + cidade.getRecursos());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JogoConstrucaoCidadesGUI gui = new JogoConstrucaoCidadesGUI();
            gui.setVisible(true);
        });
    }
}
