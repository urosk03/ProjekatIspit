package com.example.coworkinghub.service;

import com.example.coworkinghub.model.Room;
import com.example.coworkinghub.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    private final RoomRepository repo;

    public RoomService(RoomRepository repo) { this.repo = repo; }

    public Room create(Room r) { return repo.save(r); }
    public Room update(Long id, Room req) {
        Room r = repo.findById(id).orElseThrow();
        r.setName(req.getName());
        r.setDescription(req.getDescription());
        r.setCapacity(req.getCapacity());
        r.setWorkingHours(req.getWorkingHours());
        return repo.save(r);
    }
    public void delete(Long id) { repo.deleteById(id); }

    public List<Room> search(String q) {
        if (q == null || q.isBlank()) return repo.findAll();
        return repo.findByNameContainingIgnoreCase(q);
    }

    public List<Room> sortBy(List<Room> rooms, String sortKey, Map<Long, Long> popularity) {
        if ("capacity".equalsIgnoreCase(sortKey)) {
            rooms.sort(Comparator.comparingInt(Room::getCapacity).reversed());
        } else if ("popularity".equalsIgnoreCase(sortKey)) {
            rooms.sort((a,b) -> Long.compare(
                popularity.getOrDefault(b.getId(), 0L),
                popularity.getOrDefault(a.getId(), 0L)
            ));
        }
        return rooms;
    }
}
