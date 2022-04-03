package ua.tunepoint.audio.security;

public interface AccessManager<U, T> {

    void authorize(U userIdentity, T objectIdentity);
}
