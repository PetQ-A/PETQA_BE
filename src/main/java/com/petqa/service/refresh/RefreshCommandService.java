package com.petqa.service.refresh;

public interface RefreshCommandService {
    
    void addRefresh(String username, String socialId, String refresh, Long expiration);
}
