package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.rest.dto.AuthorDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().map(AuthorDto::toDto).toList();
    }
}
