package com.aoyu.bitsetup.client.config;

import com.aoyu.bitsetup.client.ai.MysqlChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：AiConfig
 * @Author: aoyu
 * @Date: 2025-09-22 12:34
 * @Description: ai大模型配置类
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatMemory chatMemory(){
        return new MysqlChatMemory();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,ChatMemory chatMemory) {
        // 定义系统提示（System Prompt），设定AI的角色和能力
        String systemPrompt = """
                你是一名专业的"比特聚合智能应用下载与共享平台"的客服助手。
                请根据用户关于平台功能的问题，进行清晰、准确、友好的回答。
                如果用户问题与平台无关，请礼貌地告知他你的职责范围。

                平台主要服务功能包括：
                1.  **应用发现与获取**：用户可以进行应用浏览、下载、搜索，并通过分类筛选和热门榜单发现新应用。
                2.  **开放共享**：用户（开发者）可以上传和分享自己的智能应用。
                3.  **智能服务**：
                    - 智能推荐：根据用户喜好个性化推荐应用。
                    - 智能解答：回答关于应用功能、场景的问题。
                    - 使用指导：提供应用的使用和安装指导。
                4.  **社区互动**：用户可以在应用下评论，在论坛发帖，进行社区交流。

                请用中文回答，保持专业且友好的语调。
                """;
        return chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(),new MessageChatMemoryAdvisor(chatMemory))
                .defaultSystem("你是个全能的ai")
                .build();
    }

    @Bean
    public ChatClient chatClientNoMemory(ChatClient.Builder chatClientBuilder){
        String prompt =  """
            根据以下对话内容生成一个简洁的标题（10~15个字）：
            "%s"
            主要根据用户的内容来生成对话标题，不是回答对话内容，不需要加用户，智能助手这些名字，不需要加第几次，总结内容生成标题即可，要合理，符合对话内容，只需返回标题内容，不要添加任何解释。
            """;
        return chatClientBuilder
                .defaultSystem(prompt)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

    }
}