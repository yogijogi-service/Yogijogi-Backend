package com.springboot.yogijogii.data.dto.myPageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinTeamStatusDto {
    private String teamName;
    private String position;
    private String status;
    private String teamImageUrl;
}
