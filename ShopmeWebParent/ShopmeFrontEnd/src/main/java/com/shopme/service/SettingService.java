package com.shopme.service;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.entity.EmailSettingBag;

import java.util.List;

public interface SettingService {
    List<Setting> getAllSettings();

    List<Currency> getAllCurrencies();

    List<Setting> getGeneralSettings();

    EmailSettingBag getEmailSettingBag();
}
