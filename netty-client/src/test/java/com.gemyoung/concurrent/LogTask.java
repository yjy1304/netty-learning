package com.gemyoung.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author weilong
 * @date 2017/12/15 下午4:06.
 */
@Slf4j
public class LogTask implements Runnable{
    private Integer id;
    LogTask(Integer id){
        this.id = id;
    }
    @Override
    public void run() {
        log.info("任务id={}执行完成", id);
    }
}
