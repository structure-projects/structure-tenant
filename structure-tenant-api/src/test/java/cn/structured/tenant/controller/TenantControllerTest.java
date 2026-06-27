package cn.structured.tenant.controller;

import cn.structured.tenant.config.AbstractIntegrationTest;
import cn.structured.tenant.config.TestConfig;
import cn.structured.tenant.dto.TenantDTO;
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
 * 租户管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("租户管理接口测试")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TenantControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateUniqueName() {
        return "测试租户_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniqueCode() {
        return "TENANT_CODE_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Test
    @DisplayName("创建租户 - 成功")
    void testCreateTenant_Success() throws Exception {
        TenantDTO dto = new TenantDTO();
        dto.setName(generateUniqueName());
        dto.setCode(generateUniqueCode());
        dto.setDescription("这是一个测试租户");
        dto.setIndustry("IT");
        dto.setAddress("北京市海淀区");
        dto.setContactPhone("13800138000");
        dto.setState(1);

        mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建租户 - 失败（缺少必填字段）")
    void testCreateTenant_Fail_MissingRequiredField() throws Exception {
        TenantDTO dto = new TenantDTO();
        // 不设置 name，应该校验失败

        mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("创建租户 - 失败（租户名称重复）")
    void testCreateTenant_Fail_DuplicateName() throws Exception {
        String uniqueName = generateUniqueName();
        TenantDTO dto = new TenantDTO();
        dto.setName(uniqueName);
        dto.setState(1);

        // 第一次创建应该成功
        mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 第二次创建相同名称应该失败
        mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("110704"));
    }

    @Test
    @DisplayName("更新租户 - 成功")
    void testUpdateTenant_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 更新租户
        TenantDTO updateDto = new TenantDTO();
        updateDto.setName(generateUniqueName());
        updateDto.setCode(generateUniqueCode());
        updateDto.setDescription("这是更新后的租户描述");
        updateDto.setState(1);

        mockMvc.perform(put("/api/tenant/{id}", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("更新租户 - 失败（租户编码重复）")
    void testUpdateTenant_Fail_DuplicateCode() throws Exception {
        // 创建第一个租户
        TenantDTO dto1 = new TenantDTO();
        dto1.setName(generateUniqueName());
        String code1 = generateUniqueCode();
        dto1.setCode(code1);
        dto1.setState(1);

        String response1 = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long tenantId1 = objectMapper.readTree(response1).get("data").asLong();

        // 创建第二个租户
        TenantDTO dto2 = new TenantDTO();
        dto2.setName(generateUniqueName());
        String code2 = generateUniqueCode();
        dto2.setCode(code2);
        dto2.setState(1);

        mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 更新第一个租户，尝试使用第二个租户的编码，应该失败
        TenantDTO updateDto = new TenantDTO();
        updateDto.setName(generateUniqueName());
        updateDto.setCode(code2);
        updateDto.setState(1);

        mockMvc.perform(put("/api/tenant/{id}", tenantId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("110703"));
    }

    @Test
    @DisplayName("删除租户 - 成功")
    void testDeleteTenant_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 删除租户
        mockMvc.perform(delete("/api/tenant/{id}", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取租户详情 - 成功")
    void testGetTenantById_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 获取租户详情
        mockMvc.perform(get("/api/tenant/{id}", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询租户 - 成功")
    void testPageTenants_Success() throws Exception {
        mockMvc.perform(get("/api/tenant/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询租户 - 按名称筛选")
    void testPageTenants_FilterByName() throws Exception {
        // 创建测试租户
        TenantDTO dto = new TenantDTO();
        String uniqueName = generateUniqueName() + "_XYZ";
        dto.setName(uniqueName);
        dto.setState(1);

        mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 按名称筛选查询
        mockMvc.perform(get("/api/tenant/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10")
                        .param("name", "XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("激活租户 - 成功")
    void testActivateTenant_Success() throws Exception {
        // 先创建一个租户（初始化状态）
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(0);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 激活租户
        mockMvc.perform(put("/api/tenant/{id}/activate", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("停用租户 - 成功")
    void testSuspendTenant_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 停用租户
        mockMvc.perform(put("/api/tenant/{id}/suspend", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("冻结租户 - 成功")
    void testFreezeTenant_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 冻结租户
        mockMvc.perform(put("/api/tenant/{id}/freeze", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("过期租户 - 成功")
    void testExpireTenant_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createDto = new TenantDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tenantId = objectMapper.readTree(response).get("data").asLong();

        // 过期租户
        mockMvc.perform(put("/api/tenant/{id}/expire", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("应用模板到租户 - 成功")
    void testApplyTemplate_Success() throws Exception {
        // 先创建一个租户
        TenantDTO createTenantDto = new TenantDTO();
        createTenantDto.setName(generateUniqueName());
        createTenantDto.setState(1);

        String tenantResponse = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTenantDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tenantId = objectMapper.readTree(tenantResponse).get("data").asLong();

        // 创建一个模板
        cn.structured.tenant.dto.TenantTemplateDTO templateDto = new cn.structured.tenant.dto.TenantTemplateDTO();
        templateDto.setName("测试模板_" + UUID.randomUUID().toString().substring(0, 8));
        templateDto.setState(1);

        String templateResponse = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId = objectMapper.readTree(templateResponse).get("data").asLong();

        // 应用模板到租户
        mockMvc.perform(put("/api/tenant/{tenantId}/template/{templateId}", tenantId, templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("应用模板到租户 - 失败（租户不存在）")
    void testApplyTemplate_Fail_TenantNotFound() throws Exception {
        // 创建一个模板
        cn.structured.tenant.dto.TenantTemplateDTO templateDto = new cn.structured.tenant.dto.TenantTemplateDTO();
        templateDto.setName("测试模板_" + UUID.randomUUID().toString().substring(0, 8));
        templateDto.setState(1);

        String templateResponse = mockMvc.perform(post("/api/tenant-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long templateId = objectMapper.readTree(templateResponse).get("data").asLong();

        // 应用不存在的租户ID
        mockMvc.perform(put("/api/tenant/{tenantId}/template/{templateId}", 999999L, templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("110701"));
    }

    @Test
    @DisplayName("应用模板到租户 - 失败（模板不存在）")
    void testApplyTemplate_Fail_TemplateNotFound() throws Exception {
        // 先创建一个租户
        TenantDTO createTenantDto = new TenantDTO();
        createTenantDto.setName(generateUniqueName());
        createTenantDto.setState(1);

        String tenantResponse = mockMvc.perform(post("/api/tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTenantDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tenantId = objectMapper.readTree(tenantResponse).get("data").asLong();

        // 应用不存在的模板ID
        mockMvc.perform(put("/api/tenant/{tenantId}/template/{templateId}", tenantId, 999999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("110721"));
    }
}
