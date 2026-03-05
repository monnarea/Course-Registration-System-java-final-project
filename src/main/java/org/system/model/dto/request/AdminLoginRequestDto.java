package org.system.model.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminLoginRequestDto {
    private String fullName;
    private String passwordHast;
}
