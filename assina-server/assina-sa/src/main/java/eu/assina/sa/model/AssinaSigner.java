package eu.assina.sa.model;

import eu.assina.sa.client.ClientContext;

public interface AssinaSigner {

    /** Prepares the AssignSigner */
    ClientContext prepCredenital();

    byte[] signHash(byte[] pdfHash, ClientContext context);
}
