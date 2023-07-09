package com.example.mediasoftjwt.mysql.interfaces;

import com.example.mediasoftjwt.mysql.tables.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    ArrayList<UserInfo> findByLogin(final String login);
    ArrayList<UserInfo> findByEmail(final String email);

    boolean existsByLogin(final String login);
    boolean existsByEmail(final String email);
}
