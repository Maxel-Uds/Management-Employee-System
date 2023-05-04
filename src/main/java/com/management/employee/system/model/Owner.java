package com.management.employee.system.model;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Owner {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String ownerDocument;
    @ToString.Exclude
    private String password;
    private String username;
}
