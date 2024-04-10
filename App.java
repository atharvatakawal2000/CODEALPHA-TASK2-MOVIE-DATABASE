import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        Connection con = null;
        BufferedReader br = null;
        String URL = "jdbc:mysql://localhost:3306/movie";
        String USER = "root";
        String PWD = "Test1234";
        int choice = 0;
        try {
            con = getConnection(con, URL, USER, PWD);
            while (choice != 4) {
                System.out.println("Enter your choice");
                System.out.println("1: Display Movie Information\n2: Add Movie\n3:Search Movie\n4:Exit");
                br = new BufferedReader(new InputStreamReader(System.in));
                choice = Integer.parseInt(br.readLine());
                System.out.println(choice);
                List<Movie> movieInfoList;
                switch (choice) {
                    case 1:
                        movieInfoList = displayMovieInfo(con);
                        if (null != movieInfoList && movieInfoList.size() != 0) {
                            movieInfoList.forEach(p -> System.out.println(p));
                        }
                        break;
                    case 2:
                        addMovie(con);

                        break;
                    case 3:
                       searchInformation(con);
                        
                        break;
                    case 4:
                        System.out.println("Selected to exit....");
                        break;
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            br.close();
            con.close();
        }

    }

    private static void  searchInformation(Connection con) throws Exception {
        int searchNum = 0;
        List<Movie> mvList=new ArrayList<>();
        try {
            while (searchNum != 4) {
                System.out.println(
                        "Enter your choice: \n 1:Search by name\n2:Search by Genere\n3: Search by release year\n4:Exit");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                searchNum = Integer.parseInt(br.readLine());
                PreparedStatement pr;
                ResultSet rs;
                switch (searchNum) {
                    
                    case 1:
                    
                        System.out.println("Enter Movie Name:");
                        String name = br.readLine();
                        System.out.println(name);
                        String query = "Select * from Movie_information where movie_name=?";
                        pr = con.prepareStatement(query);
                        pr.setString(1, name);
                        rs = pr.executeQuery();
                        while (rs.next()) {
                            String movieName = rs.getString("movie_name");
                            String genere = rs.getString("movie_genre");
                            int year = Integer.parseInt(rs.getString("release_yr"));
                            Movie mv = new Movie(movieName, genere, year);
                            mvList.add(mv);
                        }
                        break;
                    case 2:
                        System.out.println("Enter Movie Genere:");
                        String gerenre = br.readLine();
                        String genereQuery = "Select * from Movie_information where movie_genre=?";
                        pr = con.prepareStatement(genereQuery);
                        pr.setString(1, gerenre);
                        rs = pr.executeQuery();
                        while (rs.next()) {
                            String movie_name = rs.getString("movie_name");
                            String genere = rs.getString("movie_genre");
                            int year = Integer.parseInt(rs.getString("release_yr"));
                            Movie mv = new Movie(movie_name, genere, year);
                            mvList.add(mv);
                        }
                        break;
                    case 3:
                        System.out.println("Enter Movie release year:");
                        int releaseYear = Integer.parseInt(br.readLine());
                        String releaseYrQuery = "Select * from Movie_information where release_yr=?";
                        pr = con.prepareStatement(releaseYrQuery);
                        pr.setInt(1, releaseYear);
                        rs = pr.executeQuery();
                        while (rs.next()) {
                            String movieName = rs.getString("movie_name");
                            String genere = rs.getString("movie_genre");
                            int year = Integer.parseInt(rs.getString("release_yr"));
                            Movie mv = new Movie(movieName, genere, year);
                            mvList.add(mv);
                        }
                        break;
                    case 4:
                        System.out.println("Selected to exit from search");
                        break;
                }
                if (null != mvList && mvList.size() != 0) {
                    mvList.forEach(p -> System.out.println(p));
                }
                mvList.clear();
            }
            
        } catch (Exception e) {
            throw e;
        }
  
    }

    private static void addMovie(Connection con) throws Exception {
        try {
            System.out.println("Enter movie name:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String name = br.readLine();
            System.out.println("Enter movie genere:");
            String genere = br.readLine();
            System.out.println("Enter movie release year:");
            String year = br.readLine();

            String query = "Insert into Movie_information(movie_name,movie_genre,release_yr) values(?,?,?)";
            PreparedStatement pr = con.prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, genere);
            pr.setString(3, year);
            pr.execute();

        } catch (Exception e) {
            throw e;
        }
    }

    private static List<Movie> displayMovieInfo(Connection con) throws Exception {
        List<Movie> movieInfoList = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from movie_information");
            while (rs.next()) {
                String name = rs.getString("movie_name");

                String genere = rs.getString("movie_genre");

                int release_yr = rs.getInt("release_yr");

                Movie m = new Movie(name, genere, release_yr);
                movieInfoList.add(m);
            }
            return movieInfoList;
        } catch (Exception e) {
            throw e;
        }
    }

    private static Connection getConnection(Connection con, String URL, String USER, String PWD) throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection sucessful");
            return con;
        } catch (Exception e) {
            throw e;
        }

    }

}