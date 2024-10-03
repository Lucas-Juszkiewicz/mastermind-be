package com.lucas.mastermind.repository;

import com.lucas.mastermind.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Override
    List<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findByNick(String nick);

    boolean existsByEmail(String email);
    boolean existsByNick(String nick);

    @Query(value = "SELECT * FROM users u WHERE u.number_of_games > 0 ORDER BY (u.total / u.number_of_games) DESC LIMIT 3", nativeQuery = true)
    List<User> findTop3UsersByTotalPerGameRatioNative();

}
