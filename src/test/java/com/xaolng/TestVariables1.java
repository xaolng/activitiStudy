package com.xaolng;

import com.xaolng.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xaolng
 * @date: 2022/9/8 15:00
 * @description:
 */
public class TestVariables1 {

    /**
     * 部署流程
     */
    @Test
    public void testDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-variables")
                .addClasspathResource("bpmn/evection-global.bpmn")
                .deploy();
        System.out.println("流程部署id: " + deploy.getId());
        System.out.println("流程部署名字：" + deploy.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcessInstance(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String key = "myEvectionGlobal";
        //设置流程变量
        //流程变量的 Map
        Map<String,Object> variablesMap = new HashMap<>();
        Evection evection = new Evection();
        evection.setNum(3d);
        variablesMap.put("evection",evection);

        //设定任务的负责人
        variablesMap.put("assignee0","李四");
        variablesMap.put("assignee1","李经理");
        variablesMap.put("assignee2","王总经理");
        variablesMap.put("assignee3","赵财务");


        //启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(key,variablesMap);

        System.out.println("流程定义Id: " +instance.getProcessDefinitionId());
        System.out.println("流程实例ID: " +instance.getId());
        System.out.println("当前活动id: " +instance.getActivityId());

    }

    /**
     * 完成个人任务
     */

    @Test
    public void completeTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        String key = "myEvectionGlobal";

        //任务负责人
//        String assignee = "李四";
        String assignee = "李经理";
//        String assignee = "赵财务";

        //创建查询任务

        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();

        if (task != null){
            //完成任务
            taskService.complete(task.getId());
        }

    }

}
