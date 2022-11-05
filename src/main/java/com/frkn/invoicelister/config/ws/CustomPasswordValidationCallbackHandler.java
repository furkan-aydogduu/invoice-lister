package com.frkn.invoicelister.config.ws;

import com.frkn.invoicelister.model.SystemUser;
import com.frkn.invoicelister.repository.customer.UserRepository;
import com.sun.xml.wss.impl.callback.PasswordValidationCallback;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.soap.security.callback.AbstractCallbackHandler;
import org.springframework.ws.soap.security.callback.CleanupCallback;
import org.springframework.ws.soap.security.wss4j2.callback.UsernameTokenPrincipalCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CustomPasswordValidationCallbackHandler extends AbstractCallbackHandler {

    @Autowired
    UserRepository userRepository;

    private Map<String, String> users = new HashMap<String, String>();

    @Override
    protected final void handleInternal(Callback callback) throws IOException, UnsupportedCallbackException {
        if(callback instanceof CleanupCallback) {
            handleCleanup((CleanupCallback) callback);
        }
        else if(callback instanceof PasswordValidationCallback) {
            handleUsernameToken(callback);
        }
    }

    /**
     * Invoked when the callback has a {@link WSPasswordCallback#DECRYPT} usage.
     * <p>
     * This method is invoked when WSS4J needs a password to get the private key of the
     * {@link WSPasswordCallback#getIdentifier() identifier} (username) from the keystore. WSS4J uses this private key to
     * decrypt the session (symmetric) key. Because the encryption method uses the public key to encrypt the session key
     * it needs no password (a public key is usually not protected by a password).
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */
    protected void handleDecrypt(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {
        throw new UnsupportedCallbackException(callback);
    }

    /**
     * Invoked when the callback has a {@link WSPasswordCallback#USERNAME_TOKEN} usage.
     * <p>
     * This method is invoked when WSS4J needs the password to fill in or to verify a UsernameToken.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */

    public void handleUsernameToken(Callback callback) throws IOException, UnsupportedCallbackException {
        PasswordValidationCallback _callback = (PasswordValidationCallback) callback;
        _callback.setValidator(new SimplePlainTextPasswordValidator());
        String username = ((PasswordValidationCallback.PlainTextPasswordRequest)_callback.getRequest()).getUsername();
        String password = ((PasswordValidationCallback.PlainTextPasswordRequest)_callback.getRequest()).getPassword();

        try {
            _callback.getValidator().validate(_callback.getRequest());
        } catch (PasswordValidationCallback.PasswordValidationException e) {
            throw new IOException(e);
        }
    }

    /**
     * Invoked when the callback has a {@link WSPasswordCallback#SIGNATURE} usage.
     * <p>
     * This method is invoked when WSS4J needs the password to get the private key of the
     * {@link WSPasswordCallback#getIdentifier() identifier} (username) from the keystore. WSS4J uses this private key to
     * produce a signature. The signature verfication uses the public key to verfiy the signature.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */

    public void setUsers(Properties users) {
        for (Map.Entry<Object, Object> entry : users.entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                this.users.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public void setUsersMap(Map<String, String> users) {
        this.users = users;
    }

    protected void handleSignature(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {
        throw new UnsupportedCallbackException(callback);
    }

    /**
     * Invoked when the callback has a {@link WSPasswordCallback#SECURITY_CONTEXT_TOKEN} usage.
     * <p>
     * This method is invoked when WSS4J needs the key to to be associated with a SecurityContextToken.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */
    protected void handleSecurityContextToken(WSPasswordCallback callback)
            throws IOException, UnsupportedCallbackException {
        throw new UnsupportedCallbackException(callback);
    }

    /**
     * Invoked when the callback has a {@link WSPasswordCallback#CUSTOM_TOKEN} usage.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */
    protected void handleCustomToken(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {
        throw new UnsupportedCallbackException(callback);
    }

    /**
     * Invoked when the callback has a {@link WSPasswordCallback#SECRET_KEY} usage.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */
    protected void handleSecretKey(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {
        throw new UnsupportedCallbackException(callback);
    }

    /**
     * Invoked when a {@link CleanupCallback} is passed to {@link #handle(Callback[])}.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */
    protected void handleCleanup(CleanupCallback callback) throws IOException, UnsupportedCallbackException {
        SecurityContextHolder.clearContext();
    }

    /**
     * Invoked when a {@link UsernameTokenPrincipalCallback} is passed to {@link #handle(Callback[])}.
     * <p>
     * Default implementation throws an {@link UnsupportedCallbackException}.
     */
    protected void handleUsernameTokenPrincipal(UsernameTokenPrincipalCallback callback)
            throws IOException, UnsupportedCallbackException {
        throw new UnsupportedCallbackException(callback);
    }

    private class SimplePlainTextPasswordValidator implements PasswordValidationCallback.PasswordValidator {

        @Override
        public boolean validate(PasswordValidationCallback.Request request)
                throws PasswordValidationCallback.PasswordValidationException {
            PasswordValidationCallback.PlainTextPasswordRequest plainTextPasswordRequest = (PasswordValidationCallback.PlainTextPasswordRequest) request;
            String username = plainTextPasswordRequest.getUsername();
            SystemUser systemUserFromDb = userRepository.findByName(username);

            if(systemUserFromDb != null){
                String passwordFromDb = systemUserFromDb.getPassword();
                String passwordFromRequest = plainTextPasswordRequest.getPassword();

                if(passwordFromDb != null && passwordFromRequest != null
                        && passwordFromDb.equals(passwordFromRequest)
                ){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }
}
