import java.io.FileNotFoundException;
import java.util.Scanner;

public class TesteCsv {

    private static Boolean continuar = true;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Exportar o arquivo csv para .db? Digite S para sim e N para não");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next().trim().toLowerCase();
        if(input.equals("s")) {
            ExportarDatabase exportar = new ExportarDatabase();
            exportar.lerCsv();
        }
        else {
            ConectarDatabase conectar = new ConectarDatabase();
            conectar.totalRegistros();
            while (continuar == true) {
                Boolean emitirRelatorio = true;
                System.out.println("Escolha as opções:");
                System.out.println("1 - para obter dados de Novas confirmações no Brasil");
                System.out.println("2 - para obter dados de Mortes confirmadas no Brasil");
                System.out.println("3 - para obter dados por Estado ou Cidade.");
                System.out.println("4 - para obter dados por Dia de um mês do ano de 2020.");
                System.out.println("5 - para obter dados por Mês do ano de 2020 no Brasil.");
                System.out.println("6 - para obter dados de Classificação de Estados" +
                        " com maiores novas confirmações");
                System.out.println("7 - para obter dados de Classificação de Estados" +
                        " com maiores novas mortes");
                System.out.println("8 - para obter dados de Classificação de Cidades" +
                        " com maiores novas confirmações");
                System.out.println("9 - para obter dados de Classificação de Cidades" +
                        " com maiores novas mortes");
                System.out.println("10 - para obter dados por Estado ou Cidade de um Mês de 2020.");
                System.out.println("11 - para obter dados por Estado ou Cidade de um Dia de 2020.");
                System.out.println("0 - Cancelar.");
                String escolha = scanner.next().trim();
                if(escolha.equals("1")) {
                    conectar.selecionarNovasConfirmacoes();
                }
                else if(escolha.equals("2")) {
                    conectar.selecionarNovasMortes();
                }
                else if(escolha.equals("3")) {
                    System.out.println("Digite a cidade ou estado: ");
                    scanner.nextLine();
                    String escolhaSecundaria = scanner.nextLine().trim();
                    conectar.selecionarCidadeEstado(escolhaSecundaria);
                }
                else if(escolha.equals("4")) {
                    System.out.println("Digite o número do dia: ");
                    String escolhaDia = scanner.next().trim();
                    System.out.println("Digite o número do mês: ");
                    String escolhaMes = scanner.next().trim();
                    conectar.selecionarDiaMes(escolhaMes,escolhaDia);
                }
                else if(escolha.equals("5")) {
                    System.out.println("Digite o número do mês: ");
                    String escolhaMes = scanner.next().trim();
                    conectar.selecionarMes(escolhaMes);
                }
                else if(escolha.equals("6")) {
                    conectar.estadoClassificacaoConfirmados();
                }
                else if(escolha.equals("7")) {
                    conectar.estadoClassificacaoMortes();
                }
                else if(escolha.equals("8")) {
                    conectar.cidadeClassificacaoConfirmados();
                }
                else if(escolha.equals("9")) {
                    conectar.cidadeClassificacaoMortes();
                }
                else if(escolha.equals("10")) {
                    System.out.println("Digite a cidade ou estado: ");
                    scanner.nextLine();
                    String escolhaCidadeEstado = scanner.nextLine().trim();
                    System.out.println("Digite o número do mês: ");
                    String escolhaMes = scanner.next().trim();
                    conectar.selecionarCidadeEstadoMes(escolhaCidadeEstado,escolhaMes);
                }
                else if(escolha.equals("11")) {
                    System.out.println("Digite a cidade ou estado: ");
                    scanner.nextLine();
                    String escolhaCidadeEstado = scanner.nextLine().trim();
                    System.out.println("Digite o número do dia: ");
                    String escolhaDia = scanner.next().trim();
                    System.out.println("Digite o número do mês: ");
                    String escolhaMes = scanner.next().trim();
                    conectar.selecionarCidadeEstadoDiaMes(escolhaCidadeEstado,escolhaMes,escolhaDia);
                }
                else if(escolha.equals("0")) {
                    continuar = false;
                    emitirRelatorio = false;
                }
                else {
                    emitirRelatorio = false;
                }
                if(emitirRelatorio) {
                    System.out.println("Emitir relatório dos dados atuais pesquisados? " +
                            "Digite S para sim e N para não");
                    String escolhaSecundaria = scanner.next().trim().toLowerCase();
                    if(escolhaSecundaria.equals("s")) {
                        System.out.println("Digite o nome para o arquivo do relatório: ");
                        scanner.nextLine();
                        String escolhaNome = scanner.nextLine().trim();
                        continuar = false;
                        new ExportarRelatorio(conectar.lista, escolhaNome);;
                    }
                    else {
                        conectar.lista.clear();
                    }
                }
            }
        }
    }
}
