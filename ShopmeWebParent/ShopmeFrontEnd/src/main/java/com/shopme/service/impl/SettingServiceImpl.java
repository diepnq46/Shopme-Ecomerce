package com.shopme.service.impl;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.entity.EmailSettingBag;
import com.shopme.repository.CurrencyRepository;
import com.shopme.repository.SettingRepository;
import com.shopme.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Setting> getGeneralSettings() {

        return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    @Override
    public EmailSettingBag getEmailSettingBag() {
        List<Setting> mailSettings = settingRepo.findByTwoCategories(SettingCategory.MAIL_SERVER, SettingCategory.MAIL_TEMPLATE);

        return new EmailSettingBag(mailSettings);
    }

}
