package cn.structured.tenant.service;

import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.tenant.dto.TenantTemplateDTO;
import cn.structured.tenant.query.TenantTemplateQuery;
import cn.structured.tenant.vo.TenantTemplateVO;

import java.util.List;

public interface ITenantTemplateService {

    Long create(TenantTemplateDTO dto);

    void update(Long id, TenantTemplateDTO dto);

    void delete(Long id);

    TenantTemplateVO findById(Long id);

    ResPage<TenantTemplateVO> page(TenantTemplateQuery query, ReqPage reqPage);

    void enable(Long id);

    void disable(Long id);

    void setDefault(Long id);

    TenantTemplateVO getDefault();

    List<OptionVO> listSelect();
}