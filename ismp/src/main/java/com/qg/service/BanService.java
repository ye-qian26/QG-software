package com.qg.service;

import com.qg.domain.Ban;

import java.util.List;

public interface BanService {
    List<Ban> selectAll();

    boolean add(Ban ban);

    boolean delete(Ban ban);

    Ban selectByUserId(Long userId);

    boolean judgeBan(Long userId);
}
