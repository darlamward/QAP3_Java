
import com.keyin.client.DBFunctions;
import com.keyin.utils.*;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBFunctions database = new DBFunctions();
        Connection connection = database.DBConnect("QAP2_Java", "postgres", "cupcake");

        database.createBookTable(connection, "BooksList", "AuthorList");
        database.createAuthorTable(connection, "AuthorList");
        database.createPatronTable(connection, "PatronList");
        database.createBookCheckoutTable(connection, "BooksCheckoutList", "PatronList", "BookList");

        List<Books> listOfBooks = new ArrayList<>();
        List<Author> listOfAuthors = new ArrayList<>();
        List<Patron> listOfPatrons = new ArrayList<>();
        List<BooksCheckedOut> listOfBooksCheckedOut = new ArrayList<>();

        database.insertIntoAuthor(connection, "Sarah J.", "Maas", "", Date.valueOf("1986-03-05"));
        database.insertIntoAuthor(connection, "Brandon", "Sanderson", "", Date.valueOf("1975-12-19"));
        database.insertIntoAuthor(connection, "Richelle", "Mead", "", Date.valueOf("1976-11-12"));
        database.insertIntoAuthor(connection, "J.R.R.", "Tolkien", "John Ronald Reuel Tolkien CBE FRSL was an" +
                "English writer and philologist. He was the author of the high fantasy works The Hobbit and The Lord of the Rings." +
                "From 1925 to 1945, Tolkien was the Rawlinson and Bosworth Professor of Anglo-Saxon and a Fellow of Pembroke College," +
                "both at the University of Oxford.", Date.valueOf("1892-01-03"));

        database.insertIntoBook(connection, "A Court of Thorns and Roses", 1, "Bloomsbury", Date.valueOf("2015-05-05"), "1619634457");
        database.insertIntoBook(connection, "A Court of Mist and Fury", 1, "Bloomsbury", Date.valueOf("2016-04-22"), "1619634458");
        database.insertIntoBook(connection, "A Court of Wings and Ruin", 1, "Bloomsbury", Date.valueOf("2017-05-02"), "1619634459");
        database.insertIntoBook(connection, "The Way of Kings", 2, "Dragonsteel Entertainment", Date.valueOf("2010-08-31"), "1429992808");
        database.insertIntoBook(connection, "Vampire Academy", 3, "Penguin", Date.valueOf("2007-08-16"), "159514174X");
        database.insertIntoBook(connection, "Frostbite", 3, "Penguin", Date.valueOf("2008-04-10"), "1619634458");

        database.insertIntoPatron(connection, "John", "Willis", "john_willy@123.com",
                "13 Bisqe Ave.", "709-666-8787");
        database.insertIntoPatron(connection, "Phillip", "Bigglesworth", "phillip.biggle@gmail.com", "123 Main St.", "709-333-1234");
        database.insertIntoPatron(connection, "Betty", "Crocker", "BettyMakesCakes@gmail.com", "3 Box Cakeway", "333-222-8009");
        database.insertIntoBookCheckout(connection, 1,3,Date.valueOf("2023-04-04"), Date.valueOf("2023-05-04"), Date.valueOf("2023-05-04"));
        database.insertIntoBookCheckout(connection, 1,2,Date.valueOf("2023-01-01"), Date.valueOf("2023-02-01"), Date.valueOf("2023-01-23"));
        database.insertIntoBookCheckout(connection, 2,2,Date.valueOf("2023-01-01"), Date.valueOf("2023-02-01"), Date.valueOf("2023-01-23"));



        String GET_AUTHORLIST_QUERY = "SELECT * FROM authorlist";
        String GET_BOOKLIST_QUERY = "SELECT * FROM bookslist";
        String GET_PATRONLIST_QUERY = "SELECT * FROM patronlist";
        String GET_ALL_BOOKS_CHECKED_OUT_QUERY = "SELECT c.checkout_id, p.patron_id, b.book_id, c.checkout_date, c.due_date, c.return_date,"
                + "p.first_name, p.last_name " +
                "FROM bookscheckoutlist c " +
                "JOIN patronlist p ON c.patron_id = p.patron_id " +
                "JOIN bookslist b ON c.book_id = b.book_id " +
                "ORDER BY p.last_name";


        LibraryManagement library = new LibraryManagement();

        ResultSet AllAuthorsResult = database.queryDatabase(connection, GET_AUTHORLIST_QUERY);
        while (AllAuthorsResult.next()) {
            Author author = new Author(AllAuthorsResult.getInt("author_id"), AllAuthorsResult.getString("first_name"),
                    AllAuthorsResult.getString("last_name"), AllAuthorsResult.getDate("birth_date"));
            listOfAuthors.add(author);
        }

        ResultSet AllBooksResult = database.queryDatabase(connection, GET_BOOKLIST_QUERY);
        while (AllBooksResult.next()) {
            Books book = new Books(AllBooksResult.getInt("book_id"),
                    AllBooksResult.getString("title"), AllBooksResult.getInt("author_id"),
                    AllBooksResult.getString("publisher"), AllBooksResult.getDate("publication_date"),
                    AllBooksResult.getString("isbn")
            );
            listOfBooks.add(book);
        }

        ResultSet AllPatronsResult = database.queryDatabase(connection, GET_PATRONLIST_QUERY);
        while (AllPatronsResult.next()) {
            Patron patron = new Patron(AllPatronsResult.getInt("patron_id"), AllPatronsResult.getString("first_name"),
                    AllPatronsResult.getString("last_name"), AllPatronsResult.getString("email"),
                    AllPatronsResult.getString("address"), AllPatronsResult.getString("phone")
            );
            listOfPatrons.add(patron);
        }

        ResultSet resultSetForBooksCheckedOut = database.queryDatabase(connection,GET_ALL_BOOKS_CHECKED_OUT_QUERY);
        while (resultSetForBooksCheckedOut.next()){
            BooksCheckedOut bookCheckedOut = new BooksCheckedOut(resultSetForBooksCheckedOut.getInt("checkout_id"),
                    resultSetForBooksCheckedOut.getInt("patron_id"),resultSetForBooksCheckedOut.getInt("book_id"),
                    resultSetForBooksCheckedOut.getDate("checkout_date"), resultSetForBooksCheckedOut.getDate("due_date"),
                    resultSetForBooksCheckedOut.getDate("return_date")
            );
            listOfBooksCheckedOut.add(bookCheckedOut); }

        library.setBooks(listOfBooks);
        List<Books> resultsBook = library.getBooks();
        int bookCounter = 0;
        System.out.println("\n");
        System.out.println("           Book List           \n");
        for (Books item : resultsBook) {

            bookCounter+=1;
            System.out.println(bookCounter + ". " + item.getTitle()+ "\n");
        }

        library.setAuthors(listOfAuthors);
        List<Author> results = library.getAuthors();
        int AuthorCounter = 0;
        System.out.println("           Author List           \n");
        for (Author authors : results) {
            AuthorCounter+=1;
            System.out.println(AuthorCounter + ". " + authors.getFirst_name() + " " + authors.getLast_name() + "\n");

        }

        library.setPatrons(listOfPatrons);
        List<Patron> resultsPatron = library.getPatrons();
        int patronCounter=0;
        System.out.println("           Patron List           \n");
        for (Patron patronItem : resultsPatron) {
            patronCounter+=1;
            System.out.println(patronCounter + ". " + patronItem.getFirst_name() + " " + patronItem.getLast_name() + " " + patronItem.getEmail() + "\n");
        }

        library.setCheckedOutBooks(listOfBooksCheckedOut);
        List<BooksCheckedOut> resultsBooksCheckedOut = library.getCheckedOutBooks();
        int checkoutBookCtr = 0;
        System.out.println("           Checked Out Book List           \n");
        for (BooksCheckedOut bookItem : resultsBooksCheckedOut) {
            checkoutBookCtr += 1;
            System.out.println(checkoutBookCtr + ". " + bookItem.toString() + "\n");

        }

        System.out.println("Book Search by id: "+ library.getBooksById(4));
    }
}