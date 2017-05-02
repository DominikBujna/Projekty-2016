import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;


public class Praktikum_test {
    public static void main( String args[] )
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/bujna8",
                            "bujna8", "12345");


            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String username = in.readLine();
            stmt = c.createStatement();
            ResultSet rs;
            boolean notFound = true;
            //kym som nenasiel existujuceho pouzivatela
            while(true) {
                //vypytam si prihlasovacie meno
                System.out.println("Login:");
                username = in.readLine();
                //prehladam databazu
                rs = stmt.executeQuery( "SELECT prihlasovacie_meno FROM student WHERE UPPER(prihlasovacie_meno) = "
                        + username.toUpperCase() + " ;" );
                //ak sa tam nachadzalo, mozem odist z cyklu
                if(rs.next()){
                    break;
                }
            }
            //vyhladam si testy napisane studentom
            String sql =
                  "SELECT u.meno, u.priezvisko, t.nazov, p.cas_pridelenia, v.cas_vypracovania, v.skore " +
                          "FROM pridelenie p JOIN student s ON s.id = p.studentid " +
                          "JOIN test t ON t.id = p.testid left " +
                          "JOIN vysledok v ON p.id = v.pridelenieid " +
                          "JOIN ucitel u ON u.ucitelid = t.autor_ucitelid " +
                          "WHERE s.prihlasovacie_meno = "+ username +" AND NOT EXISTS (" +
                          "SELECT *" +
                          "FROM (SELECT p1.id, v1.skore " +
                          "FROM pridelenie p1 JOIN vysledok v1 ON p1.id = v1.id_pridelenia, " +
                          "PRIDELENIE p2 JOIN vysledok v2 ON p2.id = v2.id_pridelenia " +
                          "WHERE p1.id = p2.id AND v1.skore < v2.skore) " +
                          "    WHERE p1.id = p.id " +
                          ");";

            rs = stmt.executeQuery(sql);
            //zapamatam si id testov, aby som ich vedel nasledne vybrat
            ArrayList<Integer> testIDs = new ArrayList<Integer>();
            ArrayList<Integer> pridelenieID = new ArrayList<Integer>();
            int poradie = 1;
            //vypisem testy
            while(rs.next()){
                testIDs.add(rs.getInt("id"));
                pridelenieID.add(rs.getInt("pridelenieid"));
                System.out.println(poradie + " " + rs.getString("t.nazov") + " " + rs.getString("u.meno") +
                " " + rs.getString("u.priezvisko") + " " + rs.getString("p.cas_pridelenia") + " " + rs.getString("p.skore"));
                poradie++;
            }
            //precitam vyber uzivatela
            int chosenTest = Integer.parseInt(in.readLine());
            //zistim si najvyssie ID, vysledok bude pouzivat dalsie
            sql = "SELECT max(vysledok_id) FROM vysledok";
            rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("vysledokid") + 1;
            //percenta vypocitam ako podiel premennych,rychlejsie ako pocitat to spatne
            int spravne = 0;
            int zodpovedane = 0;
            //zoberiem otazky testu
            sql = "SELECT * FROM otazka WHERE testid = " + testIDs.get(chosenTest) + ";";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                String otazka = rs.getString("text");
                //odpovede ulozim a premiesam, aby nebola spravna vzdy na rovnakom mieste
                ArrayList<String> odpovede = new ArrayList<>();
                odpovede.add(rs.getString("spravne"));
                odpovede.add(rs.getString("nespravne1"));
                odpovede.add(rs.getString("nespravne2"));
                odpovede.add(rs.getString("nespravne3"));
                Collections.shuffle(odpovede);
                System.out.println(otazka);
                //vypisem odpovede
                for(int i = 0; i < 4; i++){
                    System.out.println((char)(65+i) + ": "+ odpovede.get(i));
                }
                String answer = in.readLine();
                String odpoved = odpovede.get((int)answer.toUpperCase().charAt(0));
                if(odpoved.equals(rs.getString("spravne"))) spravne++;
                //zapisem odpoved do databazy
                sql = "INSERT INTO odpoved (vysledokid, otazkaid, text_odpovede) " +
                        "VALUES (" + id + ", " + rs.getInt("otazkaid") + ", " + odpoved + ");";
                rs = stmt.executeQuery(sql);
            }
            int uspesnost = (zodpovedane*100)/spravne;
            System.out.println("Uspesnost: " + uspesnost);
            sql = "INSERT INTO vysledok (vysledokid, pridelenieid, skore, cas_vypracovania) " +
                    "VALUES (" + id + ", " + pridelenieID.get(chosenTest) + ", " + uspesnost + ");";
            rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }
}