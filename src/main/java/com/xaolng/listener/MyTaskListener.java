package com.xaolng.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author xaolng
 * @date: 2022/9/7 21:30
 * @description: 监听器
 */

public class MyTaskListener implements TaskListener {

    /**
     * 指定负责人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        //判断当前的任务是创建申请并且是 create 事件
        if("创建申请".equals(delegateTask.getName())
        && "create".equals(delegateTask.getEventName())){
            delegateTask.setAssignee("张三");
        }
    }
}
