package com.eidtrust.signer.sa.model;

import com.eidtrust.signer.sa.client.ClientContext;

public interface AssinaSigner {

    /** Prepares the AssignSigner */
    ClientContext prepCredenital();

    byte[] signHash(byte[] pdfHash, ClientContext context);
}
