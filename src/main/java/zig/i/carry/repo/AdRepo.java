package zig.i.carry.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import zig.i.carry.model.Ad;

import java.util.List;

@NoRepositoryBean
public interface AdRepo extends JpaRepository<Ad, Long> {
    List<Ad> getAdByUserLogin(String login);
}
