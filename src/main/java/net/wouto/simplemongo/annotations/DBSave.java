package net.wouto.simplemongo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DBSave {
    
    public String value();
    
    public boolean index() default false;
    
}
