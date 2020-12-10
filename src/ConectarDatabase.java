import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConectarDatabase {

    private final String database = "database";
    DecimalFormat df = new DecimalFormat("###,###.###");
    public ArrayList<String> lista = new ArrayList();

    public boolean criarDatabase() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("" +
                        "CREATE TABLE IF NOT EXISTS " + database + " (" +
                        "city VARCHAR (30), city_ibge_code VARCHAR (30)," +
                        "date VARCHAR (30), epidemiological_week INT," +
                        "estimated_population VARCHAR (30), estimated_population_2019 VARCHAR (30)," +
                        "is_last VARCHAR (30), is_repeated VARCHAR (30)," +
                        "last_available_confirmed INT," +
                        "last_available_confirmed_per_100k_inhabitants VARCHAR (30)," +
                        "last_available_date VARCHAR (30), last_available_death_rate DOUBLE," +
                        "last_available_deaths INT, order_for_place INT," +
                        "place_type VARCHAR (30), state CHAR," +
                        "new_confirmed INT, new_deaths INT)")) {
                    stmt.execute();
                    stmt.close();
                    return true;
                }
            }
            else {
                return false;
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void inserirDados(String[] lines) {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("" +
                        "INSERT INTO " + database + " (city, city_ibge_code, date," +
                        "epidemiological_week, estimated_population," +
                        "estimated_population_2019, is_last, is_repeated, last_available_confirmed," +
                        "last_available_confirmed_per_100k_inhabitants, last_available_date," +
                        "last_available_death_rate, last_available_deaths, order_for_place," +
                        "place_type, state, new_confirmed, new_deaths)" +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                    stmt.setString(1, lines[0]);
                    stmt.setString(2, lines[1]);
                    stmt.setString(3, lines[2]);
                    stmt.setInt(4, Integer.parseInt(lines[3]));
                    stmt.setString(5, lines[4]);
                    stmt.setString(6, lines[5]);
                    stmt.setString(7, lines[6]);
                    stmt.setString(8, lines[7]);
                    stmt.setInt(9, Integer.parseInt(lines[8]));
                    stmt.setString(10, lines[9]);
                    stmt.setString(11, lines[10]);
                    stmt.setDouble(12, Double.parseDouble(lines[11]));
                    stmt.setInt(13, Integer.parseInt(lines[12]));
                    stmt.setInt(14, Integer.parseInt(lines[13]));
                    stmt.setString(15, lines[14]);
                    stmt.setString(16, lines[15]);
                    stmt.setInt(17, Integer.parseInt(lines[16]));
                    stmt.setInt(18, Integer.parseInt(lines[17]));
                    stmt.execute();
                    stmt.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void totalRegistros() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*)" +
                        "As total FROM " + database + "")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.println("Total de registros no banco de dados: "
                            + df.format(rs.getInt("total")) + "\n");
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarNovasConfirmacoes() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As total FROM " + database + "")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-20s\n", "Total Casos confirmados");
                    System.out.printf("%-20s\n", df.format(rs.getInt("total")));
                    lista.add("Total Casos confirmados");
                    lista.add(rs.getString("total"));
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarNovasMortes() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As total FROM " + database + "")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-20s\n", "Total Mortes confirmadas");
                    System.out.printf("%-20s\n", df.format(rs.getInt("total")));
                    lista.add("Total Mortes confirmadas");
                    lista.add(rs.getString("total"));
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarCidadeEstado(String string) {
        String tipo;
        String clausula;
        if(string.length() <= 2) {
            tipo = "state";
            string = string.toUpperCase();
            clausula = " = '" + string + "'";
        }
        else {
            tipo = "city";
            clausula = "LIKE '%" + string + "%'";
        }
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                String casos;
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As total FROM " + database + " WHERE " + tipo + " " + clausula + "")) {
                    ResultSet rs = stmt.executeQuery();
                    casos = df.format(rs.getInt("total"));
                    rs.close();
                }
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As total FROM " + database + " WHERE " + tipo + " " + clausula + "")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-30s%-20s%-20s\n", "Estado/Cidade", "Casos confirmados",
                            "Mortes confirmadas");
                    System.out.printf("%-30s%-20s%-20s\n", string, casos,
                            df.format(rs.getInt("total")));

                    lista.add("Estado/Cidade,Total Casos confirmados,Mortes confirmadas");
                    lista.add("" + string + "," + rs.getInt("total") + "," +
                            "" + casos + "");
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarDiaMes(String mes, String dia) {
        if(mes.length() <= 1) {
            mes = "0" + mes + "";
        }
        if(dia.length() <= 1) {
            dia = "0" + dia + "";
        }
        String data = "2020-" + mes + "-" + dia + "";
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                String casos;
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As total FROM " + database + " WHERE date = '" + data + "' ")) {
                    ResultSet rs = stmt.executeQuery();
                    casos = df.format(rs.getInt("total"));
                    rs.close();
                }
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As total FROM " + database + " WHERE date = '" + data + "' ")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-20s%-20s%-20s\n", "Data", "Casos confirmados",
                            "Mortes confirmadas");
                    System.out.printf("%-20s%-20s%-20s\n", "" + dia + "-" + mes + "-2020", casos,
                            df.format(rs.getInt("total")));

                    lista.add("Data,Casos confirmados,Mortes confirmadas");
                    lista.add("" + dia + "-" + mes + "-2020," + casos + "," +
                            "" + rs.getInt("total") + "");
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarMes(String mes) {
        if(mes.length() <= 1) {
            mes = "0" + mes + "";
        }
        String inicio = "2020-" + mes + "-01";
        String fim = "2020-" + mes + "-31";
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                String casos;
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As total FROM " + database + " WHERE date >= '" + inicio + "'" +
                        "AND date <= '" + fim + "'")) {
                    ResultSet rs = stmt.executeQuery();
                    casos = df.format(rs.getInt("total"));
                    rs.close();
                }
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As total FROM " + database + " WHERE date >= '" + inicio + "'" +
                        "AND date <= '" + fim + "'")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-20s%-20s%-20s%-20s\n", "Data Inicial", "Data Final",
                            "Casos confirmados",
                            "Mortes confirmadas");
                    System.out.printf("%-20s%-20s%-20s%-20s\n", "01-" + mes + "-2020",
                            "31-" + mes + "-2020", casos,
                            df.format(rs.getInt("total")));

                    lista.add("Data Inicial,Data Final,Casos confirmados,Mortes confirmadas");
                    lista.add("01-" + mes + "-2020,31-" + mes + "-2020," + casos + "," +
                            "" + rs.getInt("total") + "");
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void estadoClassificacaoConfirmados() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As count, state FROM " + database + "  WHERE new_confirmed > 0" +
                        " GROUP BY state ORDER BY count DESC")) {
                    ResultSet rs = stmt.executeQuery();
                    Integer i = 1;
                    System.out.printf("%-10s%-10s%-20s\n", "Posição", "Estado", "Casos confirmados");
                    lista.add("Posição,Estado,Casos confirmados");
                    while(rs.next()) {
                        System.out.printf("%-10s%-10s%-20s\n", i, rs.getString("state"),
                                df.format(rs.getInt("count")));
                        lista.add("" + i + "," + rs.getString("state") + "," +
                                "" + rs.getString("count") + "");
                        i++;
                    }
                    rs.close();
                    System.out.println("");
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void estadoClassificacaoMortes() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As count, state FROM " + database + "  WHERE new_deaths > 0" +
                        " GROUP BY state ORDER BY count DESC")) {
                    ResultSet rs = stmt.executeQuery();
                    Integer i = 1;
                    System.out.printf("%-10s%-10s%-20s\n", "Posição", "Estado", "Mortes confirmadas");
                    lista.add("Posição,Estado,Mortes confirmadas");
                    while(rs.next()) {
                        System.out.printf("%-10s%-10s%-20s\n", i, rs.getString("state"),
                                df.format(rs.getInt("count")));
                        lista.add("" + i + "," + rs.getString("state") + "," +
                                "" + rs.getString("count") + "");
                        i++;
                    }
                    rs.close();
                    System.out.println("");
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cidadeClassificacaoConfirmados() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As count, city FROM " + database + "  WHERE new_confirmed > 0 " +
                        "AND city != ''" +
                        " GROUP BY city ORDER BY count DESC")) {
                    ResultSet rs = stmt.executeQuery();
                    Integer i = 1;
                    System.out.printf("%-10s%-30s%-20s\n", "Posição", "Cidade", "Casos confirmados");
                    lista.add("Posição,Cidade,Casos confirmados");
                    while(rs.next()) {
                        System.out.printf("%-10s%-30s%-20s\n", i, rs.getString("city"),
                                df.format(rs.getInt("count")));
                        lista.add("" + i + "," + rs.getString("city") + "," +
                                "" + rs.getString("count") + "");
                        i++;
                    }
                    rs.close();
                    System.out.println("");
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cidadeClassificacaoMortes() {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As count, city FROM " + database + "  WHERE new_deaths > 0 " +
                        "AND city != ''" +
                        " GROUP BY city ORDER BY count DESC")) {
                    ResultSet rs = stmt.executeQuery();
                    Integer i = 1;
                    System.out.printf("%-10s%-30s%-20s\n", "Posição", "Cidade", "Mortes confirmadas");
                    lista.add("Posição,Cidade,Mortes confirmadas");
                    while(rs.next()) {
                        System.out.printf("%-10s%-30s%-20s\n", i, rs.getString("city"),
                                df.format(rs.getInt("count")));
                        lista.add("" + i + "," + rs.getString("city") + "," +
                                "" + rs.getString("count") + "");
                        i++;
                    }
                    rs.close();
                    System.out.println("");
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarCidadeEstadoMes(String string, String mes) {
        String tipo;
        String clausula;
        if(string.length() <= 2) {
            tipo = "state";
            string = string.toUpperCase();
            clausula = " = '" + string + "'";
        }
        else {
            tipo = "city";
            clausula = "LIKE '%" + string + "%'";
        }
        if(mes.length() <= 1) {
            mes = "0" + mes + "";
        }
        String inicio = "2020-" + mes + "-01";
        String fim = "2020-" + mes + "-31";
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                String casos;
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As total FROM " + database + " WHERE " + tipo + " " + clausula + " " +
                        "AND date >= '" + inicio + "' AND date <= '" + fim + "'")) {
                    ResultSet rs = stmt.executeQuery();
                    casos = df.format(rs.getInt("total"));
                    rs.close();
                }
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As total FROM " + database + " WHERE " + tipo + " " + clausula + " " +
                        "AND date >= '" + inicio + "' AND date <= '" + fim + "'")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-20s%-20s%-30s%-20s%-20s\n", "Data Inicial", "Data Final",
                            "Cidade/Estado", "Casos confirmados",
                            "Mortes confirmadas");
                    System.out.printf("%-20s%-20s%-30s%-20s%-20s\n", "01-" + mes + "-2020",
                            "31-" + mes + "-2020", string, casos,
                            df.format(rs.getInt("total")));

                    lista.add("Data Inicial,Data Final,Cidade/Estado,Casos confirmados," +
                            "Mortes confirmadas");
                    lista.add("01-" + mes + "-2020,31-" + mes + "-2020," + string + "," +
                            "" + casos + "," + rs.getInt("total") + "");
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selecionarCidadeEstadoDiaMes(String string, String mes, String dia) {
        String tipo;
        String clausula;
        if(string.length() <= 2) {
            tipo = "state";
            string = string.toUpperCase();
            clausula = " = '" + string + "'";
        }
        else {
            tipo = "city";
            clausula = "LIKE '%" + string + "%'";
        }
        if(mes.length() <= 1) {
            mes = "0" + mes + "";
        }
        if(dia.length() <= 1) {
            dia = "0" + dia + "";
        }
        String data = "2020-" + mes + "-" + dia + "";
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
            if(conn != null) {
                String casos;
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_confirmed)" +
                        "As total FROM " + database + " WHERE " + tipo + " " + clausula + " " +
                        "AND date = '" + data + "'")) {
                    ResultSet rs = stmt.executeQuery();
                    casos = df.format(rs.getInt("total"));
                    rs.close();
                }
                try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(new_deaths)" +
                        "As total FROM " + database + " WHERE " + tipo + " " + clausula + " " +
                        "AND date = '" + data + "'")) {
                    ResultSet rs = stmt.executeQuery();
                    System.out.printf("%-20s%-30s%-20s%-20s\n", "Data",
                            "Cidade/Estado", "Casos confirmados",
                            "Mortes confirmadas");
                    System.out.printf("%-20s%-30s%-20s%-20s\n",
                            "" + dia + "-" + mes + "-2020", string, casos,
                            df.format(rs.getInt("total")));

                    lista.add("Data,Cidade/Estado,Casos confirmados," +
                            "Mortes confirmadas");
                    lista.add("" + dia + "-" + mes + "-2020," + string + "," +
                            "" + casos + "," + rs.getInt("total") + "");
                    rs.close();
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
