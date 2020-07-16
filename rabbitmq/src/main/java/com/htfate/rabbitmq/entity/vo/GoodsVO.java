package com.htfate.rabbitmq.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GoodsVO implements Serializable {
    private String name;
    private double size;
}
