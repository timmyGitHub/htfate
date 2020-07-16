package com.htfate.oauth2center.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
	private String id;
	private String username;
}
