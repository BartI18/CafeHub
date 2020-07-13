package org.boka.cafe.Misc;

import org.boka.cafe.pojo.Cafe;
import org.boka.cafe.pojo.Coordinates;
import org.checkerframework.checker.nullness.Opt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Cache {

    private static final Map<Coordinates, Entry> CAFE_MAPS = new ConcurrentHashMap<>(128);
    private static Cache instance;

    private Cache() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (true) {
                CAFE_MAPS.entrySet().forEach(coordinatesEntry -> {
                    Entry entry = coordinatesEntry.getValue();
                    LocalDateTime lastTime = LocalDateTime.
                            ofInstant(Instant.ofEpochMilli(entry.getTimeCreate() + entry.getTimeLife()), TimeZone.getDefault().toZoneId());
                    if (LocalDateTime.now().isAfter(lastTime)) { //если дата уже прошла
                        CAFE_MAPS.remove(coordinatesEntry.getKey());
                        System.out.println(String.format("Удалил координаты: %s", coordinatesEntry.getKey()));
                    }
                });
                try {
                    Thread.sleep(60_000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
    }

    public boolean insertInCache(Coordinates coordinates, List<Cafe> cafeList, int radius, long timeLife) {
        Entry entry = new Entry(cafeList, radius, timeLife);
        System.out.println("new Obj");
        return CAFE_MAPS.put(coordinates, entry) != null;
    }

    public Entry getEntry(Coordinates coordinates) {
        return null;
    }

    public List<Cafe> getSimilarListCafe(Coordinates coordinates, int raduis) {
        List<Map.Entry<Coordinates, Entry>> entryList = CAFE_MAPS.entrySet().stream()
                .filter(coordinatesEntry -> Misc.isSimilarCoordinates(coordinates, coordinatesEntry.getKey()))
                .collect(Collectors.toList());
        if (!entryList.isEmpty()) { // ищу по указанному радиусу
            Optional<Map.Entry<Coordinates, Entry>> example = entryList.stream().filter(entry -> entry.getValue().radius == raduis).findFirst();
            if (example.isPresent()) {
                return example.get().getValue().getListCafe();
            }
        }
        return null;
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    public static class Entry {

        private List<Cafe> listCafe;
        private int radius;
        private long timeCreate;
        private long timeLife;

        public Entry(List<Cafe> listCafe, int radius, long timeLife) {
            this.listCafe = listCafe;
            this.radius = radius;
            this.timeLife = timeLife;
            this.timeCreate = System.currentTimeMillis();
        }

        public List<Cafe> getListCafe() {
            return listCafe;
        }

        protected void setListCafe(List<Cafe> listCafe) {
            this.listCafe = listCafe;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        protected long getTimeCreate() {
            return timeCreate;
        }

        protected void setTimeCreate(long timeCreate) {
            this.timeCreate = timeCreate;
        }

        protected long getTimeLife() {
            return timeLife;
        }

        protected void setTimeLife(long timeLife) {
            this.timeLife = timeLife;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return timeCreate == entry.timeCreate &&
                    timeLife == entry.timeLife &&
                    Objects.equals(listCafe, entry.listCafe);
        }

        @Override
        public int hashCode() {
            return Objects.hash(listCafe, timeCreate, timeLife);
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "listCafe=" + listCafe +
                    ", timeCreate=" + timeCreate +
                    ", timeLife=" + timeLife +
                    '}';
        }
    }
}
