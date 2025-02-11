//package com.jxg.isn_backend.service;
//
//import com.jxg.isn_backend.model.*;
//import com.jxg.isn_backend.repository.SavedRepository;
//import com.jxg.isn_backend.repository.PostRepository;
//import com.jxg.isn_backend.repository.ItemRepository;
//import com.jxg.isn_backend.exception.ResourceNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class SavedService {
//
//    private final SavedRepository savedRepository;
//    private final PostRepository postRepository;
//    private final ItemRepository itemRepository;
//    private final AuthService authService;
//
//    public SavedService(SavedRepository savedRepository, PostRepository postRepository, ItemRepository itemRepository, AuthService authService) {
//        this.savedRepository = savedRepository;
//        this.postRepository = postRepository;
//        this.itemRepository = itemRepository;
//        this.authService = authService;
//    }
//
//    public Saved savePost(Long postId) {
//        User currentUser = authService.getCurrentLoggedUser();
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
//
//        Optional<Saved> existingSaved = savedRepository.findByPostAndCreatedBy(post, currentUser);
//
//        if (existingSaved.isPresent()) {
//            savedRepository.delete(existingSaved.get());
//            return null;
//        } else {
//            Saved saved = new Saved();
//            saved.setPost(post);
//            saved.setCreatedBy(currentUser);
//            return savedRepository.save(saved);
//        }
//    }
//
//    public Saved saveItem(Long itemId) {
//        User currentUser = authService.getCurrentLoggedUser();
//        Item item = itemRepository.findById(itemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));
//
//        Optional<Saved> existingSaved = savedRepository.findByItemAndCreatedBy(item, currentUser);
//
//        if (existingSaved.isPresent()) {
//            savedRepository.delete(existingSaved.get());
//            return null;
//        } else {
//            Saved saved = new Saved();
//            saved.setItem(item);
//            saved.setCreatedBy(currentUser);
//            return savedRepository.save(saved);
//        }
//    }
//
//    public List<Post> getSavedPostsForCurrentUser() {
//        User currentUser = authService.getCurrentLoggedUser();
//        List<Saved> savedItems = savedRepository.findByCreatedBy(currentUser);
//        return savedItems.stream()
//                .filter(saved -> saved.getPost() != null)
//                .map(Saved::getPost)
//                .collect(Collectors.toList());
//    }
//
//    public List<Item> getSavedItemsForCurrentUser() {
//        User currentUser = authService.getCurrentLoggedUser();
//        List<Saved> savedItems = savedRepository.findByCreatedBy(currentUser);
//        return savedItems.stream()
//                .filter(saved -> saved.getItem() != null)
//                .map(Saved::getItem)
//                .collect(Collectors.toList());
//    }
//}






package athiq.veh.isn_backend.service;
import athiq.veh.isn_backend.dto.response.SavedResponseDTO;
import athiq.veh.isn_backend.model.Item;
import athiq.veh.isn_backend.model.Saved;
import athiq.veh.isn_backend.model.User;
import athiq.veh.isn_backend.repository.SavedRepository;
import athiq.veh.isn_backend.repository.ItemRepository;
import athiq.veh.isn_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedService {

    private final SavedRepository savedRepository;
    private final ItemRepository itemRepository;
    private final AuthService authService;

    public SavedService(SavedRepository savedRepository, ItemRepository itemRepository, AuthService authService) {
        this.savedRepository = savedRepository;
        this.itemRepository = itemRepository;
        this.authService = authService;
    }



    public Saved saveItem(Long itemId) {
        User currentUser = authService.getCurrentLoggedUser();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        Optional<Saved> existingSaved = savedRepository.findByItemAndCreatedBy(item, currentUser);

        if (existingSaved.isPresent()) {
            savedRepository.delete(existingSaved.get());
            return null;
        } else {
            Saved saved = new Saved();
            saved.setItem(item);
            saved.setCreatedBy(currentUser);
            return savedRepository.save(saved);
        }
    }


    public List<SavedResponseDTO> getSavedItemsForCurrentUser() {
        User currentUser = authService.getCurrentLoggedUser();
        List<Saved> savedItems = savedRepository.findByCreatedBy(currentUser);
        return savedItems.stream()
                .filter(saved -> saved.getItem() != null)
                .map(saved -> new SavedResponseDTO(saved.getItem(), saved.getItem().getCreatedBy())) // Get createdBy from Item
                .collect(Collectors.toList());
    }
}
