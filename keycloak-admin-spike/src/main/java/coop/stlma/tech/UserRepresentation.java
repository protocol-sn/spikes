package coop.stlma.tech;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Introspected
@Data
@Serdeable
public class UserRepresentation {
    private String id;
    private String username;
    private boolean enabled;
    private Map<String, List<String>> realmRoles;
    private Map<String, List<String>> clientRoles;
    private Map<String, List<String>> attributes;
}
