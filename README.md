# Overview:
The Loan Management System is a Java Swing application, made by Vinicio Yusuke Hayashibara (13642797), Enzo Castelo Branco Biondi (14568142) and Gustavo Negrão Ribeiro Souza (14577045) that manages library loan records. It allows users to view, add, update, delete, and search for loan records in a database. The application features a graphical user interface (GUI) to interact with the loan data stored in a relational database.

# Requirements:
  - Java Development Kit (JDK) 8 or higher
  - JDBC driver for the database being used (e.g., MySQL, PostgreSQL)

# Features:
 - View Records: Display all loan, books and patrons records in a table format.
 - Add Record: Add new loan, books and patrons records to the database.
 - Update Record: Modify existing loan, books and patrons records.
 - Delete Record: Remove loan, books and patrons records from the database.
 - Search Records: Search for loan, books and patrons records based on various criteria.
 - Back Navigation: Return to the main application window.

 # Components:
 - LoanPanel: The main panel for managing loan records, including a table for displaying data and buttons for various actions.
 - BookPanel: The main panel to manage book records, including author`s name, and the genre.
 - PatronPanel: The main pannel for managging patron records such as the name, phone number and id of each patron
 - Database Connection: Connects to a database to retrieve and manipulate loan data.

 # Test plans:
  - Adding a book, loan or patron: check if the add functionality is working
  - Removing a book, loan or patron: check if the remove functionality is working
  - Updating a book, loan or patron: check if the update functionality is working
  - Inability to remove certain books: check if lent books can be removed
  - Can't lend a book out on loan: check if the a book that is currently unavailable can be lent
  - Book availability: check if once the book is returned it can be lent again

   # Test results:
  - Adding a book, loan or patron: By pressing add you can input the info and add a book, loan or patreon
  - Removing a book, loan or patron: The button removes the selected object
  - Updating a book, loan or patron: The button opens the info tab so you can update the selected object's info
  - Inability to remove certain books: The program will not let you remove a book if it's in use
  - Can't lend a book out on loan: The program won't let you lend a book if it's currently unavailable
  - Book availability: Once a book is deemed as returned it may once again be lent

# How to Run the Code

1.⁠ ⁠*Download PostgreSQL Server:*
   - If you haven't already downloaded PostgreSQL server, please do so. This [video tutorial](https://www.youtube.com/watch?v=0n41UTkOBb0&t=398s) may help you.

2.⁠ ⁠*Create Database:*
   - Open PostgreSQL on your device.
   - Create a database called "Biblioteca".

3.⁠ ⁠*Update Password:*
   - Change the database connection password in the main class to match your PostgreSQL password.

4.⁠ ⁠*Apply JDBC Driver:*
   - If you are using IntelliJ IDEA, this [video tutorial](https://www.youtube.com/watch?v=o9dcSS_82gw&list=PL0vVAYYSRbD2zL7o_TBPnVAgBZmg6f4JA) (from minute 3 to 4) may help you apply the JDBC driver.

5.⁠ ⁠*Run the Main Code:*
   - Execute the main code to start the application.
