package coop.stlma.tech.protocolsn.spike;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Serdeable
public record SomeRequestObj(@NotNull String nonNullField,
                             String nullableField,
                             @NotBlank String nonBlankField,
                             @NotNull LocalDate nonNullDate) {
}
