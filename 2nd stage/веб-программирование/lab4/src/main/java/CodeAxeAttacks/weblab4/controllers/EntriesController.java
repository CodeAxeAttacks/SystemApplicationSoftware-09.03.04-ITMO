package CodeAxeAttacks.weblab4.controllers;

import CodeAxeAttacks.weblab4.entities.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import CodeAxeAttacks.weblab4.DTO.EntryDTO;
import CodeAxeAttacks.weblab4.repositories.EntryRepository;
import java.util.List;


@RestController
@RequestMapping("/web-lab4/api/entries")
public class EntriesController {

    private final EntryRepository entryRepository;

    public EntriesController(EntryRepository entryRepository) {

        this.entryRepository = entryRepository;
    }

    @GetMapping
    ResponseEntity<List<Entry>> getUserEntries(@RequestParam String username) {
        return ResponseEntity.ok(entryRepository.findByUsername(username));
    }

    @PostMapping
    ResponseEntity<Entry> addEntry(@RequestBody EntryDTO entryDTO){
        return ResponseEntity.ok(entryRepository.save(new Entry(
                entryDTO.getX(),
                entryDTO.getY(),
                entryDTO.getR(),
                entryDTO.getUsername()
                )));
    }

    @DeleteMapping
    ResponseEntity<Long> deleteUserEntries(@RequestParam String username) {

        return ResponseEntity.ok(entryRepository.deleteByUsername(username));
    }
}
