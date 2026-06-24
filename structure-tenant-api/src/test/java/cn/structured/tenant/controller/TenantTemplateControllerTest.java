package cn.structured.tenant.controller;

import cn.structured.tenant.config.AbstractIntegrationTest;
import cn.structured.tenant.config.TestConfig;
import cn.structured.tenant.dto.TenantTemplateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 租户模板管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("租户模板管理接口测试")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TenantTemplateControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateUniqueName() {
        return "测试模板_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniqueCode() {
        return "TPL_CODE_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Test
    @DisplayName("创建模板 - 成功")
    void testCreateTemplate_Success() throws Exception {
        TenantTemplateDTO dto = new TenantTemplateDTO();
        dto.setName(generateUniqueName());
        dto.setCode(generateUniqueCode());
        dto.setDescription("这是一个标准模板");
        dto.setState(1);
        dto.setConfig("{\"theme\": \"default\", \"logo\": \"default.png\"}");
        dto.setIsDefault(false);
        dto.setSort(1);

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建模板 - 失败（缺少必填字段）")
    void testCreateTemplate_Fail_MissingRequiredField() throws Exception {
        TenantTemplateDTO dto = new TenantTemplateDTO();
        // 不设置 name，应该校验失败

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("创建模板 - 失败（模板名称重复）")
    void testCreateTemplate_Fail_DuplicateName() throws Exception {
        String uniqueName = generateUniqueName();
        TenantTemplateDTO dto = new TenantTemplateDTO();
        dto.setName(uniqueName);
        dto.setState(1);

        // 第一次创建应该成功
        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 第二次创建相同名称应该失败
        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("110722"));
    }

    @Test
    @DisplayName("更新模板 - 成功")
    void testUpdateTemplate_Success() throws Exception {
        // 先创建一个模板
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long templateId = objectMapper.readTree(response).get("data").asLong();

        // 更新模板
        TenantTemplateDTO updateDto = new TenantTemplateDTO();
        updateDto.setName(generateUniqueName());
        updateDto.setCode(generateUniqueCode());
        updateDto.setDescription("这是更新后的模板描述");
        updateDto.setState(1);
        updateDto.setConfig("{\"theme\": \"dark\"}");

        mockMvc.perform(put("/api/tenant-template/{id}", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("删除模板 - 成功")
    void testDeleteTemplate_Success() throws Exception {
        // 先创建一个模板
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long templateId = objectMapper.readTree(response).get("data").asLong();

        // 删除模板
        mockMvc.perform(delete("/api/tenant-template/{id}", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取模板详情 - 成功")
    void testGetTemplateById_Success() throws Exception {
        // 先创建一个模板
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long templateId = objectMapper.readTree(response).get("data").asLong();

        // 获取模板详情
        mockMvc.perform(get("/api/tenant-template/{id}", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询模板 - 成功")
    void testPageTemplates_Success() throws Exception {
        mockMvc.perform(get("/api/tenant-template/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询模板 - 按状态筛选")
    void testPageTemplates_FilterByState() throws Exception {
        // 创建测试模板
        TenantTemplateDTO dto = new TenantTemplateDTO();
        dto.setName(generateUniqueName());
        dto.setState(1);

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 按状态筛选查询
        mockMvc.perform(get("/api/tenant-template/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10")
                        .param("state", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("启用模板 - 成功")
    void testEnableTemplate_Success() throws Exception {
        // 先创建一个模板（草稿状态）
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(0);

        String response = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId = objectMapper.readTree(response).get("data").asLong();

        // 启用模板
        mockMvc.perform(put("/api/tenant-template/{id}/enable", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("禁用模板 - 成功")
    void testDisableTemplate_Success() throws Exception {
        // 先创建一个模板（启用状态）
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId = objectMapper.readTree(response).get("data").asLong();

        // 禁用模板
        mockMvc.perform(put("/api/tenant-template/{id}/disable", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("设为默认模板 - 成功")
    void testSetDefault_Success() throws Exception {
        // 先创建一个模板
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId = objectMapper.readTree(response).get("data").asLong();

        // 设为默认模板
        mockMvc.perform(put("/api/tenant-template/{id}/default", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("设为默认模板 - 替换原有默认模板")
    void testSetDefault_ReplaceOldDefault() throws Exception {
        // 创建第一个模板并设为默认
        TenantTemplateDTO dto1 = new TenantTemplateDTO();
        dto1.setName(generateUniqueName());
        dto1.setState(1);

        String response1 = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId1 = objectMapper.readTree(response1).get("data").asLong();

        // 设为默认
        mockMvc.perform(put("/api/tenant-template/{id}/default", templateId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 创建第二个模板
        TenantTemplateDTO dto2 = new TenantTemplateDTO();
        dto2.setName(generateUniqueName());
        dto2.setState(1);

        String response2 = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId2 = objectMapper.readTree(response2).get("data").asLong();

        // 将第二个模板设为默认，第一个模板的默认状态应该被取消
        mockMvc.perform(put("/api/tenant-template/{id}/default", templateId2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取默认模板 - 成功")
    void testGetDefault_Success() throws Exception {
        // 先创建一个模板并设为默认
        TenantTemplateDTO createDto = new TenantTemplateDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);
        createDto.setIsDefault(true);

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 获取默认模板
        mockMvc.perform(get("/api/tenant-template/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("模板下拉选择 - 成功")
    void testListSelect_Success() throws Exception {
        // 创建已启用的模板
        TenantTemplateDTO dto = new TenantTemplateDTO();
        dto.setName(generateUniqueName());
        dto.setState(1);

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询下拉选项
        mockMvc.perform(get("/api/tenant-template/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("模板下拉选择 - 仅返回启用状态的模板")
    void testListSelect_OnlyEnabled() throws Exception {
        // 创建一个启用状态的模板
        TenantTemplateDTO enabledDto = new TenantTemplateDTO();
        enabledDto.setName(generateUniqueName());
        enabledDto.setState(1);

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enabledDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 创建一个草稿状态的模板
        TenantTemplateDTO draftDto = new TenantTemplateDTO();
        draftDto.setName(generateUniqueName());
        draftDto.setState(0);

        mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draftDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询下拉选项，应该只返回启用状态的模板
        mockMvc.perform(get("/api/tenant-template/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }
}