package com.aoyu.bitsetup.client.utils;

import com.aoyu.bitsetup.common.utils.RangeStringUtil;
import com.aoyu.bitsetup.model.query.AppQuery;
import com.aoyu.bitsetup.model.vo.app.AppFilterQueryVO;

import java.util.Map;

public class AppQueryConverter {
    public static AppQuery toAppQuery(AppFilterQueryVO vo) {
        AppQuery query = new AppQuery();
        query.setPageNum(vo.getPageNum());
        query.setPageSize(vo.getPageSize());

        query.setFatherCategoryId(vo.getCategory() == 0 ? null : vo.getCategory());
        query.setChildCategoryId(vo.getSubCategory() == 0 ? null : vo.getSubCategory());
        if (vo.getCategory() != 0 && vo.getSubCategory() != 0) {
            query.setFatherCategoryId(null);
            query.setChildCategoryId(vo.getSubCategory());
        }

        Map<String, Double> rating = RangeStringUtil.getMinAndMax(vo.getRating(), Double.class);
        query.setMinRating(rating.get("min"));
        query.setMaxRating(rating.get("max"));

        Map<String, Integer> download = RangeStringUtil.getMinAndMax(vo.getDownloadLevel(), Integer.class);
        query.setMinDownload(download.get("min"));
        query.setMaxDownload(download.get("max"));

        query.setPlatformName(vo.getPlatform().isEmpty() ? null : vo.getPlatform());
        query.setPricingModel(vo.getPaymentModel().isEmpty() ? null : vo.getPaymentModel());

        Map<String, Long> size = RangeStringUtil.getMinAndMax(vo.getSize(), Long.class);
        query.setMinSize(size.get("min"));
        query.setMaxSize(size.get("max"));

        query.setInstaller(vo.getInstallMethod().isEmpty() ? null : vo.getInstallMethod());

        handleOtherField(vo.getOther(), query);

        return query;
    }

    private static void handleOtherField(String other, AppQuery query) {
        switch (other) {
            case "is_personal_develop": query.setIsPersonalDevelop(true); break;
            case "is_recommend": query.setIsRecommend(true); break;
            case "must_have": query.setIsEssential(true); break;
            case "popular": query.setIsHot(true); break;
            case "is_cracked": query.setIsCracked(true); break;
            case "free_download": query.setPoints(0); break;
            case "point_download": query.setPoints(1); break;
            case "domestic":
            case "foreign": query.setOriginRegion(other); break;
            default: break;
        }
    }
}
