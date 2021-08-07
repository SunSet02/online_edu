package com.sunset.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadAly(MultipartFile file);

    void removeAlyVideos(List videoIdList);
}
