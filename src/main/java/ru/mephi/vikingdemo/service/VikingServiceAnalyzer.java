package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.*;

@Service
public class VikingServiceAnalyzer {

    private final VikingStorage vikingStorage;
    private final Random random = new Random();

    public VikingServiceAnalyzer(VikingStorage vikingStorage) {
        this.vikingStorage = vikingStorage;
    }

    public long countVikingsOlderThan(int age) {
        return vikingStorage.findAll().stream().filter(v -> v.age() > age).count();
    }

    public long countVikingsYoungerThan(int age) {
        return vikingStorage.findAll().stream().filter(v -> v.age() < age).count();
    }

    public long countVikingsAgeBetween(int minAge, int maxAge) {
        return vikingStorage.findAll().stream().filter(v -> v.age() >= minAge && v.age() <= maxAge).count();
    }

    public long countVikingsAgeOutside(int minAge, int maxAge) {
        return vikingStorage.findAll().stream().filter(v -> v.age() < minAge || v.age() > maxAge).count();
    }

    public long countByBeardAndHair(BeardStyle beard, HairColor hair) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.beardStyle() == beard && v.hairColor() == hair)
                .count();
    }

    public long countByAxeQuantity(int expectedAxesCount) {
        return vikingStorage.findAll().stream()
                .filter(v -> {
                    long axes = v.equipment().stream()
                            .filter(eq -> eq.name().toLowerCase().contains("axe") || eq.name().toLowerCase().contains("топор"))
                            .count();
                    return axes == expectedAxesCount;
                })
                .count();
    }

    public Optional<Viking> getRandomTallViking() {
        List<Viking> tallVikings = vikingStorage.findAll().stream()
                .filter(v -> v.heightCm() > 180)
                .toList();

        if (tallVikings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tallVikings.get(random.nextInt(tallVikings.size())));
    }

    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(eq -> "Legendary".equalsIgnoreCase(eq.quality())))
                .toList();
    }

    public List<Viking> getRedHairedVikingsSortedByAge() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Blond)
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }

    public Optional<Integer> getMaxId() {
        return vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(Objects::nonNull)
                .max(Integer::compareTo);
    }

    public List<Integer> getEvenIds() {
        return vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(id -> id % 2 == 0)
                .toList();
    }
}