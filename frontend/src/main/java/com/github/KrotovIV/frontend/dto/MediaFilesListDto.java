package com.github.KrotovIV.frontend.dto;

import java.util.List;

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