package net.wouto.modelsync.mongo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DBSync {
    
    public String value() default "";
    
    public boolean index() default false;
    
}
