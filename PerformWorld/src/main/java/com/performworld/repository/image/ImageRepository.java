package com.performworld.repository.image;

import com.performworld.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByEventEventIdAndIsThumbnailTrue(Long eventId);
    List<Image> findByEventEventId(Long eventId);
    List<Image> findByEventEventIdAndIsThumbnailFalse(Long eventId);
}
