package eu.assina.csc.payload;

import eu.assina.csc.model.AbstractInfo;
import eu.assina.csc.model.CSCInfo;

/**
 * Info response
 * From section 11.1 of the CSC API V_1.0.4.0 spec
 */
public class CSCInfoResponse extends AbstractInfo {
    // all methods inherited from the abstract base class

    public CSCInfoResponse(CSCInfo other) {
        setAuthType(other.getAuthType());
        setDescription(other.getDescription());
        setLang(other.getLang());
        setLogo(other.getLogo());
        setName(other.getName());
        setMethods(other.getMethods());
        setRegion(other.getRegion());
        setOauth2(other.getOauth2());
        setSpecs(other.getSpecs());
    }
}
