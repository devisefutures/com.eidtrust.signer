package eu.assina.csc.model;

/**
 * Represents cert status from the CSC spec:
 * One of valid | expired | revoked | suspended
 * The status of validity of the end entity certificate.
 */
public enum CertificateStatus {
    valid, expired, revoked, suspended;
}
