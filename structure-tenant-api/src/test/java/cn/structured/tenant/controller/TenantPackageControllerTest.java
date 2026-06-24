package cn.structured.tenant.controller;

import cn.structured.tenant.config.AbstractIntegrationTest;
import cn.structured.tenant.config.TestConfig;
import cn.structured.tenant.dto.TenantPackageDTO;
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
 * 租户套餐管理控制器测试
 *
 * @author chuck
 * @since 2024-01-01
 */
@AutoConfigureMockMvc
@Import(TestConfig.class)
@DisplayName("租户套餐管理接口测试")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TenantPackageControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateUniqueName() {
        return "测试套餐_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateUniqueCode() {
        return "PKG_CODE_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Test
    @DisplayName("创建套餐 - 成功")
    void testCreatePackage_Success() throws Exception {
        TenantPackageDTO dto = new TenantPackageDTO();
        dto.setName(generateUniqueName());
        dto.setCode(generateUniqueCode());
        dto.setDescription("这是一个基础套餐");
        dto.setState(1);
        dto.setMaxUserCount(100);
        dto.setMaxStorageGB(500);
        dto.setMaxDeptCount(50);
        dto.setCustomFieldEnabled(true);
        dto.setApiEnabled(true);
        dto.setSort(1);

        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @DisplayName("创建套餐 - 失败（缺少必填字段）")
    void testCreatePackage_Fail_MissingRequiredField() throws Exception {
        TenantPackageDTO dto = new TenantPackageDTO();
        // 不设置 name，应该校验失败

        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("VERIFICATION_FAILED"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("创建套餐 - 失败（套餐名称重复）")
    void testCreatePackage_Fail_DuplicateName() throws Exception {
        String uniqueName = generateUniqueName();
        TenantPackageDTO dto = new TenantPackageDTO();
        dto.setName(uniqueName);
        dto.setState(1);

        // 第一次创建应该成功
        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 第二次创建相同名称应该失败
        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("110712"));
    }

    @Test
    @DisplayName("更新套餐 - 成功")
    void testUpdatePackage_Success() throws Exception {
        // 先创建一个套餐
        TenantPackageDTO createDto = new TenantPackageDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long packageId = objectMapper.readTree(response).get("data").asLong();

        // 更新套餐
        TenantPackageDTO updateDto = new TenantPackageDTO();
        updateDto.setName(generateUniqueName());
        updateDto.setCode(generateUniqueCode());
        updateDto.setDescription("这是更新后的套餐描述");
        updateDto.setState(1);
        updateDto.setMaxUserCount(200);

        mockMvc.perform(put("/api/tenant-package/{id}", packageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("删除套餐 - 成功")
    void testDeletePackage_Success() throws Exception {
        // 先创建一个套餐
        TenantPackageDTO createDto = new TenantPackageDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long packageId = objectMapper.readTree(response).get("data").asLong();

        // 删除套餐
        mockMvc.perform(delete("/api/tenant-package/{id}", packageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("获取套餐详情 - 成功")
    void testGetPackageById_Success() throws Exception {
        // 先创建一个套餐
        TenantPackageDTO createDto = new TenantPackageDTO();
        createDto.setName(generateUniqueName());
        createDto.setCode(generateUniqueCode());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // 从响应中提取 ID
        Long packageId = objectMapper.readTree(response).get("data").asLong();

        // 获取套餐详情
        mockMvc.perform(get("/api/tenant-package/{id}", packageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询套餐 - 成功")
    void testPagePackages_Success() throws Exception {
        mockMvc.perform(get("/api/tenant-package/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("分页查询套餐 - 按状态筛选")
    void testPagePackages_FilterByState() throws Exception {
        // 创建测试套餐
        TenantPackageDTO dto = new TenantPackageDTO();
        dto.setName(generateUniqueName());
        dto.setState(1);

        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 按状态筛选查询
        mockMvc.perform(get("/api/tenant-package/page")
                        .param("currentPage", "1")
                        .param("pageSize", "10")
                        .param("state", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("启用套餐 - 成功")
    void testEnablePackage_Success() throws Exception {
        // 先创建一个套餐（草稿状态）
        TenantPackageDTO createDto = new TenantPackageDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(0);

        String response = mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long packageId = objectMapper.readTree(response).get("data").asLong();

        // 启用套餐
        mockMvc.perform(put("/api/tenant-package/{id}/enable", packageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("禁用套餐 - 成功")
    void testDisablePackage_Success() throws Exception {
        // 先创建一个套餐（启用状态）
        TenantPackageDTO createDto = new TenantPackageDTO();
        createDto.setName(generateUniqueName());
        createDto.setState(1);

        String response = mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long packageId = objectMapper.readTree(response).get("data").asLong();

        // 禁用套餐
        mockMvc.perform(put("/api/tenant-package/{id}/disable", packageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @DisplayName("套餐下拉选择 - 成功")
    void testListSelect_Success() throws Exception {
        // 创建已启用的套餐
        TenantPackageDTO dto = new TenantPackageDTO();
        dto.setName(generateUniqueName());
        dto.setState(1);

        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询下拉选项
        mockMvc.perform(get("/api/tenant-package/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("套餐下拉选择 - 仅返回启用状态的套餐")
    void testListSelect_OnlyEnabled() throws Exception {
        // 创建一个启用状态的套餐
        TenantPackageDTO enabledDto = new TenantPackageDTO();
        enabledDto.setName(generateUniqueName());
        enabledDto.setState(1);

        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enabledDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 创建一个草稿状态的套餐
        TenantPackageDTO draftDto = new TenantPackageDTO();
        draftDto.setName(generateUniqueName());
        draftDto.setState(0);

        mockMvc.perform(post("/api/tenant-package")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draftDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));

        // 查询下拉选项，应该只返回启用状态的套餐
        mockMvc.perform(get("/api/tenant-package/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }
}
