package com.udemy.microservice.bookservice.controller;

import com.udemy.microservice.bookservice.model.Book;
import com.udemy.microservice.bookservice.proxy.CambioProxy;
import com.udemy.microservice.bookservice.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book endpoint")
@RestController
@RequestMapping("/book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository repository;

    @Autowired
    private CambioProxy proxy;

//    @GetMapping(value = "/{id}/{currency}")
//    public Book findBook(
//            @PathVariable Long id,
//            @PathVariable String currency
//    ) {
//
//        try {
//            var book = repository.getReferenceById(id);
//
//            HashMap<String, String> params = new HashMap<>();
//            params.put("amount", book.getPrice().toString());
//            params.put("from", "USD");
//            params.put("to", currency);
//
//            var response = new RestTemplate().getForEntity(
//                    "http://localhost:8001/cambio-service/{amount}/{from}/{to}",
//                    Cambio.class,
//                    params
//            );
//
//            var cambio = response.getBody();
//
//            var port = environment.getProperty("local.server.port");
//            book.setEnvironment(port);
//            book.setPrice(cambio.getConvertedValue());
//
//            return book;
//
//        } catch (JpaObjectRetrievalFailureException e) {
//            throw new RuntimeException("Book not found");
//        }
//    }

    @GetMapping(value = "/{id}/{currency}")
    @Operation(summary = "Find a specific book by your ID")
    public Book findBook(
            @PathVariable Long id,
            @PathVariable String currency
    ) {

        try {
            var book = repository.getReferenceById(id);

            var cambio = proxy.getCambio(book.getPrice(), "USD", currency);

            var port = environment.getProperty("local.server.port");
            book.setEnvironment("Book port: " + port + " Cambio port: " + cambio.getEnvironment());
            book.setPrice(cambio.getConvertedValue());

            return book;

        } catch (JpaObjectRetrievalFailureException e) {
            throw new RuntimeException("Book not found");
        }
    }

}
