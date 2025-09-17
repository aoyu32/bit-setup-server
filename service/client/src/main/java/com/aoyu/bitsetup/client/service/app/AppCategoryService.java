package com.aoyu.bitsetup.client.service.app;

import com.aoyu.bitsetup.model.dto.PageResultDTO;
import com.aoyu.bitsetup.model.dto.app.AppCategoryDTO;
import com.aoyu.bitsetup.model.dto.app.AppInfoDto;
import com.aoyu.bitsetup.model.query.AppQuery;
import com.aoyu.bitsetup.model.vo.app.AppFilterQueryVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @InterfaceName：AppCategoryService
 * @Author: aoyu
 * @Date: 2025/9/16 下午1:23
 * @Description:
 */

public interface AppCategoryService {

   /**
    * @description: 根据分类等级获取分类
    * @author: aoyu
    * @date: 2025/9/16 下午1:35
    * @param: 分类等级
    * @return: 分类列表
    */
   List<AppCategoryDTO> getCategoryByLevel(Integer level);

   /**
    * @description: 根据id获取该分类下的所有子分类
    * @author: aoyu
    * @date: 2025/9/16 下午1:57
    * @param: 分类id
    * @return: 子分类列表
    */
   List<AppCategoryDTO> getSubCategoryById(Integer id);

   PageResultDTO<AppInfoDto> getAppByFilter(AppFilterQueryVO appFilterQueryVO);

}
