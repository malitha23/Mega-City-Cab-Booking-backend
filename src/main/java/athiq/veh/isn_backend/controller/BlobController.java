//package com.jxg.isn_backend.controller;
//
//import com.jxg.isn_backend.service.BlobService;
//import com.jxg.isn_backend.util.MediaTypeUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@RestController
//@RequestMapping("/api/blobs")
//@CrossOrigin
//public class BlobController {
//
//    @Value("${spring.app.localBlobDirectory}")
//    private String FILE_DIRECTORY;
//
//    private final BlobService blobService;
//
//    public BlobController(BlobService blobService) {
//        this.blobService = blobService;
//    }
//
//    @GetMapping("fetch/{uuid}")
//    public ResponseEntity<InputStreamResource> getBlob(@PathVariable String uuid) throws IOException {
//
//        MediaType contentType = MediaTypeUtil.getMediaType(uuid);
//
//        InputStream in = new ByteArrayInputStream(blobService.getBlobFromLocal(FILE_DIRECTORY, uuid));
//        return ResponseEntity.ok()
//                .contentType(contentType)
//                .body(new InputStreamResource(in));
//    }
//}

package athiq.veh.isn_backend.controller;

import athiq.veh.isn_backend.service.BlobService;
import athiq.veh.isn_backend.util.MediaTypeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/blobs")
@CrossOrigin
public class BlobController {

    @Value("${spring.app.localBlobDirectory}")
    private String FILE_DIRECTORY;

    private final BlobService blobService;

    public BlobController(BlobService blobService) {
        this.blobService = blobService;
    }

    @GetMapping("fetch/{uuid}")
    public ResponseEntity<InputStreamResource> getBlob(@PathVariable String uuid) throws IOException {

        MediaType contentType = MediaTypeUtil.getMediaType(uuid);

        InputStream in = new ByteArrayInputStream(blobService.getBlobFromLocal(FILE_DIRECTORY, uuid));
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }
}
