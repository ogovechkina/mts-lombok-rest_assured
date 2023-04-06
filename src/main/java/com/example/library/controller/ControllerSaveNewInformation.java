package com.example.library.controller;

import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import com.example.library.model.AddUserForBookSaveResponse;
import com.example.library.model.AuthorSaveResponse;
import com.example.library.model.BookSaveResponse;
import com.example.library.model.CustomerSaveResponse;
import com.example.library.model.ListBooksId;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CustomerRepository;
import java.util.Objects;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerSaveNewInformation {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CustomerRepository customerRepository;

    public ControllerSaveNewInformation(BookRepository bookRepository, AuthorRepository authorRepository, CustomerRepository customerRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.customerRepository = customerRepository;
    }


    @PostMapping("/library/authors/save")
    public AuthorSaveResponse saveAuthor(@RequestBody Author request) {
        authorRepository.save(request);

        AuthorSaveResponse response = new AuthorSaveResponse();
        response.setAuthorId(request.getId());
        return response;
    }

    @PostMapping("/library/users/save")
    public CustomerSaveResponse saveUser(@RequestBody Customer customer) {
        customer.setFirstName(customer.getFirstName())
                .setSecondName(customer.getSecondName());
        customerRepository.save(customer);

        CustomerSaveResponse response = new CustomerSaveResponse();
        response.setCustomerId(customer.getId());
        return response;
    }

    @PostMapping("/library/books/save")
    public BookSaveResponse saveBooks(@RequestBody Book book) {
        book.setBookTitle(book.getBookTitle())
            .setAuthor(authorRepository.getById(book.getAuthor().getId()));
        bookRepository.save(book);

        BookSaveResponse response = new BookSaveResponse();
        response.setBookId(book.getId());
        return response;
    }

    @PostMapping("/library/users/{userId}/getBooks")
    public AddUserForBookSaveResponse getNewBooks(@PathVariable Long userId, @RequestBody ListBooksId booksId) {
        if(Objects.isNull(booksId) || booksId.getBooksId().isEmpty()) {
            throw new RuntimeException("Список книг пустой");
        }

        Customer customer;
        try {
            customer = customerRepository.getById(userId);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Пользователь не найден");
        }

        for(Long bookId: booksId.getBooksId()) {
            Book book = bookRepository.getById(bookId);

            if(Objects.nonNull(book.getCustomer())) {
                throw new RuntimeException(String.format("Книгу id = %s уже читают", bookId));
            }

            book.setCustomer(customer);
            bookRepository.save(book);
        }

        AddUserForBookSaveResponse response = new AddUserForBookSaveResponse();
        response.setBooksId(booksId.getBooksId());
        response.setCustomerId(userId);
        return response;
    }

    @PostMapping("/library/books/return")
    public AddUserForBookSaveResponse returnBooks(@RequestBody ListBooksId booksId) {
        if(Objects.isNull(booksId) || booksId.getBooksId().isEmpty()) {
            throw new RuntimeException("Список книг пустой");
        }

        for(Long bookId: booksId.getBooksId()) {
            try {
                Book book = bookRepository.getById(bookId);

                if(Objects.isNull(book.getCustomer())) {
                    throw new RuntimeException(String.format("Книгу id = %s уже вернули", bookId));
                }

                book.setCustomer(null);
                bookRepository.save(book);

            } catch (RuntimeException ex) {
                throw new RuntimeException(String.format("Книга с id = %s не существует", bookId));
            }
        }

        AddUserForBookSaveResponse response = new AddUserForBookSaveResponse();
        response.setBooksId(booksId.getBooksId());
        return response;
    }
}
