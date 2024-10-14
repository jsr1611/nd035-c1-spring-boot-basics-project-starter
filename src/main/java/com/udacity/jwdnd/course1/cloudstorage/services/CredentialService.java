package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

import java.util.List;

public interface CredentialService {
    Credential findById(Integer id);

    List<Credential> findAllCredentials();

    Integer updateCredential(Credential credential);

    Integer insertCredential(Credential credential);

    Integer deleteById(Integer credentialId);
}
