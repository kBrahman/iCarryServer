package zig.i.carry.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zig.i.carry.model.Ad;

public interface AdRepo extends JpaRepository<Ad, Long> {
}
