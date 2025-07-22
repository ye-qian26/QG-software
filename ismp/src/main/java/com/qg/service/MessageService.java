package com.qg.service;


import com.qg.domain.ApplyDeveloper;
import com.qg.domain.ApplySoftware;
import com.qg.domain.Message;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MessageService {

    List<Message> checkIfHaveNewMessage(Long userId);

    List<Message> getAllMessage(Long userId);

    boolean read(Long userId, Long messageId);

    boolean deleteAllRead(Long userId);

    boolean applyDeveloperSuccess(ApplyDeveloper applyDeveloper);

    boolean applyDeveloperFailure(ApplyDeveloper applyDeveloper);


    boolean applySoftwareSuccess(ApplySoftware applySoftware);

    boolean applySoftwareFailure(ApplySoftware applySoftware);

    void orderSoftwareLaunch(Long softwareId);
}
