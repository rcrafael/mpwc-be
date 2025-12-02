package com.mahabang_parang.api_settings.service;

import com.mahabang_parang.api_settings.model.dto.DefaultValueDto;

public interface DefaultValueService {

     public DefaultValueDto getDefaultValue() throws Exception;
     public DefaultValueDto updateDefaultValue(DefaultValueDto payload) throws Exception;

}
