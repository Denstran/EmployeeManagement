package com.manageemployee.employeemanagement.integration.task;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.task.dto.TaskDTO;
import com.manageemployee.employeemanagement.task.dto.TaskExtensionDeadlineDTO;
import com.manageemployee.employeemanagement.task.model.Task;
import com.manageemployee.employeemanagement.task.model.TaskStatus;
import com.manageemployee.employeemanagement.task.service.TaskService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TaskService taskService;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "secret"))
            .withPerMethodLifecycle(true);

    @Test
    @WithMockUser(username = "john.doe@exampe.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_contain_one_task() throws Exception {
        Task task = setupTask(3L);
        task = taskService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("taskDTOS"))
                .andExpect(MockMvcResultMatchers.model().attribute("taskDTOS", hasSize(1)));

        taskService.deleteById(task.getId());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_contain_two_tasks_when_looking_through_given_tasks() throws Exception {
        Task task = setupTask(3L);
        Task task2 = setupTask(3L);
        task = taskService.saveTask(task);
        task2 = taskService.saveTask(task2);

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks/headOfDepartment/givenTasks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("taskDTOS"))
                .andExpect(MockMvcResultMatchers.model().attribute("taskDTOS", hasSize(2)));

        taskService.deleteById(task.getId());
        taskService.deleteById(task2.getId());
    }

    @Test
    @WithMockUser(username = "john.doe@exampe.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_have_forbidden_status_when_looking_through_given_tasks_with_illegal_role() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks/headOfDepartment/givenTasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_contain_dto_in_model_when_requesting_creation_form_with_legal_role() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks/headOfDepartment/createTask/3"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("taskDTO"))
                .andExpect(MockMvcResultMatchers.view().name("task/createTask"));
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_throw_exception_when_trying_to_create_task_for_employee_from_another_branch() {
        assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks/headOfDepartment/createTask/17"))
                    .andExpect(status().isInternalServerError());
        }).hasCauseInstanceOf(SecurityException.class);
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_throw_exception_when_trying_to_create_task_for_employee_from_another_department() {
        assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks/headOfDepartment/createTask/6"))
                    .andExpect(status().isInternalServerError());
        }).hasCauseInstanceOf(SecurityException.class);
    }

    @Test
    @WithMockUser(username = "john.doe@exampe.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_have_forbidden_status_when_trying_to_access_with_illegal_role() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/tasks/headOfDepartment/createTask/3"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_create_task_when_legal_role_and_valid_data() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(3L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("taskDTO", taskDTO))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/tasks/headOfDepartment/givenTasks"));

        assertEquals(1, taskService.getTasksByOwnerId(3L).size());
        taskService.deleteAllByOwnerId(3L);
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_send_email_when_task_created() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(3L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("taskDTO", taskDTO))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/tasks/headOfDepartment/givenTasks"));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        while (!greenMail.waitForIncomingEmail(1)) {
            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("john.doe@exampe.com", message.getAllRecipients()[0].toString());
            assertTrue(message.getContent().toString().contains("TEST"));
            assertTrue(message.getContent().toString().contains(LocalDate.now().plusDays(15L).toString()));
            assertEquals("Выдана задача", message.getSubject());
        }

        TestTransaction.start();
        assertEquals(1, taskService.getTasksByOwnerId(3L).size());
        taskService.deleteAllByOwnerId(3L);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_have_forbidden_status_when_illegal_role_when_creating_task_with_post_request() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(3L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("taskDTO", taskDTO))
                .andExpect(status().isForbidden());

        assertEquals(0, taskService.getTasksByOwnerId(3L).size());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_errors_when_data_is_not_correct_creating_task_with_post_request() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(3L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().minusDays(15));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("taskDTO", taskDTO))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("task/createTask"));

        assertEquals(0, taskService.getTasksByOwnerId(3L).size());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void model_should_contain_errors_with_size_1_when_fired_employee_when_creating_task_with_post_request() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(2L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("taskDTO", taskDTO))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("task/createTask"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors"))
                .andExpect(MockMvcResultMatchers.model().attribute("errors", hasSize(1)));

        assertEquals(0, taskService.getTasksByOwnerId(2L).size());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void model_should_contain_errors_with_size_1_when_employee_on_vacation_when_creating_task_with_post_request() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(4L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/4")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("taskDTO", taskDTO))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("task/createTask"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors"))
                .andExpect(MockMvcResultMatchers.model().attribute("errors", hasSize(1)));

        assertEquals(0, taskService.getTasksByOwnerId(4L).size());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_throw_exception_when_creating_task_for_employee_from_another_department() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(6L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/6")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .flashAttr("taskDTO", taskDTO))
                    .andExpect(status().isInternalServerError());
        }).hasCauseInstanceOf(SecurityException.class);

        assertEquals(0, taskService.getTasksByOwnerId(6L).size());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_throw_exception_when_creating_task_for_employee_from_another_branch() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskOwnerId(17L);
        taskDTO.setTaskGiverId(1L);
        taskDTO.setTaskDescription("TEST");
        taskDTO.setTaskDeadLine(LocalDate.now().plusDays(15));

        assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/createTask/17")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .flashAttr("taskDTO", taskDTO))
                    .andExpect(status().isInternalServerError());
        }).hasCauseInstanceOf(SecurityException.class);

        assertEquals(0, taskService.getTasksByOwnerId(17L).size());
    }

    @Test
    @WithMockUser(username = "john.doe@exampe.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_update_task_status_and_send_email_when_employee_finish_task() throws Exception {
        Task task = setupTask(3L);
        task = taskService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/" + task.getId() + "/finish")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/tasks"));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        while (!greenMail.waitForIncomingEmail(1)) {
            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("john.doe@examle.com", message.getAllRecipients()[0].toString());
            assertTrue(message.getContent().toString().contains("TEST"));
            assertTrue(message.getContent().toString().contains(LocalDate.now().toString()));
            assertEquals("Задача на проверку", message.getSubject());
        }

        TestTransaction.start();
        task = taskService.getTaskById(task.getId());
        assertEquals(TaskStatus.ON_VALIDATION, task.getTaskStatus());
        taskService.deleteTask(task);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @WithMockUser(username = "john.doe@exampe.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_throw_exception_when_trying_to_access_other_employee_task() {
        Task task = setupTask(2L);
        task = taskService.saveTask(task);

        final Task finalTask = task;
        assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/" + finalTask.getId() + "/finish")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isInternalServerError());
        });

        task = taskService.getTaskById(task.getId());
        assertEquals(TaskStatus.IN_PROCESS, task.getTaskStatus());
        taskService.deleteTask(task);
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_approve_task_and_send_email_when_role_is_legal() throws Exception {
        Task task = setupTask(3L);
        task.setTaskStatus(TaskStatus.ON_VALIDATION);
        task = taskService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/" + task.getId() + "/approveTask")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/tasks/headOfDepartment/givenTasks"));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        while (!greenMail.waitForIncomingEmail(1)) {
            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("john.doe@exampe.com", message.getAllRecipients()[0].toString());
            assertTrue(message.getContent().toString().contains("TEST"));
            assertTrue(message.getContent().toString().contains(LocalDate.now().toString()));
            assertEquals("Задача на проверку", message.getSubject());
        }

        TestTransaction.start();
        task = taskService.getTaskById(task.getId());
        assertEquals(TaskStatus.FINISHED, task.getTaskStatus());
        taskService.deleteTask(task);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_have_forbidden_status_when_approving_with_illegal_role() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/1/approveTask")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_throw_exception_when_approving_another_head_task() throws Exception {
        Task task = setupTask(3L);
        task.setTaskGiver(employeeService.getReference(5L));
        task = taskService.saveTask(task);

        final Task finalTask = task;
        assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/" + finalTask.getId() + "/approveTask")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isBadRequest());
        }).hasCauseInstanceOf(SecurityException.class);
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_change_status_and_send_email_when_disapproving_task_with_correct_role() throws Exception {
        Task task = setupTask(3L);
        task.setTaskStatus(TaskStatus.ON_VALIDATION);
        task = taskService.saveTask(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/" + task.getId() + "/disapproveTask")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/tasks/headOfDepartment/givenTasks"));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        while (!greenMail.waitForIncomingEmail(1)) {
            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("john.doe@exampe.com", message.getAllRecipients()[0].toString());
            assertTrue(message.getContent().toString().contains("TEST"));
            assertTrue(message.getContent().toString().contains(LocalDate.now().toString()));
            assertEquals("Задача не выполнена", message.getSubject());
        }

        TestTransaction.start();
        task = taskService.getTaskById(task.getId());
        assertEquals(TaskStatus.IN_PROCESS, task.getTaskStatus());
        taskService.deleteTask(task);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_extend_deadline_and_send_mail_when_extending_email_with_valid_data_and_role() throws Exception {
        Task task = taskService.saveTask(setupTask(3L));
        TaskExtensionDeadlineDTO dateExtensionDTO = new TaskExtensionDeadlineDTO();
        dateExtensionDTO.setExtendedDeadline(task.getTaskDeadLine().plusDays(15L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/" + task.getId() + "/extendTask")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("dateExtensionDTO", dateExtensionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/tasks/headOfDepartment/givenTasks"));

        TestTransaction.flagForCommit();
        TestTransaction.end();

        while (!greenMail.waitForIncomingEmail(1)) {
            MimeMessage message = greenMail.getReceivedMessages()[0];
            assertEquals("john.doe@exampe.com", message.getAllRecipients()[0].toString());
            assertTrue(message.getContent().toString().contains("TEST"));
            assertTrue(message.getContent().toString().contains(dateExtensionDTO.getExtendedDeadline().toString()));
            assertEquals("Срок сдачи задачи передвинут", message.getSubject());
        }

        TestTransaction.start();
        task = taskService.getTaskById(task.getId());
        assertEquals(dateExtensionDTO.getExtendedDeadline(), task.getTaskDeadLine());
        taskService.deleteTask(task);
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    @Transactional
    void should_not_extend_task_deadline_when_not_valid_data() throws Exception {
        Task task = taskService.saveTask(setupTask(3L));
        TaskExtensionDeadlineDTO dateExtensionDTO = new TaskExtensionDeadlineDTO();
        dateExtensionDTO.setExtendedDeadline(task.getTaskDeadLine().minusDays(15L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/tasks/headOfDepartment/" + task.getId() + "/extendTask")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("dateExtensionDTO", dateExtensionDTO))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("task/extendTaskDeadline"));
    }

    private Task setupTask(Long empId) {
        Task task = new Task();
        task.setTaskGiver(employeeService.getReference(1L));
        task.setTaskOwner(employeeService.getReference(empId));
        task.setTaskCreated(LocalDate.now());
        task.setTaskDeadLine(LocalDate.now().plusDays(15));
        task.setTaskDescription("TEST");
        task.setTaskStatus(TaskStatus.IN_PROCESS);

        return task;
    }
}
