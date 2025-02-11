//package com.jxg.isn_backend.controller;
//
//import com.jxg.isn_backend.model.Message;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.service.MessageService;
//import com.jxg.isn_backend.service.UserService;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/api/messages")
//public class MessageController {
//
//    private final MessageService messageService;
//    private final UserService userService;
//
//    public MessageController(MessageService messageService, UserService userService) {
//        this.messageService = messageService;
//        this.userService = userService;
//    }
//
//
//    @GetMapping("/from-sender")
//    public ResponseEntity<List<Message>> getMessagesFromSender(
//            @RequestParam(name = "otherUserId") Long otherUserId) {
//        List<Message> messages = messageService.getChatHistory(otherUserId);
//        return ResponseEntity.ok(messages);
//    }
//
//    @PostMapping
//    public ResponseEntity<Message> sendMessage(
//            @RequestParam(name = "receiverId") Long receiverId,
//            @RequestParam(name = "content") String content) {
//
//        Message message = messageService.sendMessage(receiverId, content);
//        return ResponseEntity.ok(message);
//    }
//
//    @GetMapping("/users-who-messaged")
//    public ResponseEntity<List<User>> getUsersWhoMessaged() {
//        List<User> users = messageService.getUsersWhoMessaged();
//        return ResponseEntity.ok(users);
//    }
//
//
//}

package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.model.Message;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.service.MessageService;
import athiq.veh.isn_backend.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/from-sender")
    public ResponseEntity<List<Message>> getMessagesFromSender(
            @RequestParam(name = "otherUserId") Long otherUserId) {
        List<Message> messages = messageService.getChatHistory(otherUserId);
        return ResponseEntity.ok(messages);
    }

//    @PostMapping
//    public ResponseEntity<Message> sendMessage(
//            @RequestParam(name = "receiverId") Long receiverId,
//            @RequestParam(name = "content") String content) {
//
//        Message message = messageService.sendMessage(receiverId, content);
//        return ResponseEntity.ok(message);
//    }

@PostMapping
public ResponseEntity<Message> sendMessage(
        @RequestParam(name = "receiverId") Long receiverId,
        @RequestParam(name = "content") String content,
        @RequestParam(name = "postId", required = false) Long postId,
        @RequestParam(name = "itemId", required = false) Long itemId) {

    Message message = messageService.sendMessage(receiverId, content, postId, itemId);
    return ResponseEntity.ok(message);
}

    @GetMapping("/users-who-messaged")
    public ResponseEntity<List<User>> getUsersWhoMessaged() {
        List<User> users = messageService.getUsersWhoMessaged();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Message>> getUnreadMessages() {
        List<Message> unreadMessages = messageService.getUnreadMessages();
        return ResponseEntity.ok(unreadMessages);
    }
}
