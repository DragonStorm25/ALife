package src.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Profiler {
    public static final Profiler SINGLETON = new Profiler();

    private final Map<String, Long> startTimes;
    private final Map<String, Long> durations;
    private final Map<String, Long> totalDurations;
    private Map<String, Long> lastDurations;
    private TimerTask profilerTask = null;
    private Timer profilerTimer = null;
    private int cyclesRun = 0;
    public final int tickInSeconds = 1;
    public boolean print = false;
    public boolean running = false;

    private Profiler() {
        startTimes = new HashMap<>();
        durations = new HashMap<>();
        totalDurations = new HashMap<>();
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
        for (String s : durations.keySet()) {
            lastDurations.put(s, durations.get(s));
            if (totalDurations.containsKey(s))
                totalDurations.put(s, totalDurations.get(s) + durations.get(s));
            else
                totalDurations.put(s, durations.get(s));
        }
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
                    int longestLength = 0;
                    for (String s : getProfile().keySet()) {
                        if (s.length() > longestLength)
                            longestLength = s.length();
                    }
                    System.out.println("Profile for cycle " + cyclesRun + ":");
                    System.out.println("-".repeat(longestLength + 4));
                    for (String s : getProfile().keySet()) {
                        System.out.println(s + ":" + " ".repeat(longestLength - s.length() + 4) + getProfile().get(s)/1e6);
                    }
                    System.out.println("-".repeat(longestLength + 4));
                }
                cyclesRun++;
            }
        };

        profilerTimer = new Timer("RenderTimer");//create a new Timer
        profilerTimer.scheduleAtFixedRate(profilerTask, 0, tickInSeconds * 1000);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Average Profiler Results: ");
                int longestLength = 0;
                for (String s : totalDurations.keySet()) {
                    if (s.length() > longestLength)
                        longestLength = s.length();
                }
                System.out.println("-".repeat(longestLength + 4));
                for (String s : totalDurations.keySet()) {
                    System.out.println(s + ":" + " ".repeat(longestLength - s.length() + 4) + totalDurations.get(s)/1e6/cyclesRun);
                }
            }
        });
    }

    public Map<String, Long> getProfile() {
        return lastDurations;
    }
}
