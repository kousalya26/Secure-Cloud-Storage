package com.cahcet.FinalProject.model;

import javax.persistence.*;

@Entity
@Table(name = "file")
public class FileModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "part_name")
    private String partName;

    public FileModel(String fileName, String partName) {
        this.fileName = fileName;
        this.partName = partName;
    }

    public FileModel() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
