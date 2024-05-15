package coop.stlma.tech.protocolsn.spike;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Serdeable
public record SomeResponseObj(@NotBlank String nonBlankField,
                              @NotNull String nonNullField,
                              Integer nullableInt,
                              int nonNullableInt,
                              @Size(min = 1, max = 10) List<String> nonEmptyArray) {
}
