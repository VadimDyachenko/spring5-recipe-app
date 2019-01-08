package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure source) {
        return source == null ? null : convertSource(source);
    }

    private UnitOfMeasureCommand convertSource(UnitOfMeasure source) {
        final UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());

        return command;
    }
}
