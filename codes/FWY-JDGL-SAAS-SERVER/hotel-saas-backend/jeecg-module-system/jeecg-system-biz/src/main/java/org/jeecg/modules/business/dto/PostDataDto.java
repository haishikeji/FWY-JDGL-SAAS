package org.jeecg.modules.business.dto;

import lombok.Data;

import java.util.List;


@Data
public class PostDataDto {
    private String id;
    private List<String> ids;
}
