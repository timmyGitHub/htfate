package com.htfate.modelcenter.entity.pojo;

import lombok.Data;

@Data
public class OauthResult {
	private String access_token;
	private String token_type;
	private String expires_in;
	private String scope;

	private String error;
	private String error_description;
}
