package com.htfate.wangeditor.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PasteContext {
	private String context;
	private String title;
	private Integer type;
	private String createDate;
}
