// Códigos corrigidos, com o main implementado e valores para conseguir testar as funções.
// Também foi implementada a função buscaID que já estava inclusa nas questões da prova 
// Dei as notas com base na pontuação descrita na prova

import java.util.Scanner;

class Conta {
    int id;
    String cliente;
    double saldo;
    double limite;
}

class Operacao {
    int id;
    int idConta;
    char tipo;
    double valor;
}

public class correcaoprova2 {

    public static final int TAM_CONTAS = 100;
    public static final int TAM_OP = 1000;

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Conta[] contas = new Conta[TAM_CONTAS];
        Operacao[] operacoes = new Operacao[TAM_OP];

        int tamConta = 0;
        int tamOp = 0;
        int opcao;

        do {
            System.out.println("\n===== SISTEMA BANCARIO =====");
            System.out.println("1 - Cadastrar conta");
            System.out.println("2 - Buscar cliente");
            System.out.println("3 - Cadastrar operacao");
            System.out.println("4 - Consultar extrato");
            System.out.println("5 - Listar contas ordenadas");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();

            switch (opcao) {

                case 1:
                    tamConta = cadastrarConta(contas, tamConta);
                    break;

                case 2:
                    if (tamConta == 0) {
                        System.out.println("Nenhuma conta cadastrada!");
                    } else {
                        insertionSortCliente(contas, tamConta);

                        sc.nextLine();
                        System.out.print("Digite o nome do cliente: ");
                        String nome = sc.nextLine();

                        int pos = buscaBinariaCliente(contas, tamConta, nome);

                        if (pos != -1) {
                            System.out.println("Cliente: " + contas[pos].cliente);
                            System.out.println("ID: " + contas[pos].id);
                        } else {
                            System.out.println("Cliente nao encontrado!");
                        }
                    }
                    break;

                case 3:
                    if (tamOp < TAM_OP) {
                        Operacao nova = new Operacao();

                        System.out.print("Digite o ID da operacao: ");
                        nova.id = sc.nextInt();

                        System.out.print("Digite o ID da conta: ");
                        nova.idConta = sc.nextInt();

                        if (buscaId(contas, tamConta, nova.idConta) == -1) {
                            System.out.println("Conta nao encontrada!");
                            break;
                        }

                        System.out.print("Digite o tipo (D, S ou T): ");
                        nova.tipo = sc.next().charAt(0);

                        System.out.print("Digite o valor: ");
                        double valor = sc.nextDouble();

                        if (nova.tipo == 'S') {
                            nova.valor = -valor;
                        } else {
                            nova.valor = valor;
                        }

                        operacoes[tamOp] = nova;
                        tamOp++;

                        System.out.println("Operacao cadastrada!");
                    } else {
                        System.out.println("Vetor de operacoes cheio!");
                    }
                    break;

                case 4:
                    extrato(contas, tamConta, operacoes, tamOp);
                    break;

                case 5:
                    insertionSortCliente(contas, tamConta);

                    for (int i = 0; i < tamConta; i++) {
                        System.out.println(contas[i].id + " - "
                                + contas[i].cliente);
                    }
                    break;

                case 0:
                    System.out.println("Encerrando...");
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }

        } while (opcao != 0);
    }

    public static int cadastrarConta(Conta[] v, int tam) {

        if (tam == TAM_CONTAS) {
            System.out.println("Vetor cheio!");
            return tam;
        }

        Conta nova = new Conta();

        System.out.print("Digite o ID: ");
        nova.id = sc.nextInt();

        if (buscaId(v, tam, nova.id) != -1) {
            System.out.println("ID ja cadastrado!");
            return tam;
        }

        sc.nextLine();

        System.out.print("Digite o nome do cliente: ");
        nova.cliente = sc.nextLine();

        System.out.print("Digite o saldo: ");
        nova.saldo = sc.nextDouble();

        if (nova.saldo < 0) {
            System.out.println("Saldo invalido!");
            return tam;
        }

        System.out.print("Digite o limite: ");
        nova.limite = sc.nextDouble();

        if (nova.limite < 0) {
            System.out.println("Limite invalido!");
            return tam;
        }

        v[tam] = nova;

        System.out.println("Conta cadastrada!");

        return tam + 1;
    }

    public static int buscaId(Conta[] v, int tam, int x) {

        for (int i = 0; i < tam; i++) {
            if (v[i].id == x) {
                return i;
            }
        }

        return -1;
    }

    public static int buscaBinariaCliente(Conta[] v, int tam, String x) {

        int inicio = 0;
        int fim = tam - 1;

        while (inicio <= fim) {

            int meio = (inicio + fim) / 2;

            int resultado = v[meio].cliente.compareToIgnoreCase(x);

            if (resultado == 0) {
                return meio;
            } else if (resultado < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return -1;
    }

    public static int insertionSortCliente(Conta[] v, int tam) {

        for (int i = 1; i < tam; i++) {

            Conta chave = v[i];
            int j = i - 1;

            while (j >= 0 &&
                   v[j].cliente.compareToIgnoreCase(chave.cliente) > 0) {

                v[j + 1] = v[j];
                j--;
            }

            v[j + 1] = chave;
        }

        return tam;
    }

    public static int filtraOperacoes(
            Operacao[] vOp,
            int tamOp,
            Operacao[] vOpFiltrado,
            int x) {

        int contador = 0;

        for (int i = 0; i < tamOp; i++) {

            if (vOp[i] != null && vOp[i].idConta == x) {
                vOpFiltrado[contador] = vOp[i];
                contador++;
            }
        }

        return contador;
    }

    public static void extrato(Conta[] vContas, int tamConta,
                               Operacao[] vOp, int tamOp) {

        System.out.print("Digite o ID da conta: ");
        int id = sc.nextInt();

        int pos = buscaId(vContas, tamConta, id);

        if (pos == -1) {
            System.out.println("Conta nao encontrada!");
            return;
        }

        Operacao[] vf = new Operacao[TAM_OP];

        int tam = filtraOperacoes(vOp, tamOp, vf, id);

        int inicio = tam - 5;

        if (inicio < 0) {
            inicio = 0;
        }

        double saldo = 0;

        for (int i = 0; i < inicio; i++) {
            saldo = saldo + vf[i].valor;
        }

        System.out.println("\nCONTA N." + vContas[pos].id);
        System.out.println("CLIENTE: " + vContas[pos].cliente);
        System.out.println("-----------------------------------------------");
        System.out.println("Saldo Anterior: " + formata(saldo));

        for (int i = inicio; i < tam; i++) {

            saldo = saldo + vf[i].valor;

            System.out.println("-----------------------------------------------");

            System.out.println("ID: " + vf[i].id
                    + " | Operacao: " + descricaoOperacao(vf[i].tipo)
                    + " | Valor: " + formata(vf[i].valor)
                    + " | Saldo: " + formata(saldo));
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Saldo Atual: " + formata(saldo));
    }

    public static String descricaoOperacao(char tipo) {

        if (tipo == 'D') {
            return "Deposito";
        } else if (tipo == 'S') {
            return "Saque";
        } else if (tipo == 'T') {
            return "Transferencia";
        }

        return "Desconhecida";
    }

    public static double valorAbsoluto(double v) {

        if (v < 0) {
            return -v;
        }

        return v;
    }

    public static String formata(double v) {

        char letra = 'C';

        if (v < 0) {
            letra = 'D';
        }

        return String.format("%.2f%c", valorAbsoluto(v), letra);
    }
}



