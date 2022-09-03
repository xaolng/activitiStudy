package com.xaolng;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * @author xaolng
 */
public class TestCreate {

    /**
     * 使用 activiti 提供的默认方式创建 mysql 表
     */
    @Test
    public void testCreateDbTable(){
        //需要使用 activiti 提供的工具类 ProcessEngines,使用方法 getDefaultProcessEngine
        //getDefaultProcessEngine 会默认从 resource 下读取名字为 activiti.cfg.xml 的文件
        //创建 processEngines 时，就会创建 mysql 的表
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }
}