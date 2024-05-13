package com.example.twt.Service;

import com.example.twt.DTO.MediaDTO;
import com.example.twt.Model.Media;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public interface MediaService {

    public ResponseEntity<?> createMedia(MultipartFile file, Integer id) throws IOException, ParseException;

    public List<Media> getMediaByUser(Integer id);

    public ResponseEntity<?> deleteMedia(Integer id);

}
