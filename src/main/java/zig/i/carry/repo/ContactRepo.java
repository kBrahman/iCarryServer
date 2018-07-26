package zig.i.carry.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zig.i.carry.model.Contact;

public interface ContactRepo extends JpaRepository<Contact, Long> {
}
