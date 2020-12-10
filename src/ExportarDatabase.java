import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ExportarDatabase {

    private Boolean res = false;
    private Integer limite = 1000;

    public void lerCsv() throws FileNotFoundException {
        BufferedReader br = null;
        String line;
        Integer contar = 0;
        ConectarDatabase conectar = new ConectarDatabase();
        try {
            br = new BufferedReader(new FileReader("caso_full.csv"));
            while ((line = br.readLine().replace("'", "")) != null && contar <= limite) {
                String[] lines = line.split(",");
                System.out.printf("%-30s%-20s%-20s%-30s%-30s%-30s%-20s%-20s%-30s%-50s%-20s%-30s" +
                        "%-30s%-20s%-20s%-20s%-20s%-20s\n", lines[0], lines[1], lines[2], lines[3],
                        lines[4], lines[5], lines[6], lines[7], lines[8], lines[9],
                        lines[10], lines[11], lines[12], lines[13], lines[14], lines[15],
                        lines[16], lines[17]);
                if(contar == 0) {
                    res = conectar.criarDatabase();
                }
                else {
                    if (res) {
                        conectar.inserirDados(lines);
                    }
                }
                contar++;
            }
            System.out.println("\nDados foram registrados com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
