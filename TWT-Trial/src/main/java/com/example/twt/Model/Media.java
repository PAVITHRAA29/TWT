package com.example.twt.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String mediaPath;

    private String mediaType;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Timestamp uploadedTime;

    @ManyToOne
    private twtUser twtUser;



}
