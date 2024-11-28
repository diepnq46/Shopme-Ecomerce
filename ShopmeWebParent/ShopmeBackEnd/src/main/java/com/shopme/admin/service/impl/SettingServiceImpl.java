package com.shopme.admin.service.impl;

import com.shopme.admin.repository.CurrencyRepository;
import com.shopme.admin.repository.SettingRepository;
import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.GeneralSettingBag;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService {
    @Autowired
    private SettingRepository settingRepo;

    @Autowired
    private CurrencyRepository currencyRepo;

    @Override
    public List<Setting> getAllSettings() {
        return settingRepo.findAll();
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepo.findAllByOrderByNameAsc();
    }

    @Override
    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepo.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepo.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    @Override
    public List<Setting> getSettingsByCategory(SettingCategory category) {
        return settingRepo.findByCategory(category);

    }

    @Override
    public void saveAllSettings(List<Setting> settings) {
        settingRepo.saveAll(settings);
    }

    @Override
    public void updateCurrencySymbol(int currencyId, GeneralSettingBag settingBag) {
        Optional<Currency> optional = currencyRepo.findById(currencyId);

        if (optional.isPresent()) {
            Currency currency = optional.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }
}
