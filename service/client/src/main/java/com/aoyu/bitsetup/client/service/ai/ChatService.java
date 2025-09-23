package com.aoyu.bitsetup.client.service.ai;

import com.aoyu.bitsetup.model.vo.ai.ChatReqVO;
import com.aoyu.bitsetup.model.vo.ai.ChatRespVO;
import reactor.core.publisher.Flux;

/**
 * @InterfaceName：ChatService
 * @Author: aoyu
 * @Date: 2025/9/23 下午2:30
 * @Description:
 */

public interface ChatService {

    Flux<String> chat(ChatReqVO chatReqVO);

}
