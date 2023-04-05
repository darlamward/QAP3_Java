// Library Management System connected to SQL database.
// Completed by Darla Ward.
// Completed on April 5, 2023.

package com.keyin.client;
import java.sql.*;

public class DBFunctions {
    public Connection DBConnect(String database, String username, String password){

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, username, password);
            if (connection != null){
                System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
                //connection.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error in Connecting to Server");
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return connection;
    }

    public void createBookTable(Connection connection, String tableName, String refAuthor) {
        Statement statement;
        try {
            String query = "CREATE TABLE " + tableName + "(book_id SERIAL NOT NULL PRIMARY KEY,title VARCHAR(200) NOT NULL," +
                    "author_id INT NOT NULL,publisher VARCHAR(100),publication_date DATE,isbn VARCHAR(20)," +
                    "CONSTRAINT aauthor_bauthor FOREIGN KEY (author_id) REFERENCES public." + refAuthor +
                    "(author_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID)";


            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Book Table Created Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAuthorTable(Connection connection, String table_name) {
        Statement statement;
        try {
            String query = "CREATE TABLE " + table_name + "(author_id SERIAL NOT NULL PRIMARY KEY, first_name VARCHAR(50) NOT NULL," +
                    "last_name VARCHAR(50) NOT NULL, bio VARCHAR(500), birth_date DATE NOT NULL)";

            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Author Table Created Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPatronTable(Connection connection, String table_name) {
        Statement statement;
        try {
            String query = "CREATE TABLE " + table_name + "(patron_id SERIAL NOT NULL, first_name VARCHAR(50) NOT NULL," +
                    "last_name VARCHAR(50) NOT NULL, email VARCHAR(100) NOT NULL, phone VARCHAR(20), address VARCHAR(200)," +
                    "PRIMARY KEY (patron_id))";

            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Patron Table Created Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
  }
    public void createBookCheckoutTable(Connection connection, String tableName, String refPatron, String refBook) {
        Statement statement;
        try {
            String query = "CREATE TABLE " + tableName + "(checkout_id serial NOT NULL, patron_id integer NOT NULL," +
                    "book_id integer NOT NULL, checkout_date date NOT NULL, due_date date, return_date date, PRIMARY KEY (checkout_id))";

            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println(" Book Checkout Table Created Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
 }

    public ResultSet queryDatabase(Connection connection,String query) {
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void insertIntoAuthor(Connection conn, String first_name, String last_name, String bio, Date birth_date) {
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO authorlist (first_name, last_name, bio, birth_date) VALUES (?, ?, ?, ?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, bio);
            statement.setDate(4, birth_date);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Author added successfully.");
            } else {
                System.out.println("ERROR: Author could not be added!");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void insertIntoBook(Connection connection, String title, int author_id, String publisher, Date publication_date, String isbn) {
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO bookslist (title, author_id, publisher, publication_date, isbn) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, title);
            statement.setInt(2, author_id);
            statement.setString(3, publisher);
            statement.setDate(4, publication_date);
            statement.setString(5, isbn);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book added successfully.");
            } else {
                System.out.println("ERROR: Book could not be added!");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void insertIntoPatron(Connection connection, String first_name, String last_name, String email,
                                 String address, String phone) {
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO patronlist ( first_name, last_name, email, address, phone) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setString(4, address);
            statement.setString(5, phone);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patron added successfully.");
            } else {
                System.out.println("ERROR: Patron could not be added!");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public void insertIntoBookCheckout(Connection connection, int book_id, int patron_id, Date checkout_date, Date due_date, Date return_date) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String query = "INSERT INTO bookscheckoutlist (book_id, patron_id, checkout_date, " +
                    "due_date, return_date) " +
                    "SELECT b.book_id, p.patron_id, ?, ?, ? " +
                    "FROM bookslist b " +
                    "JOIN patronlist p ON p.patron_id = ? " +
                    "WHERE b.book_id = ?";

            statement = connection.prepareStatement(query);
            statement.setDate(1, checkout_date);
            statement.setDate(2, due_date);
            statement.setDate(3, return_date);
            statement.setInt(4, patron_id);
            statement.setInt(5, book_id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book checkout has been added");
            } else {
                System.out.println("ERROR: Book checkout could not be added");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
