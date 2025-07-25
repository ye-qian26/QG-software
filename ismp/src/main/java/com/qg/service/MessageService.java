package com.qg.service;


import com.qg.domain.ApplyDeveloper;
import com.qg.domain.ApplySoftware;
import com.qg.domain.Message;
import com.qg.domain.Software;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MessageService {

    boolean adminUpdateSoftwareInformation(Software software);

    List<Message> checkIfHaveNewMessage(Long userId);

    List<Message> getAllMessage(Long userId);

    boolean read(Long userId, Long messageId);

    boolean deleteAllRead(Long userId);

    boolean applyDeveloperSuccess(Long userId);

    boolean applyDeveloperFailure(Long userId, String reason);

    boolean applySoftwareSuccess(ApplySoftware applySoftware);

    boolean applySoftwareFailure(ApplySoftware applySoftware);

    void orderSoftwareLaunch(Long softwareId);
}
