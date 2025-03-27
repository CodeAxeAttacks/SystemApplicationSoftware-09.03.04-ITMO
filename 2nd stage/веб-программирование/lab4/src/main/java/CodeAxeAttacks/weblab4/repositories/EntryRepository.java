package CodeAxeAttacks.weblab4.repositories;

import CodeAxeAttacks.weblab4.entities.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByUsername(String username);

    @Transactional
    long deleteByUsername(String username);
}
