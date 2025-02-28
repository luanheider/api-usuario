package com.luanheider.apiusuario.business;

import com.luanheider.apiusuario.business.converter.UsuarioConverter;
import com.luanheider.apiusuario.business.dto.EnderecoDTO;
import com.luanheider.apiusuario.business.dto.TelefoneDTO;
import com.luanheider.apiusuario.business.dto.UsuarioDTO;
import com.luanheider.apiusuario.infrastructure.entity.Endereco;
import com.luanheider.apiusuario.infrastructure.entity.Telefone;
import com.luanheider.apiusuario.infrastructure.entity.Usuario;
import com.luanheider.apiusuario.infrastructure.exceptions.ConflictException;
import com.luanheider.apiusuario.infrastructure.exceptions.ResourceNotFoundException;
import com.luanheider.apiusuario.infrastructure.repository.EnderecoRepository;
import com.luanheider.apiusuario.infrastructure.repository.TelefoneRepository;
import com.luanheider.apiusuario.infrastructure.repository.UsuarioRepository;
import com.luanheider.apiusuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExistente(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExistente(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email).orElseThrow(
                            () -> new ResourceNotFoundException("Email não encontrado " + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado ", e.getCause());
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado " + email));
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public EnderecoDTO atualizaEndereco(Long id, EnderecoDTO enderecoDTO) {
        Endereco enderecoEntity = enderecoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado " + id));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoEntity, enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(endereco);
    }

    public TelefoneDTO atualizaTelefone(Long id, TelefoneDTO telefoneDTO) {
        Telefone telefoneEntity = telefoneRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado " + id));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneEntity, telefoneDTO);
        telefone = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefone);
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO enderecoDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado " + email));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO telefoneDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado " + email));
        Telefone telefone = usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }
}