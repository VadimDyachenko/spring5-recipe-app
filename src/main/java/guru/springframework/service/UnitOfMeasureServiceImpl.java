package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository repository;
    private final UnitOfMeasureToUnitOfMeasureCommand converter;

    @Autowired
    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository repository, UnitOfMeasureToUnitOfMeasureCommand converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUom() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(converter::convert)
                .collect(Collectors.toSet());
    }
}
