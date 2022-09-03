package com.xaolng;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author xaolng
 */
public class ActivitiDemo {

    @Test
    public void deployProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn20.xml")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        System.out.println("流程部署id: " +deploy.getId());//流程部署id: 7501

        System.out.println("流程部署名称: " +deploy.getName()); //流程部署名称: 出差申请流程
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
        //1.创建 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取 RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3.根据流程定义的 id 启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        //4.输出内容
        System.out.println("流程定义Id: " +instance.getProcessDefinitionId());
        System.out.println("流程实例ID: " +instance.getId());
        System.out.println("当前活动id: " +instance.getActivityId());

    }
    /**
     * 查询个人待执行的业务
     */
    @Test
    public void findPersonTaskList(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 taskService
        TaskService taskService = processEngine.getTaskService();
        //根据流程 key 和任务负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") //流程 key
                .taskAssignee("张三")  //流程负责人
                .list();
        //输出
        for (Task task : taskList) {
            System.out.println("流程实例id：" +task.getProcessInstanceId());
            System.out.println("任务Id: " +task.getId());
            System.out.println("任务负责人：" +task.getAssignee());
            System.out.println("任务名称：" +task.getName());
        }
    }

    @Test
    public void completTask(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 seervice
        TaskService taskService = processEngine.getTaskService();
        //根据任务 id 完成任务
//        taskService.complete("2505");
        //获取 jerry 对应的任务,并完成
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection")
//                .taskAssignee("jerry")
//                .singleResult();
//        System.out.println("流程实例id：" +task.getProcessInstanceId());
//        System.out.println("任务Id: " +task.getId());
//        System.out.println("任务负责人：" +task.getAssignee());
//        System.out.println("任务名称：" +task.getName());
//        taskService.complete(task.getId());
        //获取 jack 的任务,并完成
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection")
//                .taskAssignee("jack")
//                .singleResult();
//        System.out.println("流程实例id：" +task.getProcessInstanceId());
//        System.out.println("任务Id: " +task.getId());
//        System.out.println("任务负责人：" +task.getAssignee());
//        System.out.println("任务名称：" +task.getName());
//        taskService.complete(task.getId());
        //获取 xaolng 任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("xaolng")
                .singleResult();
        System.out.println("流程实例id：" +task.getProcessInstanceId());
        System.out.println("任务Id: " +task.getId());
        System.out.println("任务负责人：" +task.getAssignee());
        System.out.println("任务名称：" +task.getName());
        taskService.complete(task.getId());
    }

    @Test
    public void deployProcessByZip(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //流程部署
        //读取资源包文件，构建成 InputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.bpmn20.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());

    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //获取 ProcessDifinitionQuery 对象
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        //查询当前所有的流程定义
        //根据流程定义的 Key
        List<ProcessDefinition> definitionList = definitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc() //倒叙
                .list(); //查询出所有内容
        //输出信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义ID：" +processDefinition.getId());
            System.out.println("流程定义名称：" +processDefinition.getName());
            System.out.println("流程定义的Key：" +processDefinition.getKey());
            System.out.println("流程定义的版本：" +processDefinition.getVersion());
            System.out.println("流程部署Id：" +processDefinition.getDeploymentId());
        }
    }

    /**
     * 删除流程部署信息
     */

    @Test
    public void deleteDeployMent(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //通过部署 id 来删除流程部署信息
        String deployment = "2501";
        repositoryService.deleteDeployment(deployment,true);
    }

    /**
     * 资源文件下载
     */
    @Test
    public void getDeployment() throws IOException {
        //1.获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取 api,RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.获取查询对象 ProcessDefinitionQuery ,查询流程定义信息
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        //4.通过流程定义信息，获取部署 Id
        ProcessDefinition myEvection = definitionQuery.processDefinitionKey("myEvection")
                .singleResult();
        String deploymentId = myEvection.getDeploymentId();
        String diagramResourceName = myEvection.getDiagramResourceName();
        String resourceName = myEvection.getResourceName();
        System.out.println(deploymentId);

        //5.通过 RepositoryService ,传递部署 id 参数，读取资源信息 （png and bpmn）
        //5.1 获取 png 图片的流
        //通过部署 id 和文件名字来获取图片的资源
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
        //5.2 获取 bpmn 的流
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, resourceName);
        //6.构造 OutputStream 流

        File file = new File("d:/xaolngtest/evecionflow01.png");
        File file2 = new File("d:/xaolngtest/evecionflow01.xml");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
        //7.输入流，输出流的转换
        IOUtils.copy(pngInput,fileOutputStream);
        IOUtils.copy(bpmnInput,fileOutputStream2);
        //8.按顺序关闭流
        pngInput.close();
        bpmnInput.close();
        fileOutputStream.close();
        fileOutputStream2.close();
    }
}