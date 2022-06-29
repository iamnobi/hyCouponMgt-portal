package com.cherri.acs_portal.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/** Reference https://www.cnblogs.com/huahua035/p/7839834.html */
@Component
public class OceanJobFactory extends AdaptableJobFactory {

  @Autowired private AutowireCapableBeanFactory capableBeanFactory;

  @Override
  protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    Object jobInstance = super.createJobInstance(bundle);
    capableBeanFactory.autowireBean(jobInstance);
    return jobInstance;
  }
}
