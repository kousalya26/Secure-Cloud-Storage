package com.cahcet.FinalProject.web.dao;

import com.cahcet.FinalProject.model.User;

public interface UserDAO {
    User findByUsername(String username);
}

