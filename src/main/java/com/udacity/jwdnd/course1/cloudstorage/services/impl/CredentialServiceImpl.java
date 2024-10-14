package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialServiceImpl implements CredentialService {
    private final CredentialMapper credMapper;
    private final AuthenticationService authService;
    private final EncryptionService encryptionService;

    public CredentialServiceImpl(CredentialMapper credMapper, AuthenticationService authService,
                            EncryptionService encryptionService) {
        this.credMapper = credMapper;
        this.authService = authService;
        this.encryptionService = encryptionService;
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
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setCkey(encodedKey);
//        String decryptedPassword = encryptionService.decryptValue(encryptedPassword, encodedKey);
        return credMapper.updateCredential(credential);
    }

    @Override
    public Integer insertCredential(Credential credential) {
        credential.setUserId(authService.getCurrentUser().getUserId());
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setCkey(encodedKey);
        return credMapper.insertCredential(credential);
    }

    @Override
    public Integer deleteById(Integer credentialId) {
        return credMapper.deleteById(credentialId, authService.getCurrentUser().getUserId());
    }
}
