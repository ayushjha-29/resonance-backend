package com.resonance.resonance.mapper;

import com.resonance.resonance.dto.request.RegisterRequest;
import com.resonance.resonance.dto.response.RegisterResponse;
import com.resonance.resonance.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toEntity(RegisterRequest request);

    RegisterResponse toDTO(AppUser appUser);

}
