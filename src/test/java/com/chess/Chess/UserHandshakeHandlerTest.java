package com.chess.Chess;

import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserHandshakeHandlerTest {

    @Test
    void determineUser() {
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        WebSocketHandler wsHandler = mock(WebSocketHandler.class);
        Map<String, Object> attributes = mock(Map.class);

        UserHandshakeHandler handshakeHandler = new UserHandshakeHandler();

        Principal principal = handshakeHandler.determineUser(request, wsHandler, attributes);

        assertEquals(UserPrincipal.class, principal.getClass());
    }
}