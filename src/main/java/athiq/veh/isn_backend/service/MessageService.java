//package com.jxg.isn_backend.service;
//
//import com.jxg.isn_backend.dto.auth.UserDTO;
//import com.jxg.isn_backend.model.Message;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.repository.MessageRepository;
//import com.jxg.isn_backend.repository.UserRepository;
//import com.jxg.isn_backend.security.service.UserDetailsImpl;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class MessageService {
//
//    private final MessageRepository messageRepository;
//    private final UserRepository userRepository;
//
//    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
//        this.messageRepository = messageRepository;
//        this.userRepository = userRepository;
//    }
//
//    private UserDetailsImpl getLoggedInUserDetails() {
//        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
//
//
//    public Message sendMessage(Long receiverId, String content) {
//        UserDetailsImpl senderDetails = getLoggedInUserDetails();
//        Optional<User> receiver = userRepository.findById(receiverId);
//
//        if (receiver.isEmpty()) {
//            throw new IllegalArgumentException("Invalid receiver information");
//        }
//
//        Message message = new Message();
//        message.setSender(senderDetails.getUser());
//        message.setReceiver(receiver.get());
//        message.setContent(content);
//        message.setTimestamp(LocalDateTime.now());
//
//        return messageRepository.save(message);
//    }
//
//
//
//    public List<Message> getChatHistory(Long otherUserId) {
//        UserDetailsImpl loggedInUserDetails = getLoggedInUserDetails();
//        Optional<User> otherUser = userRepository.findById(otherUserId);
//
//        if (otherUser.isEmpty()) {
//            throw new IllegalArgumentException("Invalid user information");
//        }
//
//        User loggedInUser = loggedInUserDetails.getUser();
//        return messageRepository.findMessagesBetweenUsers(loggedInUser, otherUser.get());
//    }
//
//    public List<User> getUsersWhoMessaged() {
//        UserDetailsImpl loggedInUserDetails = getLoggedInUserDetails();
//        User loggedInUser = loggedInUserDetails.getUser();
//        return messageRepository.findUsersWhoMessaged(loggedInUser);
//    }
//}
//
//
//

package athiq.veh.isn_backend.service;


import athiq.veh.isn_backend.model.Message;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.MessageRepository;
import athiq.veh.isn_backend.repository.UserRepository;
import athiq.veh.isn_backend.security.service.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    private UserDetailsImpl getLoggedInUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

//    public Message sendMessage(Long receiverId, String content) {
//        UserDetailsImpl senderDetails = getLoggedInUserDetails();
//        Optional<User> receiver = userRepository.findById(receiverId);
//
//        if (receiver.isEmpty()) {
//            throw new IllegalArgumentException("Invalid receiver information");
//        }
//
//        Message message = new Message();
//        message.setSender(senderDetails.getUser());
//        message.setReceiver(receiver.get());
//        message.setContent(content);
//        message.setTimestamp(LocalDateTime.now());
//        message.setRead(false); // Set new messages as unread
//
//        return messageRepository.save(message);
//    }

    public Message sendMessage(Long receiverId, String content, Long postId, Long itemId) {
        UserDetailsImpl senderDetails = getLoggedInUserDetails();
        Optional<User> receiver = userRepository.findById(receiverId);

        if (receiver.isEmpty()) {
            throw new IllegalArgumentException("Invalid receiver information");
        }

        Message message = new Message();
        message.setSender(senderDetails.getUser());
        message.setReceiver(receiver.get());
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
        message.setPostId(postId);
        message.setItemId(itemId);

        if (postId != null) {
            String postUrl = " " + postId;
            message.setContent(content + " [View Post]" + " - "+postUrl +"  ");
        }

        if (itemId != null) {
            String itemUrl = " " + itemId;
            message.setContent(content + " [View Item]" + " - "+itemUrl +"  ");
        }
        return messageRepository.save(message);
    }


    public List<Message> getChatHistory(Long otherUserId) {
        UserDetailsImpl loggedInUserDetails = getLoggedInUserDetails();
        Optional<User> otherUserOpt = userRepository.findById(otherUserId);

        if (otherUserOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user information");
        }

        User loggedInUser = loggedInUserDetails.getUser();
        User otherUser = otherUserOpt.get();

        // Retrieve messages between the logged-in user and the selected user
        List<Message> messages = messageRepository.findMessagesBetweenUsers(loggedInUser, otherUser);

        // Mark messages as read only for the current conversation
        messageRepository.markMessagesAsRead(loggedInUser,otherUser);

        return messages;
    }



    public List<User> getUsersWhoMessaged() {
        UserDetailsImpl loggedInUserDetails = getLoggedInUserDetails();
        User loggedInUser = loggedInUserDetails.getUser();
        return messageRepository.findUsersWhoMessaged(loggedInUser);
    }

    public List<Message> getUnreadMessages() {
        UserDetailsImpl loggedInUserDetails = getLoggedInUserDetails();
        User loggedInUser = loggedInUserDetails.getUser();
        return messageRepository.findMessagesByReadStatus(loggedInUser, false); // `false` for unread messages
    }

}
