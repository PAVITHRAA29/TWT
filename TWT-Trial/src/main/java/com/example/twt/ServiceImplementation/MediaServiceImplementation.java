package com.example.twt.ServiceImplementation;

import com.example.twt.DTO.MediaDTO;
import com.example.twt.Model.Media;
import com.example.twt.Model.twtUser;
import com.example.twt.Repo.MediaRepo;
import com.example.twt.Repo.twtUserRepository;
import com.example.twt.Service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImplementation implements MediaService {

    @Autowired
    private MediaRepo mediaRepo;

    @Autowired
    private twtUserRepository twtUserRepository;

    String IMG_FOLDER_PATH = "/home/divum/Downloads/TWT/src/main/resources/static/";
    String VID_FOLDER_PATH = "/home/divum/Downloads/TWT/src/main/resources/static/";

    @Override
    public ResponseEntity<?> createMedia(MultipartFile file, Integer id) throws IOException{
        Media media = new Media();

        media.setMediaType(file.getContentType());
        media.setUploadedTime(Timestamp.valueOf(LocalDateTime.now()));
        if(file != null && !file.isEmpty()){
            String filepath;
            if (file.getContentType().startsWith("image/")) {
                filepath = IMG_FOLDER_PATH + file.getOriginalFilename();
            }
            else{
                filepath = VID_FOLDER_PATH + file.getOriginalFilename();
            }
            file.transferTo(new File(filepath));
            media.setMediaPath(filepath);
        }
        if (id != null) {
            Optional<twtUser> twtUserOptional = twtUserRepository.findById(id);
            if (twtUserOptional.isPresent()) {
                media.setTwtUser(twtUserOptional.get());
            } else {
                throw new RuntimeException("User not found");
            }
        }

        Media savedMedia = mediaRepo.save(media);
        return ResponseEntity.ok(savedMedia);
    }

    @Override
    public List<Media> getMediaByUser(Integer id){
        Optional<twtUser> twtUserOptional = twtUserRepository.findById(id);

        if(twtUserOptional.isPresent()){
            twtUser user = twtUserOptional.get();
            return mediaRepo.findByTwtUser(user);
        }
        else{
            throw new RuntimeException("User Not Found");
        }
    }

    @Override
    public ResponseEntity<?> deleteMedia(Integer id){
        if (mediaRepo.existsById(id)) {
            mediaRepo.deleteById(id);
            return ResponseEntity.ok("Media deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Media not found");
        }
    }

}
