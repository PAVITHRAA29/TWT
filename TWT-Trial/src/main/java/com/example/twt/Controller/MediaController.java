package com.example.twt.Controller;

import com.example.twt.DTO.MediaDTO;
import com.example.twt.Model.Media;
import com.example.twt.Repo.MediaRepo;
import com.example.twt.Service.MediaService;
import com.example.twt.api.MediaApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MediaController implements MediaApi {

    private final MediaService mediaService;

    private final MediaRepo mediaRepo;


    @Override
    public ResponseEntity<?> createMedia(@PathVariable("id") Integer id, @RequestPart MultipartFile file) throws IOException, ParseException {
        ResponseEntity<?> response = mediaService.createMedia(file, id);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @Override
    public ResponseEntity<List<Media>> getAllMediaById(@PathVariable Integer id){
        List<Media> mediaList = mediaService.getMediaByUser(id);
        return ResponseEntity.ok(mediaList);
    }

    @Override
    public ResponseEntity<?> deleteMedia(@PathVariable Integer id){
        return ResponseEntity.ok(mediaService.deleteMedia(id));
    }

}
