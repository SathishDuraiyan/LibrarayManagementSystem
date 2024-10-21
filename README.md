Here’s a detailed summary for your **README** file for the Library Management System project:

---

# Library Management System

## Project Overview

The Library Management System is a console-based Java application designed to manage a library's core operations, such as adding and removing books, managing members, and borrowing and returning books. The project demonstrates the use of **Object-Oriented Programming (OOP)** principles such as **Encapsulation**, **Inheritance**, **Polymorphism**, and **Abstraction**, providing a modular and scalable solution for library management. This system integrates with a **MySQL** database for data persistence.

---

## Key Features

### 1. **Book Management**
   - **Add Book**: The librarian can add new books to the library's catalog. Each book is stored in the **Books** database table, which includes an ID, title, and author.
   - **Remove Book**: The librarian can remove a book from the library's catalog by its unique ID. The book is removed from the **Books** table and is no longer available for borrowing.
   - **Search Book by ID**: Allows a user to retrieve details of a specific book based on its unique ID from the database.
   
### 2. **Member Management**
   - **Add Member**: The librarian can add new members to the system, storing member details in the **Members** database table.
   - **Search Member by ID**: Retrieves details about a member using their unique ID.

### 3. **Borrow and Return Books**
   - **Borrow Book**: A member can borrow a book by providing their member ID and the book's ID. The system will update the **BorrowedBooks** table to reflect that the member has borrowed the book.
   - **Return Book**: A member can return a borrowed book by providing their member ID and the book's ID. The book will be removed from the member's borrowed books list in the **BorrowedBooks** table.
   - **Get Borrowed Books by Member ID**: Lists all the books currently borrowed by a specific member.

### 4. **Library Inventory and Member Listing**
   - **List Books**: Displays all the books currently available in the library.
   - **List Members**: Shows all members registered in the library system.

---

## Classes and Methods Overview

### 1. **`DatabaseConnection`**
   - Establishes a connection to the **MySQL** database using JDBC.
   - **Method**: 
     ```java
     public static Connection getConnection() throws SQLException
     ```
     Returns a `Connection` object to interact with the database.

### 2. **`Person` (Abstract Class)**
   - Represents a general person in the system, including both librarians and members.
   - **Attributes**:
     - `String name`: The person's name.
     - `int id`: The person's unique ID.
   - **Abstract Method**:
     - `public abstract void displayDetails()`: Displays the details of the person (either librarian or member).

### 3. **`Librarian` (Extends `Person`)**
   - Manages the library's operations, such as adding/removing books, searching for members, and managing borrowed books.
   - **Methods**:
     - `public void addBook(Book book)`: Adds a new book to the **Books** table in the database.
     - `public void removeBook(int bookId)`: Removes a book from the database by its ID.
     - `public Book getBookById(int bookId)`: Retrieves details of a book by its ID from the database.
     - `public Member getMemberById(int memberId)`: Retrieves details of a member by their ID from the database.
     - `public List<Book> getBorrowedBooksByMemberId(int memberId)`: Returns a list of books borrowed by a specific member.

### 4. **`Member` (Extends `Person`)**
   - Represents a member of the library. Members can borrow and return books.
   - **Attributes**:
     - `List<Book> borrowedBooks`: Stores the list of books currently borrowed by the member.
   - **Methods**:
     - `public void borrowBook(Book book)`: Borrows a book and adds it to the member's `borrowedBooks` list.
     - `public void returnBook(Book book)`: Returns a borrowed book and removes it from the member's `borrowedBooks` list.
     - `private void borrowBookFromDatabase(int memberId, int bookId)`: Updates the **BorrowedBooks** table when a book is borrowed.
     - `private void returnBookToDatabase(int memberId, int bookId)`: Updates the **BorrowedBooks** table when a book is returned.

### 5. **`Book`**
   - Represents a book in the library. Demonstrates **Encapsulation** by keeping the book's attributes private and providing getters.
   - **Attributes**:
     - `String title`: The title of the book.
     - `String author`: The author of the book.
     - `int id`: The unique ID of the book.
   - **Methods**:
     - `public String getTitle()`: Returns the title of the book.
     - `public String getAuthor()`: Returns the author of the book.
     - `public int getId()`: Returns the ID of the book.
     - `public void displayDetails()`: Displays the book's details, including title, author, and ID.

### 6. **`Library`**
   - Manages the overall operations of the library, such as storing books and members in the system.
   - **Attributes**:
     - `List<Book> books`: Stores all books in the library.
     - `List<Member> members`: Stores all members in the library.
   - **Methods**:
     - `public void addBook(Book book)`: Adds a book to the library's list.
     - `public void removeBook(Book book)`: Removes a book from the library's list.
     - `public void addMember(Member member)`: Adds a new member to the system.
     - `public void listBooks()`: Lists all books available in the library.
     - `public void listMembers()`: Lists all members registered in the system.

---

## Database Schema

### 1. **Books Table**
| Column   | Type     | Description             |
|----------|----------|-------------------------|
| id       | INT      | Primary key (Book ID)    |
| title    | VARCHAR  | Title of the book        |
| author   | VARCHAR  | Author of the book       |

### 2. **Members Table**
| Column   | Type     | Description             |
|----------|----------|-------------------------|
| id       | INT      | Primary key (Member ID)  |
| name     | VARCHAR  | Name of the member       |

### 3. **BorrowedBooks Table**
| Column     | Type     | Description                    |
|------------|----------|--------------------------------|
| member_id  | INT      | Foreign key referencing Member |
| book_id    | INT      | Foreign key referencing Book   |

---

## How to Run the Project

1. **Clone the Repository**: Clone this repository from GitHub to your local machine.
2. **Set up the Database**:
   - Install MySQL and create a database named `librarymanagement`.
   - Create the **Books**, **Members**, and **BorrowedBooks** tables as per the schema above.
3. **Run the Application**:
   - Open the project in your preferred IDE (like IntelliJ IDEA or Eclipse).
   - Run the `Main.java` class to start the Library Management System.
4. **Use the Application**:
   - Follow the on-screen menu to manage books, members, and borrow/return operations.

---

## Technology Stack

- **Programming Language**: Java
- **Database**: MySQL
- **Tools**: JDBC, IntelliJ IDEA/Ecl, Git

---
