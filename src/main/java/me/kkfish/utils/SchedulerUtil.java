package me.kkfish.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SchedulerUtil {
    
    public static org.bukkit.scheduler.BukkitTask runSync(JavaPlugin plugin, Runnable task) {
        try {
            return Bukkit.getScheduler().runTask(plugin, task);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static org.bukkit.scheduler.BukkitTask runSyncDelayed(JavaPlugin plugin, Runnable task, long delayTicks) {
        try {
            return Bukkit.getScheduler().runTaskLater(plugin, task, Math.max(1, delayTicks));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static org.bukkit.scheduler.BukkitTask runSyncTimer(JavaPlugin plugin, Runnable task, long delayTicks, long periodTicks) {
        try {
            return Bukkit.getScheduler().runTaskTimer(plugin, task, Math.max(1, delayTicks), periodTicks);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static org.bukkit.scheduler.BukkitTask runAsync(JavaPlugin plugin, Runnable task) {
        try {
            return Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        } catch (Exception e) {
            e.printStackTrace();
            return runSync(plugin, task);
        }
    }
    
    public static org.bukkit.scheduler.BukkitTask runAsyncDelayed(JavaPlugin plugin, Runnable task, long delayTicks) {
        try {
            return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, Math.max(1, delayTicks));
        } catch (Exception e) {
            e.printStackTrace();
            return runSyncDelayed(plugin, task, delayTicks);
        }
    }
    
    public static org.bukkit.scheduler.BukkitTask runAsyncTimer(JavaPlugin plugin, Runnable task, long delayTicks, long periodTicks) {
        try {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, Math.max(1, delayTicks), periodTicks);
        } catch (Exception e) {
            e.printStackTrace();
            return runSyncTimer(plugin, task, delayTicks, periodTicks);
        }
    }
    
    public static <T> CompletableFuture<T> supplyAsync(JavaPlugin plugin, Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        
        runAsync(plugin, () -> {
            try {
                T result = callable.call();
                runSync(plugin, () -> future.complete(result));
            } catch (Exception e) {
                runSync(plugin, () -> future.completeExceptionally(e));
            }
        });
        
        return future;
    }
    
    public static <T> void supplyAsyncThenConsume(JavaPlugin plugin, Callable<T> callable, Consumer<T> consumer) {
        supplyAsync(plugin, callable).thenAcceptAsync(result -> {
            runSync(plugin, () -> consumer.accept(result));
        });
    }
    
    public static org.bukkit.scheduler.BukkitTask scheduleTask(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        if (period > 0) {
            return (org.bukkit.scheduler.BukkitTask) runSyncTimer(plugin, runnable, delay, period);
        } else if (delay > 0) {
            return (org.bukkit.scheduler.BukkitTask) runSyncDelayed(plugin, runnable, delay);
        } else {
            return (org.bukkit.scheduler.BukkitTask) runSync(plugin, runnable);
        }
    }
    
    public static void cancelTask(org.bukkit.scheduler.BukkitTask task) {
        if (task != null) {
            task.cancel();
        }
    }
}