package com.performworld.repository.admin;

import com.performworld.dto.admin.TierDTO;

public interface TierCustomRepo {

    TierDTO getUserTier(String userId);
}
