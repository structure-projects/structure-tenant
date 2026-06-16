package cn.structured.tenant.service;

import cn.structure.common.vo.OptionVO;
import cn.structure.common.vo.ReqPage;
import cn.structure.common.vo.ResPage;
import cn.structured.tenant.dto.TenantPackageDTO;
import cn.structured.tenant.query.TenantPackageQuery;
import cn.structured.tenant.vo.TenantPackageVO;

import java.util.List;

public interface ITenantPackageService {

    Long create(TenantPackageDTO dto);

    void update(Long id, TenantPackageDTO dto);

    void delete(Long id);

    TenantPackageVO findById(Long id);

    ResPage<TenantPackageVO> page(TenantPackageQuery query, ReqPage reqPage);

    void enable(Long id);

    void disable(Long id);

    List<OptionVO> listSelect();
}