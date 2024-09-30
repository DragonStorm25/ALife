package src.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Profiler {
    public static final Profiler SINGLETON = new Profiler();

    private final Map<String, Long> startTimes;
    private final Map<String, Long> durations;
    private Map<String, Long> lastDurations;
    private TimerTask profilerTask = null;
    private Timer profilerTimer = null;
    public boolean print = false;
    public boolean running = false;

    private Profiler() {
        startTimes = new HashMap<>();
        durations = new HashMap<>();
        lastDurations = new HashMap<>();
    }

    public void startTimer(String s) {
        startTimes.put(s, System.nanoTime());
    }

    public void stopTimer(String s) {
        if (startTimes.containsKey(s)) {
            if (durations.containsKey(s)) 
                durations.put(s, durations.get(s) + System.nanoTime() - startTimes.get(s));
            else
                durations.put(s, System.nanoTime() - startTimes.get(s));
            startTimes.remove(s);
        }
    }

    public void reset() {
        for (String s : durations.keySet())
            lastDurations.put(s, durations.get(s));
        startTimes.clear();
        durations.clear();
    }

    public void startProfiling() {
        running = true;
        if (profilerTimer != null)
            profilerTimer.cancel();

        profilerTask = new TimerTask() {

            @Override
            public void run() {
                reset();
                if (print) {
                    for (String s : getProfile().keySet())
                        System.out.println(s + ": " + getProfile().get(s)/1e6);
                    System.out.println("------------");
                }
            }
        };

        profilerTimer = new Timer("RenderTimer");//create a new Timer
        profilerTimer.scheduleAtFixedRate(profilerTask, 0, 1000);
    }

    public Map<String, Long> getProfile() {
        return lastDurations;
    }
}
