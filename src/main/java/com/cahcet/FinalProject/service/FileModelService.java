package com.cahcet.FinalProject.service;

import com.cahcet.FinalProject.model.FileModel;
import com.cahcet.FinalProject.model.User;
import com.cahcet.FinalProject.web.dto.FileModelDto;
import com.cahcet.FinalProject.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FileModelService {
    FileModel save(FileModelDto fileModelDto);
    List<String> getAllFileNames();
    List<String> getPartNameByFilename(String filename);
}
