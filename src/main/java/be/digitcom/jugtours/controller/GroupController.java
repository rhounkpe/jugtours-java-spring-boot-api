package be.digitcom.jugtours.controller;

import be.digitcom.jugtours.model.Group;
import be.digitcom.jugtours.model.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GroupController {
    private final Logger logger = LoggerFactory.getLogger(GroupController.class);
    private GroupRepository groupRepository;

    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping("/groups")
    Collection<Group> groups() {
        return groupRepository.findAll();
    }

    @GetMapping("/group/{id}")
    ResponseEntity<?> getGroup(@PathVariable Long id) {
        Optional<Group> group = groupRepository.findById(id);

        return group.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/group")
    ResponseEntity<Group> createGroup(@Validated @RequestBody Group group) throws URISyntaxException {
        logger.info("Request to create group: {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.created(new URI("/api/group" + result.getId()))
                .body(result);
    }

    @PutMapping("/group/{id}")
    ResponseEntity<Group> updateGroup(@Validated @PathVariable Long id, @RequestBody Group group) {
        logger.info("Request to update group: {}", group);
        Optional<Group> optional = groupRepository.findById(id);
        if (optional.isPresent()) {
            Group g = optional.get();
            System.out.println("Group reçu: " + group);
          /*  g.setName(group.getName);
            g.setAddress(group.getAddress);
            g.setCity(group.getCity);
            g.setStateOrProvince(group.getStateOrProvince);
            g.setCountry(group.getCountry);
            g.setPostalCode(group.getPostalCode);*/
            Group result = groupRepository.save(group);
            return ResponseEntity.ok().body(result);
        }
        return null;
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        logger.info("Request to delete group: {}", id);
        groupRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
