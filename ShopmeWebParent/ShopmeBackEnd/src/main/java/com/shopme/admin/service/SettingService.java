package com.shopme.admin.service;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.GeneralSettingBag;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

import java.util.List;

public interface SettingService {
    List<Setting> getAllSettings();

    List<Currency> getAllCurrencies();

    GeneralSettingBag getGeneralSettings();

    List<Setting> getSettingsByCategory(SettingCategory category);

    void saveAllSettings(List<Setting> settings);

    void updateCurrencySymbol(int currencyId, GeneralSettingBag generalSettingBag);

}
