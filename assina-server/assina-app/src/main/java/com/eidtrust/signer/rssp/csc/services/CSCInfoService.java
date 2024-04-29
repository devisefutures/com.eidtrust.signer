package com.eidtrust.signer.rssp.csc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eidtrust.signer.csc.model.CSCInfo;
import com.eidtrust.signer.rssp.common.config.CSCProperties;

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
