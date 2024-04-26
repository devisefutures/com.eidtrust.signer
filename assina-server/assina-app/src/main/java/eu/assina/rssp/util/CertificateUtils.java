package eu.assina.rssp.util;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class CertificateUtils {

    private static final SimpleDateFormat X509DateFormat
            = new SimpleDateFormat("yyyyMMddHHmmss'Z'");

    /**
     * Formats a date as a string according to x509 RFC 5280
     * Assumes the given date is UTC
     * @param date
     * @return null if the date is null otherwise formatted as YYYMMMDDHHMMSSZ
     */
    public static String x509Date(Date date) {
        if (date == null) return null;

        return X509DateFormat.format(date);
    }

    public static String x509ToCSCString(X509Certificate x509Certificate) {
        throw new IllegalArgumentException("IMPLEMENT x509 to string");
        // TODO
    }

    public static void main(String[] args) {
        Date now = Date.from(Instant.now());
        System.out.println(x509Date(now));
    }

}
