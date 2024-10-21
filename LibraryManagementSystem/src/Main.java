import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// JDBC connection class
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/librarymanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "0000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Abstract class for people in the library system
abstract class Person {
    private String name;
    private int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    // Abstract method to display person details
    public abstract void displayDetails();
}

// Librarian class that extends Person
class Librarian extends Person {
    public Librarian(String name, int id) {
        super(name, id);
    }

    @Override
    public void displayDetails() {
        System.out.println("Librarian Name: " + getName());
        System.out.println("Librarian ID: " + getId());
    }

    // Add book to database
    public void addBook(Book book) {
        String query = "INSERT INTO Books (id, title, author) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.executeUpdate();
            System.out.println("Book added to database: " + book.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBook(int bookId) {
        String query = "DELETE FROM Books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            System.out.println("Book removed from database with ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get book details from database by book ID
    public Book getBookById(int bookId) {
        String query = "SELECT * FROM Books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                return new Book(title, author, bookId);
            } else {
                System.out.println("No book found with ID: " + bookId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get member details from database by member ID
    public Member getMemberById(int memberId) {
        String query = "SELECT * FROM Members WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                return new Member(name, memberId);
            } else {
                System.out.println("No member found with ID: " + memberId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all books borrowed by a specific member using their member ID
    public List<Book> getBorrowedBooksByMemberId(int memberId) {
        String query = "SELECT b.id, b.title, b.author FROM BorrowedBooks bb JOIN Books b ON bb.book_id = b.id WHERE bb.member_id = ?";
        List<Book> borrowedBooks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int bookId = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                borrowedBooks.add(new Book(title, author, bookId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }
}

// Member class that extends Person
class Member extends Person {
    private List<Book> borrowedBooks;

    public Member(String name, int id) {
        super(name, id);
        borrowedBooks = new ArrayList<>();
    }

    @Override
    public void displayDetails() {
        System.out.println("Member Name: " + getName());
        System.out.println("Member ID: " + getId());
        System.out.println("Borrowed Books: ");
        for (Book book : borrowedBooks) {
            System.out.println(" - " + book.getTitle());
        }
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
        borrowBookFromDatabase(getId(), book.getId());
        System.out.println(getName() + " borrowed " + book.getTitle());
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
        returnBookToDatabase(getId(), book.getId());
        System.out.println(getName() + " returned " + book.getTitle());
    }

    private void borrowBookFromDatabase(int memberId, int bookId) {
        String query = "INSERT INTO BorrowedBooks (member_id, book_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
            System.out.println("Book with ID " + bookId + " borrowed by member with ID " + memberId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBookToDatabase(int memberId, int bookId) {
        String query = "DELETE FROM BorrowedBooks WHERE member_id = ? AND book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
            System.out.println("Book with ID " + bookId + " returned by member with ID " + memberId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Book class demonstrating Encapsulation
class Book {
    private String title;
    private String author;
    private int id;

    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public void displayDetails() {
        System.out.println("Book Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Book ID: " + id);
    }
}

// Library class managing the overall system
class Library {
    private List<Book> books;
    private List<Member> members;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void addMember(Member member) {
        members.add(member);
        System.out.println("Member added: " + member.getName());
    }

    public void listBooks() {
        System.out.println("Books in the library:");
        for (Book book : books) {
            book.displayDetails();
        }
    }

    public void listMembers() {
        System.out.println("Library Members:");
        for (Member member : members) {
            member.displayDetails();
        }
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Book> getBooks() {
        return books;
    }
}

// Main class to test the system
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Creating librarian and library
        Librarian librarian = new Librarian("Alice", 1);
        Library library = new Library();

        boolean running = true;

        while (running) {
            System.out.println("\nLibrary System Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. List Books");
            System.out.println("6. List Members");
            System.out.println("7. Add Member");
            System.out.println("8. Search Book by ID");
            System.out.println("9. Search Member by ID");
            System.out.println("10. Get Borrowed Books by Member ID");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add book
                    System.out.print("Enter Book ID: ");
                    int bookId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter Book Title: ");
                    String bookTitle = sc.nextLine();
                    System.out.print("Enter Book Author: ");
                    String bookAuthor = sc.nextLine();
                    Book newBook = new Book(bookTitle, bookAuthor, bookId);
                    librarian.addBook(newBook);
                    library.addBook(newBook);
                    break;

                case 2:
                    // Remove book
                    System.out.print("Enter Book ID to Remove: ");
                    int removeBookId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    Book bookToRemove = null;
                    for (Book book : library.getBooks()) {
                        if (book.getId() == removeBookId) {
                            bookToRemove = book;
                            break;
                        }
                    }
                    if (bookToRemove != null) {
                        librarian.removeBook(removeBookId);
                        library.removeBook(bookToRemove);
                    } else {
                        System.out.println("Book not found with ID: " + removeBookId);
                    }
                    break;

                case 3:
                    // Borrow book
                    System.out.print("Enter Member ID: ");
                    int memberId = sc.nextInt();
                    System.out.print("Enter Book ID to Borrow: ");
                    int borrowBookId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    Member borrowingMember = librarian.getMemberById(memberId);
                    Book bookToBorrow = librarian.getBookById(borrowBookId);
                    if (borrowingMember != null && bookToBorrow != null) {
                        borrowingMember.borrowBook(bookToBorrow);
                    } else {
                        System.out.println("Member or Book not found.");
                    }
                    break;

                case 4:
                    // Return book
                    System.out.print("Enter Member ID: ");
                    int returnMemberId = sc.nextInt();
                    System.out.print("Enter Book ID to Return: ");
                    int returnBookId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    Member returningMember = librarian.getMemberById(returnMemberId);
                    Book bookToReturn = librarian.getBookById(returnBookId);
                    if (returningMember != null && bookToReturn != null) {
                        returningMember.returnBook(bookToReturn);
                    } else {
                        System.out.println("Member or Book not found.");
                    }
                    break;

                case 5:
                    // List books
                    library.listBooks();
                    break;

                case 6:
                    // List members
                    library.listMembers();
                    break;

                case 7:
                    // Add member
                    System.out.print("Enter Member ID: ");
                    int newMemberId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter Member Name: ");
                    String memberName = sc.nextLine();
                    Member newMember = new Member(memberName, newMemberId);
                    library.addMember(newMember);
                    break;

                case 8:
                    // Get book details by ID
                    System.out.print("Enter Book ID: ");
                    int searchBookId = sc.nextInt();
                    Book book = librarian.getBookById(searchBookId);
                    if (book != null) {
                        book.displayDetails();
                    }
                    break;

                case 9:
                    // Get member details by ID
                    System.out.print("Enter Member ID: ");
                    int searchMemberId = sc.nextInt();
                    Member member = librarian.getMemberById(searchMemberId);
                    if (member != null) {
                        member.displayDetails();
                    }
                    break;

                case 10:
                    // Get borrowed books by member ID
                    System.out.print("Enter Member ID to see borrowed books: ");
                    int memberWithBorrowedBooksId = sc.nextInt();
                    List<Book> borrowedBooks = librarian.getBorrowedBooksByMemberId(memberWithBorrowedBooksId);
                    if (!borrowedBooks.isEmpty()) {
                        System.out.println("Borrowed Books:");
                        for (Book borrowedBook : borrowedBooks) {
                            borrowedBook.displayDetails();
                        }
                    } else {
                        System.out.println("No books borrowed by member with ID: " + memberWithBorrowedBooksId);
                    }
                    break;

                case 11:
                    // Exit
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        sc.close();
    }
}