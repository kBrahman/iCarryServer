package zig.i.carry.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zig.i.carry.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
