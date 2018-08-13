package zig.i.carry.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zig.i.carry.model.Ad;

import java.util.List;

public interface AdRepo extends JpaRepository<Ad, Long> {
    List<Ad> findByUserLogin(String login);

}
