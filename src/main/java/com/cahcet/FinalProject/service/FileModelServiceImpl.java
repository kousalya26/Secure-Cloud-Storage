package com.cahcet.FinalProject.service;

import com.cahcet.FinalProject.model.FileModel;
import com.cahcet.FinalProject.repository.FileRepository;
import com.cahcet.FinalProject.web.dto.FileModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileModelServiceImpl implements FileModelService{

    @Autowired
    private FileRepository fileRepository;

    public FileModelServiceImpl(FileRepository fileRepository) {
        super();
        this.fileRepository = fileRepository;
    }

    @Override
    public FileModel save(FileModelDto fileModelDto) {
        FileModel file = new FileModel(fileModelDto.getFileName(),fileModelDto.getPartName());

        return fileRepository.save(file);
    }
    public List<String> getAllFileNames() {
        return fileRepository.findAllFileNames();
    }
    @Override
    public List<String> getPartNameByFilename(String fileName){
        return fileRepository.findPartNamesByFileName(fileName);
    }
}
