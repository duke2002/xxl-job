package com.xxl.job.core.enums;

/**
 * 阻塞处理策略
 *
 * 为了解决执行线程因并发问题、执行效率慢、任务多等原因而做的一种线程处理机制，主要包括 串行、丢弃后续调度、覆盖之前调度，一般常用策略是串行机制
 * Created by xuxueli on 17/5/9.
 */
public enum ExecutorBlockStrategyEnum {
    // 串行：对当前线程不做任何处理，并在当前线程的队列里增加一个执行任务
    SERIAL_EXECUTION("Serial execution"),
    /*CONCURRENT_EXECUTION("并行"),*/
    // 丢弃后续调度：如果当前线程阻塞，后续任务不再执行，直接返回失败
    DISCARD_LATER("Discard Later"),
    // 覆盖之前调度：创建一个移除原因，新建一个线程去执行后续任务
    COVER_EARLY("Cover Early");

    private String title;
    private ExecutorBlockStrategyEnum (String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public static ExecutorBlockStrategyEnum match(String name, ExecutorBlockStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}
