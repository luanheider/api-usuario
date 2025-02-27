package com.luanheider.apiusuario.business.dto;

import com.luanheider.apiusuario.infrastructure.entity.Endereco;
import com.luanheider.apiusuario.infrastructure.entity.Telefone;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;
}
