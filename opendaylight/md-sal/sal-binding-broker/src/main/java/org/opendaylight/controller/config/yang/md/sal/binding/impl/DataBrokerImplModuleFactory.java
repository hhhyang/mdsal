/**
 * Generated file

 * Generated from: yang module name: opendaylight-sal-binding-broker-impl  yang module local name: binding-data-broker
 * Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
 * Generated at: Wed Nov 20 17:33:01 CET 2013
 *
 * Do not modify this file unless it is present under src/main directory
 */
package org.opendaylight.controller.config.yang.md.sal.binding.impl;

import org.opendaylight.controller.config.api.DependencyResolver;
import org.opendaylight.controller.config.api.DynamicMBeanWithInstance;
import org.opendaylight.controller.config.spi.Module;
import org.osgi.framework.BundleContext;

/**
*
*/
public class DataBrokerImplModuleFactory extends
        org.opendaylight.controller.config.yang.md.sal.binding.impl.AbstractDataBrokerImplModuleFactory {

    @Override
    public Module createModule(String instanceName, DependencyResolver dependencyResolver, BundleContext bundleContext) {
        DataBrokerImplModule module = (DataBrokerImplModule) super.createModule(instanceName, dependencyResolver,
                bundleContext);
        module.setBundleContext(bundleContext);
        return module;
    }

    @Override
    public Module createModule(String instanceName, DependencyResolver dependencyResolver,
            DynamicMBeanWithInstance old, BundleContext bundleContext) throws Exception {
        DataBrokerImplModule module = (DataBrokerImplModule) super.createModule(instanceName, dependencyResolver, old,
                bundleContext);
        module.setBundleContext(bundleContext);
        return module;
    }

}
