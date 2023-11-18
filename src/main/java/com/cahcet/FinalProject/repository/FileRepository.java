package com.cahcet.FinalProject.repository;

import com.cahcet.FinalProject.model.FileModel;
import com.cahcet.FinalProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<FileModel, Long> {
    FileModel findByFileName(String fileName);

    @Query("SELECT fileName FROM FileModel f")
    List<String> findAllFileNames();

    @Query("SELECT partName FROM FileModel f where fileName =:fname")
    List<String> findPartNamesByFileName(@Param("fname") String fileName);
}
