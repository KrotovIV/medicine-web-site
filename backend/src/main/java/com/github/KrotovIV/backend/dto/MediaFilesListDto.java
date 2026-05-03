package com.github.KrotovIV.backend.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MediaFilesListDto(
    List<MediaFileInfo> videosNameslist,
    List<MediaFileInfo> photosNamesList,
    List<MediaFileInfo> audiosNamesList
) {
    public record MediaFileInfo(
        String name,
        boolean streamInsteadOfDownload
    ) {}
}