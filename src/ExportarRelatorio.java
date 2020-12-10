import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExportarRelatorio {

    public ExportarRelatorio(ArrayList<String> lista, String nome) {
        try {
            FileWriter csvWriter = new FileWriter("" + nome + ".csv");

            for (String rowData : lista) {
                csvWriter.append(String.join(",", rowData));
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println("Dados foram exportados com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
