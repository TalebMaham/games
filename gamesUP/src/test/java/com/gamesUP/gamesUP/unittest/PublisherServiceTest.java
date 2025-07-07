package com.gamesUP.gamesUP.unittest;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repositories.PublisherRepository;
import com.gamesUP.gamesUP.services.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PublisherServiceTest {

    private PublisherRepository repository;
    private PublisherService service;

    @BeforeEach
    void setup() {
        repository = mock(PublisherRepository.class);
        service = new PublisherService(repository);
    }

    @Test
    void testCreateNewPublisher() {
        String name = "Unique Publisher";
        when(repository.existsByName(name)).thenReturn(false);
        when(repository.save(any(Publisher.class))).thenAnswer(inv -> inv.getArgument(0));

        Publisher result = service.create(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(repository).existsByName(name);
        verify(repository).save(any(Publisher.class));
    }

    @Test
    void testCreateDuplicatePublisherThrows() {
        String name = "Duplicate Publisher";
        when(repository.existsByName(name)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.create(name));
        verify(repository).existsByName(name);
        verify(repository, never()).save(any());
    }

    @Test
    void testGetAllPublishers() {
        List<Publisher> list = List.of(new Publisher("Pub1"), new Publisher("Pub2"));
        when(repository.findAll()).thenReturn(list);

        List<Publisher> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}
