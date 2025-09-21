package com.aoyu.bitsetup.client.service.mail;

/**
 * @InterfaceName：MailService
 * @Author: aoyu
 * @Date: 2025/9/20 下午1:04
 * @Description:
 */

public interface MailService {

    void sendCodeMail(String to, String subject, String body);

}
