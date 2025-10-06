package com.aoyu.bitsetup.model.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName：UserSearchHistoryDTO
 * @Author: aoyu
 * @Date: 2025-10-06 20:50
 * @Description: 用户搜索历史DTO
 */

@Data
public class UserSearchHistoryDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String keyword;
}
