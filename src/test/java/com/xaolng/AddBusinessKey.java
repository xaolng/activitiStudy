package com.xaolng;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * @author xaolng
 */
public class AddBusinessKey {

    /**
     * 添加 businesskey 到 Activiti 表
     */
    @Test
    public void addBusinessKey(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //启动流程的过程中，添加 businessKey
        //第一个参数：流程定义的key
        //第二个参数：businessKey,存出差申请单的 id，id 为：1001
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        //输出
        System.out.println(instance.getBusinessKey());
    }

    /**
     * 全部流程实例的挂起和激活
     */
    @Test
    public void suspendAllProcessInstance(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //查询流程定义，获取流程定义的查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        //获取当前流程定义的实例是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        //获取流程定义 id
        String processDefinitionId = processDefinition.getId();
        //如果是挂起，可以执行激活的操作
        if (suspended){
            //如果是挂起，执行激活操作
            //参数1：流程定义 id 参数2：是否激活 参数3：激活时间
            repositoryService.activateProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义id:" +processDefinitionId + " 已激活");
        }else {
            //如果是激活状态，改为挂起
            //参数1：流程定义 id 参数2：是否暂停 参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义id:" +processDefinitionId + " 已挂起");

        }

    }

    /**
     * 挂起或激活当流程实例
     * 通过流程实例
     */

    @Test
    public void susppendSingleProcessInstance(){
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("30001")
                .singleResult();
        //得到当前流程实例的暂停状态，true-已暂停 false-激活
        boolean suspended = processInstance.isSuspended();
        //获取流程实例id
        String processInstanceId = processInstance.getId();
        //判断是否已经暂停，如果已经暂停，就执行激活操作
        if (suspended){
            //如果暂停，就执行激活
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例id: " + processInstanceId + " 激活");
        }else {
            //如果激活，就执行挂起
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例id: " + processInstanceId + " 挂起");
        }

    }

    /**
     * 测试流程被挂起后
     *完成个人任务
     * result:
     *        org.activiti.engine.ActivitiException: Cannot complete a suspended task
     * 流程被挂起后无法继续执行
     */
    @Test
    public void complete(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 TaskService
        TaskService taskService = processEngine.getTaskService();
        //创建查询任务 通过流程实例 id 和任务负责人查询
        Task task = taskService.createTaskQuery()
                .processInstanceId("30001")
                .taskAssignee("张三")
                .singleResult();
        //获取任务 id
        String taskId = task.getId();
        //输出
        System.out.println("任务id：" +taskId);
        System.out.println("流程实例id：" +task.getProcessInstanceId());
        System.out.println("任务名称：" +task.getName());
        System.out.println("任务负责人：" +task.getAssignee());
        //完成任务
        taskService.complete(taskId);

    }
}
