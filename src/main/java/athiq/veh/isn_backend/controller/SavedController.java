//package com.jxg.isn_backend.controller;
//
//import com.jxg.isn_backend.model.Post;
//import com.jxg.isn_backend.model.Item;
//import com.jxg.isn_backend.model.Saved;
//import com.jxg.isn_backend.service.SavedService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/saved")
//@CrossOrigin
//public class SavedController {
//
//    private final SavedService savedService;
//
//    public SavedController(SavedService savedService) {
//        this.savedService = savedService;
//    }
//
//    @PostMapping("/post/{postId}")
//    public ResponseEntity<?> savePost(@PathVariable Long postId) {
//        Saved saved = savedService.savePost(postId);
//        if (saved == null) {
//            return ResponseEntity.ok().body("Post unsaved.");
//        }
//        return ResponseEntity.ok().body(saved);
//    }
//
//    @PostMapping("/item/{itemId}")
//    public ResponseEntity<?> saveItem(@PathVariable Long itemId) {
//        Saved saved = savedService.saveItem(itemId);
//        if (saved == null) {
//            return ResponseEntity.ok().body("Item unsaved.");
//        }
//        return ResponseEntity.ok().body(saved);
//    }
//
//    @GetMapping("/posts")
//    public ResponseEntity<List<Post>> getSavedPostsForCurrentUser() {
//        List<Post> savedPosts = savedService.getSavedPostsForCurrentUser();
//        return ResponseEntity.ok(savedPosts);
//    }
//
//    @GetMapping("/items")
//    public ResponseEntity<List<Item>> getSavedItemsForCurrentUser() {
//        List<Item> savedItems = savedService.getSavedItemsForCurrentUser();
//        return ResponseEntity.ok(savedItems);
//    }
//}


package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.dto.response.SavedResponseDTO;
import athiq.veh.isn_backend.model.Saved;
import athiq.veh.isn_backend.service.SavedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved")
@CrossOrigin
public class SavedController {

    private final SavedService savedService;

    public SavedController(SavedService savedService) {
        this.savedService = savedService;
    }



    @PostMapping("/item/{itemId}")
    public ResponseEntity<?> saveItem(@PathVariable Long itemId) {
        Saved saved = savedService.saveItem(itemId);
        if (saved == null) {
            return ResponseEntity.ok().body("Item unsaved.");
        }
        return ResponseEntity.ok().body(saved);
    }



    @GetMapping("/items")
    public ResponseEntity<List<SavedResponseDTO>> getSavedItemsForCurrentUser() {
        List<SavedResponseDTO> savedItems = savedService.getSavedItemsForCurrentUser();
        return ResponseEntity.ok(savedItems);
    }
}
