package com.wjy.oauth2.repo;

import com.wjy.oauth2.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User queryByUserName(String userName);
}
