package com.cahcet.FinalProject.web.dto;

public class FileModelDto {
        private String fileName;
        private String partName;

    public FileModelDto(String fileName, String partName) {
        this.fileName = fileName;
        this.partName = partName;
    }

    public FileModelDto() {

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


