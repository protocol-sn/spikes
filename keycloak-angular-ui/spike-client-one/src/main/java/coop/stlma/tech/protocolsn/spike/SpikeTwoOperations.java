package coop.stlma.tech.protocolsn.spike;

public interface SpikeTwoOperations {
    String getUnsecured();
    String getSecured();
    String getSecuredWithNoRole();
    String getSecuredWithInvalidRole();
}
