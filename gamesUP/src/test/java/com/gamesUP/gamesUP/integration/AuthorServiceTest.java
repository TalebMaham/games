package com.gamesUP.gamesUP.integration;
// AuthorServiceTest.java

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repositories.AuthorRepository;
import com.gamesUP.gamesUP.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testCreateAuthor() {
        Author author = authorService.create("Shigeru Miyamoto");

        assertNotNull(author.getId());
        assertEquals("Shigeru Miyamoto", author.getName());
    }

    @Test
    void testGetAllAuthors() {
        authorService.create("Author A");
        authorService.create("Author B");

        List<Author> authors = authorService.getAll();
        assertTrue(authors.size() >= 2);
    }
}
