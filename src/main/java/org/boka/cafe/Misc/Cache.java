package org.boka.cafe.Misc;

import org.boka.cafe.pojo.Cafe;
import org.boka.cafe.pojo.Coordinates;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Cache {

    private static final Map<Object, Entry> OBJECTS_MAPS = new ConcurrentHashMap<>(128);
    private static Cache instance;

    private Cache() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (true) {
                OBJECTS_MAPS.entrySet().forEach(innerEntry -> {
                    Entry entry = innerEntry.getValue();
                    LocalDateTime lastTime = LocalDateTime.
                            ofInstant(Instant.ofEpochMilli(entry.getTimeCreate() + entry.getTimeLife()), TimeZone.getDefault().toZoneId());
                    if (LocalDateTime.now().isAfter(lastTime)) { //если дата уже прошла
                        OBJECTS_MAPS.remove(innerEntry.getKey());
                        System.out.println(String.format("Удалил координаты: %s", innerEntry.getKey()));
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

    public boolean insertInCache(Object key, List data, int radius, long timeLife) {
        Entry entry = new Entry(data, radius, timeLife);
        System.out.println("new Obj");
        return OBJECTS_MAPS.put(key, entry) != null;
    }

    public Entry getEntry(Object key) {
        return OBJECTS_MAPS.get(key);
    }

    public List<Cafe> getSimilarListCafe(Coordinates coordinates, int raduis) {
        List<Map.Entry<Object, Entry>> entryList = OBJECTS_MAPS.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Coordinates && !entry.getValue().listObjects.isEmpty() && entry.getValue().listObjects.get(0) instanceof Cafe)
                .filter(coordinatesEntry -> Misc.isSimilarCoordinates(coordinates, (Coordinates) coordinatesEntry.getKey()))
                .collect(Collectors.toList());
        if (!entryList.isEmpty()) { // ищу по указанному радиусу
            Optional<Map.Entry<Object, Entry>> example = entryList.stream().filter(entry -> entry.getValue().radius == raduis).findFirst();
            if (example.isPresent()) {
                return example.get().getValue().getListObjects();
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

        private List listObjects;
        private int radius;
        private long timeCreate;
        private long timeLife;

        public Entry(List listObjects, int radius, long timeLife) {
            this.listObjects = listObjects;
            this.radius = radius;
            this.timeLife = timeLife;
            this.timeCreate = System.currentTimeMillis();
        }

        public List getListObjects() {
            return listObjects;
        }

        protected void setListCafe(List listCafe) {
            this.listObjects = listCafe;
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
                    Objects.equals(listObjects, entry.listObjects);
        }

        @Override
        public int hashCode() {
            return Objects.hash(listObjects, timeCreate, timeLife);
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "listCafe=" + listObjects +
                    ", timeCreate=" + timeCreate +
                    ", timeLife=" + timeLife +
                    '}';
        }
    }
}
