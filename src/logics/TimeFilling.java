package logics;

import data.Epic;
import data.Subtask;
import data.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TimeFilling {
    protected final HashMap<LocalDateTime, Boolean> timeOverlay = new HashMap<>();

    protected Epic epicTimeCalculation(Epic epic, HashMap<Integer, Subtask> subtasks) {
        LocalDateTime min = LocalDateTime.MAX;
        LocalDateTime max = LocalDateTime.MIN;
        Duration durationEpic = Duration.ofMinutes(0);
        for (int includeId : epic.getSubtaskIdList()) {
            Subtask findTime = subtasks.get(includeId);
            if (findTime.getStartTime() != null) {
                LocalDateTime currentStart = findTime.getStartTime();
                LocalDateTime currentEnd = findTime.getEndTime();
                if (currentStart.isBefore(min)) {
                    min = currentStart;
                    epic.setStartTime(min);
                }
                if (currentEnd.isAfter(max)) {
                    max = currentEnd;
                    epic.setEndTime(max);
                }
                durationEpic = durationEpic.plus(findTime.getDurationWork());
            }
        }
        epic.setDurationWork(durationEpic);
        return epic;
    }

    protected void fillTimeOverlay(Task task) {
        if (task.getStartTime() != null) {
            LocalDateTime start = task.getStartTime();
            LocalDateTime end = task.getEndTime();
            while (start.isBefore(end)) {
                timeOverlay.put(start, false);
                start = start.plusMinutes(15);
            }
        }
    }

    protected boolean checkTimeOverlay(Task task) { //возвращает false при пересечении
        if (task.getStartTime() != null) {
            LocalDateTime start = task.getStartTime();
            LocalDateTime end = task.getEndTime();
            while (start.isBefore(end)) {
                if (timeOverlay.containsKey(start)) {
                    return false;
                }
                start = start.plusMinutes(15);
            }
        }
        return true;
    }

    protected void removeTimeOverlay(Task task) {
        if (task.getStartTime() != null) {
            LocalDateTime start = task.getStartTime();
            LocalDateTime end = task.getEndTime();
            while (end.isAfter(start)) {
                timeOverlay.remove(start);
                start = start.plusMinutes(15); // Ограничение при создании, при указании времени, минуты кратны 15
            }
        }
    }
}