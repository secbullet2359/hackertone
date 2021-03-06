package com.example.server.dto;

import com.example.server.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoomDto {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(String roomId,String name){
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessageDto chatMessageDto, ChatService chatService){
        if (chatMessageDto.getType().equals(ChatMessageDto.MessageType.ENTER)){
            sessions.add(session);
            chatMessageDto.setMessage(chatMessageDto.getSender() +"님이 입장하였습니다.");
        }
        sendMessage(chatMessageDto, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatservice){
        sessions.parallelStream().forEach(session -> chatservice.sendMessage(session, message));
    }

}
