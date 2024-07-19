package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialServiceImpl implements CredentialService {
    private final CredentialMapper credMapper;
    private final AuthenticationService authService;

    public CredentialServiceImpl(CredentialMapper credMapper, AuthenticationService authService) {
        this.credMapper = credMapper;
        this.authService = authService;
    }

    @Override
    public Credential findById(Integer id) {
        return credMapper.findById(id, authService.getCurrentUser().getUserId());
    }

    @Override
    public List<Credential> findAllCredentials() {
        return credMapper.findAllCredentials(authService.getCurrentUser().getUserId());
    }

    @Override
    public Integer updateCredential(Credential credential) {
        credential.setUserId(authService.getCurrentUser().getUserId());
        return credMapper.updateCredential(credential);
    }

    @Override
    public Integer insertCredential(Credential credential) {
        credential.setUserId(authService.getCurrentUser().getUserId());
        return credMapper.insertCredential(credential);
    }

    @Override
    public Integer deleteById(Integer credentialId) {
        return credMapper.deleteById(credentialId, authService.getCurrentUser().getUserId());
    }
}
