package com.aoyu.bitsetup.client.service.app;

import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.query.SearchQuery;
import com.aoyu.bitsetup.model.vo.app.AppSearchQueryVO;

import java.util.List;

/**
 * @InterfaceName：AppSearchService
 * @Author: aoyu
 * @Date: 2025/9/18 下午3:15
 * @Description:
 */

public interface AppSearchService {
    List<String> getSearchTips(String keyword);
    PageResultDTO<AppInfoDto> searchApp(Integer pageNum,Integer pageSize,AppSearchQueryVO appSearchQueryVO);
}
