package com.example.backend.repository;

import com.example.backend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByUserId(Long userId);
    Optional<Photo> findByPhotoUrlAndUser_Id(String photoUrl, Long userId);

    Optional<Photo> findByPhotoUrl(String photoUrl);
}
