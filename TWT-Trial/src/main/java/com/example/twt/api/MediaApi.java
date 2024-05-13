package com.example.twt.api;

import com.example.twt.DTO.MediaDTO;
import com.example.twt.Model.Media;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequestMapping("/v1/api/media/")
public interface MediaApi {

    @PostMapping("createmedia/{id}")
    public ResponseEntity<?> createMedia(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file) throws IOException, ParseException;

    @GetMapping("getallmedia/{id}")
    public ResponseEntity<List<Media>> getAllMediaById(@PathVariable Integer id);

    @DeleteMapping("deletemedia/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable Integer id);

}
