package eu.assina.rssp.csc.services;

import eu.assina.rssp.common.config.CSCProperties;
import eu.assina.csc.model.CSCInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CSCInfoService {
	private CSCProperties cscProperties;

	@Autowired
	public CSCInfoService(CSCProperties cscProperties) {
		this.cscProperties = cscProperties;
	}

	public CSCInfo getInfo() {
	    return cscProperties.getInfo();
	}
}
