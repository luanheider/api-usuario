package com.luanheider.apiusuario.business;

import com.luanheider.apiusuario.business.converter.UsuarioConverter;
import com.luanheider.apiusuario.business.dto.UsuarioDTO;
import com.luanheider.apiusuario.infrastructure.entity.Usuario;
import com.luanheider.apiusuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }
}