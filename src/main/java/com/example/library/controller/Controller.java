package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CustomerRepository;
import java.util.Objects;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class Controller {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CustomerRepository customerRepository;

    public Controller(BookRepository bookRepository, AuthorRepository authorRepository, CustomerRepository customerRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.customerRepository = customerRepository;
    }


    @GetMapping("/library/users/{userId}/books")
    public List<Book> getAllBooks(@PathVariable Long userId) {
        return bookRepository.findAllByCustomer_Id(userId);
    }

    @GetMapping("/library/authors/{authorId}/books")
    public List<Book> getAllBooksFromAuthor(@PathVariable Long authorId) {
        return bookRepository.findAllByAuthor_Id(authorId);
    }

    @GetMapping("/library/authors/{authorId}/users")
    public List<Customer> getAllUsersByAuthor(@PathVariable Long authorId) {
        List<Book> books = bookRepository.findAllByAuthor_Id(authorId);

        HashMap<Long, Customer> users = new HashMap<>();

        books.forEach(book -> {
            Customer customer = book.getCustomer();
            if (Objects.nonNull(customer)) {
                users.put(customer.getId(), customer);
            }
        });

        return new ArrayList(users.values());
    }


}