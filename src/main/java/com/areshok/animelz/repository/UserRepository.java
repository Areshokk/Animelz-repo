package com.areshok.animelz.repository;

import com.areshok.animelz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     */
    User findByUsername(String username);

    /**
     * Find by activation code user.
     *
     * @param code the code
     * @return the user
     * " I won't lose too much, just myself.
     * There are always other things that are more important than others "
     * - The Fool
     */
    User findByActivationCode(String code);

    @Modifying
    @Transactional
    @Query("update User user set user.userImage = ?1 where user.id = ?2")
    void updateUserImage(String userImage, Long id);

    Optional<User> findById(Long id);
}
