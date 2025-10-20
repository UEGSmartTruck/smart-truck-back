package com.smarttruck.shared.token;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TokenBlacklist {
    private final Set<String> invalidatedTokens = new HashSet<>();

    public void addToBlacklist(final String token) {
        invalidatedTokens.add(token);
    }

    public boolean isBlacklisted(final String token) {
        return invalidatedTokens.contains(token);
    }

    public void removeFromBlacklist(final String token) {
        invalidatedTokens.remove(token);
    }
}
