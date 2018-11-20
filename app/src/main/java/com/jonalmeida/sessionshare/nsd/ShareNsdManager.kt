package com.jonalmeida.sessionshare.nsd

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.PROTOCOL_DNS_SD
import android.net.nsd.NsdServiceInfo
import com.jonalmeida.sessionshare.Components
import com.jonalmeida.sessionshare.discovery.DiscoveryService
import com.jonalmeida.sessionshare.discovery.DiscoveryServiceReceiver
import com.jonalmeida.sessionshare.ext.Log
import com.jonalmeida.sessionshare.ext.toDiscoveryItem

/**
 * Takes care of (un)registering the network service as well as listening for service registration
 * changes (because life is too short to done them separately).
 */
class ShareNsdManager(
    val components: Components,
    delegate: DiscoveryServiceReceiver
) : LifecycleObserver, DiscoveryService, DiscoveryServiceReceiver by delegate {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun startService() {
        components.nsdManager.apply {
            registerService(components.nsdServiceInfo, PROTOCOL_DNS_SD, mRegistrationListener)
            discoverServices(SERVICE_TYPE, PROTOCOL_DNS_SD, mDiscoveryListener)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun stopService() {
        components.nsdManager.apply {
            unregisterService(mRegistrationListener)
            stopServiceDiscovery(mDiscoveryListener)
        }
    }

    private val mRegistrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            val currentServiceName = components.nsdServiceName
            if (currentServiceName != serviceInfo.serviceName) {
                components.nsdServiceName = serviceInfo.serviceName
            }
            Log.d("Service registered: ${serviceInfo.serviceName}")
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Registration failed! Put debugging code here to determine why.
            Log.d("Registration failed: ${serviceInfo.serviceName} with error: $errorCode")
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            // Service has been unregistered. This only happens when you call
            // ShareNsdManager.unregisterService() and pass in this listener.
            Log.d("Service unregistered: ${serviceInfo.serviceName}")
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Unregistration failed. Put debugging code here to determine why.
            Log.d("Unregistration failed: ${serviceInfo.serviceName} with error: $errorCode")
        }
    }

    // Instantiate a new DiscoveryListener
    private val mDiscoveryListener = object : NsdManager.DiscoveryListener {

        // Called as soon as service discovery begins.
        override fun onDiscoveryStarted(regType: String) {
            Log.d("Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            // A service was found! Do something with it.
            Log.d("Service discovery success$service")
            when {
                service.serviceName == components.nsdServiceName ->
                    Log.d("Found ourselves, ignoring..")
                service.serviceType == SERVICE_TYPE -> {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d("Found our service: ${service.serviceName}")
                    components.nsdManager.resolveService(service, object: NsdManager.ResolveListener {
                        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                            // Called when the resolve fails. Use the error code to debug.
                            Log.e("Resolve failed: $errorCode")
                        }

                        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                            Log.d("Resolve Succeeded. $serviceInfo")
                            serviceFound(serviceInfo.toDiscoveryItem())
                        }
                    })
                }
                else -> {
                    // transport layer for this service.
                    Log.d("Unknown Service Type: ${service.serviceType}")
                }
            }
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            service.let {
                Log.e("service lost: $service")
                serviceLost(service.toDiscoveryItem())
            }
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.d("Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e("Discovery failed: Error code:$errorCode")
            components.nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e("Discovery failed: Error code:$errorCode")
            components.nsdManager.stopServiceDiscovery(this)
        }
    }

    companion object {
        private const val SERVICE_TYPE = "_mozShare._tcp."
    }
}
