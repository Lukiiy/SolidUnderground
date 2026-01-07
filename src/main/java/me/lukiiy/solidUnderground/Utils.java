package me.lukiiy.solidUnderground;

import org.bukkit.Location;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    static Set<Location> getCircleEdge(Location center, int radius) {
        int rSq = (int) Math.pow(radius, 2);

        return IntStream.rangeClosed(-radius, radius).boxed()
                .flatMap(x -> IntStream.rangeClosed(-radius, radius)
                        .mapToObj(z -> new int[]{x, z}))
                .filter(offset -> Math.abs(Math.pow(offset[0], 2) + Math.pow(offset[1], 2) - rSq) <= 1)
                .map(offset -> center.clone().add(offset[0], 0, offset[1]))
                .collect(Collectors.toSet());
    }

}
