package com.luanheider.apiusuario.business.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelefoneDTO {
    private Long id;
    private String numero;
    private String ddd;
}
