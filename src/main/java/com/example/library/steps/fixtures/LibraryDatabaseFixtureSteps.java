package com.example.library.steps.fixtures;

import com.example.library.config.DatabaseConfiguration;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import io.qameta.allure.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LibraryDatabaseFixtureSteps {

    private final Session session;

    public LibraryDatabaseFixtureSteps() {
        this.session = DatabaseConfiguration.getSessionFactory().openSession();
    }

    public void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @Step("Создаем автора с именем {0} {1}")
    public Author insertAuthor(String firstName, String secondName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setSecondName(secondName);

        Transaction tr = session.beginTransaction();
        session.persist(author);
        tr.commit();
        return author;
    }

    @Step("Создаем клиента с именем {0} {1}")
    public Customer insertCustomer(String firstName, String secondName) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setSecondName(secondName);

        Transaction tr = session.beginTransaction();
        session.persist(customer);
        tr.commit();
        return customer;
    }

    @Step("Создаем книгу с заголовком {0}, id автора {1}, id клиента {2}")
    public Book insertBook(String title, Long authorId, Long customerId) {
        Book book = new Book();
        book.setBookTitle(title);
        book.setAuthorId(authorId);
        book.setCustomerId(customerId);

        Transaction tr = session.beginTransaction();
        session.persist(book);
        tr.commit();
        return book;
    }

    @Step("Получаем книгу с ID {0}")
    public Book getBookById(Long newBookId) {return session.find(Book.class, newBookId);}


    @Step("Получаем автора с ID {0}")
    public Author getAutorId(Long newAutorId) {return session.find(Author.class, newAutorId);}

    @Step("Получаем клиента с ID {0}")
    public Customer getCustomerId(Long newCustomerId) {return session.find(Customer.class, newCustomerId);}
}