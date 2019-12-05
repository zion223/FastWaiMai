package com.zrp.latte.app;

import android.app.Activity;
import android.os.Handler;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.zrp.latte.delegates.web.event.Event;
import com.zrp.latte.delegates.web.event.EventManager;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

public class Configurator {

    public static final HashMap<String, Object> LATTE_CONFIGS = new HashMap<>();
    public static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    public static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    public static final Handler HANDLER = new Handler();

    private Configurator(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(), false);
        LATTE_CONFIGS.put(ConfigType.HANDLER.name(), HANDLER);
    }
    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }
    final HashMap<String, Object> getLatteConfigs(){
        return LATTE_CONFIGS;
    }
    private static class Holder {
        public static final Configurator INSTANCE = new Configurator();
    }
    public final void configure(){
        initIcons();
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(), true);

    }

    private void checkConfiguration(){
        final boolean isReady = (boolean) LATTE_CONFIGS.get(ConfigType.CONFIG_READY.name());
        if(!isReady){
            throw new RuntimeException("Configuration is not ready ");
        }

    }
    final <T> T getConfiguration(Object key){
        checkConfiguration();
        return (T) LATTE_CONFIGS.get(key);
    }

    /**
     * 初始化Icon图标库
     */
    private void initIcons(){
        if(ICONS.size() > 0){
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for(int i=1; i<ICONS.size(); i++){
                initializer.with(ICONS.get(i));
            }
        }
    }
    public final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST.name(),host);
        return this;
    }
    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }
    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR.name(),INTERCEPTORS);
        return this;
    }
    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR.name(),INTERCEPTORS);
        return this;
    }
    public final Configurator withActivity(Activity activity) {
        LATTE_CONFIGS.put(ConfigType.ACTIVITY.name(), activity);
        return this;
    }
    public final Configurator withJavascriptInterface(String name) {
        LATTE_CONFIGS.put(ConfigType.JAVASCRIPT_INTERFACE.name(),name);
        return this;
    }
    public final Configurator withWebEvent(String name, Event event) {
        final EventManager manager = EventManager.getInstance();
        manager.addEvent(name,event);
        return this;
    }
}
