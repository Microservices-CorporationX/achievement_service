package faang.school.achievement.service;

import java.util.List;

public interface Cache<T> {
    T get(String title);
    List<T> getAll();
}