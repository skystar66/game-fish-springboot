/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xl.game.mina.websocket;

/**
 * Wraps around a string that represents a websocket handshake response from
 * the server to the browser.
 * 
 * @author DHRUV CHOPRA
 */
public class WebSocketHandShakeResponse {
    
    private final String response;
    public WebSocketHandShakeResponse(String response){
        this.response = response;
    }
    
    public String getResponse(){
        return response;
    }
}
